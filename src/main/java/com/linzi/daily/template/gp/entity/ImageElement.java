package com.linzi.daily.template.gp.entity;

import com.linzi.daily.template.gp.enums.ImageType;
import lombok.Getter;
import lombok.Setter;

import java.awt.image.BufferedImage;

@Getter
@Setter
public class ImageElement extends BaseElement{

    private ImageType imgType = ImageType.URL;

    @Override
    public Boolean needToImg() {
        return false;
    }

    @Override
    public BufferedImage getImage() {
        return null;
    }
}
