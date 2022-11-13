package CustomProcessing;

import java.util.ArrayList;

public class FrameAvg
{
    // TODO: Init Variables
    private ArrayList<Byte> frameList;
    // TODO: SumFrames
    // TODO: firstFrame
    // TODO: lastFrame
    private int maxFrames;
    // TODO: average // Average Frame
    // TODO: ret_avg // Return the average frame that is in the proper image type
    private int size;

    public FrameAvg(int maxFrames)
    {
        frameList = new ArrayList<>();
        this.maxFrames = maxFrames;
        size = maxFrames;
        // TODO: Init ret_avg
    }

//    public void addFrame(Mat frame)
//    {
//        if(frameList.size() == 0)
//        {
//            firstFrame = new Mat(frame.rows(), frame.cols(), CvType.CV_16U);
//            frame.copyTo(firstFrame);
//            firstFrame.convertTo(firstFrame, CvType.CV_16U);
//            sumFrames = Mat.zeros(frame.rows(), frame.cols(), CvType.CV_16U);
//            average   = new Mat(frame.rows(), frame.cols(), CvType.CV_8U);
//            lastFrame = new Mat(frame.rows(), frame.cols(), CvType.CV_16U);
//        }
//        frame.copyTo(lastFrame);
//        lastFrame.convertTo(lastFrame, CvType.CV_16U);
//
//        frameList.add(lastFrame);
//        Core.add(sumFrames, lastFrame, sumFrames);
//
//        if(frameList.size() < maxFrames+1)
//        {
//            Core.divide(sumFrames, new Scalar(frameList.size()), average);
//            return;
//        }
//        Core.subtract(sumFrames, firstFrame, sumFrames);
//        frameList.remove(0);
//        firstFrame = frameList.get(0);
//
//        Core.divide(sumFrames, scalar, average);
//    }
//    public Mat getAverage()
//    {
//        average.copyTo(ret_avg);
//        ret_avg.convertTo(ret_avg, CvType.CV_8U);
//        return ret_avg;
//    }
}