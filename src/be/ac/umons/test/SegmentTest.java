package be.ac.umons.test;

import be.ac.umons.bsp.Segment;
import be.ac.umons.bsp.SegmentLoader;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by clement on 2/26/16.
 */
public class SegmentTest {
    SegmentLoader loader = new SegmentLoader("assets/other/wikipediaExample.txt");
    List<Segment> segmentList = loader.loadAsList();

    @org.junit.Test
    public void testComputeLine() throws Exception {
        /*
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
*/
    }

    @org.junit.Test
    public void testComputePosition() throws Exception {
        Segment segment1 = new Segment(2,3,6,3,"Rouge");
        double[] line1 = segment1.computeLine();

        // -------- 1
        // -------- 2
        Segment segmentTest1 = new Segment(2,2,6,2, "Bleu");
        double[] expectedAnswerTest1 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest1, segmentTest1.computePosition(line1, segment1), 0.0001);

        // -------- 2
        // -------- 1
        Segment segmentTest2 = new Segment(2,4,6,4, "Bleu");
        double[] expectedAnswerTest2 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest2, segmentTest2.computePosition(line1, segment1), 0.0001);

        // |
        // |------- 1
        // |
        Segment segmentTest3 = new Segment(2,1,2,5, "Bleu");
        double[] expectedAnswerTest3 = {2,3};
        assertArrayEquals(expectedAnswerTest3, segmentTest3.computePosition(line1, segment1), 0.0001);

        // |
        // |_________ 1
        Segment segmentTest4 = new Segment(2,3,2,5, "Bleu");
        double[] expectedAnswerTest4 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest4, segmentTest4.computePosition(line1, segment1), 0.0001);

        //  _____ 1
        // |
        Segment segmentTest5 = new Segment(2,3,2,1, "Bleu");
        double[] expectedAnswerTest5 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest5, segmentTest5.computePosition(line1, segment1), 0.0001);

        //             |
        // 1 ----------|
        //             |
        Segment segmentTest6 = new Segment(6,1,6,5, "Bleu");
        double[] expectedAnswerTest6 = {6,3};
        assertArrayEquals(expectedAnswerTest6, segmentTest6.computePosition(line1, segment1), 0.0001);

        //              |
        // 1 -----------|
        Segment segmentTest7 = new Segment(6,3,6,6, "Bleu");
        double[] expectedAnswerTest7 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest7, segmentTest7.computePosition(line1, segment1), 0.0001);

        // 1 -----------|
        //              |
        Segment segmentTest8 = new Segment(6,3,6,1, "Bleu");
        double[] expectedAnswerTest8 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest8, segmentTest8.computePosition(line1, segment1), 0.0001);

        //           |
        // 1 --------|-----
        Segment segmentTest9 = new Segment(4,3,4,6, "Bleu");
        double[] expectedAnswerTest9 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest9, segmentTest9.computePosition(line1, segment1), 0.0001);

        // 1 --------|-----
        //           |
        Segment segmentTest10 = new Segment(4,3,4,2, "Bleu");
        double[] expectedAnswerTest10 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest10, segmentTest10.computePosition(line1, segment1), 0.0001);

        //           |
        // 1 --------|----
        //           |
        Segment segmentTest11 = new Segment(4,5,4,1, "Bleu");
        double[] expectedAnswerTest11 = {4,3};
        assertArrayEquals(expectedAnswerTest11, segmentTest11.computePosition(line1, segment1), 0.0001);

        // 1 --------  |
        //             |
        Segment segmentTest12 = new Segment(8,3,8,1, "Bleu");
        double[] expectedAnswerTest12 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest12, segmentTest12.computePosition(line1, segment1), 0.0001);

        //             |
        // 1 --------  |
        //             |
        Segment segmentTest13 = new Segment(8,1,8,6, "Bleu");
        double[] expectedAnswerTest13 = {8,3};
        assertArrayEquals(expectedAnswerTest13, segmentTest13.computePosition(line1, segment1), 0.0001);

        //             |
        // ----------  |
        Segment segmentTest14 = new Segment(8,3,8,6, "Bleu");
        double[] expectedAnswerTest14 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest14, segmentTest14.computePosition(line1, segment1), 0.0001);

        //    |
        //    |
        //
        // 1 -------
        Segment segmentTest15 = new Segment(4,4,4,6, "Bleu");
        double[] expectedAnswerTest15 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest15, segmentTest15.computePosition(line1, segment1), 0.0001);

        // 1 -------
        //
        //     |
        //     |
        Segment segmentTest16 = new Segment(5,1,5,2, "Bleu");
        double[] expectedAnswerTest16 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest16, segmentTest16.computePosition(line1, segment1), 0.0001);

        Segment segment2 = new Segment(4,5,6,3,"Rouge");
        double[] line2 = segment2.computeLine();

        Segment segmentTest17 = new Segment(6,5,7,6, "Bleu");
        double[] expectedAnswerTest17 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest17, segmentTest17.computePosition(line2, segment2), 0.0001);

        Segment segmentTest18 = new Segment(2,3,3,4, "Bleu");
        double[] expectedAnswerTest18 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest18, segmentTest18.computePosition(line2, segment2), 0.0001);

        Segment segment3 = new Segment(2,1,5,2,"Rouge");
        double[] line3 = segment3.computeLine();

        Segment segmentTest19 = new Segment(6,2,7,1, "Bleu");
        double[] expectedAnswerTest19 = {Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY};
        assertArrayEquals(expectedAnswerTest19, segmentTest19.computePosition(line3, segment3), 0.0001);

        Segment segmentTest20 = new Segment(3,2,4,3, "Bleu");
        double[] expectedAnswerTest20 = {Double.NaN,Double.NaN};
        assertArrayEquals(expectedAnswerTest20, segmentTest20.computePosition(line3, segment3), 0.0001);

        Segment segmentTest21 = new Segment(5,4,8,2, "Bleu");
        double[] expectedAnswerTest21 = {7,(double) 2.67};
        assertArrayEquals(expectedAnswerTest21, segmentTest21.computePosition(line3, segment3), 0.01);


/*
        SegmentLoader loader2 = new SegmentLoader("assets/other/wikipediaExample.txt");
        List<Segment> segmentList2 = loader.loadAsList();

        double[] line1 = segmentList2.get(0).computeLine();
        double[] intersection = segmentList2.get(1).computePosition(line1, segmentList2.get(0));
        System.out.println(intersection[0]+" "+intersection[1]);
        double[] actual = {7,9};
        assertArrayEquals(intersection,actual,0.0001);*/


    }
}