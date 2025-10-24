package service;

import domain.Account;
import domain.Customer;
import domain.Transaction;
import domain.Type;
import repostiory.AccountRepository;
import repostiory.CustomerRepository;
import repostiory.TransactionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class BankServiceImpl implements BankService{
    private final AccountRepository accountRepository  = new AccountRepository();
    private final TransactionRepository transactionRepository = new TransactionRepository();
    private final CustomerRepository customerRepository = new CustomerRepository();
    @Override
    public String openAccount(String name, String email, String accountType) {
        String customerId = UUID.randomUUID().toString();
        Customer c = new Customer(email , customerId , name);
        customerRepository.save(c);

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

    @Override
    public void transfer(String fromAcc, String toAcc, Double amount, String note) {
        if(fromAcc.equals(toAcc)){
            throw new RuntimeException("cannot be  transfer to your own account");
        }
        Account  from = accountRepository.findByNumber(fromAcc).orElseThrow(()->new RuntimeException("Account not found"+fromAcc));
        Account  to = accountRepository.findByNumber(toAcc).orElseThrow(()->new RuntimeException("Account not found"+toAcc));
        if(from.getBalance().compareTo(amount)<0){
            throw new RuntimeException("Insufficient Balance");
        }
        from.setBalance(from.getBalance()-amount);
        to.setBalance(to.getBalance()+amount);
        Transaction fromtransaction = new Transaction(from.getAccountNumber(), Type.TRANSFER_OUT,UUID.randomUUID().toString() , amount , LocalDateTime.now(),note);
        transactionRepository.add(fromtransaction);

        Transaction totransaction = new Transaction(to.getAccountNumber(), Type.TRANSFER_IN,UUID.randomUUID().toString() , amount , LocalDateTime.now(),note);
        transactionRepository.add(totransaction);
    }

    @Override
    public List<Transaction> getStatement(String account) {
        return transactionRepository.findByAccount(account).stream().sorted(Comparator.comparing(Transaction::getTimestamp)).collect(Collectors.toList());
    }

    @Override
    public List<Account> searchAccountsByCustomerName(String q) {
        String query = ( q==null?"":q.toLowerCase());
        List<Account> result = new ArrayList<>();
        for(Customer c : customerRepository.findAll()){
            if(c.getName().toLowerCase().contains(query)){
                result.addAll(accountRepository.findByCustomerId(c.getId()));
            }
        }
        result.sort(Comparator.comparing(Account::getAccountNumber));
        return result;
    }

    private String getAccountNumber() {
        int size = accountRepository.findAll().size()+1;
        return String.format("AC%06d" , size);
    }
}
