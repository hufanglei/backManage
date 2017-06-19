package models.templates;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by hfl on 2016-6-20. select * from rec_frontuser where id = 61
 */
public class AnnexFormat extends JavaExtensions {

    //根据用户id 显示用户的姓名
    public static Object  getFrontNameAndLoginByIdFormat(String frontUserId){
        Query query = Model.em().createNativeQuery("select name , login from rec_frontuser where id=?");
        query.setParameter(1,frontUserId);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList()==null ? null : query.getResultList().get(0);
    }

    //根据审核code 显示3证类型对应汉字 select name from view_dictype  where eng = 'annex' and levelcode = '001'
    public static Object annexTypeFormat(String  levelcode){
        Query query = Model.em().createNativeQuery("select name from view_dictype  where eng = 'annex' and levelcode=?");
        query.setParameter(1,levelcode);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList()==null ? null : query.getResultList().get(0);
    }

    //3证审核状态
    public static Object affirmRegTypeFormat(String  levelcode){
        Query query = Model.em().createNativeQuery("select name from view_dictype  where eng = 'affirmReg' and levelcode=?");
        query.setParameter(1,levelcode);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList()==null ? null : query.getResultList().get(0);
    }
    //得到所有的类型(code = "guest" )   / 认证状态( code= "affirmReg") / 有效状态  (code="status")
    public static List registerType(String  code){
        Query query = Model.em().createNativeQuery("select levelcode,name  from view_dictype where eng=?");
        query.setParameter(1,code);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }
}
