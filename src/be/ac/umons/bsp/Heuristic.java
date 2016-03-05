package be.ac.umons.bsp;

import java.util.List;

/**
 * Created by mr_robot on 04-03-16.
 */
public interface Heuristic {
    double EPSILON = 0.0000001;
    BSPNode createTree(List<Segment> segmentList);

}
