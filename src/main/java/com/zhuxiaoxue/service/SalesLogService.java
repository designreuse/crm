package com.zhuxiaoxue.service;

import com.zhuxiaoxue.mapper.SalesLogMapper;
import com.zhuxiaoxue.mapper.SalesMapper;
import com.zhuxiaoxue.pojo.Sales;
import com.zhuxiaoxue.pojo.SalesLog;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import javax.inject.Named;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Named
public class SalesLogService {

    @Inject
    private SalesLogMapper salesLogMapper;
    @Inject
    private SalesMapper salesMapper;

    /**
     * 根据salesid查询跟进记录
     * @param id salesid
     * @return
     */
    public List<SalesLog> findBySalesid(Integer id) {
        return salesLogMapper.findBySalesid(id);
    }

    public void addLog(SalesLog salesLog,Integer id) {
        salesLog.setSalesid(id);
        salesLog.setType(SalesLog.TYPE_MANUAL);

        salesLogMapper.saveSalesLog(salesLog);

        Sales sales = salesMapper.findByid(id);
        //获取当前时间，并保存为最后跟进时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sales.setLasttime(df.format(new Date()));
        salesMapper.update(sales);
    }

    @Transactional
    public void editLog(SalesLog salesLog,Integer id) {
        salesLog.setType(SalesLog.TYPE_AUTO);
        salesLog.setSalesid(id);

        salesLogMapper.saveSalesLog(salesLog);

        Sales sales = salesMapper.findByid(id);
        sales.setProgress(salesLog.getContext());

        //获取当前时间，并保存为最后跟进时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sales.setLasttime(df.format(new Date()));
        salesMapper.update(sales);
    }
}