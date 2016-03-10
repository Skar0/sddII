package be.ac.umons.gui;

import be.ac.umons.bsp.BSPNode;
import be.ac.umons.bsp.Segment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;

/**
 * Created by mr_robot on 04-03-16.
 */
public class SegmentsPainter extends JPanel {

    private JPanel panel;
    private BSPNode root;
    private int scale = 1;
    private double min;
    private Pov pov = new Pov();
    private double maxWidth;
    private double maxHeight;

    //Lines that represent the angle of the point of view
    private Line2D line1 = null;
    private Line2D line2 = null;

    private Line2D lineToDraw1 = null;
    private Line2D lineToDraw2 = null;

    double norm;

    private double angle = Double.POSITIVE_INFINITY; //angle infini de base
    private double[] povScaledPosition = null;
    private double[] povPosition = null;

    //TODO manière dégeulasse
    private int clickCounter = 0;

    public SegmentsPainter(final BSPNode root, final double maxWidth, final double maxHeight) {
        this.root = root;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;

        System.out.println(root.getHeight());
        this.panel = this;
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {

                double x = e.getX();
                double y = e.getY();

                double x_scaled = ( e.getPoint().getX() - ( (double) panel.getWidth()/2.0) )/min;
                double y_scaled = ( -(e.getPoint().getY() - ( (double) panel.getHeight()/2.0)) )/min;

                switch (clickCounter % 3) {
                    case 0 :

                            lineToDraw1 =null;
                            lineToDraw2 = null;
                            pov.setProjectionLine(null);

                            povScaledPosition = new double[2];
                            povScaledPosition[0] = x_scaled;
                            povScaledPosition[1] = y_scaled;

                            povPosition = new double[2];
                            povPosition[0] = x;
                            povPosition[1] = y;
                            clickCounter+=1;

                            panel.revalidate();
                            panel.repaint();

                            break;
                    case 1 :
                            line1 = new Line2D.Double(povScaledPosition[0], povScaledPosition[1], x_scaled ,y_scaled);
                            lineToDraw1 = new Line2D.Double(povScaledPosition[0], povScaledPosition[1], x_scaled ,y_scaled);
                            norm = Math.sqrt( Math.pow(x_scaled-povScaledPosition[0],2) + Math.pow(y_scaled-povScaledPosition[1],2));

                            panel.revalidate();
                            panel.repaint();
                            clickCounter+=1;
                            break;
                    case 2 :

                            //TODO ici on crée le pov, on dessine le panel
                            double vectorNorm = Math.sqrt( Math.pow(x_scaled-povScaledPosition[0],2) + Math.pow(y_scaled-povScaledPosition[1],2));
                            double[] vector = {(x_scaled-povScaledPosition[0])/vectorNorm , (y_scaled-povScaledPosition[1])/vectorNorm};
                            double[] secondPoint = {povScaledPosition[0]+(vector[0]*norm), povScaledPosition[1]+(vector[1]*norm)};

                            line2 = new Line2D.Double(povScaledPosition[0], povScaledPosition[1], secondPoint[0] ,secondPoint[1]);
                            lineToDraw2 = new Line2D.Double(povScaledPosition[0], povScaledPosition[1], secondPoint[0] ,secondPoint[1]);
                            panel.revalidate();
                            panel.repaint();
                            clickCounter+=1;
                            pov = new Pov(line1,line2);
                        //TOREMOVE
                        JFrame testFrame = new JFrame();
                        testFrame.setSize(600,600);
                        testFrame.setContentPane(new PaintersAlgorithm(pov,root));
                        testFrame.setVisible(true);

                        //
                             break;
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

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        double scalex = (double) panel.getHeight()/maxWidth;
        double scaley = (double) panel.getWidth()/maxHeight;
        min = Math.min(scalex,scaley);
        g2.translate((double) panel.getWidth()/2, (double) panel.getHeight()/2);
        g2.scale(min,-min);
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
        if(pov.getProjectionLine() != null) {
            g2.draw(pov.getProjectionLine());
        }
        if(pov.getDirectorVector() != null) {
            g2.draw(pov.getDirectorVector());
        }
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
}