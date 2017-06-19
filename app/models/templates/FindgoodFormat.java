package models.templates;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by dong-qianqian on 2016/7/21.
 */
public class FindgoodFormat  extends JavaExtensions {

    //得到所有的进展状态
    public static List dealcodeType(String  code){
        Query query = Model.em().createNativeQuery("select levelcode,name  from view_dictype where eng=?");
        query.setParameter(1,code);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }

    //根据受理类型code  显示对应受理类型 中文
    public static Object findgoodFormat(String dealcode){
            Query query = Model.em().createNativeQuery("select name from view_dictype where eng = 'deal' and levelcode = ?");
            query.setParameter(1, dealcode);
            query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
            return query.getResultList() == null ? null : query.getResultList().get(0);
    }

    //根据id  显示对应姓名 中文
    public static Object userFormat(String operationid){
        Query query = Model.em().createNativeQuery("select name from rec_FrontUser where id = ?");
        query.setParameter(1, operationid);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList().size()==0 ? null:query.getSingleResult();
    }

    //联系人linkman
    public static List linkmanList(){
        Query query = Model.em().createNativeQuery("select distinct linkman from rec_FindGood");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }

    //手机号phone
    public static List phoneList(){
        Query query = Model.em().createNativeQuery("select distinct phone from rec_FindGood");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }

    //成交供应商sup
    public static List supList(){
        Query query = Model.em().createNativeQuery("select distinct sup from rec_FindGood");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }
}
