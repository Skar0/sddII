package be.ac.umons.bsp;

import java.util.Collections;
import java.util.List;

/**
 * Created by mr_robot on 04-03-16.
 */
public class RandomHeuristic implements Heuristic {

    @Override
    public BSPNode createTree(List<Segment> segmentList) {
        InOrderHeuristic inOrder = new InOrderHeuristic();
        Collections.shuffle(segmentList);
        return inOrder.createTree(segmentList);
    }

    @Override public String toString() {
        return "Random";
    }
}
