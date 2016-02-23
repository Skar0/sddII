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
    public BSPNode createTree(List<Segment> segmentList) {
        Segment firstLineInList = segmentList.get(0);
        segmentList.remove(0);
        BSPNode root = new BSPNode(null, null, segmentList,firstLineInList);
        createRoot(root);
        //TODO remove
        root.printTree();
        return root;
    }

    public void createRoot(BSPNode node) {
        if(node !=  null) {
            for (Segment seg : node.getSegmentsInHyperplane()) {
                //if lines are equals, seg is contained in the cutting line of the node
                if( (seg.computeLine()[0] == node.getLine()[0]) && (seg.computeLine()[1] == node.getLine()[1]) && (seg.computeLine()[2] == node.getLine()[2]) ) {
                    node.addSegment(seg);
                    node.getSegmentsInHyperplane().remove(seg);
                }
            }

            for (Segment seg : node.getSegmentsInHyperplane()) {
                double[] intersection = seg.computePosition(node.getLine(), node.getSegmentsInLine().get(0));
                if(intersection[0] == Double.POSITIVE_INFINITY) {
                    //add right node
                     //       delete from node list ?
                }
                else if(intersection[0] == Double.POSITIVE_INFINITY) {
                    //add left node
                }
                else {

                }
            }

            createRoot(node.getLeftSon());
            createRoot(node.getRightSon());
        }
    }
}
