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

        // TODO: rename Image Matrix to Frame Matrix
        Mat sourceImage = new Mat();
        //        Mat sourceImage = Imgcodecs.imread(imgPath); // Used for images
        Mat grayScaleImage = new Mat();
        Mat binaryImage = new Mat();
        Mat distanceTImage = new Mat();
        Mat sobelX = new Mat();
        Mat sobelY = new Mat();
        Mat finalSobel = new Mat();

        for(int i = 0; i < 300; i++)
        {
            if(!videoCapture.isOpened()) continue;
            videoCapture.read(sourceImage);
            System.out.printf("Source Image: %s\n", sourceImage);

            System.out.println("Image processing: Grayscaling");
            Imgproc.cvtColor(sourceImage, grayScaleImage,Imgproc.COLOR_BGR2GRAY);
            System.out.printf("GrayScaled Image: %s\n", sourceImage);

            System.out.println("Image processing: Thresholding");
//            Imgproc.threshold(grayScaleImage, binaryImage, 200, 255, Imgproc.THRESH_BINARY); // manual
            Imgproc.threshold(grayScaleImage, binaryImage, 0, 255, Imgproc.THRESH_OTSU);
//            binaryImage.convertTo(binaryImage, CvType.CV_8UC1);
            System.out.printf("Binary Image: %s\n", binaryImage);

            System.out.println("Image processing: distance transform");
            Imgproc.distanceTransform(binaryImage, distanceTImage, Imgproc.DIST_L2, 0);
            System.out.printf("Image Transform: %s\n", distanceTImage);


            distanceTImage.convertTo(distanceTImage, CvType.CV_8UC1);

            Imgproc.Sobel(distanceTImage, sobelX, CvType.CV_8UC1, 1, 0, 3, 1, 0, Core.BORDER_DEFAULT );
            Imgproc.Sobel(distanceTImage, sobelY, CvType.CV_8UC1, 0, 1, 3, 1, 0, Core.BORDER_DEFAULT );

            Core.convertScaleAbs( sobelX, sobelX);
            Core.convertScaleAbs( sobelY, sobelY);

            Core.addWeighted( sobelX, 0.5, sobelY, 0.5, 0, finalSobel);

            Imgproc.threshold(finalSobel, finalSobel, 0, 255, Imgproc.THRESH_BINARY);
            Core.bitwise_not(finalSobel, finalSobel);

            Scalar color = new Scalar(203,192, 255); // hot pink
//            Scalar color = new Scalar(255,255,255); // white
            sourceImage.setTo(color, finalSobel);

            System.out.println("Finished processing");

            System.out.printf("Writing image to disk: %s.\n", OUTPUT_FILES + fileName);

//            Imgcodecs.imwrite(OUTPUT_FILES + "grayscale.jpg", grayScaleImage);
//            Imgcodecs.imwrite(OUTPUT_FILES + "binaryimage.jpg", binaryImage);
//            Imgcodecs.imwrite(OUTPUT_FILES + "transform.jpg", distanceTImage);
//            Imgcodecs.imwrite(OUTPUT_FILES + "laplacian.jpg", finalSobel);
//            Imgcodecs.imwrite(OUTPUT_FILES + "result.jpg", sourceImage);

            videoWriter.write(sourceImage);
        }

        videoCapture.release();
        videoWriter.release();
    }
}