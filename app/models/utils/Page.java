package models.utils;

/**
 * 分页bean
 */
public class Page{
    public int count;     //总记录数
    public int pageSize;  //每页显示记录数
    public int pages;     //总页数
    public int beginIndex; //起始记录数
    public int curPage;   //当前页
    private boolean hasPrePage;//是否有上一页
    private boolean hasNextPage; //是否有下一页

    public Page(int curPage, int pageSize, int totalcount) {
        this.count = totalcount;
        this.pageSize = pageSize;
        this.pages =  (int)Math.ceil((double)count / pageSize) ;
        this.beginIndex = (curPage-1)*pageSize;
        this.curPage = curPage;
    }

    //是否有上一页
    public  boolean getHasPrePage(int currentPage){
        return currentPage==1 ? false : true;
    }

    //是否有下一页
    public  boolean getHasNextPage(int pages , int curPage){
        return  curPage == pages || pages == 0 ? false : true;
    }


}
