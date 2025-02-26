package fa.appcode.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "master_mail")
public class MasterMail {
    @Id
    @Column(name = "mail_id", nullable = false)
    private Integer id;

    @Column(name = "subject", nullable = false, length = 100)
    private String subject;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

}