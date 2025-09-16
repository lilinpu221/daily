package com.linzi.daily.ocr.opencv;



import cn.hutool.core.text.CharSequenceUtil;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;

/**
 * @author Lil
 */
public class ImagePreprocessService {

    static {
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
    }

    public void preprocess(String imagePath) throws InterruptedException {
        /*
         * IMREAD_UNCHANGED=-1 表示不改变加载图像类型，可以包含透明通道
         * IMREAD_GRAYSCALE=0 表示加载的图像为灰度图像
         * IMREAD_COLOR=1 表示加载图像为彩色图像
         * Mat对象中包含了图像各种的基本信息与图像像素数据,Mat是由头部与数据部分组成
         */
        Mat colorImage = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_COLOR);
        if(colorImage.empty()){
            throw new RuntimeException("无法读取原始图片，请检查图片路径");
        }
        //zxing二维码和条码识别(整张图片)
        List<BaseResult> results =detectByZxing(colorImage, null);
        if(results.isEmpty()){
            System.err.println("未找到二维码/条码");
        }
        // 创建处理后的图像
        Mat processedImage = colorImage.clone();
        // 设置默认颜色为白色
        Scalar coverColor = new Scalar(0, 0, 255);
        for(BaseResult result: results){
            System.out.println(result.toString());
            //把原图条码和二维码的位置用白色覆盖
            coverArea(processedImage, result, coverColor);
        }
        // 保存处理后的图像
        Imgcodecs.imwrite("F:\\C200I_covert.png", processedImage);

        processedImage.release();
        colorImage.release();
    }

    /**
     * 使用指定颜色绘制矩形线框（而不是填充区域）
     * @param image 要处理的图像
     * @param result 检测到的条码/二维码结果
     * @param color 线框颜色（BGR格式）
     */
    private void coverArea(Mat image, BaseResult result, Scalar color) {
        // 创建矩形区域
        Rect rect = new Rect(result.getX(), result.getY(), result.getWidth(), result.getHeight());
        // 确保矩形不超出图像边界
        rect = ensureRectWithinBounds(rect, image.size());

        // 使用指定颜色绘制矩形线框（而不是填充）
        // 参数说明：
        // image - 要绘制的图像
        // new Point(rect.x, rect.y) - 矩形左上角点
        // new Point(rect.x + rect.width, rect.y + rect.height) - 矩形右下角点
        // color - 线框颜色
        // 2 - 线框粗细（2像素）
        Imgproc.rectangle(
                image,
                new Point(rect.x, rect.y),
                new Point(rect.x + rect.width, rect.y + rect.height),
                color,
                2
        );
    }

    /**
     * 确保矩形不超出图像边界
     * @param rect 原始矩形
     * @param imageSize 图像尺寸
     * @return 调整后的矩形
     */
    private Rect ensureRectWithinBounds(Rect rect, Size imageSize) {
        int x = Math.max(0, rect.x);
        int y = Math.max(0, rect.y);
        int width = (int) Math.min(imageSize.width - x, rect.width);
        int height = (int) Math.min(imageSize.height - y, rect.height);
        return new Rect(x, y, width, height);
    }

    private List<BaseResult> detectByZxing(Mat image, Rect baseRect) {
        List<BaseResult> results = new ArrayList<>();
        try {
            BufferedImage bufferedImage = matToBufferedImage(image);
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            //先二维码识别
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
            hints.put(DecodeHintType.POSSIBLE_FORMATS, List.of(BarcodeFormat.QR_CODE));
            QRCodeMultiReader reader = new QRCodeMultiReader();
            Result[] qrcodes = reader.decodeMultiple(bitmap, hints);
            for (Result qrcode : qrcodes) {
                Rect location = calculateQrcodeLocation(qrcode, baseRect);
                QrcodeResult qrcodeResult = new QrcodeResult(location.x, location.y,
                        location.width, location.height,"QRCODE", qrcode.getText());
                results.add(qrcodeResult);
            }
            //再条码识别
            List<BarcodeResult> barcodeResults = detectBarcodeRegion(image);
            results.addAll(barcodeResults);
            return results;
        } catch (Exception e) {
            // 忽略识别过程中的异常，继续处理其他区域
            System.err.println("条码识别错误: " + e.getMessage());
            return  Collections.emptyList();
        }
    }

    private Rect calculateQrcodeLocation(Result result, Rect baseRect) {
        if (baseRect == null) {
            baseRect = new Rect(0, 0, 0, 0);
        }

        ResultPoint[] points = result.getResultPoints();
        if (points == null || points.length < 3) {
            return baseRect;
        }
        // 二维码的三个定位点
        Point2D bottomLeft = new Point2D.Float(points[0].getX(), points[0].getY());
        Point2D topLeft = new Point2D.Float(points[1].getX(), points[1].getY());
        Point2D topRight = new Point2D.Float(points[2].getX(), points[2].getY());
        // 计算右下角点
        Point2D bottomRight = qrcodeBottomRight(bottomLeft, topLeft, topRight);
        // 计算二维码模块大小（通过定位点之间的距离估算）
        double moduleSize = qrcodeModuleSize(bottomLeft, topLeft, topRight);
        // 计算二维码的实际大小（通常比定位点形成的矩形稍大）,静区通常是4个模块大小
        int quietZone = (int) Math.round(moduleSize * 4);
        // 计算包含静区的完整边界
        return qrcodeBoundingBox(bottomLeft, topLeft, topRight, bottomRight, quietZone);
    }

    /**
     * 计算二维码右下角点
     * @param bottomLeft Point2D
     * @param topLeft Point2D
     * @param topRight Point2D
     * @return Point2D
     */
    private static Point2D qrcodeBottomRight(Point2D bottomLeft, Point2D topLeft, Point2D topRight) {
        // 计算从左上角到右上角的向量
        double vectorX = topRight.getX() - topLeft.getX();
        double vectorY = topRight.getY() - topLeft.getY();

        // 右下角点 = 左下角点 + 向量
        return new Point2D.Double(
                bottomLeft.getX() + vectorX,
                bottomLeft.getY() + vectorY
        );
    }

    /**
     * 估算二维码模块大小
     * @param bottomLeft Point2D
     * @param topLeft Point2D
     * @param topRight Point2D
     * @return double
     */
    private static double qrcodeModuleSize(Point2D bottomLeft, Point2D topLeft, Point2D topRight) {
        // 计算左上角到右上角的距离（水平方向）
        double horizontalDistance = topLeft.distance(topRight);

        // 计算左上角到左下角的距离（垂直方向）
        double verticalDistance = topLeft.distance(bottomLeft);

        // 二维码版本1有21×21个模块，定位点占7×7个模块
        // 所以定位点之间的距离大约是14个模块
        double avgDistance = (horizontalDistance + verticalDistance) / 2;
        // 除以14得到每个模块的大小
        return avgDistance / 14.0;
    }
    /**
     * 计算二维码的完整边界（包含静区）
     * @param bottomLeft Point2D
     * @param topLeft Point2D
     * @param topRight Point2D
     * @param bottomRight Point2D
     * @param quietZone int
     * @return Rect
     */
    private static Rect qrcodeBoundingBox(Point2D bottomLeft, Point2D topLeft, Point2D topRight, Point2D bottomRight, int quietZone) {
        // 收集所有四个角点
        Point2D[] corners = {bottomLeft, topLeft, topRight, bottomRight};
        // 找到最小和最大的X、Y坐标
        double minX = Arrays.stream(corners).mapToDouble(Point2D::getX).min().getAsDouble();
        double maxX = Arrays.stream(corners).mapToDouble(Point2D::getX).max().getAsDouble();
        double minY = Arrays.stream(corners).mapToDouble(Point2D::getY).min().getAsDouble();
        double maxY = Arrays.stream(corners).mapToDouble(Point2D::getY).max().getAsDouble();
        // 扩展边界以包含静区
        minX -= quietZone;
        maxX += quietZone;
        minY -= quietZone;
        maxY += quietZone;
        return new Rect(
                (int) Math.round(minX),
                (int) Math.round(minY),
                (int) Math.round(maxX - minX),
                (int) Math.round(maxY - minY));
    }

    /**
     * 检测图像中的条码区域
     * @param image 输入图像
     * @return 条码区域的矩形坐标，如果未检测到则返回null
     */
    public List<BarcodeResult> detectBarcodeRegion(Mat image) {
        List<BarcodeResult> results = new ArrayList<>();
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
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(21, 7));
        Imgproc.morphologyEx(thresh, thresh, Imgproc.MORPH_CLOSE, kernel);
        // 去除小的噪点
        Mat eroded = new Mat();
        Imgproc.erode(thresh, eroded, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        Imgproc.dilate(eroded, thresh, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3)));
        // 查找轮廓
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(thresh, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        // 遍历轮廓，寻找最可能是条码的区域
        for (MatOfPoint contour : contours) {
            Rect rect = Imgproc.boundingRect(contour);
            // 计算宽高比
            double aspectRatio = (double) rect.width / rect.height;
            // 条码通常有较大的宽高比（长条形）
            if (aspectRatio > 2.5 && rect.width > 100 && rect.height > 20) {
                // 扩大检测区域，确保包含整个条码
                int padding = 1;
                rect.x = Math.max(0, rect.x - padding);
                rect.y = Math.max(0, rect.y - padding);
                rect.width = Math.min(image.width() - rect.x, rect.width + 2 * padding);
                rect.height = Math.min(image.height() - rect.y, rect.height + 2 * padding);
                Mat croppedBarcode = new Mat(image, rect);
                BufferedImage bufferedImage = matToBufferedImage(croppedBarcode);
                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                try {
                    Result result = new MultiFormatReader().decode(bitmap);
                    if (result != null&&result.getBarcodeFormat()!=null&& CharSequenceUtil.isNotBlank(result.getText())) {
                        BarcodeResult barcodeResult = new BarcodeResult(rect.x, rect.y, rect.width, rect.height,"BARCODE",
                                result.getText(),result.getBarcodeFormat().name());
                        results.add(barcodeResult);
                    }
                } catch (NotFoundException ignored) {
                }
            }
        }
        return results;
    }

    private BufferedImage matToBufferedImage(Mat mat) {
        // Mat转BufferedImage的辅助方法
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        byte[] data = new byte[mat.cols() * mat.rows() * (int)mat.elemSize()];
        mat.get(0, 0, data);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        image.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), data);
        return image;
    }

    public static void main(String[] args) throws InterruptedException {
        ImagePreprocessService service = new ImagePreprocessService();
        service.preprocess("F:\\C200I.png");
    }
}
