
/**
 * Created by hfl on 2016-6-23.
 * 放大镜插件
 * 使用方法:
 *  ②该页面引入 该js
 *  ①需要放大的图像添加事件 ： width="100" height="100"  onmouseover="toMagnifier(this)"
 */

function toMagnifier(obj){
    magi(obj);
}
function  magi(objImg){
    var doms = reayDoms();
    //查询到需要放大的图片 标示 magnifier的类样式
    //var imgDom = document.getElementsByClassName("magnifier")[0];
    var imgDom = objImg;
    //父类
    var imgParentDom = imgDom.parentElement;
    imgParentDom.cssText = "width:200px;"

    doms.imgbox.appendChild(imgDom);
    doms.imgbox.appendChild(doms.spanDom);
    doms.imgbox.appendChild(doms.markDom);
    doms.imgbox.appendChild(doms.maxBoxDom);
    imgParentDom.appendChild(doms.imgbox);

    init(doms.imgbox);

    function init(boxDom){
        //拖拽小图层
        var spanDom = boxDom.children[1];
        //获取图层中的图片地址
        var minImgBox =  boxDom.children[0];
        //鼠标进入层，拖拽层显示
        boxDom.onmouseover = function(e){
            //小图显示
            spanDom.style.display = "block";
            //大图显示
            doms.maxBoxDom.style.display = "block";
            //小图层中图片，赋给大图层
            doms.maxImgDom.src = minImgBox.src;

            //大图层是通过小图层和外层宽度计算得来的 this.parentElement ===    wrapDoms对象
            var top = this.parentElement.offsetTop   + 10;
            var left = this.parentElement.offsetLeft + boxDom.offsetWidth + 20;
            //this.parentElement.
            doms.maxBoxDom.style.bottom = 0;
            doms.maxBoxDom.style.left = boxDom.offsetWidth+"px";
        };

        //鼠标离开的时候触发拖拽层隐藏
        boxDom.onmouseout = function(){
            //小图和大图隐藏
            doms.spanDom.style.display = "none";
            doms.maxBoxDom.style.display = "none";

        };
        //css解决方案

        //鼠标大层onmousemove事件
        boxDom.onmousemove = function(e){
            var ev = e || window.event;
            //clientX pageX --当前鼠标的位置
            //offsetLeft和offsetTop是元素在浏览器的位置 理解一个人在某个教室里的作为，距离浏览器的顶部距离和左边的距离
            //offsetWidth和offsetHeight是元素的宽度和高度，理解就是一个人高矮胖瘦.
            var sl =  (document.documentElement.scrollLeft || document.body.scrollLeft);
            var st =  (document.documentElement.scrollTop|| document.body.scrollTop);
            var x = (ev.clientX || ev.pageX) - this.offsetLeft +sl;
            var y = (ev.clientY || ev.pageY) - this.offsetTop + st;
            //坐标赋予拖拽层
            var left = x - doms.spanDom.offsetWidth/2;
            var top = y -doms.spanDom.offsetHeight/2;
            //最大移动位置 boxDom的宽度减去拖拽层的宽度计算得来，高度同理.
            var maxLeft = this.offsetWidth - doms.spanDom.offsetWidth;
            var maxTop = this.offsetHeight - doms.spanDom.offsetHeight;
            //拖拽层边界的判断
            //上边界
            if(left<0)left=0;
            if(top<0)top=0;
            //下边界
            if(left>maxLeft)left = maxLeft;
            if(top>maxTop)top = maxTop;
            //改变拖拽层的位置
            doms.spanDom.style.left =  left +"px";
            doms.spanDom.style.top = top +"px";

            //拖拽层移动的比例
            var sbit = left / maxLeft;
            var ybit = top / maxTop;
            //拖过图片层减去父元素maxImgbox的宽度，因为要把图片限制在可视区域中
            var sleft = (doms.maxImgDom.offsetWidth-doms.maxImgDom.parentElement.offsetWidth) * sbit*-1;
            var stop= (doms.maxImgDom.offsetHeight-doms.maxImgDom.parentElement.offsetHeight) *ybit*-1;
            //赋予图片位置
            doms.maxImgDom.style.top = stop+"px";
            doms.maxImgDom.style.left = sleft+"px";
        };
    };

}

//准备dom
function reayDoms(){
    //创建大图层盒子
    var  maxBoxDom  =  document.createElement("div");
    maxBoxDom.id = "maxImgbox";
    maxBoxDom.style.cssText="width:800px;height:800px;position:absolute;bottom:0px;left:0px;display:none;overflow: hidden;border:10px solid #e5e5e5;background:#fff;";
    //创建大图层里面的图片
    var  maxImgDom  =  document.createElement("img");
    maxImgDom.id = "maxImg";
    maxImgDom.style.cssText ="position:absolute;display:block";

    maxBoxDom.appendChild(maxImgDom);
    document.body.appendChild(maxBoxDom);

    //动态创建父元素imgbox
    var  imgbox = document.createElement("div");
    imgbox.id = "imgbox";
    imgbox.style.cssText = "height:100px;width:100px;position:relative;top:0;left:0;margin: 0 auto;";
    //动态创建兄弟元素span （即 拖拽层）
    var spanDom = document.createElement("span");
    spanDom.style.cssText = "width:50px;height:50px;background:yellow;display:block;position:absolute;top:0;left:0;opacity:0.5;filter:alpha(opacity=50);display:none";
    //动态创建挡板层  mark
    var markDom = document.createElement("div");
    markDom.id = "mark";
    markDom.style.cssText="position:absolute;top:0;left:0;width:100%;height:100%;opacity:0;filter:alpha(opacity=0);cursor:move;";
    return {
        maxBoxDom : maxBoxDom,//装大图的盒子
        maxImgDom : maxImgDom, //大图对象
        imgbox : imgbox,  //装小图和挡板和拖拽层的盒子
        spanDom : spanDom, //拖拽层
        markDom : markDom //挡板
    }

}




