package ImageReader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class VideoManager extends FileManager
{
    // TODO: Video Capture
    // TODO: Video Writer
    FileInputStream reader;
    FileOutputStream writer;

    private byte[] b;
    public VideoManager(String readPath, String savePath)
    {
        File readFile = new File(readPath);
        try
        {
//            reader = new FileInputStream(new File(readPath));
            reader = new FileInputStream(readFile);
            writer = new FileOutputStream(savePath);
        } catch (Exception e) {
            System.out.println("VideoManager: Constructor failed.");
        }
        finally
        {
//            b = new byte[(int) readFile.length()];
            b = new byte[1180*796*4];
        }
    }

    /**
     * Reads image from file path.
     * @return true when succeeded and false when failed
     */
    @Override
    public boolean read()
    {
        try
        {
            reader.read(b, 0, 1180*796*4);
        }
        catch(Exception e)
        {
            System.out.println("----- ERROR -----");
            System.out.println("Read failed in Video Manager");
            System.out.println("----------");
        }
        return false;
    }

    @Override
    public void write()
    {
        try
        {
            writer.write(b);
        }
        catch(Exception e)
        {
            System.out.println("----- ERROR -----");
            System.out.println("Writer failed in Video Manager");
            System.out.println("----------");
        }
    }

    @Override
    public void release()
    {
        System.out.println("TODO: Impelement release in VideoManager");
    }
}
