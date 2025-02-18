package fa.appcode.repositories;

import fa.appcode.entities.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("accountRepository")
public interface AccountRepository extends JpaRepository <AccountInfo,Integer>{
}
