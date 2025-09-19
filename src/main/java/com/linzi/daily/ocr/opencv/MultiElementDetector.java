package com.linzi.daily.ocr.opencv;


import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.QRCodeDetector;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MultiElementDetector {

    static {
        // 加载OpenCV本地库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        // 读取图像
        Mat image = Imgcodecs.imread("D:\\work\\orc\\M220_BOX.png");
        if (image.empty()) {
            System.out.println("无法加载图像文件");
            return;
        }

        // 检测并保存所有元素区域
        detectAllElements(image,"D:\\work\\orc\\M220_BOX_covert.png");
    }

    /**
     * 检测并保存图像中的所有元素区域
     * @param image 输入图像
     */
    public static void detectAllElements(Mat image,String filePath) {
        // 转换为灰度图
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);

        // 1. 检测二维码区域
        List<Rect> qrCodeRects = detectQRCodeRegions(image);

        // 2. 检测条码区域
        List<Rect> barcodeRects = detectBarcodeRegions(gray);

        // 3. 检测横线区域
        List<Rect> horizontalLineRects = detectHorizontalLines(gray);

        // 4. 检测竖线区域
        List<Rect> verticalLineRects = detectVerticalLines(gray);

        // 5. 检测文本区域
        List<Rect> textRects = detectTextRegions(gray);

        // 合并所有区域并去除重叠
        List<Rect> allRects = new ArrayList<>();
        allRects.addAll(qrCodeRects);
        allRects.addAll(barcodeRects);
        allRects.addAll(horizontalLineRects);
        allRects.addAll(verticalLineRects);
        allRects.addAll(textRects);

        // 合并重叠的区域
        List<Rect> mergedRects = mergeOverlappingRects(allRects);

        // 保存所有检测到的区域
        //saveDetectedRegions(image, mergedRects);

        // 绘制检测结果（可选）
        Mat resultImage = image.clone();
        for (Rect rect : mergedRects) {
            Imgproc.rectangle(resultImage,
                    new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0), 1);
        }
        Imgcodecs.imwrite(filePath, resultImage);

        // 释放资源
        gray.release();
        resultImage.release();
    }

    /**
     * 检测二维码区域
     * @param image 原始图像（彩色）
     * @return 检测到的二维码区域列表
     */
    public static List<Rect> detectQRCodeRegions(Mat image) {
        List<Rect> rects = new ArrayList<>();
        QRCodeDetector qrCodeDetector = new QRCodeDetector();

        Mat points = new Mat();
        boolean detected = qrCodeDetector.detect(image, points);

        if (detected) {
            // 将检测到的点转换为矩形
            for (int i = 0; i < points.rows(); i++) {
                MatOfPoint contour = new MatOfPoint(
                        new Point(points.get(i, 0)),
                        new Point(points.get(i, 1)),
                        new Point(points.get(i, 2)),
                        new Point(points.get(i, 3))
                );
                Rect rect = Imgproc.boundingRect(contour);
                rects.add(rect);
            }
        }

        points.release();
        return rects;
    }

    /**
     * 检测条码区域
     * @param gray 灰度图像
     * @return 检测到的条码区域列表
     */
    public static List<Rect> detectBarcodeRegions(Mat gray) {
        List<Rect> rects = new ArrayList<>();

        // 计算X方向的梯度
        Mat gradX = new Mat();
        Imgproc.Sobel(gray, gradX, CvType.CV_32F, 1, 0);
        Core.convertScaleAbs(gradX, gradX);

        // 对梯度图像进行模糊处理
        Imgproc.blur(gradX, gradX, new Size(5, 5));

        // 二值化处理
        Mat thresh = new Mat();
        Imgproc.threshold(gradX, thresh, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);

        // 形态学闭操作以连接条码区域
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(27, 4));
        Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_CLOSE, kernel);

        // 查找轮廓
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // 筛选可能是条码的区域
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            // 筛选条件：宽高比和面积（条码通常是长条形）
            double aspectRatio = (double) rect.width / rect.height;
            int area = rect.width * rect.height;

            if ((aspectRatio > 2.0 || aspectRatio < 0.5) && area > 500) {
                rects.add(rect);
            }
        }

        // 释放资源
        gradX.release();
        thresh.release();
        kernel.release();
        hierarchy.release();

        return rects;
    }

    /**
     * 检测横线区域
     * @param gray 灰度图像
     * @return 检测到的横线区域列表
     */
    public static List<Rect> detectHorizontalLines(Mat gray) {
        List<Rect> rects = new ArrayList<>();

        // 使用自适应阈值
        Mat binary = new Mat();
        Imgproc.adaptiveThreshold(gray, binary, 255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 5);

        // 使用水平方向的结构元素进行形态学操作
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(30, 1));
        Mat morph = new Mat();
        Imgproc.morphologyEx(binary, morph, Imgproc.MORPH_CLOSE, kernel);

        // 查找轮廓
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(morph, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // 筛选横线
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            // 筛选条件：宽度大于高度，且有一定长度
            if (rect.width > rect.height * 5 && rect.width > 100) {
                rects.add(rect);
            }
        }

        // 释放资源
        binary.release();
        kernel.release();
        morph.release();

        return rects;
    }

    /**
     * 检测竖线区域
     * @param gray 灰度图像
     * @return 检测到的竖线区域列表
     */
    public static List<Rect> detectVerticalLines(Mat gray) {
        List<Rect> rects = new ArrayList<>();

        // 使用自适应阈值
        Mat binary = new Mat();
        Imgproc.adaptiveThreshold(gray, binary, 255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 15, 5);

        // 使用垂直方向的结构元素进行形态学操作
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(1, 30));
        Mat morph = new Mat();
        Imgproc.morphologyEx(binary, morph, Imgproc.MORPH_CLOSE, kernel);

        // 查找轮廓
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(morph, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // 筛选竖线
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            // 筛选条件：高度大于宽度，且有一定长度
            if (rect.height > rect.width * 5 && rect.height > 100) {
                rects.add(rect);
            }
        }

        // 释放资源
        binary.release();
        kernel.release();
        morph.release();

        return rects;
    }

    /**
     * 检测文本区域
     * @param gray 灰度图像
     * @return 检测到的文本区域列表
     */
    public static List<Rect> detectTextRegions(Mat gray) {
        List<Rect> rects = new ArrayList<>();

        // 使用MSER检测文本区域
        Mat mserMask = new Mat();
        Imgproc.adaptiveThreshold(gray, mserMask, 255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY_INV, 11, 2);

        // 形态学操作连接文本区域
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 4));
        Imgproc.morphologyEx(mserMask, mserMask, Imgproc.MORPH_CLOSE, kernel);

        // 查找轮廓
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(mserMask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        // 筛选文本区域
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            // 筛选条件：排除太大或太小的区域
            int area = rect.width * rect.height;
            if (area > 100 && area < 10000) {
                rects.add(rect);
            }
        }

        // 释放资源
        mserMask.release();
        kernel.release();

        return rects;
    }

    /**
     * 合并重叠的矩形区域
     * @param rects 原始矩形列表
     * @return 合并后的矩形列表
     */
    public static List<Rect> mergeOverlappingRects(List<Rect> rects) {
        List<Rect> mergedRects = new ArrayList<>();
        boolean[] merged = new boolean[rects.size()];

        for (int i = 0; i < rects.size(); i++) {
            if (merged[i]) {
                continue;
            }

            Rect current = rects.get(i);
            for (int j = i + 1; j < rects.size(); j++) {
                if (merged[j]) {
                    continue;
                }

                Rect other = rects.get(j);
                // 检查两个矩形是否重叠
                if (current.x < other.x + other.width &&
                        current.x + current.width > other.x &&
                        current.y < other.y + other.height &&
                        current.y + current.height > other.y) {

                    // 合并两个矩形
                    int x = Math.min(current.x, other.x);
                    int y = Math.min(current.y, other.y);
                    int width = Math.max(current.x + current.width, other.x + other.width) - x;
                    int height = Math.max(current.y + current.height, other.y + other.height) - y;

                    current = new Rect(x, y, width, height);
                    merged[j] = true;
                }
            }
            mergedRects.add(current);
            merged[i] = true;
        }

        return mergedRects;
    }

    /**
     * 保存检测到的区域为单独的图像文件
     * @param image 原始图像
     * @param rects 检测到的区域列表
     */
    public static void saveDetectedRegions(Mat image, List<Rect> rects) {
        for (int i = 0; i < rects.size(); i++) {
            Rect rect = rects.get(i);
            // 从原始图像中提取区域
            Mat region = new Mat(image, rect);
            // 保存区域图像
            Imgcodecs.imwrite("D:\\work\\orc\\region_" + i + "_" + UUID.randomUUID() + ".png", region);
            region.release();
        }
    }
}