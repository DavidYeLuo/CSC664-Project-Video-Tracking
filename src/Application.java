import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.CvType;

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

        String fileName = "0001.jpg";
        String imgPath = MEDIA_PATH + fileName;

        System.out.printf("Reading image from disk: %s.\n", imgPath);
        Mat sourceImage = Imgcodecs.imread(imgPath);

        Mat binaryImage = new Mat();
        Mat distanceTImage = new Mat();

        System.out.println("Image processing: Grayscaling");
        Imgproc.cvtColor(sourceImage, sourceImage,Imgproc.COLOR_BGR2GRAY);

        System.out.println("Image processing: Thresholding");
        Imgproc.threshold(sourceImage, binaryImage, 100, 255, Imgproc.THRESH_BINARY);
        binaryImage.convertTo(binaryImage, CvType.CV_8UC1);

        System.out.println("Image processing: distance transform");
        Imgproc.distanceTransform(binaryImage, distanceTImage, Imgproc.DIST_C, 3);

        System.out.println("Finished processing");

        System.out.printf("Writing image to disk: %s.\n", OUTPUT_FILES + fileName);
        Imgcodecs.imwrite(OUTPUT_FILES + fileName, distanceTImage);
    }
}