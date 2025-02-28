package fa.appcode.repositories;

import fa.appcode.common.vo.MasterMailVo;
import fa.appcode.entities.MasterMail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MasterMailRepository extends JpaRepository<MasterMail, Integer> {
    MasterMailVo findByIdAndDeleteFlg(Integer id, Boolean deleteFlg);
}
