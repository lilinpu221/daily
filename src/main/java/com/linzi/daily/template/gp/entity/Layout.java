package com.linzi.daily.template.gp.entity;

import com.linzi.daily.template.gp.enums.Density;
import com.linzi.daily.template.gp.enums.Speed;
import com.linzi.daily.template.gp.enums.Method;
import com.linzi.daily.template.gp.enums.PaperType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import java.util.List;

/**
 * @author Lil
 */
@Getter
@Setter
public class Layout {

    private String templateType;
    /**
     * 标签名称
     */
    private String projectName;
    /**
     * 打印宽度(mm)
     */
    @Range(min = 5, max = 150,message = "打印宽度范围5-150mm")
    private Integer width;
    /**
     * 打印高度(mm)
     */
    @Range(min = 5, max = 300,message = "打印高度范围5-300mm")
    private Integer height;
    /**
     * 出纸方向
     */
    private Integer direction = 0;
    /**
     * 打印列数
     */
    @Range(min = 1, max = 4,message = "最少1列最多4列")
    private Integer column = 1;
    /**
     * 列间距(mm)
     */
    @Range(min = 1, max = 10,message = "列间距范围1-10mm")
    private Integer columnGap = 0;

    /**
     * 打印速度
     */
    private Speed speed = Speed.NORMAL;

    /**
     * 打印浓度
     */
    private Density density = Density.NORMAL;

    /**
     * 撕纸模式(默认启用撕纸)
     */
    private Boolean tear = true;

    /**
     * 打印方式(默认热敏)
     */
    private Method mode = Method.THERMAL;

    /**
     * 纸张类型(默认间隙纸)
     */
    private PaperType paperType = PaperType.GAP;

    /**
     * 间隙纸宽度(mm)
     */
    @Range(min = 0, max = 10,message = "间隙范围0-10mm")
    private Integer gap = 2;

    /**
     * 出纸上下偏移量(mm)
     */
    private Integer shift = 0;

    /**
     * 打印联数
     */
    private Integer number = 1;

    private List<BaseElement> layer;

    public Layout(Integer width, Integer height){
        this.width = width;
        this.height = height;
    }
}
