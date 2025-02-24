package fa.appcode.services.impl;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.entities.AccountInfo;
import fa.appcode.repositories.AccountRepository;
import fa.appcode.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public Page<AccountInfo> findAll(Pageable pageable)
            {
        return accountRepository.findAll(pageable);
    }

    @Override
    public List<AccountInfo> findAllRoles(){
        return accountRepository.findAllRole();
    }


    @Override
    public Page<ParentVo> findAllParent(Pageable pageable) {
        return accountRepository.findAllParent(pageable);
    }

    @Override
    public ParentVo findParentById(int id) {
        return accountRepository.findParentById(id);
    }

    @Override
    public Page<EnrolledSchoolVo> findEnrolledSchoolBy(int id,Pageable pageable) {
        return accountRepository.findParentEnrolledSchoolBy(id,pageable);
    }
}
