select tb.bank_name "银行",
       to_char(tor.insert_time, 'dd') "小时",
       count(1) "数量"
  from t_outer_request tor, t_bank tb
 where tor.insert_time >=
       to_date(to_char(sysdate - 1, 'yyyy-mm-dd'), 'yyyy-mm-dd')
      -- and ceil((tor.update_time - tor.insert_time) * 24 * 60 * 60) > 4
   and cate in (102， 2)
   and subject = 1
   and tor.bank = tb.bank_code
   --and tor.bank = '0004'
--and tor.bank in ('0001', '0002', '0003', '0004', '0005', '0006')
 group by tb.bank_name, to_char(tor.insert_time, 'dd')
 order by tb.bank_name, to_char(tor.insert_time, 'dd')
 
 
 
