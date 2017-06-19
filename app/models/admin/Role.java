package models.admin;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.List;

/**
 *角色管理
 */
@Entity
@Table(name="role")
public class Role extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role")
    @SequenceGenerator(initialValue = 1, name = "role", schema= "RJDS",sequenceName = "SEQ_ROLE" ,allocationSize = 1)
    @Column(name = "ID")
    public long id;

    @Column(name = "version")
    public Long version;

    @Column(name = "name")
    public String name;

    @Column(name = "enable")
    public int enable;

    @ManyToMany
    @JoinTable(name = "role_authority", joinColumns = { @JoinColumn(name = "roleid", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "authorityid", nullable = false) })
    public List<Authority> authorityList;

}
