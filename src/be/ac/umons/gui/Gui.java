package be.ac.umons.gui;

import be.ac.umons.bsp.BSPNode;
import be.ac.umons.bsp.InOrderHeuristic;
import be.ac.umons.bsp.Segment;
import be.ac.umons.bsp.SegmentLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Created by clement on 2/27/16.
 */
public class Gui extends JFrame{

    public static void main(String[] args) {
        /*
        SegmentLoader loader = new SegmentLoader("assets/other/wikipediaExample.txt");
        List<Segment> segmentList = loader.loadAsList();

        InOrderHeuristic heuristicBuilder = new InOrderHeuristic();

        BSPNode root = heuristicBuilder.createTree(segmentList);

        MainFrame frame = new MainFrame();
        SegmentsPainter menu = new SegmentsPainter(root);
        */

        MainMenu menu = new MainMenu();
    }

    public void createMenu() {


    }
}
