import ImageReader.FileManager;
import ImageReader.ImageManager;
import ImageReader.VideoManager;
import org.opencv.core.*;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.Videoio;

import java.awt.image.ImageProducer;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Application
{
    public static final String MEDIA_PATH = "../media/";
    public static final String OUTPUT_FILES = "../output/";
    public static final String DEBUG_PATH = "../output/DebugFiles/";
    public static void main(String[]args)
    {
        System.out.println("------------------------------------");
        System.out.println("System properties: ");
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        System.out.println("Java library path: " + System.getProperty("java.library.path"));
        System.out.println("Core: " + Core.NATIVE_LIBRARY_NAME);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("------------------------------------");

        String fileName = "0001.mp4";
//        String fileName = "0001.jpg";
        String imgPath = MEDIA_PATH + fileName;
        String outImgPath = OUTPUT_FILES + fileName;

        System.out.printf("Reading image from disk: %s\n", imgPath);

        boolean DEBUG_MODE = false;
        boolean DEBUG_SAVE_IMAGE = false;
        boolean DEBUG_PRINT_IMAGE = false;

        int         fourcc       = VideoWriter.fourcc('a', 'v', 'c', '1');
        FileManager fileManager = new VideoManager(imgPath, outImgPath, fourcc); // Comment for img
//        FileManager fileManager = new ImageManager(imgPath, outImgPath); // Uncomment for img

        Mat sourceFrame = new Mat();
        Mat grayScaleFrame = new Mat();
        Mat binaryFrame = new Mat();
        Mat distanceTFrame = new Mat();
//        Mat edgeFrame = new Mat();
//        Mat edgeX = new Mat();
//        Mat edgeY = new Mat();
        Mat sobelX = new Mat();
        Mat sobelY = new Mat();
        Mat finalSobel = new Mat();

        // Create a kernel that we will use to sharpen our image
//        Mat kernel = Mat.ones(3, 3, CvType.CV_8U);

        int numOfSections = 8; // arbitrary number for now
        List<Mat> sections = new ArrayList<>();

        int numOfFrames = 1;
        for(int i = 0; i < numOfFrames; i++)
        {
            if(fileManager.read(sourceFrame) == false) break;

            /**
             * We will apply the otsu algorithm on each segment here and apply otsu algorithm here.
             */
            Imgproc.cvtColor(sourceFrame, grayScaleFrame,Imgproc.COLOR_BGR2GRAY);

            // Cool but not sure if we need to use edge detection
//            Imgproc.Sobel(grayScaleFrame, edgeX, CvType.CV_64F, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT );
//            Imgproc.Sobel(grayScaleFrame, edgeY, CvType.CV_64F, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT );
//            Core.convertScaleAbs(edgeX, edgeX);
//            Core.convertScaleAbs(edgeY, edgeY);
//            Core.addWeighted(edgeX, 0.5, edgeY, 0.5, 0, edgeFrame);
//            Imgproc.threshold(edgeFrame, edgeFrame, 0, 255, Imgproc.THRESH_OTSU);
//            Core.bitwise_not(edgeFrame,edgeFrame);
//
//            Core.bitwise_or(edgeFrame,grayScaleFrame, grayScaleFrame);

            // Reduce noise but there will be more missed zebrafish
//            Imgproc.erode(grayScaleFrame,grayScaleFrame, kernel);
//            Imgproc.dilate(grayScaleFrame,grayScaleFrame, kernel);
            Imgproc.threshold(grayScaleFrame, binaryFrame, 0, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY_INV);


//            Imgproc.adaptiveThreshold(grayScaleFrame, binaryFrame, 255,
//                                      Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV,
//                                      7, 8);

            Imgproc.distanceTransform(binaryFrame, distanceTFrame, Imgproc.DIST_L2, 3);

            Core.normalize(distanceTFrame, distanceTFrame, 0.0, 1.0, Core.NORM_MINMAX);
            Core.multiply(distanceTFrame, new Scalar(255), distanceTFrame);


            // Needs to convert to 64 bit image
            // This way it will capture negative slopes
            Imgproc.Sobel(distanceTFrame, sobelX, CvType.CV_64F, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT );
            Imgproc.Sobel(distanceTFrame, sobelY, CvType.CV_64F, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT );


            Core.convertScaleAbs(sobelX, sobelX);
            Core.convertScaleAbs(sobelY, sobelY);


            Core.addWeighted(sobelX, 0.5, sobelY, 0.5, 0, finalSobel);
            Core.normalize(finalSobel, finalSobel, 0.0, 1.0, Core.NORM_MINMAX);
            Core.multiply(finalSobel, new Scalar(255), finalSobel);

            int radius = 3;
            // 0 < sobelPixel < sobelMaxVal
            // distMinVal < distanceTFrame < 255
            // Sweet spot sobelMaxVal = 3, distMinVal = 11
            int sobelMaxVal = 3; // Lower value -> More accurate but fewer finds
            int distMinVal = 11; // Higher value -> More accurate but fewer finds
            // Cache
            Point topLeft;
            Point bottomRight;
            Scalar color = new Scalar(203,192, 255); // hot pink
            for(int row = 0; row < finalSobel.rows(); row++)
            {
                for(int col = 0; col < finalSobel.cols(); col++)
                {
                    // if sobel at (row,col) is 0
                    // and distanceTFrame at (row,col)
                    if(distanceTFrame.get(row, col)[0] <  distMinVal) continue;
                    if(finalSobel.get(row,col)[0] > sobelMaxVal) continue; // Filters out

                    topLeft = new Point(col-radius, row-radius);
                    bottomRight = new Point(col+radius, row+radius);
                    Imgproc.rectangle(sourceFrame, topLeft, bottomRight, color);
                }
            }
            fileManager.write(sourceFrame);
        }
        fileManager.release();
        if(!DEBUG_MODE)
        {
            return;
        }
        if(DEBUG_SAVE_IMAGE)
        {
            Imgcodecs.imwrite(DEBUG_PATH + "grayscale.jpg", grayScaleFrame);
            Imgcodecs.imwrite(DEBUG_PATH + "binaryimage.jpg", binaryFrame);
            Imgcodecs.imwrite(DEBUG_PATH + "transform.jpg", distanceTFrame);
            Imgcodecs.imwrite(DEBUG_PATH + "sobel.jpg", finalSobel);
            Imgcodecs.imwrite(DEBUG_PATH + "result.jpg", sourceFrame);
            //            Imgcodecs.imwrite(OUTPUT_FILES + "edge.jpg", edgeFrame);
        }
        if(DEBUG_PRINT_IMAGE)
        {
            createTextImageFile("grayscale.txt", grayScaleFrame);
            createTextImageFile("binaryimage.txt", binaryFrame);
            createTextImageFile("transform.txt", distanceTFrame);
            createTextImageFile("sobel.txt", finalSobel);
            createTextImageFile("result.txt", sourceFrame);
        }
    }
    private static void createTextImageFile(String fileName, Mat img)
    {
        try {
            File file = new File(DEBUG_PATH + fileName); // TODO: cache
            FileWriter fileWriter = new FileWriter(DEBUG_PATH + fileName); // TODO: cache
            if(!file.exists()) file.createNewFile();
            fileWriter.write("----- " + fileName + " -----\n");
            printImage(img, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printImage(Mat img, FileWriter fileWriter)
    {
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            for(int row = 0; row < img.rows(); row++)
            {
                for(int col = 0; col < img.cols(); col++)
                {
                    stringBuilder.append(String.format("%03d", (int)(img.get(row,col)[0])));
                    stringBuilder.append(" ");
                }
                stringBuilder.append("\n");
                fileWriter.write(stringBuilder.toString());
                stringBuilder.setLength(0); // Resets buffer
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}