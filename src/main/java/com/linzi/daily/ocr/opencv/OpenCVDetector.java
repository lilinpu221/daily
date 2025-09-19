package com.linzi.daily.ocr.opencv;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.awt.image.BufferedImage;
import java.util.*;

public class OpenCVDetector {

    static {
        // 加载OpenCV本地库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        OpenCVDetector detector = new OpenCVDetector();
        List<BaseResult> results = detector.process("D:\\work\\C200I.png");
        for (BaseResult result : results){
            System.out.println(result);
        }
    }

    /**
     * 处理本地图片包含的标签组件
     * @param imgPath 图片绝对路径
     * @return List<BaseResult>
     */
    public List<BaseResult> process(String imgPath){
        Mat colorImage = Imgcodecs.imread(imgPath, Imgcodecs.IMREAD_COLOR);
        if(colorImage.empty()){
            throw new RuntimeException("无法读取原始图片，请检查图片路径");
        }

        List<BaseResult> results = new ArrayList<>();
        Mat grayImage = new Mat();
        try {
            // 转换为灰度图
            Imgproc.cvtColor(colorImage, grayImage, Imgproc.COLOR_BGR2GRAY);
            //边界检测
            List<MatOfPoint> regions = detectRegion(grayImage, new Size(27, 4));
            for (MatOfPoint region : regions) {
                Rect rect = Imgproc.boundingRect(region);
                // 跳过太小的区域
                if (rect.width < 20 || rect.height < 20) {
                    continue;
                }
                Mat rectMat = new Mat(grayImage, rect);
                BufferedImage bufferedImage = matToBufferedImage(rectMat);
                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                BaseResult baseResult = identifyRect(rect, bitmap);
                if (baseResult != null){
                    results.add(baseResult);
                }
                // 释放临时Mat对象
                rectMat.release();
            }
        } finally {
            // 确保资源被释放
            colorImage.release();
            grayImage.release();
        }
        return results;
    }

    /**
     * 分割图片有内容的轮廓区域
     * @param grayImage Mat 这里应该是灰度图像
     * @param structSize 形态学操作结构大小
     * @return List<MatOfPoint>
     */
    private List<MatOfPoint> detectRegion(Mat grayImage, Size structSize) {
        Mat gradX = new Mat();
        Mat thresh = new Mat();
        Mat eroded = new Mat();
        Mat hierarchy = new Mat();
        try {
            // 计算X方向的梯度
            Imgproc.Sobel(grayImage, gradX, CvType.CV_32F, 1, 0);
            // 将梯度转换为绝对值
            Core.convertScaleAbs(gradX, gradX);
            // 对梯度图像进行模糊处理
            Imgproc.blur(gradX, gradX, new Size(5, 5));
            // 二值化处理
            Imgproc.threshold(gradX, thresh, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
            // 形态学闭操作（先膨胀后腐蚀）
            Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, structSize);
            Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_CLOSE, kernel);
            // 去除小的噪点
            Imgproc.erode(thresh, eroded, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            Imgproc.dilate(eroded, thresh, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
            // 查找轮廓
            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
            return contours;
        } finally {
            // 释放中间Mat对象
            gradX.release();
            thresh.release();
            eroded.release();
            hierarchy.release();
        }
    }

    /**
     * 识别轮廓区域类型
     * @param src Rect
     * @param bitmap BinaryBitmap
     * @return BaseResult
     */
    private BaseResult identifyRect(Rect src, BinaryBitmap bitmap){
        //先判断是否二维码
        QRCodeReader qrCodeReader = new QRCodeReader();
        // 配置解析器，设置快速解析模式
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        try {
            Result result = qrCodeReader.decode(bitmap, hints);
            if(CharSequenceUtil.isNotBlank(result.getText())){
                return new QrcodeResult(src.x, src.y, src.width, src.height, "QRCODE", result.getText());
            }
        } catch (NotFoundException | ChecksumException | FormatException ignored) {
        }
        //再判断是否条码
        try {
            Result result = new MultiFormatReader().decode(bitmap);
            if(CharSequenceUtil.isNotBlank(result.getText())){
                return new BarcodeResult(src.x, src.y, src.width, src.height,"BARCODE", result.getText(),result.getBarcodeFormat().name());
            }
        } catch (NotFoundException ignored) {
        }
        //再判断是否线条
        try {
            BitMatrix matrix = bitmap.getBlackMatrix();
            // 计算黑色像素的比例
            double blackPixelRatio = calculateBlackPixelRatio(matrix);
            // 计算区域的宽高比，判断是水平线还是垂直线
            double aspectRatio = (double) src.width / src.height;
            // 添加更严格的条件判断
            boolean isHorizontal = aspectRatio > 3 && src.height < 50;
            boolean isVertical = aspectRatio < 0.33 && src.width < 50;
            // 确保黑色像素比例足够高（至少70%）
            if((isHorizontal || isVertical) && blackPixelRatio > 0.7){
                // 计算线条粗细
                double thickness = calculateLineThickness(matrix, isHorizontal);
                if(thickness > 0){
                    int direction = isHorizontal ? 0 : 1;
                    return new LineResult(src.x, src.y, src.width, src.height, "LINE", direction, thickness);
                }
            }
        } catch (NotFoundException ignored) {
        }
        return null;
    }

    /**
     *   添加计算黑色像素比例的方法
     */
    private double calculateBlackPixelRatio(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int blackPixels = 0;
        int totalPixels = width * height;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (matrix.get(x, y)) {
                    blackPixels++;
                }
            }
        }
        return (double) blackPixels / totalPixels;
    }

    /**
     * 计算线条粗细
     * @param matrix 二值图像矩阵
     * @param isHorizontal 是否为水平线
     * @return 线条粗细（像素）
     */
    private double calculateLineThickness(BitMatrix matrix, boolean isHorizontal) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        if (isHorizontal) {
            // 对于水平线，计算垂直方向的平均黑色像素连续长度
            int[] verticalRunLengths = new int[height];
            for (int y = 0; y < height; y++) {
                int runLength = 0;
                int maxRunLength = 0;
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        runLength++;
                    } else {
                        if (runLength > maxRunLength) {
                            maxRunLength = runLength;
                        }
                        runLength = 0;
                    }
                }
                // 检查最后一组连续像素
                if (runLength > maxRunLength) {
                    maxRunLength = runLength;
                }
                verticalRunLengths[y] = maxRunLength;
            }
            // 计算平均粗细（忽略零值）
            return Arrays.stream(verticalRunLengths)
                    .filter(len -> len > 0)
                    .average()
                    .orElse(0);
        } else {
            // 对于垂直线，计算水平方向的平均黑色像素连续长度
            int[] horizontalRunLengths = new int[width];
            for (int x = 0; x < width; x++) {
                int runLength = 0;
                int maxRunLength = 0;
                for (int y = 0; y < height; y++) {
                    if (matrix.get(x, y)) {
                        runLength++;
                    } else {
                        if (runLength > maxRunLength) {
                            maxRunLength = runLength;
                        }
                        runLength = 0;
                    }
                }
                // 检查最后一组连续像素
                if (runLength > maxRunLength) {
                    maxRunLength = runLength;
                }
                horizontalRunLengths[x] = maxRunLength;
            }
            // 计算平均粗细（忽略零值）
            return Arrays.stream(horizontalRunLengths)
                    .filter(len -> len > 0)
                    .average()
                    .orElse(0);
        }
    }

    /**
     * 精确提取轮廓区域内容
     */
    private Mat extractPreciseRegion(Mat sourceImage, MatOfPoint contour, Rect boundingRect) {
        // 创建一个白色背景的图像
        Mat result = new Mat(boundingRect.size(), sourceImage.type(), new Scalar(255));

        // 创建掩码
        Mat mask = Mat.zeros(boundingRect.size(), CvType.CV_8UC1);

        // 调整轮廓坐标到ROI坐标系
        MatOfPoint adjustedContour = new MatOfPoint();
        Point[] points = contour.toArray();
        for (int i = 0; i < points.length; i++) {
            points[i] = new Point(points[i].x - boundingRect.x, points[i].y - boundingRect.y);
        }
        adjustedContour.fromArray(points);

        // 在掩码上绘制轮廓
        Imgproc.drawContours(mask, Arrays.asList(adjustedContour), -1, new Scalar(255), -1);

        // 提取ROI区域
        Mat roi = new Mat(sourceImage, boundingRect);

        // 将ROI内容复制到白色背景上
        roi.copyTo(result, mask);

        // 释放资源
        mask.release();
        roi.release();
        adjustedContour.release();

        return result;
    }

    /**
     * mat 转 BufferedImage
     * @param mat Mat
     * @return BufferedImage
     */
    private BufferedImage matToBufferedImage(Mat mat) {
        // 确保Mat是8位无符号类型
        if (mat.depth() != CvType.CV_8U) {
            Mat converted = new Mat();
            mat.convertTo(converted, CvType.CV_8U);
            mat = converted;
        }

        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        // 创建BufferedImage
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);

        // 获取数据
        byte[] data = new byte[mat.cols() * mat.rows() * mat.channels()];
        mat.get(0, 0, data);

        // 设置数据
        if (type == BufferedImage.TYPE_BYTE_GRAY) {
            // 灰度图像
            for (int y = 0; y < mat.rows(); y++) {
                for (int x = 0; x < mat.cols(); x++) {
                    int gray = data[y * mat.cols() + x] & 0xFF;
                    image.setRGB(x, y, (gray << 16) | (gray << 8) | gray);
                }
            }
        } else {
            // BGR图像
            for (int y = 0; y < mat.rows(); y++) {
                for (int x = 0; x < mat.cols(); x++) {
                    int index = (y * mat.cols() + x) * 3;
                    int b = data[index] & 0xFF;
                    int g = data[index + 1] & 0xFF;
                    int r = data[index + 2] & 0xFF;
                    image.setRGB(x, y, (r << 16) | (g << 8) | b);
                }
            }
        }

        return image;
    }
}