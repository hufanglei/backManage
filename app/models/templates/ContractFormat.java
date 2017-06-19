package models.templates;

import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import play.db.jpa.Model;
import play.templates.JavaExtensions;

import javax.persistence.Query;

/**
 * 合同
 */
public class ContractFormat extends JavaExtensions {

    //合同状态
    public static Object convertContractStatus(String  code){
        Query query = Model.em().createNativeQuery("SELECT name FROM VIEW_DICTYPE WHERE ENG='contractStatus' AND LEVELCODE=?");
        query.setParameter(1,code);
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return  query.getSingleResult();
    }


}
