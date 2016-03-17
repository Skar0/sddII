package be.ac.umons.bsp;

import java.util.Collections;
import java.util.List;

/**
 * This heuristic is similar to the in-order heuristic but it shuffles the list of segments first.
 * @author Clément Tamines
 */
public class RandomHeuristic implements Heuristic {


    /**
     * Creates a BSP tree using the random heuristic
     * @param segmentList List of segments to make a BSP tree out of
     * @return The BSPNode that is the root of the created tree.
     */
    public BSPNode createTree(List<Segment> segmentList) {

        //The random heuristic is just the in-order heuristic applied to a shuffled list of segments.
        InOrderHeuristic inOrder = new InOrderHeuristic();
        Collections.shuffle(segmentList);
        return inOrder.createTree(segmentList);
    }

    //toString is overriden to be used in the gui comboBoxes
    @Override public String toString() {
        return "Random";
    }
}
