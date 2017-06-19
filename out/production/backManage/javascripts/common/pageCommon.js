$(function () {
    var url = window.location.pathname;
    //进入分页跳转
    $(".confirm").click(function(){
        var currentPage = $("#currentPage").html();
        window.location.href = url +"?pageIndex="+currentPage;
    });

//首页的跳转
    $(".pager li .home").click(function(){
        var searchData = $("#searchForm").serialize();
        searchData = searchData + "&pageIndex=" + 1;
        window.location.href = url + "?" + searchData;
    });

//上一页的跳转
    $(".pager li .prev").click(function(){
        var searchData = $("#searchForm").serialize();
        searchData = searchData + "&pageIndex=" + ( parseInt($(this).attr("data-pageIndex")) - 1);
        window.location.href = url+"?" + searchData;
    });

//下一页的跳转
    $(".pager li .next").click(function(){
        var searchData = $("#searchForm").serialize();
        searchData = searchData + "&pageIndex=" + ( parseInt($(this).attr("data-pageIndex")) + 1);
        window.location.href = url+"?" + searchData;
    });

//尾页的跳转
    $(".pager li .end").click(function(){
        var searchData = $("#searchForm").serialize();
        searchData = searchData + "&pageIndex=" + $(this).attr("data-pageIndex");
        window.location.href = url+"?" + searchData;
    });

});


