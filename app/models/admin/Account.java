package models.admin;
import models.utils.StringHelper;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;
import play.i18n.Messages;

import javax.persistence.*;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.List;

/**
 * 后台用户表
 */
@Entity
@Table(name = "account")
public class Account extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account")
    @SequenceGenerator(initialValue = 1, name = "account", schema= "RJDS",sequenceName = "SEQ_ACCOUNT" ,allocationSize = 1)
    @Column(name = "ID")
    public long id;

    @Column(name = "enable")
    public Integer enable;   //有效状态

    @Column(name = "version")
    public Integer version;    //版本

    @Column(name = "registertime")
    public Date registertime;   //注册时间

    @Column(name = "email")
    public String email;   //电子邮箱

    @Column(name = "endtime")
    public Date endtime;   //生效终止时间

    @Column(name = "begintime")
    public Date begintime;  //授权

    @Column(name = "ic")
    public String ic;  //身份证号

    @Column(name = "codeno")
    public String codeno;  // 自动生成规则未知

    @Column(name = "password")
    public String password;   //口令

    @Column(name = "username")
    public String username;  //登录名

    @Column(name = "name")
    public String name;  //姓名

    //外键关系的列
    @ManyToOne
    @JoinColumn(name = "organizationid")
    public Organization organization; //机构



    @ManyToOne
    @JoinColumn(name = "roleid")
    public Role role;   //角色


    //根据用户名和密码查询
   public static Account login(String username ,String password){
       return find("byUsernameAndPassword" , username , password).first();
   }

    //根据用户名和密码后去当前登录对象
    public static Account getByUserNameAndPwd(String username ,String password){
        return find("username=? and password=?",username,password).first();
    }

    //新增
    public static void addAccount(Account account) throws NoSuchAlgorithmException {
        Account account_new = new Account();
        account_new.name = account.name;
        account_new.email = account.email;
        account_new.username = account.username;
        account_new.password = StringHelper.md5(account.password);
        account_new.registertime = new Date();
        account_new.version = Integer.valueOf(Messages.get("ACCOUNT_VERSION"));
        account_new.enable = Integer.valueOf(Messages.get("ACCOUNT_ENABLE"));
        account_new.save();
    }

    //获取所有后台人员名字
    public static List getAllNames(){
        Query query = Model.em().createNativeQuery("SELECT id,name FROM ACCOUNT ORDER BY registerTime DESC");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);

        return query.getResultList();

    }



}
