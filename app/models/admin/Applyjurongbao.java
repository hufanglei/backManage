package models.admin;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by dong-qianqian on 2016/9/27.
 * 申请聚融宝资质表
 */
@Entity
@Table(name = "applyjurongbao")
public class Applyjurongbao extends GenericModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applyjurongbao")
    @SequenceGenerator(initialValue = 1, name = "applyjurongbao", schema= "RJDS",sequenceName = "SEQ_APPLYJURONGBAO" ,allocationSize = 1)
    @Column(name = "id")
    public Long id; //id

    @Column(name = "createtime")
    public Date createtime; //创建时间

    @Column(name = "address")
    public String address; //详细地址

    @Column(name = "countrycode")
    public String countrycode; //区/县

    @Column(name = "citycode")
    public String citycode; //市

    @Column(name = "provcode")
    public String provcode; //省

    @Column(name = "mobphone")
    public String mobphone; //手机号

    @Column(name = "product")
    public String product; //产出产品

    @Column(name = "procmach")
    public Integer procmach; //加工机械

    @Column(name = "name")
    public String name; //用户名

    @Column(name = "usertype")
    public String usertype; //用户类型

    @Column(name = "frontuserid")
    public Integer frontuserid; //当前登录用户ID
}
