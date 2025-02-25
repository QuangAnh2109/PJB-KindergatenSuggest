package fa.appcode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "master_data", schema = "instance_kintergarden_db")
public class MasterDatum {
    @Id
    @Column(name = "master_id", nullable = false)
    private Integer id;

    @Column(name = "type_name", nullable = false, length = 50)
    private String typeName;

    @Column(name = "type_key", nullable = false)
    private Integer typeKey;

    @Column(name = "type_value", nullable = false)
    private String typeValue;

    @ColumnDefault("(1)")
    @Column(name = "record_no", nullable = false)
    private Integer recordNo;

    @Column(name = "create_id", nullable = false, length = 50)
    private String createId;

    @Column(name = "create_time", nullable = false)
    private Instant createTime;

    @Column(name = "update_id", nullable = false, length = 50)
    private String updateId;

    @Column(name = "update_time", nullable = false)
    private Instant updateTime;

    @Column(name = "delete_flg", nullable = false)
    private Boolean deleteFlg = false;

}