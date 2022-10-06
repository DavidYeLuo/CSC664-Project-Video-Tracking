import org.opencv.core.*;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.Videoio;

import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.List;

public class Application
{
    public static final String MEDIA_PATH = "../media/";
    public static final String OUTPUT_FILES = "../output/";
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
        String imgPath = MEDIA_PATH + fileName;
        String outImgPath = OUTPUT_FILES + fileName;

        System.out.printf("Reading image from disk: %s\n", imgPath);

        int fourcc = VideoWriter.fourcc('a','v','c','1');
//        int fourcc = VideoWriter.fourcc('h','2','6','4');
        VideoCapture videoCapture = new VideoCapture(imgPath);

        double fps = videoCapture.get(Videoio.CAP_PROP_FPS);
        Size frameSize = new Size(
                (int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH),
                (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
        VideoWriter videoWriter = new VideoWriter(outImgPath, fourcc, fps, frameSize,true);

        if(!videoCapture.isOpened()) System.out.println("File didn't open");

        Mat sourceFrame = new Mat();
        //        Mat sourceFrame = Imgcodecs.imread(imgPath); // Used for images
        Mat grayScaleFrame = new Mat();
        Mat binaryFrame = new Mat();
        Mat distanceTFrame = new Mat();
        Mat sobelX = new Mat();
        Mat sobelY = new Mat();
        Mat finalSobel = new Mat();

        int numOfSections = 8; // arbitrary number for now
        List<Mat> sections = new ArrayList<>();

        int numOfFrames = 1;
        for(int i = 0; i < numOfFrames; i++)
        {
            if(!videoCapture.isOpened()) continue;

//            for(int section = 0; section < numOfSections/2; section++)
//            {
//                sections[i] = sourceFrame.submat(sourceFrame.rows() * i/numOfSections,
//                                                 sourceFrame.rows() * (i+1)/numOfSections,
//                                                 ,
//                                                 sourceFrame.cols() * (i+1)/numOfSections);
//            }
            videoCapture.read(sourceFrame);

            /**
             * TODO: This is only cutting the top left part. We need to cut everything.
             */
            int rowBlock = sourceFrame.rows() / numOfSections;
            int colBlock = sourceFrame.cols() / numOfSections;
            sections.add(sourceFrame.submat(0, rowBlock
                    , 0, colBlock));
            sections.add(sourceFrame.submat(rowBlock, rowBlock*2
                    , 0, colBlock));

            /**
             * We will apply the otsu algorithm on each segment here and apply otsu algorithm here.
             */

            Core.vconcat(sections, sourceFrame); // This merges images vertically

            Imgproc.cvtColor(sourceFrame, grayScaleFrame,Imgproc.COLOR_BGR2GRAY);

            Imgproc.threshold(grayScaleFrame, binaryFrame, 0, 255, Imgproc.THRESH_OTSU);
//            Imgproc.adaptiveThreshold(grayScaleFrame, binaryFrame, 255,
//                                      Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY,
//                                      7, 8);

            Imgproc.distanceTransform(binaryFrame, distanceTFrame, Imgproc.DIST_L2, 0);

            // Needs to convert to 64 bit image
            // This way it will capture negative slopes
            Imgproc.Sobel(distanceTFrame, sobelX, CvType.CV_64F, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT );
            Imgproc.Sobel(distanceTFrame, sobelY, CvType.CV_64F, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT );

            Core.convertScaleAbs(sobelX, sobelX);
            Core.convertScaleAbs(sobelY, sobelY);

            Core.addWeighted(sobelX, 0.5, sobelY, 0.5, 0, finalSobel);

            Imgproc.threshold(finalSobel, finalSobel, 1, 255, Imgproc.THRESH_BINARY);
            Core.bitwise_not(finalSobel, finalSobel);

            Scalar color = new Scalar(203,192, 255); // hot pink
//            Scalar color = new Scalar(255,255,255); // white
            sourceFrame.setTo(color, finalSobel);

            Imgcodecs.imwrite(OUTPUT_FILES + "grayscale.jpg", grayScaleFrame);
            Imgcodecs.imwrite(OUTPUT_FILES + "binaryimage.jpg", binaryFrame);
            Imgcodecs.imwrite(OUTPUT_FILES + "transform.jpg", distanceTFrame);
            Imgcodecs.imwrite(OUTPUT_FILES + "sobel_inv.jpg", finalSobel);
            Core.bitwise_not(finalSobel, finalSobel);
            Imgcodecs.imwrite(OUTPUT_FILES + "sobel.jpg", finalSobel);
            Imgcodecs.imwrite(OUTPUT_FILES + "result.jpg", sourceFrame);

            videoWriter.write(sourceFrame);
        }

        videoCapture.release();
        videoWriter.release();
    }
}