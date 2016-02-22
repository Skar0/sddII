package be.ac.umons.bsp;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by clement on 2/22/16.
 */
public class InOrderHeuristic {

    /**
     * Creates a BSP tree using the in-order heuristic
     * @param segmentList List of segments to make a BSP tree out of
     * @return The root of the created BSP tree
     */
    public BSPNode createTree(List<Segment> segmentList, int index) {

        BSPNode root = new BSPNode(null, null, new LinkedList<Segment>());
        root.addSegment(segmentList.get(index));
        root.computeLine();
        segmentList.remove(index);
        index += 1;

        List<Segment> rightSonList = new LinkedList<>();
        List<Segment> leftSonList = new LinkedList<>();


        for (Segment seg : segmentList) {

        }
        return null;
    }
}
