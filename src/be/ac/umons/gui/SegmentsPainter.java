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

    private double angle = Double.POSITIVE_INFINITY; //angle infini de base
    private double[] povPosition = null;

    //TODO manière dégeulasse
    private int clickCounter = 0;

    public SegmentsPainter(BSPNode root, double maxWidth, double maxHeight) {
        this.root = root;
        this.maxHeight = maxHeight;
        this.maxWidth = maxWidth;

        System.out.println(root.getHeight());
        this.panel = this;
        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent e) {
                double x=e.getX()- (panel.getWidth()/2);
                double y=-(e.getY()-(panel.getHeight()/2));

                switch (clickCounter % 3) {
                    case 0 :
                            povPosition = new double[2];
                            povPosition[0] = x;
                            povPosition[1] = y;
                            clickCounter+=1;
                            break;
                    case 1 :
                            line1 = new Line2D.Double(povPosition[0]*min, povPosition[1]*min, x*min ,y*min);
                            lineToDraw1 = new Line2D.Double(povPosition[0], povPosition[1], x, y);
                            panel.revalidate();
                            panel.repaint();
                            clickCounter+=1;
                            break;
                    case 2 :
                            line2 = new Line2D.Double(povPosition[0]*min, povPosition[1]*min, x*min ,y*min);
                            lineToDraw2 = new Line2D.Double(povPosition[0], povPosition[1], x, y);
                            panel.revalidate();
                            panel.repaint();
                            clickCounter+=1;

                            //TODO ici on crée le pov, on dessine le panel
                             break;
                }


                System.out.println(x+","+y);//these co-ords are relative to the component
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
        g2.translate(this.getWidth()/2,this.getHeight()/2);
        g2.scale(min,-min);
        this.paintSegments(g2, this.root);

        if(lineToDraw1 != null) {
            g2.draw(lineToDraw1);
        }
        if(lineToDraw2 != null) {
            g2.draw(lineToDraw2);
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