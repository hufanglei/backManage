package models.templates;

import models.admin.Company;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by peak on 2016/6/23 0023.
 */
public class CompanyFormat extends JavaExtensions {
    //获取所有的公司
    public static List getCompanyList(){
        return  Company.findAll();
    }

    //根据注册类型获取合同中的公司列表
    public static List getCompanyListByGuestCode(String code){
        Query query = Model.em().createNativeQuery("SELECT DISTINCT RC.name COMPANYNAME\n" +
                "FROM CONTRACT c,REC_FRONTUSER rf,REC_COMPANY rc\n" +
                "WHERE c.FRONTUSERID=RF.id AND RF.COMPANYID=RC.id\n" +
                "AND RF.GUESTCODE=?");
        query.setParameter(1,code);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return query.getResultList();
    }
}
