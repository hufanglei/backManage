package models.templates;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

import javax.persistence.Query;
import java.util.List;

/**
 * 用于 角色管理的 页面数据展示的 辅助类
 */
public class RoleFormat extends JavaExtensions {

    //得到集合
    public static List roleList(Long id){
        Query query = Model.em().createNativeQuery("select id,levelcode,name,position,url,parentid from authority");
//            Query query = Model.em().createNativeQuery("select * from authority a where a.id in (select ra.authorityid from role_authority ra where ra.roleid = ?)");
//            query.setParameter(1,id);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }

    //得到集合
    public static List authroList(Long authid,Long roleid){
        Query query = Model.em().createNativeQuery("select * from role_authority where authorityid = ? and roleid = ?");
        query.setParameter(1,authid);
        query.setParameter(2,roleid);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }
}