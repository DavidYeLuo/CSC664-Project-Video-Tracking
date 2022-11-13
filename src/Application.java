import CustomProcessing.FrameAvg;
import ImageReader.FileManager;
import ImageReader.ImageManager;
import ImageReader.VideoManager;

public class Application
{
    public static final String MEDIA_PATH = "../media/";
    public static final String OUTPUT_FILES = "../output/";
    public static final String DEBUG_PATH = "../output/DebugFiles/";

    public static void main(String[]args)
    {
        System.out.println("------------------------------------");
        System.out.println("Native Java Implementation");
        System.out.println("Hello World!");
        System.out.println("------------------------------------");

        // Settings
        boolean DEBUG_MODE = true;
        boolean DEBUG_USE_SIMPLE_IMAGE = false;
        boolean DEBUG_SAVE_IMAGE = true;
        boolean DEBUG_PRINT_IMAGE = false;
        // Settings for image/video
        String fileName = "0001.mp4";
//        int fourcc = VideoWriter.fourcc('a', 'v', 'c', '1'); // Four cc initialization
        int numOfFrames = 300; // Number of frames to process
        // Object Tracking Settings
        int maxAvgFrames = 10; // Used to filter background
        int numOfSections = 8; // arbitrary number for now
        int radius = 1; // Box/circle around the tracking object
//        Scalar color = new Scalar(203,192, 255); // hot pink
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
//            fileManager = new VideoManager(imgPath, outImgPath, fourcc);
        }
        FrameAvg frameAvg = new FrameAvg(maxAvgFrames);

        // Init
        // TODO: Image Processing
        // TODO: Source Frame
        // TODO: GrayScale Frame
        // TODO: Difference Frame
        // TODO: Binary Frame
        // TODO: Distance Frame
        // TODO: SobelX
        // TODO: SobelY
        // TODO: Sobel
        // TODO: Average Frame
        // TODO: Output Frame


        for(int i = 0; i < numOfFrames; i++)
        {
            // TODO: Read Frame

            // TODO: Img process
            // TODO: Grayscale
            // TODO: Subtract AverageFrame

            // TODO: Copy AverageFrame to outputFrame
            // TODO: Convert Image to Grayscale

            // TODO: Threshold binaryFrame

            // TODO: distanceTransform

//         TODO: Calculates the first the change in pixel intensity of the distance transform
//         Needs to convert to 64 bit image
//         This way it will capture negative slopes
            // Sobel X
            // Sobel Y
            // Convert Sobel X and Y Image
            //  Convert to higher bit image
            //  Absolute value image
            // Or operation on X and Y (Adds Image)

            // TODO: Image Process (Draw a box after filter all unnecessary pixels)
            // Go through each pixel
            //     Filter
            //      dist > distMinVal then return
            //      finalSobel > sobelMaxVal then return
            //     Draw box
            //     if sobel at (row,col) is < sobelMaxVal
            //     and distanceTFrame at (row,col) > distMinVal

            // TODO: Write/Display Image
        }
        // TODO: Clean up
        if(!DEBUG_MODE)
        {
            return;
        }
        if(DEBUG_SAVE_IMAGE)
        {
            // Write to the Debug path
            // TODO: write Source Frame
            // TODO: write GrayScale Frame
            // TODO: write Difference Frame
            // TODO: write Binary Frame
            // TODO: write Distance Frame
            // TODO: write SobelX
            // TODO: write SobelY
            // TODO: write Sobel
            // TODO: write Average Frame
            // TODO: write Output Frame
        }
        if(DEBUG_PRINT_IMAGE)
        {
            // Write to the Debug Path
            // TODO: write Source Frame
            // TODO: write GrayScale Frame
            // TODO: write Difference Frame
            // TODO: write Binary Frame
            // TODO: write Distance Frame
            // TODO: write SobelX
            // TODO: write SobelY
            // TODO: write Sobel
            // TODO: write Average Frame
            // TODO: write Output Frame
        }
    }
//    private static void createTextImageFile(String fileName, Mat img)
//    {
//        // TODO Implement this method
//        System.out.println("TODO: Implement createTextImageFile");
//        try {
//            File file = new File(DEBUG_PATH + fileName); // TODO: cache
//            FileWriter fileWriter = new FileWriter(DEBUG_PATH + fileName); // TODO: cache
//            if(!file.exists()) file.createNewFile();
//            fileWriter.write("----- " + fileName + " -----\n");
//            printImage(img, fileWriter);
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

//    private static void printImage(Mat img, FileWriter fileWriter)
//    {
//        // TODO: Implement Method
//        System.out.println("TODO: printImage not defined");
//        StringBuilder stringBuilder = new StringBuilder();
//        try
//        {
//            for(int row = 0; row < img.rows(); row++)
//            {
//                for(int col = 0; col < img.cols(); col++)
//                {
//                    stringBuilder.append(String.format("%3d", (int)(img.get(row,col)[0])));
////                    stringBuilder.append(String.format("(%d,%d)", col, (int)(img.get(row,col)[0])));
//                    stringBuilder.append(" ");
////                    stringBuilder.append(",");
//                }
//                stringBuilder.append("\n");
//                fileWriter.write(stringBuilder.toString());
//                stringBuilder.setLength(0); // Resets buffer
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}