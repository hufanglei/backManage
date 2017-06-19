/**
 * Created by hfl on 2016-6-8.
 */


/**
 * js获取url ?后面的参数项的值  传入key的到value
 * @param name
 * @returns {null}
 */
window.getQueryString= function (name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}

try {
    $.getScript("/JqueryPagination").complete(function(){
        Testpage();
    });
} catch (e) {
    console.error("没有引入jquery.pagination-1.2.7.min.js文件!");
}

function Testpage() {
    var urlpage = $("#urlpage").val(); //获取请求url
    var pagepoint = "#" + $("#urlpage").attr("pagepoint") || "";//翻页定位锚点
    var totalPage = $("#totalPage").val();//总页数
    $("#page").page({
        total: $("#totalcount").val(),//获取总条数
        pageSize: $("#pageSize").val(),//每页显示数据条数
        firstBtnText: '首页',
        lastBtnText: '尾页',
        prevBtnText: '上一页',
        nextBtnText: '下一页',
        showInfo: true,
        infoFormat: '共{total}条/总计'+totalPage+'页'
    });
    $("#page").on("pageClicked", function (event, pageIndex) {//点击分页条查询
        var flag = urlpage.indexOf("?") == -1 ? "?" : "&";
        window.location.href = urlpage + flag + "pageIndex=" + (pageIndex + 1) + pagepoint;

    })

    //获取url链接 ?参数变量的值(截取等号后面的部分)
    var loc =getQueryString("pageIndex");
    //分页刷新后重新标记分页条的当前页数
    $(".m-pagination-page li").each(function (index) {
        if ($(this).children("a").text() == loc) {
            $(this).siblings().removeClass("active").end().addClass("active");
            return;
        }
    })
};

