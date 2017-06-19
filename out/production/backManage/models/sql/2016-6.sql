

--订单首页查询
select * from (select od.id,od.orderno,od.createtime,od.companyName ,od.frontuserName,od.orderstatuscode ,a.name backUserName from account a
 right join
(select o.id, o.orderno , to_char(o.createtime ,'yyyy-mm-dd hh:mm:ss') createtime ,c.name companyName ,f.name frontuserName , o.operationid, o.orderstatuscode
from rec_order o ,rec_company c , rec_frontuser f
where o.guestid = f.id and c.id = f.companyid
) od
on  od.operationid = a.id )
order by createtime
