package CustomProcessing;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class FrameAvg
{
    ArrayList<Mat> frameList;
    Mat       sumFrames;
    Mat       firstFrame;
    Mat       lastFrame;
    private int maxFrames;

    private Mat average;
    private Mat ret_avg;
    private Scalar scalar; // number to divide by to get the average

    public FrameAvg(int maxFrames)
    {
        frameList = new ArrayList<>();
        this.maxFrames = maxFrames;
        scalar = new Scalar(maxFrames);
        ret_avg = new Mat();
    }

    public void addFrame(Mat frame)
    {
        if(frameList.size() == 0)
        {
            firstFrame = new Mat(frame.rows(), frame.cols(), CvType.CV_16U);
            frame.copyTo(firstFrame);
            firstFrame.convertTo(firstFrame, CvType.CV_16U);
            sumFrames = Mat.zeros(frame.rows(), frame.cols(), CvType.CV_16U);
            average   = new Mat(frame.rows(), frame.cols(), CvType.CV_16U);
            lastFrame = new Mat(frame.rows(), frame.cols(), CvType.CV_16U);
        }
        frame.copyTo(lastFrame);
        lastFrame.convertTo(lastFrame, CvType.CV_16U);

        frameList.add(lastFrame);
        Core.add(sumFrames, lastFrame, sumFrames);

        if(frameList.size() < maxFrames+1)
        {
            Core.divide(sumFrames, new Scalar(frameList.size()), average);
            return;
        }
        Core.subtract(sumFrames, firstFrame, sumFrames);
        frameList.remove(0);
        firstFrame = frameList.get(0);

        Core.divide(sumFrames, scalar, average);
    }
    public Mat getAverage()
    {
        average.copyTo(ret_avg);
        ret_avg.convertTo(ret_avg, CvType.CV_8U);
        return ret_avg;
    }
}
