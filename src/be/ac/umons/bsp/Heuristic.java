package be.ac.umons.bsp;

import java.util.List;

/**
 * This interface makes it so every heuristics have the same method to create a tree. This makes it easier to add a new heuristic if we want to.
 * @author Clément Tamines
 */
public interface Heuristic {

    /**
     * This EPSILON value is used in the heuristics to allow precision errors.
     */
    double EPSILON = 0.0000001;

    /**
     * Creates a tree from a list of Segments objects.
     * @param segmentList the list of Segments.
     * @return the BSPNode which is the root of the created tree.
     */
    BSPNode createTree(List<Segment> segmentList);

}
