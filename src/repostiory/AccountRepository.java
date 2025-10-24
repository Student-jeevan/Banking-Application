package repostiory;

import domain.Account;
import domain.Customer;

import java.util.*;

public class AccountRepository {
    private final Map<String , Account> accountsByNumber = new HashMap<>();
    public void save(Account account){
        accountsByNumber.put(account.getAccountNumber() , account);
    }

    public List<Account> findAll() {

        return new ArrayList<>(accountsByNumber.values());
    }

    public Optional<Account> findByNumber(String accountNumber) {
        return Optional.ofNullable(accountsByNumber.get(accountNumber));
    }

    public List<Account> findByCustomerId(String id) {
        List<Account> result = new ArrayList<>();
        for(Account c : accountsByNumber.values()){
            if(c.getCustomerId().equals(id)){
                result.add(c);
            }
        }
        return result;
    }
}
