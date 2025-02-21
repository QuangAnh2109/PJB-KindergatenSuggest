package fa.appcode.services.impl;

import fa.appcode.entities.AccountInfo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;


}
