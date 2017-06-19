package models.admin;

import play.db.jpa.GenericModel;

import javax.persistence.*;

/**
 * Created by dong-qianqian on 2016/9/27.
 * 申请聚融宝资质常用料表
 */
@Entity
@Table(name = "commproduct")
public class Commproduct extends GenericModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commproduct")
    @SequenceGenerator(initialValue = 1, name = "commproduct", schema= "RJDS",sequenceName = "SEQ_COMMPRODUCT" ,allocationSize = 1)

    @Column(name = "id")
    public Integer id; //id

    @Column(name = "commprotype")
    public String commprotype; //常用料类型

    @Column(name = "commpronum")
    public Integer commpronum; //常用料数量

    //外键关系的列
    @ManyToOne
    @JoinColumn(name="applyjurongbaoid" , nullable = false)
    public Applyjurongbao appjrb;      // 常用料
}
