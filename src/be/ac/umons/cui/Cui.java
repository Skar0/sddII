package be.ac.umons.cui;

import be.ac.umons.bsp.*;
import be.ac.umons.gui.PaintersAlgorithm;
import be.ac.umons.gui.Pov;
import be.ac.umons.gui.SegmentsPainter;
import javafx.scene.shape.PolygonBuilder;

import java.awt.geom.Line2D;
import java.io.Console;
import java.util.List;

/**
 * This class provides a user interface using the user's terminal. It allows to choose a file containing a scene and then
 * comparing the heuristics applied on that file.
 *
 *
 * @author Clément Tamines
 */
public class Cui {

    public static void main(String [] args) {
        System.out.println("-BSP Tree comparator-\n When prompted, please enter the path to a file containing " +
                "a 2d scene. You are currently in the directory "+System.getProperty("user.dir")+".\n");

        Console console = System.console();
        System.out.print("Please enter the path to a 2d scene:");
        String path = "assets/rectangles/rectanglesMedium.txt";

        SegmentLoader loader = new SegmentLoader();
        List<Segment> segmentList = loader.loadAsList(path);

        Heuristic inOrderHeuristic = new InOrderHeuristic();
        Heuristic randomHeuristic = new RandomHeuristic();
        Heuristic freeSplitsHeuristic = new FreeSplitsHeuristic();

        Line2D line1 = new Line2D.Double(0,0,0,1);
        Line2D line2 = new Line2D.Double(0,0,1,1);
        Pov pov = new Pov(line1,line2);

        long start = System.nanoTime();
        BSPNode inOrderRoot = inOrderHeuristic.createTree(loader.loadAsList(path));
        double elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;

        System.out.println("-In order heuristic-");
        System.out.println("Time to build tree : "+elapsedTimeInSec);
        double time1 = getAverageTimeTreeCreation(inOrderHeuristic,path,200);
        System.out.println("Time to build tree 2 : "+time1);

        double time11 = getAverageTimePaintersAlgo(inOrderRoot,pov,200);
        System.out.println("Time to painter : "+time11);

        System.out.println("Number of segments :"+inOrderRoot.getSizeInSegments());
        System.out.println("Tree size :"+inOrderRoot.getSize());
        System.out.println("Tree height :"+inOrderRoot.getHeight());

        start = System.nanoTime();
        BSPNode randomRoot = randomHeuristic.createTree(loader.loadAsList(path));
        elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;

        System.out.println("-Random heuristic-");
        System.out.println("Time to build tree : "+elapsedTimeInSec);
        double time2 = getAverageTimeTreeCreation(randomHeuristic,path,200);
        System.out.println("Time to build tree 2 : "+time2);

        double time21 = getAverageTimePaintersAlgo(randomRoot,pov,200);
        System.out.println("Time to painter : "+time21);

        System.out.println("Number of segments :"+randomRoot.getSizeInSegments());
        System.out.println("Tree size :"+randomRoot.getSize());
        System.out.println("Tree height :"+randomRoot.getHeight());

        start = System.nanoTime();
        BSPNode freeSplitsRoot = freeSplitsHeuristic.createTree(loader.loadAsList(path));
        elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;

        System.out.println("-Free splits heuristic-");
        System.out.println("Time to build tree : "+elapsedTimeInSec);
        double time3 = getAverageTimeTreeCreation(freeSplitsHeuristic,path,200);
        System.out.println("Time to build tree 2 : "+time3);

        double time31 = getAverageTimePaintersAlgo(freeSplitsRoot,pov,200);
        System.out.println("Time to painter : "+time31);

        System.out.println("Number of segments :"+freeSplitsRoot.getSizeInSegments());
        System.out.println("Tree size :"+freeSplitsRoot.getSize());
        System.out.println("Tree height :"+freeSplitsRoot.getHeight());


    }

     public static double getAverageTimeTreeCreation(Heuristic heuristic, String path, double repeatCount) {
         BSPNode root;
         SegmentLoader loader = new SegmentLoader();
         long start = System.nanoTime();
         int i = 0;
         while(i < repeatCount) {
             root = heuristic.createTree(loader.loadAsList(path));
             i++;
         }
         double elapsedTimeInSec = (System.nanoTime() - start)*1.0e-9;

         return (elapsedTimeInSec/repeatCount);
    }

    public static double getAverageTimePaintersAlgo(BSPNode root, Pov pov, double repeatCount) {
        long start = System.nanoTime();
        int i = 0;
        while(i < repeatCount) {
            paintersAlgorithmForBenchmark(root, pov);
            i++;
        }
        double elapsedTimeInSec = (System.nanoTime() - start)*1.0e-9;

        return (elapsedTimeInSec/repeatCount);
    }

    public static void paintersAlgorithmForBenchmark(BSPNode root, Pov pov) {
        if (root != null) {
            if (root.isLeaf()) {
                for(Segment seg : root.getSegmentsInLine() ) {
                    //
                }
            } else if (getPovPosition(root, pov).isInfinite()) {
                paintersAlgorithmForBenchmark(root.getLeftSon(), pov);
                for(Segment seg : root.getSegmentsInLine() ) {
                    //
                }
                paintersAlgorithmForBenchmark(root.getRightSon(), pov);
            } else if (getPovPosition(root, pov).isNaN()) {
                paintersAlgorithmForBenchmark(root.getRightSon(), pov);
                for(Segment seg : root.getSegmentsInLine() ) {
                    //
                }
                paintersAlgorithmForBenchmark(root.getLeftSon(), pov);
            } else {
                paintersAlgorithmForBenchmark(root.getRightSon(), pov);
                paintersAlgorithmForBenchmark(root.getLeftSon(), pov);
            }
        }
    }


    public static Double getPovPosition(BSPNode root, Pov pov) {
        double[] line = root.getLine();

        double value;

        double returnValue = 0;

        if(Math.abs(line[0]-1)<= Heuristic.EPSILON && (line[1] == 0)) {
            value = -(line[0]*pov.getPosition()[0] + line[1]*pov.getPosition()[1] +line[2]);
        }

        else if( line[0] > 0 ) {
            value = -(line[0]*pov.getPosition()[0] + line[1]*pov.getPosition()[1] +line[2]);
        }
        else {
            value = (line[0]*pov.getPosition()[0] + line[1]*pov.getPosition()[1] +line[2]);
        }

        if(value > 0) {
            returnValue = Double.POSITIVE_INFINITY;
        }

        else if (value < 0) {
            returnValue = Double.NaN;
        }

        return returnValue;
    }

}
