import ImageReader.ImageReader;

public class Application
{
    public static void main(String[]args)
    {
        ImageReader imageReader = new ImageReader();
        imageReader.readImageFromPath("../media/0000.jpg");
    }
}