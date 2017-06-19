


--2015-5-31 根据合同code查询合同状态
select case levelcode
when '001' then '买方'
when '001001' then '初始'
when '001002' then '平台已盖章'
when '001003' then '需平台修订 '
when '001004' then '平台已修订'
when '001005' then '买方已盖章 '
when '001006' then '需协助上传 '
when '002' then '卖方'
when '002001' then '初始'
when '002002' then '需协助上传'
when '002003' then '卖方已提交 '
when '002004' then '已回传盖章'
when '002005' then '卖方已校对'
else  '其他'  end
levelcode from view_dictype where eng = 'contractStatus' and levelcode = ?


--2016-5-30 查询商品信息 名称 生产商  仓库 单价 数量 总结
select g.mark , g.producer , ss.pricehastax ,ss.initnum , ss.finalmoney
from contract c ,Rec_Order_Stock os ,rec_order o, rec_stock ss ,dic_Storage st, dic_good g
where c.orderid = o.id and os.orderid = o.id and os.stockid = ss.id and ss.goodid = g.id and ss.storageid = st.id
and ss.stockcode = '004' and c.id = ?


--2016-5-27 测试库 插入 后台原始数据
insert into organization(id, name, levelcode, parentid, position, thevalue, version, enable)
values(  6  ,'业务部','1,6',	1	,	0	,'3',	9	,	0	);

insert into role (ID, NAME, version, enable) values (  4  ,'销售人员',  9  ,  1  );
insert into role (ID, NAME, version, enable) values (  5  ,'客服人员',  9  ,  1  );
insert into role (ID, NAME, version, enable) values (  6	,'合同人员',	9	,	1	);

insert into account(id, name, roleId, organizationId, username, password, email, registertime, version, enable) values(  4  ,'王德轩',  4  ,  6  ,'wdx','5e8667a439c68f5145dd2fcbecf02209','wdx@rj.com',to_date('2016-03-03 11:33:44','yyyy-mm-dd hh24:mi:ss'),  9  , 1  );
insert into account(id, name, roleId, organizationId, username, password, email, registertime, version, enable) values(  5  ,'梁顺伟',  4  ,  6  ,'lsw','5e8667a439c68f5145dd2fcbecf02209','lsw@rj.com',to_date('2016-03-04 11:33:44','yyyy-mm-dd hh24:mi:ss'),  9  ,  1  );
insert into account(id, name, roleId, organizationId, username, password, email, registertime, version, enable) values(  6  ,'李桂东',  4  ,  6  ,'lgd','5e8667a439c68f5145dd2fcbecf02209','lgd@rj.com',to_date('2016-03-05 11:33:44','yyyy-mm-dd hh24:mi:ss'),  9  ,  1  );
insert into account(id, name, roleId, organizationId, username, password, email, registertime, version, enable) values(  7  ,'周伟',  4  ,  6  ,'zw','5e8667a439c68f5145dd2fcbecf02209','zw@rj.com',to_date('2016-03-06 11:33:44','yyyy-mm-dd hh24:mi:ss'),  9  ,  1  );
insert into account(id, name, roleId, organizationId, username, password, email, registertime, version, enable) values(  8  ,'焦玉伟',  4  ,  6  ,'jyw','5e8667a439c68f5145dd2fcbecf02209','jyw@rj.com',to_date('2016-03-07 11:33:44','yyyy-mm-dd hh24:mi:ss'),  9  ,  1  );
insert into account(id, name, roleId, organizationId, username, password, email, registertime, version, enable) values(  9  ,'曹宇',  4  ,  6  ,'cy','5e8667a439c68f5145dd2fcbecf02209','cy@rj.com',to_date('2016-03-08 11:33:44','yyyy-mm-dd hh24:mi:ss'),  9  ,  1  );
insert into account(id, name, roleId, organizationId, username, password, email, registertime, version, enable) values(  10  ,'刘军伟',  4  ,  6  ,'ljw','5e8667a439c68f5145dd2fcbecf02209','ljw@rj.com',to_date('2016-03-09 11:33:44','yyyy-mm-dd hh24:mi:ss'),  9  ,  1  );
insert into account(id, name, roleId, organizationId, username, password, email, registertime, version, enable) values(  11  ,'侯云飞',  4  ,  6  ,'hyf','5e8667a439c68f5145dd2fcbecf02209','hyf@rj.com',to_date('2016-03-10 11:33:44','yyyy-mm-dd hh24:mi:ss'),  9  ,  1  )



--2016-5-27  根据id 查询订单的状态
select
case orderstatuscode
when '001' then '线下支付'
when '001001' then '待确认'
when '001002' then '待支付'
when '001003' then '待收款'
when '001004' then '待发货'
when '001005' then '待收货'
when '001006' then '线下支付'
when '001007' then '待开票'
when '001008' then '待退款退货'
when '00001009' then '已完成'
when '001010' then '已取消'
when '002' then '商务支付'
else '其他' end
orderstatuscode
from  rec_order
where id = 81

--2016-5-27 查询所有的订单状态
select * from view_dictype where eng like '%order%'


--2016-5-25计算订单金额的sql
select s.finalmoney  from rec_Stock s , rec_order_stock os ,rec_order o
where s.id = os.stockid and o.id = os.orderid  and o.id = ?


--2016-5-25查询订单状态
select * from view_dictype where eng = 'orderStatus'


