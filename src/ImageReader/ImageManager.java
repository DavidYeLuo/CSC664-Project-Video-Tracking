package ImageReader;

public class ImageManager extends FileManager
{
    public ImageManager(String readPath, String savePath)
    {
        this.readPath = readPath;
        this.savePath = savePath;
    }
    @Override
    public boolean read()
    {
        // TODO: Find what param to use
        System.out.println("TODO: Fix read function in ImageManager");
        return true;
    }

    @Override
    public void write()
    {
        // TODO: Find what param to use
        System.out.println("TODO: Fix write function in ImageManager");
    }

    @Override
    public void release() {}
}