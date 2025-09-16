package com.linzi.daily.datasource.poscom.entity;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.Fastjson2TypeHandler;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Lil
 */
@Data
@TableName(value = "t_pa_merchplatshop", autoResultMap = true)
public class MerchPlatShopDo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer merchId;

    private Integer platformId;

    private String platformUserid;

    private String platformShopid;

    private String platformShopname;

    private Date createTime;

    private Date updateTime;
    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private JSONArray deviceCodes;

    private String autoConfirm;

    @TableField(typeHandler = Fastjson2TypeHandler.class)
    private JSONObject welcome;

    private Date expireTime;
}
