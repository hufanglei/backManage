$(function(){

    $(".form_date").datetimepicker({
        language: 'zh-CN',
        weekStart: 1,
        todayBtn: 1,
         autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 2,
        forceParse: 0,
        format: "yyyy-mm-dd"
    });

    $(".form_datetime").datetimepicker({
        language: 'zh-CN',
       // weekStart: 1,
        todayBtn: 1,
        autoclose: 1,
        todayHighlight: 1,
        startView: 2,
        minView: 0,
        maxView:4,
        forceParse: 0,
        //format: "yyyy-mm-dd hh:ii:ss"
        format: "yyyy-mm-dd hh:ii:ss"
    });

    //select2下拉框
    $(".chosen-select").select2();



})
var messageTip = {
    tip:function(message){
        $("#messagetip").remove();
        var html ="<div id='messagetip'>"+
            "         <i class='iconfont icon-bell'></i> "+ message +
            "         </div>";
        $("body").append(html);
        $("#messagetip").stop(true).animate({top:0}).delay(3000).animate({top:-59});
        $("#messagetip").on("click", function () {
            $("#messagetip").stop(true).animate({top:-59},600);
        });
    }
};