package be.ac.umons.cui;

import be.ac.umons.bsp.*;

import java.io.Console;
import java.util.List;

/**
 * Created by clement on 2/21/16.
 */
public class Cui {

    public static void main(String [] args) {
        System.out.println("-BSP Tree comparator-\n When prompted, please enter the path to a file containing " +
                "a 2d scene. You are currently in the directory "+System.getProperty("user.dir")+".\n");

        Console console = System.console();
        System.out.print("Please enter the path to a 2d scene:");
        String path = "assets/first/octogone.txt";

        SegmentLoader loader = new SegmentLoader(path);
        List<Segment> segmentList = loader.loadAsList();

        Heuristic inOrderHeuristic = new InOrderHeuristic();
        Heuristic randomHeuristic = new RandomHeuristic();
        Heuristic freeSplitssHeuristic = new FreeSplitsHeuristic();

        long start = System.nanoTime();
        BSPNode inOrderRoot = inOrderHeuristic.createTree(segmentList);
        double elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;

        System.out.println("-In order heuristic-");
        System.out.println("Time to build tree : "+elapsedTimeInSec);
        System.out.println("Tree size :"+inOrderRoot.getSize());
        System.out.println("Tree height :"+inOrderRoot.getHeight());

        inOrderRoot.printTree();


    }
}
