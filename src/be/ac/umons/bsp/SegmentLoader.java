package be.ac.umons.bsp;

import java.awt.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is used to extract a list of segments from a file.
 * @author Clément Tamines
 */

public class SegmentLoader {

    /**
     * String is the path to the desired file
     */
    private String path;

    /**
     * Maximum value used in the abscissa space.
     */
    private double maxWidth;

    /**
     * Maximum value used in the y-intercept space.
     */
    private double maxHeight;

    /**
     * Constructor
     * @param path the path to the desired file.
     */
    public SegmentLoader(String path) {
        this.path = path;
    }

    /**
     * Reads the file and adds all the segments in it to a list, extracts the value of maxWidth and maxHeight.
     * @return a list containing all the segments in the file in order.
     */
    public List<Segment> loadAsList() {

        LinkedList<Segment> segmentList = new LinkedList<Segment>();

        try {
            FileInputStream inputFile = new FileInputStream(path);
            DataInputStream in = new DataInputStream(inputFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line;

            line = br.readLine(); //First line contains information about the file
            String[] infoLine = line.split(" "); //Separator is the space

            //We multiply the value by two because the file contains the max value in the half space
            maxWidth = (double) 2*Double.parseDouble(infoLine[1]);
            maxHeight = (double) 2*Double.parseDouble(infoLine[2]);


            while((line = br.readLine()) != null) {

                String[] temp = line.split(" ");

                Color color = null;

                switch (temp[4]) {
                    case "Bleu":
                        color= Color.blue;
                        break;
                    case "Rouge":
                        color = Color.red;
                        break;
                    case "Orange":
                        color = Color.orange;
                        break;
                    case "Jaune":
                        color = Color.yellow;
                        break;
                    case "Noir":
                        color = Color.black;
                        break;
                    case "Violet":
                        color = new Color(255, 0, 255);
                        break;
                    case "Marron":
                        color = new Color(102, 51, 0);
                        break;
                    case "Vert":
                        color  = Color.green;
                        break;
                    case "Gris":
                        color = new Color(128, 128, 128);
                        break;
                    case "Rose":
                        color = Color.pink;
                        break;
                }

                segmentList.add(new Segment(Double.parseDouble(temp[0]), Double.parseDouble(temp[1]), Double.parseDouble(temp[2]), Double.parseDouble(temp[3]), color));
            }

            in.close();
        }
        catch (Exception e) {
            System.err.println("Error : "+e.getMessage()+"\n"+"Cause : "+e.getCause());
        }

        return segmentList;
    }

    /**
     *
     * @return the maximum width of the scene
     */
    public double getMaxWidth() {
        return maxWidth;
    }

    /**
     *
     * @return the maximum height of the scene
     */
    public double getMaxHeight() {
        return maxHeight;
    }
}
