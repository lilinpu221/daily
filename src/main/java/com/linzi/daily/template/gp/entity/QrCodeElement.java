package com.linzi.daily.template.gp.entity;

import com.linzi.daily.template.gp.enums.Element;
import com.linzi.daily.template.gp.enums.ErrorCorrection;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

/**
 * 二维码元素
 * 不支持旋转，不需要转图片实现，可以通过指令实现
 */
@Getter
@Setter
public class QrCodeElement extends BaseElement{

    private ErrorCorrection level = ErrorCorrection.M;

    public QrCodeElement(){
        setType(Element.QRCODE);
    }

    @Override
    public Boolean needToImg() {
        return false;
    }

    @Override
    public BufferedImage getImage() {
        return null;
    }
}
