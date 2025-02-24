package fa.appcode.repositories;

import fa.appcode.entities.AccountInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("accountRepository")
public interface AccountRepository extends JpaRepository<AccountInfo, Integer> {

    @Query("SELECT c FROM AccountInfo c WHERE c.email = ?1")
    AccountInfo findByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE AccountInfo a SET a.password = ?1 WHERE a.email = ?2")
    int updatePassword(String newPassword, String email);
}
