package models.admin;


import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Date;

/**
 * 3证的model
 */
@Entity
@Table(name="rec_annex")
public class Annex extends Model {

    @Column(name = "memo")
    public String memo;                //备注

    @Column(name = "statuscode")
    public String statuscode;         //001=有效 002=无效

    @Column(name = "createtime")
    public Date createtime;           //创建时间

    @Column(name = "operationip")
    public String operationip;        //操作IP地址

    @Column(name = "operationid")
    public Long operationid;      //操作人ID

    @Column(name = "affirmregcode")
    public String affirmregcode;    //针对每个附件的三证审核 001=待审核 002=审核中 003=审核通过 004=审核失败

    @Column(name = "charterurl")
    public String charterurl;     //URL

    @Column(name = "annexcode")
    public String annexcode;     //001=营业执照，002=组织机构代码证，...

    //外键关系的列
    @ManyToOne
    @JoinColumn(name="guestid" , nullable = true)
    public Frontuser frontuser;  //关联前台用户



}
