package service;

import domain.Account;
import domain.Transaction;
import domain.Type;
import repostiory.AccountRepository;
import repostiory.TransactionRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService{
    private final AccountRepository accountRepository  = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId = UUID.randomUUID().toString();
        String accountNumber = getAccountNumber();
        Account account = new Account(accountNumber, customerId , 0 , accountType);
        accountRepository.save(account);

        return accountNumber;
    }

    @Override
    public List<Account> listAccounts() {
        return accountRepository.findAll().stream().sorted(Comparator.comparing(Account::getAccountNumber)).collect(Collectors.toList());
    }

    @Override
    public void deposit(String accountNumber, Double amount, String note) {
        Account account = accountRepository.findByNumber(accountNumber).orElseThrow(()->new RuntimeException("Account not found"));
        account.setBalance(account.getBalance()+amount);
        Transaction transaction = new Transaction(account.getAccountNumber(), Type.DEPOSIT,UUID.randomUUID().toString() , amount , LocalDateTime.now(),note);
        transactionRepository.add(transaction);
    }

    @Override
    public void withdraw(String accountNumber, Double amount, String note) {
        Account account = accountRepository.findByNumber(accountNumber).orElseThrow(()->new RuntimeException("Account not found"));
        if(account.getBalance().compareTo(amount)<0){
            throw new RuntimeException("Insufficient Balance");
        }
        account.setBalance(account.getBalance()-amount);
        Transaction transaction = new Transaction(account.getAccountNumber(), Type.WITHDRAW,UUID.randomUUID().toString() , amount , LocalDateTime.now(),note);
        transactionRepository.add(transaction);
    }

    private String getAccountNumber() {
        int size = accountRepository.findAll().size()+1;
        return String.format("AC%06d" , size);
    }
}
