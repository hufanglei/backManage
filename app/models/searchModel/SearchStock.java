package models.searchModel;

/**
 * Created by peak on 2016/7/21 0021.
 */
public class SearchStock {
    private String mark;
    private String companyName;
    private String storageName;
    private String affirmStockCode;

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStorageName() {
        return storageName;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public String getAffirmStockCode() {
        return affirmStockCode;
    }

    public void setAffirmStockCode(String affirmStockCode) {
        this.affirmStockCode = affirmStockCode;
    }
}
