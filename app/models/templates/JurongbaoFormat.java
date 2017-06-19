package models.templates;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by dong-qianqian on 2016/9/27.
 * 用于 申请聚融宝资质的 页面数据展示的 辅助类
 */
public class JurongbaoFormat extends JavaExtensions {
    //根据用户类型code  显示对应用户类型 中文
    public static Object usertypeFormat(String userType){
        Query query = Model.em().createNativeQuery("select  name from view_dictype where eng = 'userType' and levelcode = ?");
        query.setParameter(1,userType);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList()==null ? null : query.getResultList().get(0);
    }

    //得到所有的类型(code = "usertype" )
    public static List userType(String  code){
        Query query = Model.em().createNativeQuery("select levelcode,name from view_dictype where eng=?");
        query.setParameter(1,code);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }

    //得到所有的常用料
    public static List comProduct(Long applyjurongbaoid){
        Query query = Model.em().createNativeQuery("select commprotype||'  '||commpronum||' 吨/年' as commpro from commproduct where applyjurongbaoid =?");
        query.setParameter(1,applyjurongbaoid);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getResultList();
    }
}
