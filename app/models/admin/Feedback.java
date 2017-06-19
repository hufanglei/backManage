package models.admin;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

/**
 * 帮助反馈
 * User: zjw
 * DateTime: 2016/12/2 0002 13:04
 */
@Entity
@Table(name="rec_feedback")
public class Feedback extends GenericModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rec_feedback")
    @SequenceGenerator(initialValue = 1, name = "rec_feedback", schema= "RJDS",sequenceName = "SEQ_FEEDBACK" ,allocationSize = 1)
    @Column(name = "ID")
    public long id;

    //外键关系的列
    @ManyToOne
    @JoinColumn(name = "frontuserid")
    public Frontuser frontuser;

    @Column(name = "title")
    public String title;

    @Column(name = "typecode")
    public String typecode;

    @Column(name = "content")
    public String content;

    @Column(name = "createtime")
    public Date createtime;


}
