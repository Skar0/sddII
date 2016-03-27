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
        loader = new SegmentLoader();
        segmentList = loader.loadAsList("assets/other/test.txt");
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
        loader = new SegmentLoader();
        segmentList = loader.loadAsList("assets/other/free_splits.txt");
        BSPNode root = heuristicTest.createTree(segmentList);
        practicalTreeSegments = generateSegmentList(root, new ArrayList<Segment>());
        theoreticalTreeSegments1.add(new Segment(15,9,7,9, Color.RED));
        theoreticalTreeSegments1.add(new Segment(15,9,19.29,11.1, Color.RED));
        theoreticalTreeSegments1.add(new Segment(22.56,12.7,22.66,12.58, Color.RED));
        theoreticalTreeSegments1.add(new Segment(9.58,14.1,21.14,14.28, Color.RED));
        theoreticalTreeSegments1.add(new Segment(21.16,14.28,22.55,12.69, Color.RED));
        theoreticalTreeSegments1.add(new Segment(8.34,9, 5.42, 13.39, Color.RED));
        theoreticalTreeSegments1.add(new Segment(13.07, 14.15, 11, 13, Color.RED));
        theoreticalTreeSegments1.add(new Segment(13.07, 14.15, 20, 18, Color.RED));
        theoreticalTreeSegments1.add(new Segment(18.57,17.21, 21.14, 14.28, Color.RED));
        theoreticalTreeSegments1.add(new Segment(15.81, 14.83, 18.33, 15.7, Color.RED));
        theoreticalTreeSegments1.add(new Segment(18.57, 17.21, 17, 19, Color.RED));
        theoreticalTreeSegments1.add(new Segment(15, 9, 8.77, 5.95, Color.RED));
        theoreticalTreeSegments1.add(new Segment(9.98, 6.54, 11.42, 4.39, Color.RED));
        theoreticalTreeSegments1.add(new Segment(9.98, 6.54, 8.34, 9, Color.RED));
        theoreticalTreeSegments1.add(new Segment(9.67, 8.07, 11.74, 8.25, Color.RED));
        assertThat(practicalTreeSegments, is(theoreticalTreeSegments1));
    }
}
