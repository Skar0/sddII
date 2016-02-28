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
    /*
        // -------- 1
        // -------- 2
        List<Segment> situation1 = {};

        // -------- 2
        // -------- 1
        List<Segment> situation2 = {};

        // |
        // |------- 1
        // |
        List<Segment> situation3 = {};

        // |
        // |_________ 1
        List<Segment> situation4 = {};

        //  _____ 1
        // |
        List<Segment> situation5 = {};

        //             |
        // 1 ----------|
        //             |
        List<Segment> situation6 = {};

        //              |
        // 1 -----------|
        List<Segment> situation7 = {};

        // 1 -----------|
        //              |
        List<Segment> situation8 = {};

        //           |
        // 1 --------|-----
        List<Segment> situation9 = {};

        // 1 --------|-----
        //           |
        List<Segment> situation10 = {};

        //           |
        // 1 --------|----
        //           |
        List<Segment> situation11 = {};

        // 1 --------  |
        //             |
        List<Segment> situation12 = {};

        //             |
        // 1 --------  |
        //             |
        List<Segment> situation13 = {};

        //             |
        // ----------  |
        List<Segment> situation14 = {};

*/

        SegmentLoader loader2 = new SegmentLoader("assets/other/wikipediaExample.txt");
        List<Segment> segmentList2 = loader.loadAsList();

        double[] line1 = segmentList2.get(0).computeLine();
        double[] intersection = segmentList2.get(1).computePosition(line1, segmentList2.get(0));
        System.out.println(intersection[0]+" "+intersection[1]);
        double[] actual = {7,9};
        assertArrayEquals(intersection,actual,0.0001);


    }
}