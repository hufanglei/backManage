package models.admin;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

/**
 * 帮您找货
 */
@Entity
@Table(name="rec_findgood")
public class Findgood extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rec_findgood")
    @SequenceGenerator(initialValue = 1, name = "rec_findgood", schema= "RJDS",sequenceName = "SEQ_REC_FINDGOOD" ,allocationSize = 1)

    @Column(name = "ID")
    public long id;

    @Column(name = "memo")
    public String memo;//备注

    @Column(name = "endtime")
    public Date endtime;

    @Column(name = "begintime")
    public Date begintime;

    @Column(name = "dealcode")
    public String dealcode;//受理状态 001=已申请 002=已完成 003=已作废（恶意） 004=已取消 005=已超时

    @Column(name = "operationip")
    public String operationip;

    @Column(name = "operationid")
    public Integer operationid;//操作人ID

    @Column(name = "sup")
    public String sup;//成交的供应商

    @Column(name = "phone")
    public String phone;//手机号

    @Column(name = "linkman")
    public String linkman;//联系人

    @Column(name = "content")
    public String content;

    @Column(name = "title")
    public String title;
}
