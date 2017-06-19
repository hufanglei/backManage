package models.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 分页model,用于存储分页的信息
 */
public class PageBean {
    //分页属性
    private int pageNow;//当前页
    private int pageSize = 6;////每页显示记录数
    private int totalcount =0;  //总记录数
    private int  totalPage=0;  //总页数
    private List modelList=new ArrayList();   //实体结果集合
    //查询属性
    private String tableName;//表名 或者是多表连接查询部分  不可为空（必填）
    private String  showColumn;   //--查询结果显示字段 可以为* 不可为空（必填）
    private String condition;    //  --分页查询条件 可以为空
    private String orderBy;    //--order by 后排序字段，为空表示不排序  可以多个,号分割
    private int flag;      //--排序标识 0：正序 1：倒序 可以为空

    public PageBean(int pageNow, int pageSize , int flag) {
        this.pageNow = pageNow;
        this.pageSize = pageSize;
        this.flag = flag;
    }

    public PageBean(int pageNow, int pageSize,  String tableName, String showColumn, String condition, String orderBy, int flag) {
        this.pageNow = pageNow;
        this.pageSize = pageSize;
        this.tableName = tableName;
        this.showColumn = showColumn;
        this.condition = condition;
        this.orderBy = orderBy;
        this.flag = flag;
    }

    //是否有上一页
    public  boolean getHasPrePage(int pageNow){
        return pageNow==1 ? false : true;
    }

    //是否有下一页
    public  boolean getHasNextPage(int totalPage , int pageNow){
        return  pageNow == totalPage || totalPage == 0 ? false : true;
    }


    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalcount() {
        return totalcount;
    }

    public void setTotalcount(int totalcount) {
        this.totalcount = totalcount;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public List getModelList() {
        return modelList;
    }

    public void setModelList(List modelList) {
        this.modelList = modelList;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getShowColumn() {
        return showColumn;
    }

    public void setShowColumn(String showColumn) {
        this.showColumn = showColumn;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }
}
