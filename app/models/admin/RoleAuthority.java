package models.admin;

import play.db.jpa.GenericModel;

import javax.persistence.*;

/**
 *角色权限
 */
@Entity
@Table(name="role_authority")
public class RoleAuthority extends GenericModel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_authority")
    @SequenceGenerator(initialValue = 1, name = "role_authority", schema= "RJDS",sequenceName = "SEQ_ROLE_AUTHORITY" ,allocationSize = 1)
    @Column(name = "ID")
    public long id;

    @ManyToOne
    @JoinColumn(name = "authorityid")
    public Authority authority; //权限

    @ManyToOne
    @JoinColumn(name = "roleid")
    public Role role; //角色
}