package com.linzi.daily.datasource.poscom.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.linzi.daily.datasource.poscom.entity.MerchPlatShopDo;
import com.linzi.daily.datasource.poscom.mapper.MerchPlatShopMapper;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @author Lil
 */
@Component
public class MerchPlatShopDao extends ServiceImpl<MerchPlatShopMapper, MerchPlatShopDo> {

    public List<MerchPlatShopDo> listValidShop(){
        QueryWrapper<MerchPlatShopDo> queryWrapper = new QueryWrapper<MerchPlatShopDo>()
                .gt("expire_time",new Date())
                .apply("JSON_LENGTH(device_codes) > 0");
        return this.list(queryWrapper);
    }

}
