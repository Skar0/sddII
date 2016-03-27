package be.ac.umons.test;

import be.ac.umons.bsp.Segment;
import be.ac.umons.bsp.SegmentLoader;

import java.awt.*;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by clement on 2/26/16.
 */
public class SegmentTest {
    SegmentLoader loader = new SegmentLoader();
    List<Segment> segmentList = loader.loadAsList("assets/other/wikipediaExample.txt");

    @org.junit.Test
    public void testComputeLine() throws Exception {

        double[] line1 = segmentList.get(0).computeLine();
        double[] line2 = segmentList.get(1).computeLine();
        double[] line3 = segmentList.get(2).computeLine();
        double[] line4 = segmentList.get(3).computeLine();

        double[] manuallyComputedLine1 = {0,-1,9};
        double[] manuallyComputedLine2 = {1,0,-7}; //Multiplié par -1
        double elem1 = (double) 7/6;
        double elem2 = (double) -67/6;
        double[] manuallyComputedLine3 = {elem1,-1,elem2}; // divisé par 6
        double[] manuallyComputedLine4 = {-1.5,-1,14}; //divisé par 2, multiplié par -1

        System.out.println("COMPUTED ="+line1[0]+" "+line1[1]+" "+line1[2]);
        System.out.println("ACTUAL ="+manuallyComputedLine1[0]+" "+manuallyComputedLine1[1]+" "+manuallyComputedLine1[2]);
        assertArrayEquals(manuallyComputedLine1,line1,0.0001);

        System.out.println("COMPUTED ="+line2[0]+" "+line2[1]+" "+line2[2]);
        System.out.println("ACTUAL ="+manuallyComputedLine2[0]+" "+manuallyComputedLine2[1]+" "+manuallyComputedLine2[2]);
        assertArrayEquals(manuallyComputedLine2,line2,0.0001);

        System.out.println("COMPUTED ="+line3[0]+" "+line3[1]+" "+line3[2]);
        System.out.println("ACTUAL ="+manuallyComputedLine3[0]+" "+manuallyComputedLine3[1]+" "+manuallyComputedLine3[2]);
        assertArrayEquals(manuallyComputedLine3,line3,0.0001);

        System.out.println("COMPUTED ="+line3[0]+" "+line3[1]+" "+line3[2]);
        System.out.println("ACTUAL ="+manuallyComputedLine3[0]+" "+manuallyComputedLine3[1]+" "+manuallyComputedLine3[2]);
        assertArrayEquals(manuallyComputedLine3,line3,0.0001);

        System.out.println("COMPUTED ="+line4[0]+" "+line4[1]+" "+line4[2]);
        System.out.println("ACTUAL ="+manuallyComputedLine4[0]+" "+manuallyComputedLine4[1]+" "+manuallyComputedLine4[2]);
        assertArrayEquals(manuallyComputedLine4,line4,0.0001);

    }

    @org.junit.Test
    public void testComputePosition() throws Exception {
        /** Horizontal lines **/
        Segment segment1 = new Segment(2,3,6,3, Color.red);
        double[] line1 = segment1.computeLine();

        // -------- 1
        // -------- 2
        Segment segmentTest1 = new Segment(2,2,6,2, Color.blue);
        double[] expectedAnswerTest1 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest1, segmentTest1.computePosition(line1, segment1), 0.0001);

        // -------- 2
        // -------- 1
        Segment segmentTest2 = new Segment(2,4,6,4, Color.blue);
        double[] expectedAnswerTest2 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest2, segmentTest2.computePosition(line1, segment1), 0.0001);

        // |
        // |------- 1
        // |
        Segment segmentTest3 = new Segment(2,1,2,5, Color.blue);
        double[] expectedAnswerTest3 = {2,3};
        assertArrayEquals(expectedAnswerTest3, segmentTest3.computePosition(line1, segment1), 0.0001);

        // |
        // |_________ 1
        Segment segmentTest4 = new Segment(2,3,2,5, Color.blue);
        double[] expectedAnswerTest4 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest4, segmentTest4.computePosition(line1, segment1), 0.0001);

        //  _____ 1
        // |
        Segment segmentTest5 = new Segment(2,3,2,1, Color.blue);
        double[] expectedAnswerTest5 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest5, segmentTest5.computePosition(line1, segment1), 0.0001);

        //             |
        // 1 ----------|
        //             |
        Segment segmentTest6 = new Segment(6,1,6,5, Color.blue);
        double[] expectedAnswerTest6 = {6,3};
        assertArrayEquals(expectedAnswerTest6, segmentTest6.computePosition(line1, segment1), 0.0001);

        //              |
        // 1 -----------|
        Segment segmentTest7 = new Segment(6,3,6,6, Color.blue);
        double[] expectedAnswerTest7 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest7, segmentTest7.computePosition(line1, segment1), 0.0001);

        // 1 -----------|
        //              |
        Segment segmentTest8 = new Segment(6,3,6,1, Color.blue);
        double[] expectedAnswerTest8 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest8, segmentTest8.computePosition(line1, segment1), 0.0001);

        //           |
        // 1 --------|-----
        Segment segmentTest9 = new Segment(4,3,4,6, Color.blue);
        double[] expectedAnswerTest9 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest9, segmentTest9.computePosition(line1, segment1), 0.0001);

        // 1 --------|-----
        //           |
        Segment segmentTest10 = new Segment(4,3,4,2, Color.blue);
        double[] expectedAnswerTest10 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest10, segmentTest10.computePosition(line1, segment1), 0.0001);

        //           |
        // 1 --------|----
        //           |
        Segment segmentTest11 = new Segment(4,5,4,1, Color.blue);
        double[] expectedAnswerTest11 = {4,3};
        assertArrayEquals(expectedAnswerTest11, segmentTest11.computePosition(line1, segment1), 0.0001);

        // 1 --------  |
        //             |
        Segment segmentTest12 = new Segment(8,3,8,1, Color.blue);
        double[] expectedAnswerTest12 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest12, segmentTest12.computePosition(line1, segment1), 0.0001);

        //             |
        // 1 --------  |
        //             |
        Segment segmentTest13 = new Segment(8,1,8,6, Color.blue);
        double[] expectedAnswerTest13 = {8,3};
        assertArrayEquals(expectedAnswerTest13, segmentTest13.computePosition(line1, segment1), 0.0001);

        //             |
        // ----------  |
        Segment segmentTest14 = new Segment(8,3,8,6, Color.blue);
        double[] expectedAnswerTest14 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest14, segmentTest14.computePosition(line1, segment1), 0.0001);

        //    |
        //    |
        //
        // 1 -------
        Segment segmentTest15 = new Segment(4,4,4,6, Color.blue);
        double[] expectedAnswerTest15 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest15, segmentTest15.computePosition(line1, segment1), 0.0001);

        // 1 -------
        //
        //     |
        //     |
        Segment segmentTest16 = new Segment(5,1,5,2, Color.blue);
        double[] expectedAnswerTest16 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest16, segmentTest16.computePosition(line1, segment1), 0.0001);

        /** Vertical lines **/

        //Vertical line
        Segment segment4 = new Segment(3,1,3,5,Color.red);
        double[] line4 = segment4.computeLine();

        //Other vertical line on the left
        Segment segmentTest22 = new Segment(2,1,2,5, Color.blue);
        double[] expectedAnswerTest22 = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest22, segmentTest22.computePosition(line4, segment4), 0.01);

        //Other vertical line on the right
        Segment segmentTest23 = new Segment(4,1,4,5, Color.blue);
        double[] expectedAnswerTest23 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest23, segmentTest23.computePosition(line4, segment4), 0.01);

        //Segment on the left
        Segment segmentTest24 = new Segment(0,1,1,2, Color.blue);
        double[] expectedAnswerTest24 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest24, segmentTest24.computePosition(line4, segment4), 0.01);

        //Segment on the right
        Segment segmentTest25 = new Segment(6,0,7,3, Color.blue);
        double[] expectedAnswerTest25 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest25, segmentTest25.computePosition(line4, segment4), 0.01);

        //Horizontal line crossing it
        Segment segmentTest26 = new Segment(2,2,4,2, Color.blue);
        double[] expectedAnswerTest26 = {3,2};
        assertArrayEquals(expectedAnswerTest26, segmentTest26.computePosition(line4, segment4), 0.01);

        //Line crossing it
        Segment segmentTest27 = new Segment(5,5,1,3, Color.blue);
        double[] expectedAnswerTest27 = {3,4};
        assertArrayEquals(expectedAnswerTest27, segmentTest27.computePosition(line4, segment4), 0.01);

        /** Positive slope **/

        //Line positive slope
        Segment segment5 = new Segment(1,1,4,2,Color.red);
        double[] line5 = segment5.computeLine();

        //Segment below it (on the right of it)
        Segment segmentTest28 = new Segment(4,0.5,5,1.5, Color.blue);
        double[] expectedAnswerTest28 = {Double.NaN, Double.NaN};
        assertArrayEquals(expectedAnswerTest28, segmentTest28.computePosition(line5, segment5), 0.01);

        //Segment below it (on the right of it)
        Segment segmentTest29 = new Segment(2,0.5,3,0.5, Color.blue);
        double[] expectedAnswerTest29 = {Double.NaN, Double.NaN};
        assertArrayEquals(expectedAnswerTest29, segmentTest29.computePosition(line5, segment5), 0.01);

        //Segment above it (on the left of it)
        Segment segmentTest30 = new Segment(2.5,2.5,3.4,2.4, Color.blue);
        double[] expectedAnswerTest30 = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest30, segmentTest30.computePosition(line5, segment5), 0.01);

        //Segment above it (on the left of it)
        Segment segmentTest31 = new Segment(1.7,2.5,1.2,2, Color.blue);
        double[] expectedAnswerTest31 = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest31, segmentTest31.computePosition(line5, segment5), 0.01);

        /** Negative slope **/

        //Line positive slope
        Segment segment6 = new Segment(2.6,5,6.7,3.6,Color.red);
        double[] line6 = segment6.computeLine();

        //Segment below it (on the left of it)
        Segment segmentTest32 = new Segment(2,4,2.5,2.5, Color.blue);
        double[] expectedAnswerTest32 = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest32 , segmentTest32 .computePosition(line6, segment6), 0.01);

        //Segment below it (on the left of it)
        Segment segmentTest33 = new Segment(3.6,2.5,5.2,3, Color.blue);
        double[] expectedAnswerTest33 = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest33, segmentTest33.computePosition(line6, segment6), 0.01);

        //Segment above it (on the right of it)
        Segment segmentTest34 = new Segment(6.8,5.9,7,7.8, Color.blue);
        double[] expectedAnswerTest34 = {Double.NaN, Double.NaN};
        assertArrayEquals(expectedAnswerTest34, segmentTest34.computePosition(line6, segment6), 0.01);

        //Segment above it (on the right of it)
        Segment segmentTest35 = new Segment(5.7,7.5,3.7,7.3, Color.blue);
        double[] expectedAnswerTest35 = {Double.NaN, Double.NaN};
        assertArrayEquals(expectedAnswerTest35, segmentTest35.computePosition(line6, segment6), 0.01);

        /** Tests about the octogon **/

        //Grey segment
        Segment segment7 = new Segment(0.000000 ,200.000000 ,-141.421356 ,141.421356, Color.gray);
        double[] line7 = segment7.computeLine();

        //Orange segment
        Segment segmentTest36 = new Segment(-200.000000 ,0.000000 ,-141.421356 ,-141.421356 ,Color.orange);
        double[] expectedAnswerTest36 = {Double.NaN, Double.NaN};
        assertArrayEquals(expectedAnswerTest36 , segmentTest36.computePosition(line7, segment7), 0.01);

        //Violet segment
        Segment segmentTest37 = new Segment(-141.421356 ,-141.421356 ,-0.000000 ,-200.000000 ,Color.pink);
        double[] expectedAnswerTest37 = {Double.NaN, Double.NaN};
        assertArrayEquals(expectedAnswerTest37, segmentTest37.computePosition(line7, segment7), 0.01);

        //black segment
        Segment segmentTest38 = new Segment(141.421356 ,-141.421356 ,200.000000 ,-0.000000 ,Color.black);
        double[] expectedAnswerTest38 = {Double.NaN, Double.NaN};
        assertArrayEquals(expectedAnswerTest38, segmentTest38.computePosition(line7, segment7), 0.01);

        //yellow segment
        Segment segmentTest39 = new Segment(-0.000000 ,-200.000000, 141.421356 ,-141.421356, Color.yellow);
        double[] expectedAnswerTest39 = {Double.NaN, Double.NaN};
        assertArrayEquals(expectedAnswerTest39, segmentTest39.computePosition(line7, segment7), 0.01);

    }
}