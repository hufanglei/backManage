package models.admin;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

/**
 * User: zjw
 * DateTime: 2016/12/20 0020 8:49
 */
@Entity
@Table(name = "phoneversion")
public class Phoneversion extends GenericModel {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "phoneversion")
    @SequenceGenerator(initialValue = 1, name = "phoneversion", schema = "RJDS", sequenceName = "SEQ_PHONEVERSION", allocationSize = 1)
    @Column(name = "ID")
    public long id;

    @Column(name = "name")
    public String name;

    @Column(name = "code")
    public String code;

    @Column(name = "statuscode")
    public String statuscode;

    @Column(name = "path")
    public String path;

    @Column(name = "content")
    public String content;

    @Column(name = "createtime")
    public Date createtime;

}
