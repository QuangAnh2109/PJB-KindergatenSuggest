package fa.appcode.repositories;

import fa.appcode.entities.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository("accountRepository")
public interface AccountRepository extends JpaRepository <AccountInfo,Integer>{
    @Query("SELECT c FROM AccountInfo c WHERE c.email = ?1")
    public AccountInfo findByEmail(String email );

}
