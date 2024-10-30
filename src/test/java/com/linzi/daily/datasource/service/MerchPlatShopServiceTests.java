package com.linzi.daily.datasource.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MerchPlatShopServiceTests {

    @Autowired
    MerchPlatShopService merchPlatShopService;
    @Test
    public void fixListShop(){
        merchPlatShopService.fixListShop();
    }
}
