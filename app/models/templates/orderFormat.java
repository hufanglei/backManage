package models.templates;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

import javax.persistence.Query;
import java.text.DecimalFormat;

/**
 * 用于不同的 订单状态的 页面的展示
 */
public class orderFormat extends JavaExtensions {
    public static String ccyAmount(Number number, String currencySymbol) {
        String format = "'"+currencySymbol + "'#####.##";
        return new DecimalFormat(format).format(number);

    }

    //订单首页的 根据订单code  显示中文
    public static Object orderStatusFormat(String statusCode ,String code){
        Query query = Model.em().createNativeQuery("select name from view_dictype where eng = 'orderStatus' and levelcode = ?");
        query.setParameter(1,statusCode);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList()==null ? null : query.getResultList().get(0);

    }



}
