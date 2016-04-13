package be.ac.umons.gui;

import be.ac.umons.bsp.*;
import be.ac.umons.painter.PaintersAlgorithm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.*;
import java.util.List;

/**
 * Main panel displaying the scene and the painter's algorithm.
 * @author Clément Tamines
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
                            double[] vector1 = {(directorVector[0]*Math.cos(Math.toRadians(usedAngle)/2D)) +(directorVector[1]*Math.sin(Math.toRadians(usedAngle)/2D)),(-directorVector[0]*Math.sin(Math.toRadians(usedAngle)/2D))+(directorVector[1]*Math.cos(Math.toRadians(usedAngle)/2D))};
                            double[] vector2 = {(directorVector[0]*Math.cos((Math.toRadians(-usedAngle))/2D)) +(directorVector[1]*Math.sin((Math.toRadians(-usedAngle))/2D)),(-directorVector[0]*Math.sin((Math.toRadians(-usedAngle))/2D))+(directorVector[1]*Math.cos((Math.toRadians(-usedAngle))/2D))};

                            line1 = new Line2D.Double(povScaledPosition[0],povScaledPosition[1],povScaledPosition[0]+vector1[0],povScaledPosition[1]+vector1[1]);
                            lineToDraw1 = new Line2D.Double(povScaledPosition[0],povScaledPosition[1],povScaledPosition[0]+vector1[0]*1.05,povScaledPosition[1]+vector1[1]*1.05);

                            line2 = new Line2D.Double(povScaledPosition[0],povScaledPosition[1],povScaledPosition[0]+vector2[0],povScaledPosition[1]+vector2[1]);
                            lineToDraw2 = new Line2D.Double(povScaledPosition[0],povScaledPosition[1],povScaledPosition[0]+vector2[0]*1.05,povScaledPosition[1]+vector2[1]*1.05);


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
        }
        if(itemEvent.getStateChange() == ItemEvent.SELECTED && source.getText() == "Choose angle") {
            this.inputMode = 2;
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

            PaintersAlgorithm painter = new PaintersAlgorithm();

            int inverseDrawing = 1;
            //If the we are looking down, segments must be reversed when displaying the zoomed version (angle between the director vector and y-axis > 90°)
            if(toAbsDeg(this.computeAngle(pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(),pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1()+10,pov.getDirectorVector().getX1(),pov.getDirectorVector().getY1(),pov.getDirectorVector().getX2(),pov.getDirectorVector().getY2())) > 90) {
                inverseDrawing = -1;
            }

            //both "bounds" ie ends of the projection segment
            double[] bound1 = {pov.getLine1().getX2(), pov.getLine1().getY2()};
            double[] bound2 = {pov.getLine2().getX2(), pov.getLine2().getY2()};
            double minBound = Math.min(bound1[0],bound2[0]);

            //y coordinate where the zoomed version of the painter's algorithm will be drawn
            double y = (maxHeight/2)+20;
            double u = maxWidth/Math.abs(bound1[0]-bound2[0]);

            List<Segment> toDraw = painter.getSegmentToDraw(root,pov);

            for(Segment seg : toDraw ) {
                g2.setColor(seg.getColor());
                g2.draw(new Line2D.Double(seg.getX1(),seg.getY1(),seg.getX2(),seg.getY2()));
                g2.scale(inverseDrawing,1);
                g2.draw(new Line2D.Double(((seg.getX1()-minBound)*u)-(maxWidth/2),y,((seg.getX2()-minBound)*u)-(maxWidth/2),y));
                g2.scale(inverseDrawing,1);
            }
        }

        //


    }

    private void paintSegments(Graphics2D g, BSPNode root) {
        for(Segment seg : root.getSegmentsInLine()) {
            g.setColor(seg.getColor());
            g.draw(new Line2D.Double(seg.getX1(),seg.getY1(),seg.getX2(),seg.getY2()));
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
}