package com.linzi.daily.template.gp.domain;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.linzi.daily.template.gp.entity.*;
import com.linzi.daily.template.gp.enums.Element;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lil
 */
@Getter
public abstract class AbstractLabelTemplate {

    private final Layout layout;

    public AbstractLabelTemplate(Layout layout){
        this.layout = layout;
    }

    /**
     * 动态字段替换
     * @param dataJson 字段数据json
     */
    public void replaceDynamicValue(JSONObject dataJson){
        if(dataJson.isEmpty()){
            return;
        }
        List<BaseElement> dynamicList =  layout.getLayer().stream().filter(BaseElement::getIsField).toList();
        for(BaseElement element:dynamicList){
            if(dataJson.containsKey(element.getFieldName())){
                element.setValue(dataJson.getStr(element.getFieldName()));
            }
        }
    }

    /**
     * 构建标签指令数据
     * @return String
     */
    public String parseLabelTemplate(){
        StringBuilder template = new StringBuilder();
        template.append(init()).append(buildHeader());
        for(BaseElement element:layout.getLayer()){
            switch (element.getType()){
                case TEXT -> template.append(handleTextElement((TextElement) element));
                case BARCODE -> template.append(handleBarCodeElement((BarCodeElement) element));
                case QRCODE -> template.append(handleQrCodeElement((QrCodeElement) element));
                case SHAPE -> template.append(handleShapeElement((ShapeElement) element));
                case LINE -> template.append(handleLineElement((LineElement) element));
                case VLINE -> template.append(handleVLineElement((VLineElement) element));
                case IMAGE -> template.append(handleImageElement((ImageElement) element));
                default -> {}
            }
        }
        template.append(buildTail());
        return template.toString();
    }

    protected abstract String init();

    protected abstract String buildHeader();

    protected abstract String handleTextElement(TextElement textElement);

    protected abstract String handleBarCodeElement(BarCodeElement barCodeElement);

    protected abstract String handleQrCodeElement(QrCodeElement qrCodeElement);

    protected abstract String handleShapeElement(ShapeElement shapeElement);

    protected abstract String handleLineElement(LineElement lineElement);

    protected abstract String handleVLineElement(VLineElement vLineElement);

    protected abstract String handleImageElement(ImageElement imageElement);

    protected abstract String buildTail();
}
