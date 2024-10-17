package com.linzi.daily.template.gp.domain;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.linzi.daily.tcpapi.GpApiUtils;
import com.linzi.daily.template.gp.domain.TscFunc;
import com.linzi.daily.template.gp.entity.*;
import com.linzi.daily.template.gp.enums.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IOException {
        String json = "{\"projectName\":\"默认模版\",\"templateType\":\"ZPL\",\"width\":\"46\",\"height\":50,\"direction\":0,\"column\":1,\"columnGap\":1,\"speed\":4,\"density\":\"NORMAL\",\"tear\":false,\"mode\":\"THERMAL\",\"paperType\":\"COILED\",\"gap\":0,\"shift\":0,\"layer\":[{\"id\":\"J2ACNDML2VE22A7U\",\"x\":8,\"y\":0,\"width\":356,\"height\":389,\"rotation\":0,\"name\":\"形状\",\"type\":\"shape\",\"isField\":false,\"fieldName\":\"\",\"value\":\"\",\"borderWidth\":2,\"shape\":\"ROUND\",\"zIndex\":0,\"active\":true,\"activeresiz\":true,\"isLockAspect\":false,\"isActive\":false,\"handles\":[\"tl\",\"tm\",\"tr\",\"mr\",\"br\",\"bm\",\"bl\",\"ml\"]},{\"id\":\"G1UJ8PHTBGUR63CI\",\"x\":208,\"y\":244,\"width\":144,\"height\":76,\"rotation\":0,\"name\":\"形状\",\"type\":\"shape\",\"isField\":false,\"fieldName\":\"\",\"value\":\"\",\"borderWidth\":2,\"shape\":\"OVAL\",\"zIndex\":0,\"active\":true,\"activeresiz\":true,\"isLockAspect\":false,\"isActive\":false,\"handles\":[\"tl\",\"tm\",\"tr\",\"mr\",\"br\",\"bm\",\"bl\",\"ml\"]},{\"id\":\"1JLEKGBJF3SP251Q\",\"x\":31,\"y\":17.5,\"width\":132,\"height\":25,\"rotation\":0,\"name\":\"文本\",\"type\":\"text\",\"isField\":false,\"fieldName\":\"text\",\"value\":\"123456\",\"fontFamily\":\"SIMSUN\",\"fontSize\":8,\"fontBold\":false,\"fontItalic\":false,\"fontDelete\":false,\"fontUnderline\":false,\"textAlign\":\"LEFT\",\"textWrap\":false,\"letterSpacing\":0,\"zIndex\":0,\"active\":true,\"activeresiz\":true,\"isLockAspect\":false,\"isActive\":false,\"handles\":[\"tl\",\"tm\",\"tr\",\"mr\",\"br\",\"bm\",\"bl\",\"ml\"]},{\"id\":\"AABQC0FMMRVK14KE\",\"x\":10,\"y\":102,\"width\":352,\"height\":4,\"rotation\":0,\"name\":\"横线\",\"type\":\"line\",\"isField\":false,\"fieldName\":\"\",\"value\":\"\",\"borderWidth\":2,\"borderType\":\"SOLID\",\"zIndex\":0,\"active\":true,\"activeresiz\":true,\"isLockAspect\":false,\"isActive\":false,\"handles\":[\"mr\",\"ml\"]},{\"id\":\"UDRPFI60QOQGE5NL\",\"x\":186,\"y\":103,\"width\":2,\"height\":288,\"rotation\":0,\"name\":\"竖线\",\"type\":\"vline\",\"isField\":false,\"fieldName\":\"\",\"value\":\"\",\"borderWidth\":2,\"borderType\":\"SOLID\",\"zIndex\":0,\"active\":true,\"activeresiz\":true,\"isLockAspect\":false,\"isActive\":false,\"handles\":[\"tm\",\"bm\"]},{\"id\":\"010J08184CN4M28V\",\"x\":26,\"y\":122,\"width\":145,\"height\":78,\"rotation\":0,\"name\":\"条形码\",\"type\":\"barcode\",\"isField\":false,\"fieldName\":\"barcode\",\"value\":\"123456\",\"barformat\":\"CODE39\",\"bardisplay\":true,\"fontFamily\":\"ARIAL\",\"fontSize\":10,\"zIndex\":0,\"active\":true,\"activeresiz\":true,\"isLockAspect\":false,\"isActive\":false,\"handles\":[\"tl\",\"tr\",\"br\",\"bl\"]},{\"id\":\"3CMOP6ACCT4AE60K\",\"x\":225,\"y\":263,\"width\":100,\"height\":25,\"rotation\":0,\"name\":\"图片 1\",\"type\":\"image\",\"isField\":false,\"fieldName\":\"image\",\"imgType\":\"URL\",\"value\":\"https://www.poscom.cn/images/gainscha.gif\",\"zIndex\":0,\"active\":true,\"activeresiz\":true,\"isLockAspect\":false,\"isActive\":false,\"handles\":[\"tl\",\"tm\",\"tr\",\"mr\",\"br\",\"bm\",\"bl\",\"ml\"]},{\"id\":\"TV6TA8IV9V8JN080\",\"x\":244,\"y\":11,\"width\":76,\"height\":76,\"rotation\":0,\"name\":\"形状\",\"type\":\"shape\",\"isField\":false,\"fieldName\":\"\",\"value\":\"\",\"borderWidth\":2,\"shape\":\"CIRCLE\",\"zIndex\":0,\"active\":true,\"activeresiz\":true,\"isLockAspect\":false,\"isActive\":false,\"handles\":[\"tl\",\"tm\",\"tr\",\"mr\",\"br\",\"bm\",\"bl\",\"ml\"]},{\"id\":\"EPNP9N4TDCFO4T3Q\",\"x\":258,\"y\":18,\"width\":52,\"height\":59,\"rotation\":0,\"name\":\"文本\",\"type\":\"text\",\"isField\":false,\"fieldName\":\"text\",\"value\":\"@\",\"fontFamily\":\"SIYUANHEI\",\"fontSize\":18,\"fontBold\":false,\"fontItalic\":false,\"fontDelete\":false,\"fontUnderline\":false,\"textAlign\":\"LEFT\",\"textWrap\":false,\"letterSpacing\":0,\"zIndex\":0,\"active\":true,\"activeresiz\":true,\"isLockAspect\":false,\"isActive\":false,\"handles\":[\"tl\",\"tm\",\"tr\",\"mr\",\"br\",\"bm\",\"bl\",\"ml\"]},{\"id\":\"5EM1ES7OR62UHMCL\",\"x\":203,\"y\":119,\"width\":50,\"height\":50,\"rotation\":0,\"level\":\"M\",\"name\":\"二维码\",\"type\":\"qrcode\",\"isField\":false,\"fieldName\":\"qrcode\",\"cellWidth\":\"4\",\"value\":\"www.poscom.cn\",\"zIndex\":0,\"active\":true,\"activeresiz\":false,\"isLockAspect\":true,\"isActive\":false,\"handles\":[\"tl\",\"tr\",\"br\",\"bl\"]},{\"id\":\"86AKFMM2H8ASNK15\",\"x\":31,\"y\":50,\"width\":246,\"height\":36,\"rotation\":0,\"name\":\"文本\",\"type\":\"text\",\"isField\":false,\"fieldName\":\"text\",\"value\":\"中国人民解放军\",\"fontFamily\":\"SIMSUN\",\"fontSize\":10,\"fontBold\":false,\"fontItalic\":false,\"fontDelete\":true,\"fontUnderline\":true,\"textAlign\":\"LEFT\",\"textWrap\":false,\"letterSpacing\":0,\"zIndex\":0,\"active\":true,\"activeresiz\":true,\"isLockAspect\":false,\"isActive\":false,\"handles\":[\"tl\",\"tm\",\"tr\",\"mr\",\"br\",\"bm\",\"bl\",\"ml\"]},{\"id\":\"8J7L6T658PV9QIE9\",\"x\":31,\"y\":208,\"width\":169,\"height\":26,\"rotation\":0,\"name\":\"文本\",\"type\":\"text\",\"isField\":false,\"fieldName\":\"text\",\"value\":\"ABCABCABA\",\"fontFamily\":\"SIMSUN\",\"fontSize\":8,\"fontBold\":false,\"fontItalic\":false,\"fontDelete\":false,\"fontUnderline\":false,\"textAlign\":\"LEFT\",\"textWrap\":false,\"letterSpacing\":0,\"zIndex\":0,\"active\":true,\"activeresiz\":true,\"isLockAspect\":false,\"isActive\":true,\"handles\":[\"tl\",\"tm\",\"tr\",\"mr\",\"br\",\"bm\",\"bl\",\"ml\"]},{\"id\":\"3JHTTT9ISELGF1EF\",\"x\":302,\"y\":119,\"width\":50,\"height\":50,\"rotation\":0,\"level\":\"M\",\"name\":\"二维码\",\"type\":\"qrcode\",\"isField\":false,\"fieldName\":\"qrcode\",\"cellWidth\":2,\"value\":\"www.poscom.cn\",\"zIndex\":0,\"active\":true,\"activeresiz\":false,\"isLockAspect\":true,\"isActive\":false,\"handles\":[\"tl\",\"tr\",\"br\",\"bl\"]},{\"id\":\"77KTAS4FQHMSULAA\",\"x\":26,\"y\":248,\"width\":50,\"height\":50,\"rotation\":0,\"level\":\"M\",\"name\":\"二维码\",\"type\":\"qrcode\",\"isField\":false,\"fieldName\":\"qrcode\",\"cellWidth\":\"3\",\"value\":\"www.poscom.cn\",\"zIndex\":0,\"active\":true,\"activeresiz\":false,\"isLockAspect\":true,\"isActive\":false,\"handles\":[\"tl\",\"tr\",\"br\",\"bl\"]}],\"isBlin\":\"COILED\"}";
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
        GpApiUtils apiUtils = new GpApiUtils();
        apiUtils.sendMsg("20190115017903075","ZPL","2","4",labelStr);
        //apiUtils.sendMsg("dmemrzw423t","TSPL","3","1",labelStr);
    }
}
