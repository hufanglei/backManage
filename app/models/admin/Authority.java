package models.admin;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.GenericModel;
import play.db.jpa.Model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.List;

/**
 * 后台用户表
 */
@Entity
@Table(name = "authority")
public class Authority extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authority")
    @SequenceGenerator(initialValue = 1, name = "authority", schema= "RJDS",sequenceName = "SEQ_AUTHORITY" ,allocationSize = 1)
    @Column(name = "ID")
    public Long id;

    @Column(name = "parentid")
    public Long parentid;

    @Column(name = "itemicon")
    public String itemicon;

    @Column(name = "matchurl")
    public String matchurl;

    @Column(name = "url")
    public String url;

    @Column(name = "thevalue")
    public String thevalue;

    @Column(name = "position")
    public Integer position;

    @Column(name = "levelcode")
    public String levelcode;

    @Column(name = "name")
    public String name;

    @Column(name = "enable")
    public Integer enable;

    @Column(name = "version")
    public Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToMany
    @JoinTable(name = "role_authority", joinColumns = { @JoinColumn(name = "roleid", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "authorityid", nullable = false) })
    public List<Role> roleList;

    //根据角色Id（roleId）获取权限集合
    public static List<HashMap> getListByRoleId(String roleId){
        Query query = Model.em().createNativeQuery("SELECT a.id FROM ROLE_AUTHORITY ra,AUTHORITY a WHERE RA.AUTHORITYID=a.id AND RA.ROLEID=?");
        query.setParameter(1,roleId);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
//        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.aliasToBean(AuthorityMenu.class));
        return query.getResultList();

    }

}
