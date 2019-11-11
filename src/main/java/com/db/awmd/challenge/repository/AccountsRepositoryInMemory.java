package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

	private final Map<String, Account> accounts = new ConcurrentHashMap<>();

	@Override
	public void createAccount(Account account) throws DuplicateAccountIdException {
		Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
		if (previousAccount != null) {
			throw new DuplicateAccountIdException("Account id " + account.getAccountId() + " already exists!");
		}
	}

	@Override
	public Account getAccount(String accountId) {
		return accounts.get(accountId);
	}

	@Override
	public void clearAccounts() {
		accounts.clear();
	}

	/* This method is used to transfer money between account and keep a check that an account does not end up with a negative balance
	 * @params fromAccountId, toAccountId, amount
	 */
	@Override
	public boolean moneyTransfer(String fromAccountId, String toAccountId, String amount) {
		// TODO Auto-generated method stub
		Account fromAccount = accounts.get(fromAccountId);
		BigDecimal fromBalance = fromAccount.getBalance();
		
		Account toAccount = accounts.get(toAccountId);
		BigDecimal toBalance = toAccount.getBalance();
		
		if(Integer.parseInt(amount)>0){
			double newFromBalance = fromBalance.doubleValue()-  new BigDecimal(amount).doubleValue();
			if(newFromBalance>0){
				fromAccount.setBalance(new BigDecimal(newFromBalance));
				
				double newToBalance = toBalance.doubleValue() +  new BigDecimal(amount).doubleValue();
					toAccount.setBalance(new BigDecimal(newToBalance));
			}
		}
	}

}
