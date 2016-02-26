package be.ac.umons.test;

import be.ac.umons.bsp.Segment;
import be.ac.umons.bsp.SegmentLoader;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by clement on 2/26/16.
 */
public class SegmentLoaderTest {

    @Test
    public void testLoadAsList() throws Exception {
        SegmentLoader loader = new SegmentLoader("assets/other/wikipediaExample.txt");
        for (Segment seg : loader.loadAsList()) {
            System.out.println(seg.getX1()+" "+seg.getY1()+" "+seg.getX2()+" "+seg.getY2());
        }
    }
}