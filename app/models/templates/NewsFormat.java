package models.templates;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

import javax.persistence.Query;
import java.util.List;

/**
 * 用于 资讯列表的 页面数据展示的 辅助类
 */
public class NewsFormat extends JavaExtensions {

    //得到所有的类型(code = "guest" )   / 认证状态( code= "affirmReg") / 有效状态  (code="status")
    public static List registerType(String  code){
        Query query = Model.em().createNativeQuery("select levelcode,name  from view_dictype where eng=?");
        query.setParameter(1,code);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }

    //作者author
    public static List authorList(){
        Query query = Model.em().createNativeQuery("select distinct author  from rec_news");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }

    //根据资讯类型code  显示对应资讯类型 中文
    public static Object newsCodeFormat(String newsCode){
        Query query = Model.em().createNativeQuery("select  name from view_dictype where eng = 'news' and levelcode = ?");
        query.setParameter(1,newsCode);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList()==null ? null : query.getResultList().get(0);
    }

    //根据用户认证状态code  显示对应认证状态 中文
    public static Object affirmRegFormat(String statusCode){
        Query query = Model.em().createNativeQuery("select name from view_dictype where eng = 'affirmReg' and levelcode = ?");
        query.setParameter(1,statusCode);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList()==null ? null : query.getResultList().get(0);

    }
    //根据用户认证状态code  显示对应认证状态 中文
    public static Object statusFormat(String statusCode){
        Query query = Model.em().createNativeQuery("select name from view_dictype where eng = 'status' and levelcode = ?");
        query.setParameter(1,statusCode);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList()==null ? null : query.getResultList().get(0);
    }
}
