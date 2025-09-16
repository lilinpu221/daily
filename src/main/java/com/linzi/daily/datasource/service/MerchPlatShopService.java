package com.linzi.daily.datasource.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.linzi.daily.datasource.poscom.dao.MerchPlatShopDao;
import com.linzi.daily.datasource.poscom.entity.MerchPlatShopDo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @author Lil
 */
@Service
public class MerchPlatShopService {

    @Resource
    private MerchPlatShopDao merchPlatShopDao;

    public void fixListShop(){
        List<MerchPlatShopDo> merchPlatShopDos = merchPlatShopDao.listValidShop();
        merchPlatShopDos = merchPlatShopDos.stream().peek(merchPlatShopDo -> {
            JSONArray deviceCodes = merchPlatShopDo.getDeviceCodes();
            deviceCodes.forEach(deviceCode -> {
                JSONObject deviceCodeObj = (JSONObject) deviceCode;
                deviceCodeObj.put("expireTime", DateUtil.now());
            });
            merchPlatShopDo.setDeviceCodes(deviceCodes);
        }).toList();
        merchPlatShopDao.updateBatchById(merchPlatShopDos,1000);
    }
}
