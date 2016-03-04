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

    private BSPNode root;
    private int scale = 1;
    private double min;

    public SegmentsPainter(BSPNode root) {
        this.root = root;
        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                double x=min*(e.getX() - getWidth()/2);
                double y=min*(e.getY() - getHeight()/2);
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
        Graphics2D g2 = (Graphics2D) g;
        double scalex = (double) this.getHeight()/(200*2);
        double scaley = (double) this.getWidth()/(200*2);
        min = Math.min(scalex,scaley);
        g2.translate(this.getWidth()/2,this.getHeight()/2);
        g2.scale(min,min);
        this.paintSegments(g2, this.root);
    }

    private void paintSegments(Graphics2D g, BSPNode root) {
        for(Segment seg : root.getSegmentsInLine()) {
            g.setColor(seg.getColor());
            g.draw(new Line2D.Double(seg.getX1()*scale,seg.getY1()*-scale,seg.getX2()*scale,seg.getY2()*-scale));
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