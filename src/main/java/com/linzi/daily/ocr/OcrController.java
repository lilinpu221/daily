package com.linzi.daily.ocr;

import cn.hutool.json.JSONObject;
import com.linzi.daily.ocr.paddle.PaddleOcrService;
import com.linzi.daily.ocr.tess4j.Tess4jOcrService;
import com.linzi.daily.ocr.service.TsplService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Tag(name = "Ocr请求")
@RequestMapping("/ocr")
@RestController
public class OcrController {

    @Resource
    private Tess4jOcrService tess4jOcrService;
    @Resource
    private PaddleOcrService paddleOcrService;

    @Resource
    private TsplService tsplService;

    @Operation(summary = "tesseract-ocr模型，支持整图识别和局部识别",description = "局部识别，可通过https://uutool.cn/img-coord网站获取图片对应区域的坐标值（注,当前模型使用的最基础的中文模型，如要提高准确率，建议提供更多标准的图片进行训练）")
    @Parameters({@Parameter(name = "x1",description = "区域识别起始x坐标,整图识别请填0"),
            @Parameter(name = "y1",description = "区域识别起始y坐标,整图识别请填0"),
            @Parameter(name = "x2",description = "区域识别结束x坐标,整图识别请填0"),
            @Parameter(name = "y2",description = "区域识别结束y坐标,整图识别请填0")})
    @PostMapping(value="/tess",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String tess(@RequestPart(value = "file") MultipartFile file,
                           @RequestParam(value = "x1") Integer x1,
                           @RequestParam(value = "y1") Integer y1,
                           @RequestParam(value = "x2") Integer x2,
                           @RequestParam(value = "y2") Integer y2) throws IOException, TesseractException {
        if(file==null || file.isEmpty()){
            return "请选择图片文件";
        }
        BufferedImage source = ImageIO.read(file.getInputStream());
        //图片灰度
        BufferedImage target = ImageHelper.convertImageToGrayscale(source);
        //target = ImageHelper.getScaledInstance(target, target.getWidth() * 5, target.getHeight() * 5);
        if(x1>=0&&y1>=0&&x2-x1>0&&y2-y1>0){
            return tess4jOcrService.partOcr(target,x1,y1,x2,y2);
        }else{
            return tess4jOcrService.wholeOcr(target);
        }
    }


    @Operation(summary = "paddle-ocr模型，准确率高",description = "局部识别开发中")
    @PostMapping(value="/paddle",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String paddle(@RequestPart(value = "file") MultipartFile file) throws Exception {
        if(file==null || file.isEmpty()){
            return "请选择图片文件";
        }
        BufferedImage source = ImageIO.read(file.getInputStream());
        //图片灰度
        BufferedImage target = ImageHelper.convertImageToGrayscale(source);
        return paddleOcrService.wholeOcrText(target);
    }

    @Operation(summary = "paddle-ocr模型，并把识别到的文本转为固定模版的TSPL指令",description = "用于演示，实际需要根据具体的模版继续处理")
    @PostMapping(value="/paddle2tspl",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String paddle2tspl(@RequestPart(value = "file") MultipartFile file) throws Exception {
        if(file==null || file.isEmpty()){
            return "请选择图片文件";
        }
        BufferedImage source = ImageIO.read(file.getInputStream());
        //图片灰度
        BufferedImage target = ImageHelper.convertImageToGrayscale(source);
        JSONObject ocrJson = paddleOcrService.wholeOcrJson(target);
        return tsplService.builderJDtemplate(ocrJson);
    }
}
