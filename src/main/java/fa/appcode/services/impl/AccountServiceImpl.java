package fa.appcode.services.impl;

import fa.appcode.common.vo.EnrolledSchoolVo;
import fa.appcode.common.vo.ParentVo;
import fa.appcode.common.vo.RoleVo;
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
    public Page<ParentVo> findAllParent(String search,Pageable pageable) {
        return accountRepository.findAllParent(search,pageable);
    }

    @Override
    public ParentVo findParentById(int id) {
        return accountRepository.findParentById(id);
    }

    @Override
    public Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentId(int id,Pageable pageable) {
        return accountRepository.findParentEnrolledSchoolByParentId(id,pageable);
    }

    @Override
    public Page<EnrolledSchoolVo> findParentEnrolledSchoolByParentIdAndSchoolOwner(int parentId, String schoolOwnerId, Pageable pageable) {
        return accountRepository.findParentEnrolledSchoolByParentIdAndSchoolOwner(parentId,schoolOwnerId,pageable);
    }

    @Override
    public RoleVo findByEmail(String email) {return accountRepository.findByEmail(email);}

    @Override
    public AccountInfo getAccountInfoById(int id) {
        return accountRepository.getAccountInfoById( id);
    }
}
