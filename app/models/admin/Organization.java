package models.admin;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 组织结构表
 */


@javax.persistence.Entity
@Table(name = "organization")
public class Organization extends Model {

    @Column(name="enable")
    public Integer enable;      //有效状态

    @Column(name="version")
    public Integer version;     //版本

    @Column(name="thevalue")
    public String thevalue;     //值

    @Column(name="position")
    public Integer position;    //位置

    @Column(name="levelcode")
    public String levelcode;    // 层级码

    @Column(name="name")
    public String name; //名称

    //外键关系列
    @ManyToOne
    @JoinColumn(name="parentid")
    public Organization parent;    //  父id



}
