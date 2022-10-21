package ImageReader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import ImageReader.temp.A;
public class ImageManager extends FileManager
{
    Imgcodecs imgcodecs;
    public ImageManager(String readPath, String savePath)
    {
        this.readPath = readPath;
        this.savePath = savePath;
        imgcodecs = new Imgcodecs();
    }
    @Override
    public boolean read(Mat readTo)
    {
        (Imgcodecs.imread(readPath)).copyTo(readTo);
        return true; // TODO: find a better way to return the result of imread
    }

    @Override
    public void write(Mat source)
    {
        Imgcodecs.imwrite(savePath, source);
    }

    @Override
    public void release() {}
}