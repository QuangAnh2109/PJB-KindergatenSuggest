package fa.appcode.services.impl;

import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;

public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
}
