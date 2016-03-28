package be.ac.umons.test;

import be.ac.umons.bsp.BSPNode;
import be.ac.umons.bsp.FreeSplitsHeuristic;
import be.ac.umons.bsp.Segment;
import be.ac.umons.bsp.SegmentLoader;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by jeremy on 3/19/16.
 */
public class FreeSplitsHeuristicTest {
    public class FreeSplitsNoRandom extends FreeSplitsHeuristic{
        public BSPNode createTree(List<Segment> segmentList){
            Segment firstSegment = segmentList.get(0);
            segmentList.remove(0);
            BSPNode root = new BSPNode(null, null, segmentList, firstSegment);
            treeConstruction(root);
            return root;
        }
    }

    SegmentLoader loader;
    List<Segment> segmentList;
    FreeSplitsNoRandom heuristicTest = new FreeSplitsNoRandom();
    List<Segment> theoreticalTreeSegments1 = new ArrayList<>();
    List<Segment> theoreticalTreeSegments2 = new ArrayList<>();
    List<Segment> practicalTreeSegments = new ArrayList<>();

    public List<Segment> generateSegmentList(BSPNode root, List<Segment> acc){

        for (Segment seg : root.getSegmentsInLine()){
            acc.add(seg);
        }
        if (!root.isLeaf()){
            if (root.hasNoRightSon()){
                generateSegmentList(root.getLeftSon(), acc);
            }
            else if (root.hasNoLeftSon()){
                generateSegmentList(root.getRightSon(), acc);
            }

            else{
                generateSegmentList(root.getLeftSon(), acc);
                generateSegmentList(root.getRightSon(), acc);
            }
        }
        return acc;
    }

    @Before
    public void setUp(){
        practicalTreeSegments.clear();
        theoreticalTreeSegments1.clear();
        theoreticalTreeSegments2.clear();
    }

    @Test
    public void treeTest1(){
        //Basic test with a few segments
        loader = new SegmentLoader("assets/other/test.txt");
        segmentList = loader.loadAsList();
        BSPNode root = heuristicTest.createTree(segmentList);
        practicalTreeSegments = generateSegmentList(root, new ArrayList<Segment>());
        theoreticalTreeSegments1.add(new Segment(7,9,15,9, Color.RED));
        theoreticalTreeSegments1.add(new Segment(7,9,7,11, Color.RED));
        theoreticalTreeSegments1.add(new Segment(7,9,7,7, Color.RED));
        theoreticalTreeSegments2.add(new Segment(7,9,15,9, Color.RED));
        theoreticalTreeSegments2.add(new Segment(7,8,7,11, Color.RED));
        theoreticalTreeSegments2.add(new Segment(7,9,7,7, Color.RED));
        assertThat(practicalTreeSegments, is(theoreticalTreeSegments1));
        assertThat(practicalTreeSegments, is(not(theoreticalTreeSegments2)));
    }

    @Test
    public void treeTest2(){
        //Test with more segments and three free splits
        loader = new SegmentLoader("assets/other/free_splits.txt");
        segmentList = loader.loadAsList();
        BSPNode root = heuristicTest.createTree(segmentList);
        practicalTreeSegments = generateSegmentList(root, new ArrayList<Segment>());
        theoreticalTreeSegments1.add(new Segment(15.00053883605,9,7,9, Color.RED));
        theoreticalTreeSegments1.add(new Segment(15.00053883605,9,19.2880240158, 11.0971451132, Color.RED));
        theoreticalTreeSegments1.add(new Segment(22.5574468033,12.6963235906,22.66,12.58, Color.RED));
        theoreticalTreeSegments1.add(new Segment(9.575,14.095, 21.1653069509, 14.27539388250, Color.RED));
        theoreticalTreeSegments1.add(new Segment(21.1653069509, 14.27539388250,22.5574468033,12.6963235906, Color.RED));
        theoreticalTreeSegments1.add(new Segment(8.3433333333,9, 5.42, 13.385, Color.RED));
        theoreticalTreeSegments1.add(new Segment(13.0688831065, 14.1493795036, 11, 13, Color.RED));
        theoreticalTreeSegments1.add(new Segment(13.0688831065, 14.1493795036, 20, 18, Color.RED));
        theoreticalTreeSegments1.add(new Segment(18.5780669144, 17.2100371747, 21.1653069509, 14.27539388250, Color.RED));
        theoreticalTreeSegments1.add(new Segment(15.811064862, 14.8328308814, 18.3312216432, 15.6979593287, Color.RED));
        theoreticalTreeSegments1.add(new Segment(18.5780669144, 17.2100371747, 17, 19, Color.RED));
        theoreticalTreeSegments1.add(new Segment(15.00053883605, 9, 8.765, 5.95, Color.RED));
        theoreticalTreeSegments1.add(new Segment(9.9803543899, 6.5444684151, 11.42, 4.385, Color.RED));
        theoreticalTreeSegments1.add(new Segment(9.9803543899, 6.5444684151, 8.3433333333, 9, Color.RED));
        theoreticalTreeSegments1.add(new Segment(9.665, 8.065, 11.735, 8.245, Color.RED));
        assertThat(practicalTreeSegments, is(theoreticalTreeSegments1));
    }


    @Test
    public void treeTest3(){
        //Test with 1 free split on the extremities of two segments (cutting their sides)
        loader = new SegmentLoader("assets/other/free_splits2.txt");
        segmentList = loader.loadAsList();
        BSPNode root = heuristicTest.createTree(segmentList);
        practicalTreeSegments = generateSegmentList(root, new ArrayList<Segment>());
        theoreticalTreeSegments1.add(new Segment(-1.86,-2.36,3.24,1.36,Color.RED));
        theoreticalTreeSegments1.add(new Segment(4.9485494022, 2.6062360346, 6.2770002309, 3.5752236978, Color.RED));
        theoreticalTreeSegments1.add(new Segment(7.5155555556, 2.2733333333, 6.4955555556, -1.4266666667, Color.RED));
        theoreticalTreeSegments1.add(new Segment(6.1939539697, -2.5207116349, 5.5822752645, -4.739546154, Color.RED));
        theoreticalTreeSegments1.add(new Segment(6.2770002309, 3.5752236978, 7.5155555556, 2.2733333333,Color.RED));
        theoreticalTreeSegments1.add(new Segment(5.906255144, 1.5526748971, 4.026255144, 0.7326748971,Color.RED));
        assertThat(practicalTreeSegments, is(theoreticalTreeSegments1));
    }
}
