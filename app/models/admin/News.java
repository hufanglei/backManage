package models.admin;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

/**
 *资讯管理
 */
@Entity
@Table(name="rec_news")
public class News extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rec_news")
    @SequenceGenerator(initialValue = 1, name = "rec_news", schema= "RJDS",sequenceName = "SEQ_REC_NEWS" ,allocationSize = 1)
    @Column(name = "ID")
    public long id;

    @Column(name = "memo")
    public String memo;//备注

    @Column(name = "statuscode")
    public String statuscode;//001=有效  002=无效

    @Column(name = "updatetime")
    public Date updatetime;//更新时间

    @Column(name = "createtime")
    public Date createtime;//创建时间

    @Column(name = "operationip")
    public String operationip;//操作IP地址

    @Column(name = "operationid")
    public Integer operationid;//操作人ID

    @Column(name = "author")
    public String author;//作者

    @Column(name = "content")
    public String content;//内容

    @Column(name = "summary")
    public String summary;//简介

    @Column(name = "titleimage")
    public String titleimage;//标题图片

    @Column(name = "title")
    public String title;//标题

    @Column(name = "newscode")
    public String newscode;//分类 001=主页，001001=往期活动，001002=聚化动态，001003市场分析

}
