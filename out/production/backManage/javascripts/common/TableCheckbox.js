 /**
 * 表格全选 单选   的复选框js 插件
 * @param name
  * 使用时，<tr name="includeCheckbox">，tr的name属性必须为includeCheckbox
 *
 */

 $(function(){

     function  TableCheckbox(){
         //接收id或id集合
         $("<input>",{"class":"txtValue",value:"","type":"hidden"}).appendTo(document.body);
         var arr = [];
         //var $thr = $('table thead tr[name="includeCheckbox"]');
         var $thr = $("table thead tr[name='includeCheckbox']");
         var $checkAllTh = $('<th><input type="checkbox" id="checkAll" name="checkAll" /></th>');
         //将全选/反选复选框添加到表头最前，即增加一列
         $thr.prepend($checkAllTh);
         //“全选/反选”复选框
         var $checkAll = $thr.find('input');
         $checkAll.click(function(event){
             //将所有行的选中状态设成全选框的选中状态
             $tbr.find('input').prop('checked',$(this).prop('checked'));
             //并调整所有选中行的CSS样式
             if ($(this).prop('checked')) {
                 $tbr.find('input').parent().parent().addClass('warning');
                 $tbr.find('input').parent().next().each(function(){
                     arr.push($(this).text());
                 });
             } else{
                 $tbr.find('input').parent().next()
                 $tbr.find('input').parent().parent().removeClass('warning');
                 arr = [];
             }
             //阻止向上冒泡，以防再次触发点击操作
             event.stopPropagation();
             $(".txtValue").val(arr.toString());
         });

         // 点击全选框所在单元格时也触发全选框的点击操作
         $checkAllTh.click(function(){
             $(this).find('input').click();
         });

         var $tbr = $("table tbody tr[name='includeCheckbox']");
         var $checkItemTd = $('<td><input type="checkbox" name="checkItem" /></td>');
         //每一行都在最前面插入一个选中复选框的单元格
         $tbr.prepend($checkItemTd);
         //点击每一行的选中复选框时
         $tbr.find('input').click(function(event){
             //调整选中行的CSS样式
             $(this).parent().parent().toggleClass('warning');
             if($(this).parent().parent().attr("class") == "warning"){
                 if(arr.indexOf($(this).parent().next().text())==-1){
                     arr.push($(this).parent().next().text())
                 }
                 $(".txtValue").val(arr.toString());
             }else{
                 var  index = arr.indexOf($(this).parent().next().text()) ;
                 if(index != -1){
                     arr.splice(index,1);
                 }
                 $(".txtValue").val(arr.toString());
             }
             //如果已经被选中行的行数等于表格的数据行数，将全选框设为选中状态，否则设为未选中状态
             $checkAll.prop('checked',$tbr.find('input:checked').length == $tbr.length ? true : false);
             //阻止向上冒泡，以防再次触发点击操作
             event.stopPropagation();
         });

         //点击每一行时也触发该行的选中操作
         $tbr.click(function(){
             $(this).find('input').click();
         });
     }

     //初始化 生成checkbox复选框
     TableCheckbox();

 });
