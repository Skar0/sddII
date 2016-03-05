package be.ac.umons.bsp;

import java.util.List;

/**
 * Created by mr_robot on 04-03-16.
 */
public interface Heuristic {

    BSPNode createTree(List<Segment> segmentList);

}
