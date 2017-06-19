package models.templates;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.templates.JavaExtensions;
import javax.persistence.Query;

/**
 * 用于手机 订单列表的 页面数据展示的 辅助类
 */
public class SeekBuyFormat extends JavaExtensions {

    //订单首页的 根据订单code  显示中文
    public static Object seekBuyStatusFormat(String statusCode ,String code){
        Query query = Model.em().createNativeQuery("select name from view_dictype where eng = 'orderStatus' and levelcode = ?");
        query.setParameter(1,statusCode);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList()==null ? null : query.getResultList().get(0);

    }

}
