package CustomProcessing;


import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

import java.util.ArrayList;

public class RegionProcessing
{
    private Mat     imageRegion;
    private Scalar  zeroScalar;
    private boolean initialized;

    public RegionProcessing()
    {
        initialized = false;
        zeroScalar  = new Scalar(0);
    }

    public void addFrame(Mat frame)
    {
        if (!initialized)
        {
            imageRegion = Mat.zeros(frame.rows(), frame.cols(), CvType.CV_16U);
        }

        // Resets the image to completely new
        imageRegion.setTo(zeroScalar);

        int numOfRows = frame.rows();
        int numOfCols = frame.cols();
        // 4 Path connectivity
        int up;
        int right;
        int left;
        int down;

        double data;

        int currentLabel = 1;
//        int[] label = new int[1];
//        label[0] = currentLabel; // This is needed to pass to put()
        int labelToWrite = currentLabel;

        for (int row = 1; row < numOfRows; row++)
        {
            for (int col = 1; col < numOfCols; col++)
            {
                if(frame.get(row, col)[0] < 1) continue; // Filter background

                up     = (int) imageRegion.get(row - 1, col)[0];
//                right  = (int) imageRegion.get(row, col+1)[0];
                left   = (int) imageRegion.get(row, col-1)[0];
//                down   = (int) imageRegion.get(row-1, col)[0];

                if(left == 0 && up == 0) // Both doesn't have a label
                {
                    labelToWrite = currentLabel;
                    currentLabel++;
                } else if (left > 0 && up < 1)
                {
                    labelToWrite = left;
                }
                else {
                    labelToWrite = up;
                }

                imageRegion.put(row, col, labelToWrite);
            }
        }

        // TODO: Currently this is has edge cases Implement the equivalence table
        int currentPixel = 0;
        for (int row = numOfRows - 2; row > 1; row--)
        {
            for (int col = numOfCols - 2; col > 1; col--)
            {
                if(frame.get(row, col)[0] < 1) continue; // Filter background
                currentPixel = (int) imageRegion.get(row,col)[0];
                if(currentPixel < 1) continue; // filter out non region

//                up     = (int) imageRegion.get(row - 1, col)[0];
                right  = (int) imageRegion.get(row, col+1)[0];
//                left   = (int) imageRegion.get(row, col-1)[0];
//                down   = (int) imageRegion.get(row-1, col)[0];

                if(right > 0)
                {
                    labelToWrite = right;
                }

                imageRegion.put(row, col, labelToWrite);
            }
        }
    }

    public Mat getImageRegion()
    {
        return imageRegion;
    }
}