package models.admin;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

/**
 * 签到
 * User: zjw
 * DateTime: 2016/12/2 0002 15:54
 */
@Entity
@Table(name = "rec_checkin")
public class Checkin extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rec_checkin")
    @SequenceGenerator(initialValue = 1, name = "rec_checkin", schema = "RJDS", sequenceName = "SEQ_CHECKIN", allocationSize = 1)
    @Column(name = "ID")
    public long id;

    //外键关系的列
    @ManyToOne
    @JoinColumn(name = "frontuserid")
    public Frontuser frontuser;

    @Column(name = "attendancedays")
    public Long attendancedays;

    @Column(name = "grandtotal")
    public Long grandtotal;

    @Column(name = "createtime")
    public Date createtime;

}
