package com.linzi.daily.template;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.linzi.daily.tcpapi.GpApiUtils;
import com.linzi.daily.template.gp.domain.AbstractLabelTemplate;
import com.linzi.daily.template.gp.domain.TscFunc;
import com.linzi.daily.template.gp.domain.ZplFunc;
import com.linzi.daily.template.gp.entity.*;
import com.linzi.daily.template.gp.enums.Element;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Lil
 */
@Tag(name = "模版请求")
@RequestMapping("/template")
@RestController
public class TemplateController {

    @PostMapping("/gp/test")
    public String gpTestTsc(@RequestBody String json) throws IOException {
        System.out.println("===请求json===");
        System.out.println(json);
        JSONObject jsonObj = new JSONObject(json);
        JSONArray layerArray = jsonObj.getJSONArray("layer");
        jsonObj.remove("layer");
        Layout layout = JSONUtil.toBean(jsonObj, Layout.class);
        List<BaseElement> layerList = new ArrayList<>();
        for(int i=0;i<layerArray.size();i++){
            JSONObject layerObj = layerArray.getJSONObject(i);
            switch (Element.explain(layerObj.getStr("type"))){
                case TEXT -> layerList.add(JSONUtil.toBean(layerObj, TextElement.class));
                case BARCODE -> layerList.add(JSONUtil.toBean(layerObj, BarCodeElement.class));
                case QRCODE -> layerList.add(JSONUtil.toBean(layerObj, QrCodeElement.class));
                case SHAPE -> layerList.add(JSONUtil.toBean(layerObj, ShapeElement.class));
                case LINE -> layerList.add(JSONUtil.toBean(layerObj, LineElement.class));
                case VLINE -> layerList.add(JSONUtil.toBean(layerObj, VLineElement.class));
                case IMAGE -> layerList.add(JSONUtil.toBean(layerObj, ImageElement.class));
                default -> {}
            }
        }
        layout.setLayer(layerList);
        AbstractLabelTemplate labelTemplate;
        if("TSC".equals(layout.getTemplateType())){
            labelTemplate = new TscFunc(layout);
        }else{
            labelTemplate = new ZplFunc(layout);
        }
        String labelStr = labelTemplate.parseLabelTemplate();
        System.out.println("===十六进制打印数据===");
        FileUtil.del("D:\\hex.txt");
        FileUtil.appendString(labelStr, FileUtil.file("D:\\hex.txt"), "UTF-8");
        System.out.println("===发送打印数据===");
        GpApiUtils apiUtils = new GpApiUtils();
        if("TSC".equals(layout.getTemplateType())){
            apiUtils.sendMsg("dmemrzw423t","TSPL","3","1",labelStr);
        }else{
            labelStr = HexUtil.encodeHexStr(labelStr, Charset.forName("UTF-8"));
            apiUtils.sendMsg("20190115017903075","ZPL","3","4",labelStr);
        }
        return "ok";
    }
}
