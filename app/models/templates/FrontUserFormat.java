package models.templates;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

import javax.persistence.Query;
import java.util.List;

/**
 * 用于 用户管理的 页面数据展示的 辅助类
 */
public class FrontUserFormat extends JavaExtensions {

    //得到所有的注册类型(code = "guest" )   / 认证状态( code= "affirmReg") / 有效状态  (code="status")
    public static List registerType(String  code){
        Query query = Model.em().createNativeQuery("select levelcode,name  from view_dictype where eng=?");
        query.setParameter(1,code);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }

    //得到证件类型(code = "certType" ) 身份证，组织机构代码，统一社会信用
    public static List certType(){
        Query query = Model.em().createNativeQuery("select levelcode,name from view_dictype where eng = 'certType' and levelcode in ('1','52','73')");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }

    //根据用户注册类型code  显示对应注册类型 中文
    public static Object registerCodeFormat(String statusCode){
        Query query = Model.em().createNativeQuery("select  name from view_dictype where eng = 'guest' and levelcode = ?");
        query.setParameter(1,statusCode);
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

    //根据用户类型code  显示对应认证状态 中文
    public static Object userTypeFormat(String statusCode){
        Query query = Model.em().createNativeQuery("select name from view_dictype where eng = 'userType' and levelcode = ?");
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

    //获取所有业务员
    public static Object getAccountNameByIdFormat(String accountId){
        Query query = Model.em().createNativeQuery("select name from ACCOUNT where id=?");
        query.setParameter(1,accountId);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList()==null ? null : query.getResultList().get(0);
    }

}
