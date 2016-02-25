package be.ac.umons.cui;

import be.ac.umons.bsp.BSPNode;
import be.ac.umons.bsp.InOrderHeuristic;
import be.ac.umons.bsp.Segment;
import be.ac.umons.bsp.SegmentLoader;

import java.util.List;

/**
 * Created by clement on 2/21/16.
 */
public class Cui {

    public static void main(String [] args) {
        System.out.println("test");
        SegmentLoader loader = new SegmentLoader("assets/first/octogone.txt");
        List<Segment> segmentList = loader.loadAsList();

        InOrderHeuristic heuristicBuilder = new InOrderHeuristic();

       BSPNode root = heuristicBuilder.createTree(segmentList);
        root.printTree();
        System.out.println(root.getLine()[0]);
    }


}
