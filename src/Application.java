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

        // Settings
        boolean DEBUG_MODE = true;
        boolean DEBUG_USE_SIMPLE_IMAGE = false;
        boolean DEBUG_SAVE_IMAGE = true;
        boolean DEBUG_PRINT_IMAGE = false;
        // Settings for image/video
        String fileName = "0001.mp4";
        int fourcc = VideoWriter.fourcc('a', 'v', 'c', '1');
        int numOfFrames = 1; // Number of frames to process
        // Object Tracking Settings
        int numOfSections = 8; // arbitrary number for now
        int radius = 1; // Box/circle around the tracking object
        Scalar color = new Scalar(203,192, 255); // hot pink
        // Sweet spot sobelMaxVal = 1, distMinVal = 1
        int sobelMaxVal = 1; // closer to 0 -> More accurate on filter out non-peak
        int distMinVal = 1; // closer to 0 -> More accurate on filtering background

//        if(DEBUG_USE_SIMPLE_IMAGE) fileName = "0002.png";
        if(DEBUG_USE_SIMPLE_IMAGE) fileName = "0001.jpg";
        String imgPath = MEDIA_PATH + fileName;
        String outImgPath = OUTPUT_FILES + fileName;
        System.out.printf("Reading image from disk: %s\n", imgPath);
        FileManager fileManager;
        if(DEBUG_USE_SIMPLE_IMAGE)
        {
            fileManager = new ImageManager(imgPath, outImgPath);
        }
        else
        {
            fileManager = new VideoManager(imgPath, outImgPath, fourcc);
        }

        Mat sourceFrame = new Mat();
        Mat grayScaleFrame = new Mat();
        Mat binaryFrame = new Mat();
        Mat distanceTFrame = new Mat();
//        Mat edgeFrame = new Mat();
//        Mat edgeX = new Mat();
//        Mat edgeY = new Mat();
        Mat sobelX = new Mat();
        Mat sobelY = new Mat();
//        Mat firstSobel = new Mat();
        Mat finalSobel = new Mat();
        Mat outputFrame = new Mat();

        // Create a kernel that we will use to sharpen our image
//        Mat kernel = Mat.ones(3, 3, CvType.CV_8U);

        List<Mat> sections = new ArrayList<>();

        // Cache
        Point topLeft; // Top left corner of the box
        Point bottomRight; // Bottom right corner of the box
        int rowBlock;
        int colBlock;

        if(DEBUG_USE_SIMPLE_IMAGE) numOfFrames++;
        for(int i = 0; i < numOfFrames; i++)
        {
            if(fileManager.read(sourceFrame) == false) break;

            Imgproc.cvtColor(sourceFrame, grayScaleFrame,Imgproc.COLOR_BGR2GRAY);
            Core.normalize(grayScaleFrame, grayScaleFrame, 0, 255, Core.NORM_MINMAX);

            /**
             * TODO: Need to convert image splitting, processing, and merging into a method
             */

            /**
             * We will apply the otsu algorithm on each segment here and apply otsu algorithm here.
             */
            rowBlock = sourceFrame.rows() / numOfSections;
            colBlock = sourceFrame.cols() / numOfSections;
            grayScaleFrame.copyTo(binaryFrame);
            for(int col = 0; col < 8; col++)
            {
                for (int sect = 0; sect < numOfSections; sect++) {
                    // For some reason the original image is overriden after image process
                    sections.add(binaryFrame.submat(rowBlock*(0+sect), rowBlock*(1+sect), colBlock*(0+col), colBlock*(1+col)));

                    int index= sect + numOfSections * col;
//                        System.out.println(index);
                    Imgproc.threshold(sections.get(index), sections.get(index), 0, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY_INV);
                }

            }


            ArrayList<Mat> mats = new ArrayList<>();
            for(int index = 0; index < 8; index++)
            {
                mats.add(new Mat());
            }

            int temp2 = 0;
            for (int temp = 0; temp < 64; temp += 8) {
                Core.vconcat(sections.subList(0 + temp, 8 + temp), mats.get(temp2));
                temp2++;
            }
            Core.hconcat(mats, binaryFrame); // This merges images vertically
//            sourceFrame.copyTo(outputFrame); // Darker color
            grayScaleFrame.copyTo(outputFrame);
            Imgproc.cvtColor(outputFrame, outputFrame,Imgproc.COLOR_GRAY2BGR);


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
//            Imgproc.threshold(grayScaleFrame, binaryFrame, 0, 255, Imgproc.THRESH_OTSU + Imgproc.THRESH_BINARY_INV);

//            Imgproc.adaptiveThreshold(grayScaleFrame, binaryFrame, 255,
//                                      Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY_INV,
//                                      7, 8);

            Imgproc.distanceTransform(binaryFrame, distanceTFrame, Imgproc.DIST_L1, 5);

//            Core.normalize(distanceTFrame, distanceTFrame, 0, 255, Core.NORM_MINMAX);
//            Core.multiply(distanceTFrame, new Scalar(255), distanceTFrame);


//         Calculates the first the change in pixel intensity of the distance transform
//         Needs to convert to 64 bit image
//         This way it will capture negative slopes
            Imgproc.Sobel(distanceTFrame, sobelX, CvType.CV_64F, 1, 0, 0, 1, 0, Core.BORDER_DEFAULT );
            Imgproc.Sobel(distanceTFrame, sobelY, CvType.CV_64F, 0, 1, 0, 1, 0, Core.BORDER_DEFAULT );
            Core.convertScaleAbs(sobelX, sobelX);
            Core.convertScaleAbs(sobelY, sobelY);
//            Core.addWeighted(sobelX, 0.5, sobelY, 0.5, 0, finalSobel);
            Core.bitwise_or(sobelX, sobelY, finalSobel);
            for(int row = 0; row < finalSobel.rows(); row++)
            {
                for(int col = 0; col < finalSobel.cols(); col++)
                {
                    // if sobel at (row,col) is < sobelMaxVal
                    // and distanceTFrame at (row,col) > distMinVal
                    if(distanceTFrame.get(row, col)[0] < distMinVal) continue; // Closer to 0 means likely a background
                    if(finalSobel.get(row,col)[0] > sobelMaxVal) continue; // 0 Is the peak
                    topLeft = new Point(col-radius, row-radius);
                    bottomRight = new Point(col+radius, row+radius);
                    Imgproc.rectangle(outputFrame, topLeft, bottomRight, color);
                }
            }
            fileManager.write(outputFrame);
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
            Imgcodecs.imwrite(DEBUG_PATH + "result.jpg", outputFrame);
            //            Imgcodecs.imwrite(OUTPUT_FILES + "edge.jpg", edgeFrame);
        }
        if(DEBUG_PRINT_IMAGE)
        {
            createTextImageFile("grayscale.txt", grayScaleFrame);
            createTextImageFile("binaryimage.txt", binaryFrame);
            createTextImageFile("transform.txt", distanceTFrame);
            createTextImageFile("sobelX.txt", sobelX);
            createTextImageFile("sobelY.txt", sobelY);
            createTextImageFile("sobel.txt", finalSobel);
            createTextImageFile("result.txt", outputFrame);
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
                    stringBuilder.append(String.format("%3d", (int)(img.get(row,col)[0])));
//                    stringBuilder.append(String.format("(%d,%d)", col, (int)(img.get(row,col)[0])));
                    stringBuilder.append(" ");
//                    stringBuilder.append(",");
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