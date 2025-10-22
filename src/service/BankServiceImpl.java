package service;

import domain.Account;
import repostiory.AccountRepository;

import java.util.UUID;

public class BankServiceImpl implements BankService{
    private final AccountRepository accountRepository  = new AccountRepository();
    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId = UUID.randomUUID().toString();
        int temp = accountRepository.findAll().size()+1;
        String accountNumber = String.format("AC%06d" , temp);
        Account account = new Account(accountNumber, customerId , 0 , accountType);
        accountRepository.save(account);

        return accountNumber;
    }
}
