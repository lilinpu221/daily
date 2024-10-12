package com.linzi.daily.template.jd.text;

import cn.hutool.json.JSONUtil;
import com.linzi.daily.template.jd.BaseItem;
import com.linzi.daily.template.jd.Tool;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 字体组件
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TextItem extends BaseItem {
    /**
     * 字体，暂不支持
     */
    @Deprecated
    private String fontFamily;

    /**
     * 字号
     */
    private int fontSize;

    /**
     * 字体颜色，暂不支持
     */
    @Deprecated
    private String color;

    /**
     * 字体背景色，暂不支持
     */
    @Deprecated
    private String backgroundColor;

    /**
     * 透明度，暂不支持
     */
    @Deprecated
    private int alpha;

    /**
     * 垂直对齐
     */
    private String alignItem;

    /**
     * 水平对齐
     */
    private String justifyContent;

    /**
     * 字体粗细
     * bold：粗体、正常：normal；
     */
    private String fontWeight;

    /**
     * 字体样式，暂不支持  斜体/正常
     */
    @Deprecated
    private String fontStyle;

    /**
     * 下划线，暂不支持 中横线/下划线/无
     */
    @Deprecated
    private String textDecoration;

    /**
     * 旋转角度
     */
    private int rotate;

    /**
     * 文本输出方式
     * pre-line：文本截断
     * pre-wrap：自动换行
     * compress-wrap：文本自适应压缩，暂未实现
     */
    private String whiteSpace;

    /**
     * 排列方式，暂不支持
     * tb：竖向排列
     * lr：从左到右横向排列
     */
    @Deprecated
    private String writingMode;

    /**
     * 脚本，暂不支持
     */
    @Deprecated
    private String script;
    /**
     * 边框
     */
    private String border;
    @Override
    public String builderTspl() {
        return parseBorder()+parseFont();
    }

    /**
     * 边框处理
     * @return String
     */
    private String parseBorder(){
        if ("1px".equals(this.getBorder())) {
            return "BOX " + this.getLeft() + "," + this.getTop() + "," + (this.getLeft() + this.getWidth()) + "," + (this.getTop() + this.getHeight()) + ",line 1";
        }
        return "";
    }

    /**
     * 文本处理
     * 与文字对齐方式、元素宽和高有关
     * @return String
     */
    private String parseFont(){
        Space space = Space.explain(this.getWhiteSpace());
        if(Space.COMPRESSWRAP==space){
            //自适应压缩，选择字体大小
            this.setFontSize(Tool.adaptiveFonSize(this.getContent(),this.getWidth(),this.getHeight()));
        }
        Font font = Font.explain(this.getFontSize(),this.getContent());
        //文本总换行集合
        List<String> textList = Tool.textWrap(font,this.getWidth(),this.getContent());
        StringBuilder textBuilder = new StringBuilder();
        int x = this.getLeft();
        //y坐标处理(垂直对齐)
        int y=this.getTop();
        if(Space.PRELINE!=space){
            //非截断才需要进行垂直偏移计算
            y += Tool.textVertical(font,textList.size(),this.getHeight(), Align.explain(this.getAlignItem()));
        }
        for(int i=0;i<textList.size();i++) {
            String textContent = textList.get(i);
            if(i==textList.size()-1){
                //最后一行，才需要进行x坐标水平对齐判断
                x = Tool.textHorizontal(font,this.getWidth(),textContent,this.getJustifyContent(),x);
            }
            String text = "TEXT " + x + "," + y + ",\""+font.getFontType()+"\"," + this.getRotate() +","
                    + font.getMultipleW() + "," + font.getMultipleH() + ",\"" + textContent + "\r\n";
            textBuilder.append(text);
            if("bold".equals(this.getFontWeight())){
                //加粗处理(通过偏移1dot重打实现)
                text = "TEXT " + (x-1) + "," + (y-1) + ",\""+font.getFontType()+"\"," + this.getRotate() +","
                        + font.getMultipleW() + "," + font.getMultipleH() + ",\"" + textContent + "\r\n";
                textBuilder.append(text);
                text = "TEXT " + (x+1) + "," + (y+1) + ",\""+font.getFontType()+"\"," + this.getRotate() +","
                        + font.getMultipleW() + "," + font.getMultipleH() + ",\"" + textContent + "\r\n";
                textBuilder.append(text);
            }
            //下一行打印y坐标加上字体高度
            y = y+font.getHeight();
            if(y>this.getHeight()){
                //超过高度
                break;
            }
        }
        return textBuilder.toString();
    }

    public static void main(String[] args) {
        String json = "{\"type\":\"text\",\"top\":11,\"left\":238,\"width\":240,\"height\":48,\"lineHeight\":20,\"fontName\":\"Arial\",\"fontSize\":24,\"fontWeight\":\"bold\",\"alignItem\":\"center\",\"justifyContent\":\"center\",\"content\":\"阿达撒发<发搭>嘎割发代首啊大哥12发的给对方官方dga电饭锅-四方根深蒂固34##电费广东省森岛帆高水电费感受到f\",\"border\":\"none\",\"whiteSpace\":\"compress-wrap\"}";
        TextItem textItem = JSONUtil.toBean(json,TextItem.class);
        System.out.println(textItem.builderTspl());
    }
}
