package models.templates;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by peak on 2016/6/23 0023.
 */
public class StockFormat extends JavaExtensions {
    //获取库存类型中文
    public static Object stockCodeFormat(String stockCode) {
        Query query = Model.em().createNativeQuery("select  name from view_dictype where eng = 'stock' and levelcode = ?");
        query.setParameter(1, stockCode);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getSingleResult();
    }

    //获取库存类型中文
    public static Object affirmStockFormat(String affirmStock) {
        Query query = Model.em().createNativeQuery("select  name from view_dictype where eng = 'affirmStock' and levelcode = ?");
        query.setParameter(1, affirmStock);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getSingleResult();
    }

    //获取所有的库存认证状态
    public static List getAffirmStockList() {
        Query query = Model.em().createNativeQuery("SELECT LEVELCODE,NAME FROM VIEW_DICTYPE WHERE ENG='affirmStock'");
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }
}
