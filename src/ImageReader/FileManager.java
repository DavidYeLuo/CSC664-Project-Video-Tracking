package ImageReader;

public abstract class FileManager
{
    protected String readPath;
    protected String savePath;
    /**
     * Reads image from file path.
     * @param readTo Saves image to this param
     * @return true when succeeded and false when failed
     */
    // TODO: Uncomment these after we have decided to implement these
    public abstract boolean read();
    public abstract void write();
    public abstract void release();
}
