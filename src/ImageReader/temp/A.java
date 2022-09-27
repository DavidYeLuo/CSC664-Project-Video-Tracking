// Temporary class.
package temp;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class A
{
    public void print()
    {
        System.out.println("------------------------------------");
        System.out.println("Welcome to OpenCV " + Core.VERSION);
        System.out.println("Java library path: " + System.getProperty("java.library.path"));
        System.out.println("Core: " + Core.NATIVE_LIBRARY_NAME);
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        System.out.println("------------------------------------");
        Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
        System.out.println("mat = " + mat.dump());
    }
}