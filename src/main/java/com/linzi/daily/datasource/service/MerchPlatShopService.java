package com.linzi.daily.datasource.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.linzi.daily.datasource.poscom.dao.MerchPlatShopDao;
import com.linzi.daily.datasource.poscom.entity.MerchPlatShopDo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Date;
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
                deviceCodeObj.putOpt("expireTime", DateUtil.now());
            });
            merchPlatShopDo.setDeviceCodes(deviceCodes);
        }).toList();
        merchPlatShopDao.updateBatchById(merchPlatShopDos,1000);
    }
}
