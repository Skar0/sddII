package be.ac.umons.test;

import be.ac.umons.gui.SegmentsPainter;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by clement on 3/17/16.
 */
public class SegmentsPainterTest {

    @org.junit.Test
    public void testComputeAngle() throws Exception {
        double expected1 =0;
        double actual1 = Math.toDegrees(SegmentsPainter.computeAngle(0,0,-1,0,0,0,-1,0) );
        assertEquals(expected1, actual1, 0.0001);

    }
}
