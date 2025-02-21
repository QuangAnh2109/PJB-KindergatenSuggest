package fa.appcode.repositories;

import fa.appcode.entities.AccountInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("accountRepository")
public interface AccountRepository extends JpaRepository <AccountInfo,Integer>{

    @Query("""
    SELECT ai FROM AccountInfo ai
    JOIN FETCH ai.ward w
    JOIN FETCH ai.district d
    JOIN FETCH ai.city c
    WHERE ai.deleteFlg = false
    AND (:search IS NULL OR ai.fullName LIKE %:search% OR ai.email LIKE %:search% OR ai.phone LIKE %:search%)
""")
    Page<AccountInfo> findAllWithFullAddress(@Param("search") String search, Pageable pageable);


}
