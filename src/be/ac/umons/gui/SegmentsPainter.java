package be.ac.umons.gui;

import be.ac.umons.bsp.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Created by mr_robot on 04-03-16.
 */
public class SegmentsPainter extends JPanel implements ActionListener, ItemListener {

    //Keeping reference to the parent frame
    private JPanel panel;
    private Frame frame;
    private BSPNode root;

    //TODO remove
    private int scale = 1;

    //Input method for the pov
    private int inputMode = 1;

    //Scaling of the scene so it is contained in the frame
    private double sceneScale;
    private double maxWidth;
    private double maxHeight;

    //Point of view
    private Pov pov = new Pov();

    //Keeping track of used heuristic and file
    Heuristic usedHeuristic;
    String usedFile;
    SegmentLoader loader;
    int usedAngle = 30;

    //Lines that represent the angle of the point of view
    private Line2D line1 = null;
    private Line2D line2 = null;

    //Lines that are actually drawn
    private Line2D lineToDraw1 = null;
    private Line2D lineToDraw2 = null;

    double norm;

    private double angle = Double.POSITIVE_INFINITY; //angle infini de base
    private double[] povScaledPosition = null;
    private double[] povPosition = null;

    //
    boolean okPainter = false;
    //

    //TODO manière dégeulasse
    private int clickCounter = 0;

    int debug = 10;

    public SegmentsPainter(Heuristic heuristic, String path, JFrame frame) {
        this.loader = new SegmentLoader();
        this.usedHeuristic = heuristic;
        this.usedFile = path;
        List<Segment> segmentList = loader.loadAsList(path);
        this.maxHeight = loader.getMaxHeight();
        this.maxWidth = loader.getMaxWidth();
        this.root = usedHeuristic.createTree(segmentList);
        this.frame = frame;
        this.panel = this;

        frame.setJMenuBar(this.createMenuBar());

        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                double x = e.getX();
                double y = e.getY();

                double x_scaled = ( e.getPoint().getX() - ( (double) panel.getWidth()/2.0) )/sceneScale;
                double y_scaled = ( -(e.getPoint().getY() - ( (double) panel.getHeight()/2.0)) )/sceneScale;

                if(inputMode == 1) {
                    switch (clickCounter % 3) {
                        case 0:
                            //
                            okPainter = false;

                            //
                            lineToDraw1 = null;
                            lineToDraw2 = null;
                            pov.setProjectionLine(null);

                            povScaledPosition = new double[2];
                            povScaledPosition[0] = x_scaled;
                            povScaledPosition[1] = y_scaled;

                            povPosition = new double[2];
                            povPosition[0] = x;
                            povPosition[1] = y;
                            clickCounter += 1;

                            panel.revalidate();
                            panel.repaint();

                            break;
                        case 1:
                            line1 = new Line2D.Double(povScaledPosition[0], povScaledPosition[1], x_scaled, y_scaled);
                            norm = Math.sqrt(Math.pow(x_scaled - povScaledPosition[0], 2) + Math.pow(y_scaled - povScaledPosition[1], 2));
                            double[] vector2 = {(x_scaled - povScaledPosition[0]) / norm, (y_scaled - povScaledPosition[1]) / norm};
                            lineToDraw1 = new Line2D.Double(povScaledPosition[0], povScaledPosition[1], povScaledPosition[0] + (vector2[0] * (norm + 10)), povScaledPosition[1] + (vector2[1] * (norm + 10)));
                            panel.revalidate();
                            panel.repaint();
                            clickCounter += 1;
                            break;
                        case 2:

                            //TODO ici on crée le pov, on dessine le panel
                            double vectorNorm = Math.sqrt(Math.pow(x_scaled - povScaledPosition[0], 2) + Math.pow(y_scaled - povScaledPosition[1], 2));
                            double[] vector = {(x_scaled - povScaledPosition[0]) / vectorNorm, (y_scaled - povScaledPosition[1]) / vectorNorm};
                            double[] secondPoint = {povScaledPosition[0] + (vector[0] * norm), povScaledPosition[1] + (vector[1] * norm)};

                            line2 = new Line2D.Double(povScaledPosition[0], povScaledPosition[1], secondPoint[0], secondPoint[1]);
                            lineToDraw2 = new Line2D.Double(povScaledPosition[0], povScaledPosition[1], povScaledPosition[0] + (vector[0] * (norm + 10)), povScaledPosition[1] + (vector[1] * (norm + 10)));
                            panel.revalidate();
                            panel.repaint();
                            clickCounter = 0;
                            pov = new Pov(line1, line2);
                            okPainter = true;
                            //TOREMOVE
                            /*
                            JFrame testFrame = new JFrame();
                            testFrame.setSize(600,600);
                            testFrame.setContentPane(new PaintersAlgorithm(pov,root));
                            testFrame.setVisible(true);
                            */
                            //
                            break;
                    }
                }
                else {
                    switch(clickCounter%2) {
                        case 0 :
                            //
                            okPainter = false;

                            //
                            lineToDraw1 = null;
                            lineToDraw2 = null;
                            pov.setProjectionLine(null);

                            povScaledPosition = new double[2];
                            povScaledPosition[0] = x_scaled;
                            povScaledPosition[1] = y_scaled;
                            clickCounter++;
                            break;
                        case 1 :
                            double[] directorVector = {x_scaled-povScaledPosition[0], y_scaled-povScaledPosition[1]};
                            //(x cos alpha + y sin alpha, -x sin alpha + y cos alpha).
                            double[] vector1 = {(directorVector[0]*Math.cos(usedAngle/2D)) +(directorVector[1]*Math.sin(usedAngle/2D)),(-directorVector[0]*Math.sin(usedAngle/2D))+(directorVector[1]*Math.cos(usedAngle/2D))};
                            double[] vector2 = {(directorVector[0]*Math.cos((-180+usedAngle)/2D)) +(directorVector[1]*Math.sin((-180+usedAngle)/2D)),(-directorVector[0]*Math.sin((-180+usedAngle)/2D))+(directorVector[1]*Math.cos((-180+usedAngle)/2D))};

                            line1 = new Line2D.Double(povScaledPosition[0],povScaledPosition[1],povScaledPosition[0]+vector1[0],povScaledPosition[1]+vector1[1]);
                            lineToDraw1 = new Line2D.Double(povScaledPosition[0],povScaledPosition[1],povScaledPosition[0]+vector1[0],povScaledPosition[1]+vector1[1]);

                            line2 = new Line2D.Double(povScaledPosition[0],povScaledPosition[1],povScaledPosition[0]+vector2[0],povScaledPosition[1]+vector2[1]);
                            lineToDraw2 = new Line2D.Double(povScaledPosition[0],povScaledPosition[1],povScaledPosition[0]+vector2[0],povScaledPosition[1]+vector2[1]);


                            panel.revalidate();
                            panel.repaint();
                            clickCounter = 0;
                            pov = new Pov(line1, line2);
                            okPainter = true;
                            break;
                    }
                }

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar;
        JMenu menu;
        JMenuItem menuItem;
        JRadioButtonMenuItem rbMenuItem;

        menuBar = new JMenuBar();

        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(menu);

        menuItem = new JMenuItem("Choose file",KeyEvent.VK_T);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Choose heuristic",KeyEvent.VK_T);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menu.addSeparator();

        ButtonGroup group = new ButtonGroup();

        rbMenuItem = new JRadioButtonMenuItem("Manual angle selection");
        rbMenuItem.setSelected(true);
        rbMenuItem.addItemListener(this);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Choose angle");
        rbMenuItem.addItemListener(this);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);


        menu = new JMenu("Help");
        menuBar.add(menu);

        menuItem = new JMenuItem("Help",KeyEvent.VK_T);
        menuItem.addActionListener(this);
        menu.add(menuItem);

        menuItem = new JMenuItem("About",KeyEvent.VK_T);
        menuItem.addActionListener(this);
        menu.add(menuItem);


        return menuBar;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JMenuItem source = (JMenuItem)(actionEvent.getSource());
        System.out.println(source.getText());
        switch(source.getText()) {
            case "Help":
                JOptionPane.showMessageDialog(frame, "This program allows you to load a scene, choose a point of view and see what part of the scene is seen by it.\n " +
                        "To choose a point of view, click once to choose the stating position of it. The next two clicks determine the positions of the two lines defining the point of view.", "Help",JOptionPane.INFORMATION_MESSAGE);
                break;
            case "About":
                JOptionPane.showMessageDialog(frame, "This program was created by Clément Tamines and Jérémy Gheysen for the course \"Structures de données II\" in the University of Mons", "About",JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Choose file":
                final JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")+ System.getProperty("file.separator")+"Documents"));
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    usedFile = file.getAbsolutePath();
                    System.out.println("Selected file: " + file.getAbsolutePath());
                    List<Segment> list = loader.loadAsList(usedFile);
                    this.maxHeight = loader.getMaxHeight();
                    this.maxWidth = loader.getMaxWidth();
                    this.root = usedHeuristic.createTree(list);
                    panel.repaint();
                }
                break;
            case "Choose heuristic":
                Heuristic inor = new InOrderHeuristic();
                Heuristic[] heuristics = {inor, new RandomHeuristic(), new FreeSplitsHeuristic()};
                Heuristic s = (Heuristic) JOptionPane.showInputDialog(
                        frame,
                        "Select a heuristic",
                        "Select a heuristic",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        heuristics,
                        inor);
                        if ((s != null)) {
                            this.usedHeuristic = s;
                            List<Segment> list = loader.loadAsList(usedFile);
                            this.root = usedHeuristic.createTree(list);
                            panel.repaint();
                        }

        }
    }

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        JMenuItem source = (JMenuItem)(itemEvent.getSource());
        if(itemEvent.getStateChange() == ItemEvent.SELECTED && source.getText() == "Manual angle selection") {
            this.inputMode = 1;
            System.out.println("MANUAL");
        }
        if(itemEvent.getStateChange() == ItemEvent.SELECTED && source.getText() == "Choose angle") {
            this.inputMode = 2;
            System.out.println("CHOOSE");
            int i = -1;
            do {
                String input = (String) JOptionPane.showInputDialog(
                        frame,
                        "Angle selection",
                        "Input an integer angle between 1 and 179",
                        JOptionPane.PLAIN_MESSAGE,
                        null,
                        null,
                        1);
                try {
                    if(input != null) {
                        i = Integer.parseInt(input);
                    }
                    else {
                        i = usedAngle;
                    }

                } catch (NumberFormatException e) {
                    i = -1;
                }
            } while(i < 1 || i > 179);
            this.usedAngle = i;
        }
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        double scalex = (double) panel.getHeight()/(maxHeight+50);
        double scaley = (double) panel.getWidth()/(maxWidth+50);
        sceneScale = Math.min(scalex,scaley);
        g2.translate((double) panel.getWidth()/2, (double) panel.getHeight()/2);
        g2.scale(sceneScale,-sceneScale);
        this.paintSegments(g2, this.root);
            g2.setColor(Color.BLACK);
        if(povScaledPosition != null) {
            g2.fillOval((int) povScaledPosition[0]-4,(int) povScaledPosition[1]-4,8,8);
        }
        if(lineToDraw1 != null) {
            g2.draw(lineToDraw1);
        }
        if(lineToDraw2 != null) {
            g2.draw(lineToDraw2);
        }
        /*
        if(pov.getProjectionLine() != null) {
            g2.draw(pov.getProjectionLine());
        }
        */
        /*
        if(pov.getDirectorVector() != null) {
            g2.draw(pov.getDirectorVector());
        }
        */
        //
        if(okPainter) {
            debug = 10;
            paintersAlgorithm(root, g2);
        }

        //


    }

    private void paintSegments(Graphics2D g, BSPNode root) {
        for(Segment seg : root.getSegmentsInLine()) {
            g.setColor(seg.getColor());
            g.draw(new Line2D.Double(seg.getX1()*scale,seg.getY1()*scale,seg.getX2()*scale,seg.getY2()*scale));
        }

        if (!root.isLeaf()) {

            if (root.hasNoLeftSon()) {
                paintSegments(g, root.getRightSon());
            }

            else if (root.hasNoRightSon()) {
                paintSegments(g, root.getLeftSon());
            }

            else {
                paintSegments(g, root.getLeftSon());
                paintSegments(g, root.getRightSon());
            }
        }
    }

    /* EVERYTHING FROM THIS POINT IS CCed FROM PAINTERSALORITHM */

    int toRemove = 0;

    public void paintersAlgorithm(BSPNode root, Graphics2D g2 ) {
        if (root != null) {
            if (root.isLeaf()) {
                drawSegments(root.getSegmentsInLine(), g2);
            } else if (getPovPosition(root).isInfinite()) {
                paintersAlgorithm(root.getLeftSon(), g2);
                drawSegments(root.getSegmentsInLine(), g2);
                paintersAlgorithm(root.getRightSon(), g2);
            } else if (getPovPosition(root).isNaN()) {
                paintersAlgorithm(root.getRightSon(), g2);
                drawSegments(root.getSegmentsInLine(), g2);
                paintersAlgorithm(root.getLeftSon(), g2);
            } else {
                paintersAlgorithm(root.getRightSon(), g2);
                paintersAlgorithm(root.getLeftSon(), g2);
            }
        }
    }

    public void paintersAlgorithmForBenchmark(BSPNode root) {
        if (root != null) {
            if (root.isLeaf()) {
                for(Segment seg : root.getSegmentsInLine() ) {
                    //
                }
            } else if (getPovPosition(root).isInfinite()) {
                paintersAlgorithmForBenchmark(root.getLeftSon());
                for(Segment seg : root.getSegmentsInLine() ) {
                    //
                }
                paintersAlgorithmForBenchmark(root.getRightSon());
            } else if (getPovPosition(root).isNaN()) {
                paintersAlgorithmForBenchmark(root.getRightSon());
                for(Segment seg : root.getSegmentsInLine() ) {
                    //
                }
                paintersAlgorithmForBenchmark(root.getLeftSon());
            } else {
                paintersAlgorithmForBenchmark(root.getRightSon());
                paintersAlgorithmForBenchmark(root.getLeftSon());
            }
        }
    }


    private Double getPovPosition(BSPNode root) {
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

    public void drawSegments(java.util.List<Segment> segments, Graphics2D g2) {
        double[] povLine1 = this.computeLine(pov.getLine1().getX1(), pov.getLine1().getY1(),pov.getLine1().getX2(),pov.getLine1().getY2());
        Segment povSegment1 = new Segment(pov.getLine1().getX1(), pov.getLine1().getY1(),pov.getLine1().getX2(),pov.getLine1().getY2(), Color.PINK);
        double[] povLine2 = this.computeLine(pov.getLine2().getX1(), pov.getLine2().getY1(),pov.getLine2().getX2(),pov.getLine2().getY2());
        Segment povSegment2 = new Segment(pov.getLine2().getX1(), pov.getLine2().getY1(),pov.getLine2().getX2(),pov.getLine2().getY2(), Color.PINK);
        double[] povPosition = pov.getPosition();
        double semiAngle = this.computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(),pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(),pov.getLine2().getX1(),pov.getLine2().getY1(),pov.getLine2().getX2(),pov.getLine2().getY2());

        double[] projectionLine = this.computeLine(pov.getProjectionLine().getX1(), pov.getProjectionLine().getY1(),pov.getProjectionLine().getX2(),pov.getProjectionLine().getY2());

        double[] bound1 = {pov.getLine1().getX2(), pov.getLine1().getY2()};
        double[] bound2 = {pov.getLine2().getX2(), pov.getLine2().getY2()};

        //TODO mettre en instance
        double minBound = Math.min(bound1[0],bound2[0]);
        double y = (maxHeight/2)+20;
        //double u = (double)( maxWidth/Math.abs(bound1[0]-bound2[0]))/scale;
        double u = maxWidth/Math.abs(bound1[0]-bound2[0]);
        double t = maxWidth/Math.abs(bound1[0]-bound2[0]);
        System.out.println(maxWidth+ " "+Math.abs(bound1[0]-bound2[0]));
        AffineTransform orig = g2.getTransform();
       //
       // g2.setColor(Color.red);
       // g2.draw(new Line2D.Double(pov.getLine1().getX1(), pov.getLine1().getY1(),pov.getLine1().getX2(),pov.getLine1().getY2()));
        //g2.draw(new Line2D.Double(pov.getLine2().getX1(), pov.getLine2().getY1(),pov.getLine2().getX2(),pov.getLine2().getY2()));
        //g2.setColor(Color.cyan);
        //g2.draw(pov.getProjectionLine());
        //AffineTransform orig = g2.getTransform();
       // g2.scale(1,-1);
       // g2.drawString(""+Math.toDegrees(semiAngle),0,0);

        for(Segment seg : segments) {
            double angle1 = computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(), pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(), povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
            double angle2 = computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(), pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(),povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
           // g2.setColor(seg.getColor());
            //g2.drawString(""+Math.toDegrees(angle1),0,debug);
           // g2.drawString(""+Math.toDegrees(angle2),0,debug+10);

            //g2.setTransform(orig);

            if(toAbsDeg(angle1) <= (toAbsDeg(semiAngle)+Heuristic.EPSILON) && toAbsDeg(angle2) <= (toAbsDeg(semiAngle)+Heuristic.EPSILON) ) {
                double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
                double[] povToSegmentExtremity2 = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
                double[] intersection1 = this.computeIntersection(povToSegmentExtremity1,projectionLine);
                double[] intersection2 = this.computeIntersection(povToSegmentExtremity2,projectionLine);

                g2.setColor(seg.getColor());
                g2.draw(new Line2D.Double(intersection1[0],intersection1[1],intersection2[0],intersection2[1]));

                //
                g2.draw(new Line2D.Double(((intersection1[0]-minBound)*u)-(maxWidth/2),y,((intersection2[0]-minBound)*u)-(maxWidth/2),y));
                //
            }

            else if(toAbsDeg(angle1) <= (toAbsDeg(semiAngle)+Heuristic.EPSILON) && toAbsDeg(angle2) > (toAbsDeg(semiAngle)+Heuristic.EPSILON) ) {
                //point dedans seg.getx1, seg.gety1
                //intersecction du segment avec la première ligne du pov
                double[] inter1 = seg.computePosition(povLine1,povSegment1);
                double[] inter2 = seg.computePosition(povLine2,povSegment2);

                //Si deux intersections, on prend le min
                if(!Double.isInfinite(inter1[0]) && !Double.isNaN(inter1[0]) && !Double.isInfinite(inter2[0]) && !Double.isNaN(inter2[0])) {
                    double norm1 = Math.sqrt( Math.pow(inter1[0]-seg.getX1(),2) + Math.pow(inter1[1]-seg.getY1(),2));
                    double norm2 = Math.sqrt( Math.pow(inter2[0]-seg.getX1(),2) + Math.pow(inter2[1]-seg.getY1(),2));

                    if ( Math.min(norm1,norm2) == norm1) {
                        double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
                        double[] intersection1 = this.computeIntersection(povToSegmentExtremity1,projectionLine);
                        g2.setColor(seg.getColor());
                        g2.draw(new Line2D.Double(bound1[0],bound1[1],intersection1[0],intersection1[1]));
                        //
                        g2.draw(new Line2D.Double(((bound1[0]-minBound)*u)-(maxWidth/2), y,((intersection1[0]-minBound)*u)-(maxWidth/2), y));

                    }
                    else{
                        double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
                        double[] intersection1 = this.computeIntersection(povToSegmentExtremity1,projectionLine);
                        g2.setColor(seg.getColor());
                        g2.draw(new Line2D.Double(bound2[0],bound2[1],intersection1[0],intersection1[1]));

                        //
                        g2.draw(new Line2D.Double(((bound2[0]-minBound)*u)-(maxWidth/2), y,((intersection1[0]-minBound)*u)-(maxWidth/2), y));
                    }

                }
                //bound coté povsegment1 + seg x1,x2
                else if(!Double.isInfinite(inter1[0]) && !Double.isNaN(inter1[0])) {
                    double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
                    double[] intersection1 = this.computeIntersection(povToSegmentExtremity1,projectionLine);
                    g2.setColor(seg.getColor());
                    g2.draw(new Line2D.Double(bound1[0],bound1[1],intersection1[0],intersection1[1]));
                    //
                    g2.draw(new Line2D.Double(((bound1[0]-minBound)*u)-(maxWidth/2),y,((intersection1[0]-minBound)*u)-(maxWidth/2),y));
                }
                //bound coté povsegment2 + seg x1,x2
                else if(!Double.isInfinite(inter2[0]) && !Double.isNaN(inter2[0])) {
                    double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
                    double[] intersection1 = this.computeIntersection(povToSegmentExtremity1,projectionLine);
                    g2.setColor(seg.getColor());
                    g2.draw(new Line2D.Double(bound2[0],bound2[1],intersection1[0],intersection1[1]));
                    //
                    g2.draw(new Line2D.Double(((bound2[0]-minBound)*u)-(maxWidth/2),y,((bound2[0]-minBound)*u)-(maxWidth/2),y));
                }
            }


            else if(toAbsDeg(angle1) > (toAbsDeg(semiAngle)+Heuristic.EPSILON) && toAbsDeg(angle2) <= (toAbsDeg(semiAngle)+Heuristic.EPSILON) ) {
                //point dedans seg.getx2, seg.gety2
                //intersecction du segment avec la première ligne du pov
                double[] inter1 = seg.computePosition(povLine1,povSegment1);
                double[] inter2 = seg.computePosition(povLine2,povSegment2);

                //Si deux intersections, on prend le min
                if(!Double.isInfinite(inter1[0]) && !Double.isNaN(inter1[0]) && !Double.isInfinite(inter2[0]) && !Double.isNaN(inter2[0])) {
                    double norm1 = Math.sqrt( Math.pow(inter1[0]-seg.getX2(),2) + Math.pow(inter1[1]-seg.getY2(),2));
                    double norm2 = Math.sqrt( Math.pow(inter2[0]-seg.getX2(),2) + Math.pow(inter2[1]-seg.getY2(),2));

                    if ( Math.min(norm1,norm2) == norm1) {
                        double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
                        double[] intersection1 = this.computeIntersection(povToSegmentExtremity1,projectionLine);
                        g2.setColor(seg.getColor());
                        g2.draw(new Line2D.Double(bound1[0],bound1[1],intersection1[0],intersection1[1]));
                        //
                        g2.draw(new Line2D.Double(((bound1[0]-minBound)*u)-(maxWidth/2),y,((intersection1[0]-minBound)*u)-(maxWidth/2),y));
                    }
                    else {
                        double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
                        double[] intersection1 = this.computeIntersection(povToSegmentExtremity1,projectionLine);
                        g2.setColor(seg.getColor());
                        g2.draw(new Line2D.Double(bound2[0],bound2[1],intersection1[0],intersection1[1]));
                        //
                        g2.draw(new Line2D.Double(((bound2[0]-minBound)*u)-(maxWidth/2),y,((bound2[0]-minBound)*u)-(maxWidth/2),y));
                    }

                }
                //bound coté povsegment1 + seg x1,x2
                else if(!Double.isInfinite(inter1[0]) && !Double.isNaN(inter1[0])) {
                    double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
                    double[] intersection1 = this.computeIntersection(povToSegmentExtremity1,projectionLine);
                    g2.setColor(seg.getColor());
                    g2.draw(new Line2D.Double(bound1[0],bound1[1],intersection1[0],intersection1[1]));
                    //
                    g2.draw(new Line2D.Double(((bound1[0]-minBound)*u)-(maxWidth/2),y,((intersection1[0]-minBound)*u)-(maxWidth/2),y));
                }
                //bound coté povsegment2 + seg x1,x2
                else if(!Double.isInfinite(inter2[0]) && !Double.isNaN(inter2[0])) {
                    double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
                    double[] intersection1 = this.computeIntersection(povToSegmentExtremity1,projectionLine);
                    g2.setColor(seg.getColor());
                    g2.draw(new Line2D.Double(bound2[0],bound2[1],intersection1[0],intersection1[1]));
                    //
                    g2.draw(new Line2D.Double(((bound2[0]-minBound)*u)-(maxWidth/2),y,((intersection1[0]-minBound)*u)-(maxWidth/2),y));
                }
            }

            else if( (toAbsDeg(angle1) < 90 || toAbsDeg(angle2) < 90) && toAbsDeg(angle1) > toAbsDeg(semiAngle) && toAbsDeg(angle2) > toAbsDeg(semiAngle) && ( (toDeg(angle1) >= 0 && toDeg(angle2) < 0) | (toDeg(angle1) < 0 && toDeg(angle2) >= 0)) ) {

                double[] inter1 = seg.computePosition(povLine1,povSegment1);
                double[] inter2 = seg.computePosition(povLine2,povSegment2);

                if(!Double.isInfinite(inter1[0]) && !Double.isNaN(inter1[0]) && !Double.isInfinite(inter2[0]) && !Double.isNaN(inter2[0]) ) {
                    List<Segment> tempList = new LinkedList<>();
                    tempList.add(new Segment(inter1[0],inter1[1],inter2[0],inter2[1],seg.getColor()));
                    this.drawSegments(tempList,g2);
                }

                //g2.setColor(seg.getColor());
                //g2.draw(new Line2D.Double(bound1[0],bound1[1],bound2[0],bound2[1]));
            }
        }

        g2.setTransform(orig);

       // debug+=20;
/*
        for(Segment seg : segments) {
            double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
            double angle1 = computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(), pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(), povPosition[0], povPosition[1],seg.getX1(), seg.getY1());

            double[] povToSegmentExtremity2 = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
            double angle2 = computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(), pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(),povPosition[0], povPosition[1],seg.getX2(), seg.getY2());

            double[] intersection1 = new double[2];
            double[] intersection2 = new double[2];

            if(Math.toDegrees(angle1) < 90 && Math.toDegrees(angle1) > -90 && Math.toDegrees(angle2) < 90 && Math.toDegrees(angle2) > -90){
                intersection1 = this.computeIntersection(povToSegmentExtremity1, projectionLine);
                intersection2 = this.computeIntersection(povToSegmentExtremity2, projectionLine);

                double[] positionRelativeToLine1 = seg.computePosition(povLine1,povSegment1);
                double[] positionRelativeToLine2 = seg.computePosition(povLine2,povSegment2);



                //Segment crosses
                if(!Double.isNaN(positionRelativeToLine1[0]) && !Double.isInfinite(positionRelativeToLine1[0]) && !Double.isNaN(positionRelativeToLine2[0]) && !Double.isInfinite(positionRelativeToLine2[0])) {
                    g2.setColor(seg.getColor());
                    g2.draw(new Line2D.Double(bound1[0],bound1[1],bound2[0],bound2[1]));
                }

                //Segment contained in projection
                else if( toAbsDeg(angle1) <= toAbsDeg(semiAngle) && toAbsDeg(angle2) <= toAbsDeg(semiAngle)) {
                    g2.setColor(seg.getColor());
                    g2.draw(new Line2D.Double(intersection1[0],intersection1[1],intersection2[0],intersection2[1]));
                }


                //povLine1 intersects the segment
                else if(!Double.isNaN(positionRelativeToLine1[0]) & !Double.isInfinite(positionRelativeToLine1[0])) {
                    List<Segment> recursiveList1 = new LinkedList();
                    recursiveList1.add(new Segment(seg.getX1(),seg.getY1(),positionRelativeToLine1[0],positionRelativeToLine1[1],seg.getColor()));
                    recursiveList1.add(new Segment(seg.getX2(),seg.getY2(),positionRelativeToLine1[0],positionRelativeToLine1[1],seg.getColor()));
                    this.drawSegments(recursiveList1, g2);

                }

                //povLine2 intersects the segment
                else if(!Double.isNaN(positionRelativeToLine2[0]) & !Double.isInfinite(positionRelativeToLine2[0])) {

                    List<Segment> recursiveList2 = new LinkedList();
                    recursiveList2.add(new Segment(seg.getX1(),seg.getY1(),positionRelativeToLine2[0],positionRelativeToLine2[1],seg.getColor()));
                    recursiveList2.add(new Segment(seg.getX2(),seg.getY2(),positionRelativeToLine2[0],positionRelativeToLine2[1],seg.getColor()));
                    this.drawSegments(recursiveList2, g2);

                }

            }

        }
*/
    }

    /*
    public void drawSegments(java.util.List<Segment> segments, Graphics2D g2) {
        Line2D povLine1 = pov.getLine1();
        Line2D povLine2 = pov.getLine2();
        double[] povPosition = pov.getPosition();
        double semiAngle = pov.computeAngle(pov.getDirectorVector(), pov.getLine2());


        double[] projectionLine = this.computeLine(pov.getProjectionLine().getX1(), pov.getProjectionLine().getY1(),pov.getProjectionLine().getX2(),pov.getProjectionLine().getY2());

        double[] bound1 = {pov.getLine1().getX2(), pov.getLine1().getY2()};
        double[] bound2 = {pov.getLine2().getX2(), pov.getLine2().getY2()};

        for(Segment seg : segments) {
            double[] povToSegmentExtremity1 = computeLine(povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
            double angle1 = computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(), pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(), povPosition[0], povPosition[1],seg.getX1(), seg.getY1());
            //double angle1 = pov.computeAngle(pov.getDirectorVector(), new Line2D.Double(povPosition[0], povPosition[1],seg.getX1(), seg.getY1()));
            double[] povToSegmentExtremity2 = computeLine(povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
            double angle2 = computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(), pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2(),povPosition[0], povPosition[1],seg.getX2(), seg.getY2());
            //double angle2 = pov.computeAngle(pov.getDirectorVector(), new Line2D.Double(povPosition[0], povPosition[1],seg.getX2(), seg.getY2()));

            double[] intersection1 = new double[2];
            double[] intersection2 = new double[2];
            //System.out.println(toRemove+" "+seg.getColor().toString()+" "+Math.toDegrees(angle1)+" "+Math.toDegrees(angle2)+" "+Math.toDegrees(semiAngle));
            //Segment completly in front of point of view
            //if(-Heuristic.EPSILON<Math.abs(Math.toDegrees(angle1)) && Math.abs(Math.abs(Math.toDegrees(angle1))-90)<Heuristic.EPSILON && -Heuristic.EPSILON<Math.abs(Math.toDegrees(angle2)) && Math.abs(Math.abs(Math.toDegrees(angle2))-90)<Heuristic.EPSILON) {
            if(Math.toDegrees(angle1) < 90 && Math.toDegrees(angle1) > -90 && Math.toDegrees(angle2) < 90 && Math.toDegrees(angle2) > -90){
                intersection1 = this.computeIntersection(povToSegmentExtremity1, projectionLine);
                intersection2 = this.computeIntersection(povToSegmentExtremity2, projectionLine);

                //si angle plus grand que demis angle mêe coté on fai rien

                       // cas sym pour autre coté
                //si deux dans le bon,
                //si un des deux dans le bon, on coupe



                //Segment contained in projection
                if( toAbsDeg(angle1) <= toAbsDeg(semiAngle) && toAbsDeg(angle2) <= toAbsDeg(semiAngle) ) {
                    g2.setColor(seg.getColor());
                    //g2.draw(new Line2D.Double(intersection1[0],toRemove,intersection2[0],toRemove));
                    g2.draw(new Line2D.Double(intersection1[0],intersection1[1],intersection2[0],intersection2[1]));
                    System.out.println("------");
                    System.out.println("cas dedans");
                    System.out.println(seg.getColor());
                    System.out.println(toDeg(angle1)+" "+toDeg(angle2)+" "+toAbsDeg(semiAngle));
                }
                //not in projection
                else if( toAbsDeg(angle1) > toAbsDeg(semiAngle) && toAbsDeg(angle2) > toAbsDeg(semiAngle) && ( (toDeg(angle1) >= 0 && toDeg(angle2) >= 0) | (toDeg(angle1) < 0 && toDeg(angle2) < 0)) ) {

                    System.out.println("------");
                    System.out.println("cas hors");
                    System.out.println(seg.getColor());
                    System.out.println(toDeg(angle1)+" "+toDeg(angle2)+" "+toAbsDeg(semiAngle));

                }
                //segment completely crosses both projectionlines
                else if( toAbsDeg(angle1) > toAbsDeg(semiAngle) && toAbsDeg(angle2) > toAbsDeg(semiAngle) && ( (toDeg(angle1) >= 0 && toDeg(angle2) < 0) | (toDeg(angle1) < 0 && toDeg(angle2) >= 0)) ) {
                    g2.setColor(seg.getColor());
                    //g2.draw(new Line2D.Double(bound1[0],toRemove,bound2[0],toRemove));
                    g2.draw(new Line2D.Double(bound1[0],bound1[1],bound2[0],bound2[1]));

                        System.out.println("------");
                        System.out.println("cas coupe completement");
                        System.out.println(seg.getColor());
                        System.out.println(toDeg(angle1)+" "+toDeg(angle2)+" "+toAbsDeg(semiAngle));

                }
                //on prend une des deux intersections, et l'autre sera le bound
                else if( (toAbsDeg(angle1) > toAbsDeg(semiAngle) && toAbsDeg(angle2) < toAbsDeg(semiAngle)) | (toAbsDeg(angle2) > toAbsDeg(semiAngle) && toAbsDeg(angle1) < toAbsDeg(semiAngle)) ) {

                    System.out.println("------");
                    System.out.println("coupe en deux");
                    System.out.println(seg.getColor());
                    System.out.println(toDeg(angle1)+" "+toDeg(angle2)+" "+toAbsDeg(semiAngle));


                    //double[] intersection = computeIntersection(this.computeLine(povLine1.getX1(),povLine1.getY1(),povLine1.getX2(),povLine1.getY2()), seg.computeLine());
                    double[] position = seg.computePosition(this.computeLine(povLine1.getX1(),povLine1.getY1(),povLine1.getX2(),povLine1.getY2()), new Segment(povLine1.getX1(),povLine1.getY1(),povLine1.getX2(),povLine1.getY2(), Color.black));

                    if(!Double.isNaN(position[0]) && !Double.isInfinite(position[0])) {
                        System.out.println("CAS 1");
                        System.out.println("debug "+this.computeLine(povLine1.getX1(),povLine1.getY1(),povLine1.getX2(),povLine1.getY2())[0]+"x + "+this.computeLine(povLine1.getX1(),povLine1.getY1(),povLine1.getX2(),povLine1.getY2())[1]+"y + "+this.computeLine(povLine1.getX1(),povLine1.getY1(),povLine1.getX2(),povLine1.getY2())[2]);
                        System.out.println(seg.getX1()+" "+seg.getY1()+" "+position[0]+" "+position[1]);
                        System.out.println(seg.getX2()+" "+seg.getY2()+" "+position[0]+" "+position[1]);
                        LinkedList<Segment> segmentList = new LinkedList<Segment>();
                        segmentList.add(new Segment(seg.getX1(),seg.getY1(),position[0],position[1],seg.getColor()));
                        segmentList.add(new Segment(seg.getX2(),seg.getY2(),position[0],position[1],seg.getColor()));
                        this.drawSegments(segmentList,g2);


                    }
                    else {
                        position = seg.computePosition(this.computeLine(povLine2.getX1(),povLine2.getY1(),povLine2.getX2(),povLine2.getY2()), new Segment(povLine2.getX1(),povLine2.getY1(),povLine2.getX2(),povLine2.getY2(), Color.black));
                        System.out.println("CAS 2");
                        System.out.println("debug "+this.computeLine(povLine2.getX1(),povLine2.getY1(),povLine2.getX2(),povLine2.getY2())[0]+"x +"+this.computeLine(povLine2.getX1(),povLine2.getY1(),povLine2.getX2(),povLine2.getY2())[1]+"y + "+this.computeLine(povLine2.getX1(),povLine2.getY1(),povLine2.getX2(),povLine2.getY2())[2]);
                        System.out.println(seg.getX1()+" "+seg.getY1()+" "+position[0]+" "+position[1]);
                        System.out.println(seg.getX2()+" "+seg.getY2()+" "+position[0]+" "+position[1]);
                        LinkedList<Segment> segmentList = new LinkedList<Segment>();
                        segmentList.add(new Segment(seg.getX1(),seg.getY1(),position[0],position[1],seg.getColor()));
                        segmentList.add(new Segment(seg.getX2(),seg.getY2(),position[0],position[1],seg.getColor()));
                        this.drawSegments(segmentList,g2);
                    }

                    //g2.setColor(seg.getColor());
                    //g2.draw(new Line2D.Double(bound1[0],toRemove,intersection2[0],toRemove));
                    //g2.draw(new Line2D.Double(bound1[0],0,intersection2[0],0));


                }

            }
        }
        toRemove+=10;
    }
*/
    private double toDeg(double x) {
        return Math.toDegrees(x);
    }

    private double toAbs(double x) {
        return Math.abs(x);
    }
    private double toAbsDeg(double x) {
        return Math.abs(Math.toDegrees(x));
    }

    public static double computeAngle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
        double[] vector1 = {x2-x1,y2-y1};
        double[] vector2 = {x4-x3,y4-y3};
        double crossProd = (vector1[0]*vector2[0])+(vector1[1]*vector2[1]);
        double v1Norm = Math.sqrt( Math.pow(x1-x2,2) + Math.pow(y1-y2,2));
        double v2Norm = Math.sqrt( Math.pow(x3-x4,2) + Math.pow(y3-y4,2));
        double cos = (double) crossProd/(v1Norm*v2Norm);
        //return  Math.acos(cos);
        double dotProd = (vector1[0]*vector2[1])-(vector1[1]*vector2[0]);
        return  Math.atan2(dotProd,crossProd);
    }

    public double[] computeLine(double x1, double y1, double x2, double y2) {

        double[] line = new double[3];

        //If x2-x1 == 0 the line is vertical, its equation is 1*x + 0*y - x1
        if (Math.abs(x2 - x1) <= Heuristic.EPSILON) {
            line[0] = 1;
            line[1] = 0;
            line[2] = -x1;
            return line;
        } else {
            line[0] = (y2 - y1) / (x2 - x1);
            line[1] = -1;
            line[2] = y1 - (line[0] * x1);
            return line;
        }

    }

    public double[] computeIntersection(double[] line1, double[] line2) {

        double[] intersectionPoint = new double[2];

        if(Math.abs(line1[0]-line2[0]) < Heuristic.EPSILON) {
            return null; //droites parallèles
        }
        else {
            if(Math.abs(line1[0]-1)<= Heuristic.EPSILON && line1[1]==0) {

                intersectionPoint[0] = line1[2]/(-line1[0]);
                intersectionPoint[1] = (intersectionPoint[0] * line2[0]) + line2[2];
            }
            else if (Math.abs(line2[0]-1)<= Heuristic.EPSILON && line2[1]==0){
                intersectionPoint[0] = line2[2]/(-line2[0]);
                intersectionPoint[1] = (intersectionPoint[0] * line1[0]) + line1[2];

            }
            else {
                intersectionPoint[0] = (double) ((line2[2] - line1[2]) / (line1[0] - line2[0]));
                intersectionPoint[1] = (intersectionPoint[0] * line1[0]) + line1[2];
            }

            return intersectionPoint;
        }
    }

}