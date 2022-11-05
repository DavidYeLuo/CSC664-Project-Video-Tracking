package ImageReader;
import ImageReader.temp.A;

import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.VideoWriter;
import org.opencv.videoio.Videoio;


public class VideoManager extends FileManager
{
    private int fourc;

    VideoCapture videoCapture;
    VideoWriter videoWriter;

    public VideoManager(String readPath, String savePath, int fourc)
    {
        this.readPath = readPath;
        this.savePath = savePath;
        this.fourc = fourc;
        //        int fourcc = VideoWriter.fourcc('h','2','6','4');
        videoCapture = new VideoCapture(readPath);

        double fps = videoCapture.get(Videoio.CAP_PROP_FPS);
        Size frameSize = new Size(
                (int) videoCapture.get(Videoio.CAP_PROP_FRAME_WIDTH),
                (int) videoCapture.get(Videoio.CAP_PROP_FRAME_HEIGHT));
        videoWriter = new VideoWriter(savePath, fourc, fps, frameSize,true);
        if(!videoCapture.isOpened()) System.out.println("File didn't open");
    }

    /**
     * Reads image from file path.
     * @param readTo Saves image to this param
     * @return true when succeeded and false when failed
     */
    @Override
    public boolean read(Mat readTo)
    {
        return videoCapture.read(readTo);
    }

    @Override
    public void write(Mat source)
    {
        videoWriter.write(source);
    }

    @Override
    public void release()
    {
        videoCapture.release();
        videoWriter.release();
    }
}
