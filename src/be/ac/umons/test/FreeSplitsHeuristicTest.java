package be.ac.umons.test;

import be.ac.umons.bsp.BSPNode;
import be.ac.umons.bsp.FreeSplitsHeuristic;
import be.ac.umons.bsp.Segment;
import be.ac.umons.bsp.SegmentLoader;

import java.util.List;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jeremy on 3/19/16.
 */
public class FreeSplitsHeuristicTest {
    public class FreeSplitsTest extends FreeSplitsHeuristic{
        public BSPNode createTree(List<Segment> segmentList){
            Segment firstSegment = segmentList.get(0);
            segmentList.remove(0);
            BSPNode root = new BSPNode(null, null, segmentList, firstSegment);
            treeConstruction(root);
            return root;
        }
    }
    @Test
    public void checkNodes(){
        SegmentLoader loader = new SegmentLoader();
        List<Segment>myList = loader.loadAsList("assets/other/free_splits.txt");
        FreeSplitsHeuristic test = new FreeSplitsHeuristic();
        BSPNode root = test.createTree(myList);
        root.printTree();
    }
}
