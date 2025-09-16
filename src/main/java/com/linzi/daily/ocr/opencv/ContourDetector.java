package com.linzi.daily.ocr.opencv;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 轮廓检测并标记红线
 * @author Lil
 */
public class ContourDetector {

    static {
        // 加载OpenCV本地库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {
        // 读取图像
        Mat image = Imgcodecs.imread("F:\\C200I.png");
        if (image.empty()) {
            System.out.println("无法加载图像文件");
            return;
        }
        // 检测条码区域
        Mat processedImage = detectRegion(image, new Size(27, 4),new Scalar(0, 0, 255));
        Imgcodecs.imwrite("F:\\M220_BOX_covert.png", processedImage);
    }

    /**
     * 检测图像中的条码区域
     * @param image 输入图像
     * @return 条码区域的矩形坐标，如果未检测到则返回null
     */
    public static Mat detectRegion(Mat image,Size structSize,Scalar bordeColor) {
        // 转换为灰度图
        Mat gray = new Mat();
        Imgproc.cvtColor(image, gray, Imgproc.COLOR_BGR2GRAY);
        // 计算X方向的梯度
        Mat gradX = new Mat();
        Imgproc.Sobel(gray, gradX, CvType.CV_32F, 1, 0);
        // 将梯度转换为绝对值
        Core.convertScaleAbs(gradX, gradX);
        // 对梯度图像进行模糊处理
        Imgproc.blur(gradX, gradX, new Size(5, 5));
        // 二值化处理
        Mat thresh = new Mat();
        Imgproc.threshold(gradX, thresh, 0, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
        // 形态学闭操作（先膨胀后腐蚀）以连接条码区域
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT,structSize);
        Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_CLOSE, kernel);
        // 去除小的噪点
        Mat eroded = new Mat();
        Imgproc.erode(thresh, eroded, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        Imgproc.dilate(eroded, thresh, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        // 查找轮廓
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat processedImage = image.clone();
        // 遍历轮廓，寻找最可能是条码的区域
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            // 跳过太小的区域
            if (rect.width < 20 || rect.height < 20) {
                continue;
            }
            System.out.println( rect);
            // 从原始图像中提取区域
            Mat region = new Mat(image, rect);
            Imgcodecs.imwrite("F:\\" + UUID.randomUUID() + ".png", region);
//            Imgproc.rectangle(
//                    processedImage,
//                    new Point(rect.x, rect.y),
//                    new Point(rect.x + rect.width, rect.y + rect.height),
//                    bordeColor,
//                    1
//            );
        }
        return processedImage;
    }
}
