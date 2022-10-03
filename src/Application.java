import org.opencv.core.*;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.VideoCapture;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.Videoio;

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

        for(int i = 0; i < 1; i++)
        {
            if(!videoCapture.isOpened()) continue;
            videoCapture.read(sourceFrame);
            System.out.printf("Source Image: %s\n", sourceFrame);

            System.out.println("Image processing: Grayscaling");
            Imgproc.cvtColor(sourceFrame, grayScaleFrame,Imgproc.COLOR_BGR2GRAY);
            System.out.printf("GrayScaled Image: %s\n", sourceFrame);

            System.out.println("Image processing: Thresholding");
//            Imgproc.threshold(grayScaleFrame, binaryFrame, 200, 255, Imgproc.THRESH_BINARY); // manual threshold
            Imgproc.threshold(grayScaleFrame, binaryFrame, 0, 255, Imgproc.THRESH_OTSU);
            System.out.printf("Binary Image: %s\n", binaryFrame);

            System.out.println("Image processing: distance transform");
            Imgproc.distanceTransform(binaryFrame, distanceTFrame, Imgproc.DIST_L2, 0);
            System.out.printf("Image Transform: %s\n", distanceTFrame);

            // Needs to convert to 64 bit image
            // This way it will capture negative slopes
            Imgproc.Sobel(distanceTFrame, sobelX, CvType.CV_64F, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT );
            Imgproc.Sobel(distanceTFrame, sobelY, CvType.CV_64F, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT );

            Core.convertScaleAbs(sobelX, sobelX);
            Core.convertScaleAbs(sobelY, sobelY);

            Core.addWeighted(sobelX, 0.5, sobelY, 0.5, 0, finalSobel);

            Imgproc.threshold(finalSobel, finalSobel, 0, 255, Imgproc.THRESH_BINARY);
            Core.bitwise_not(finalSobel, finalSobel);

            Scalar color = new Scalar(203,192, 255); // hot pink
//            Scalar color = new Scalar(255,255,255); // white
            sourceFrame.setTo(color, finalSobel);

            System.out.println("Finished processing");

            System.out.printf("Writing image to disk: %s.\n", OUTPUT_FILES + fileName);

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