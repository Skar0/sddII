package be.ac.umons.cui;

import be.ac.umons.bsp.*;
import be.ac.umons.painter.PaintersAlgorithm;
import be.ac.umons.painter.Pov;

import java.awt.geom.Line2D;
import java.io.*;
import java.lang.management.*;

/**
 * This class provides a user interface using the user's terminal. It allows to choose a file containing a scene and then
 * comparing the heuristics applied on that file.
 *
 *
 * @author Clément Tamines
 */
public class Cui {
    
    private enum Heuristics {Inorder, Random, Free_Splits}
    private static Heuristics currentHeuristic;
    private static int iterations;
    private static Line2D line1 = new Line2D.Double(0,0,0,1), line2 = new Line2D.Double(0,0,1,1);
    private static Pov pov = new Pov(line1,line2);
    private static computeResults myThread;



    public static void main(String [] args) {

        boolean canTakeCPUMeasurements = true;

        //Test if the JVM is able to run methods on the threads
        try {
            ThreadMXBean thread = ManagementFactory.getThreadMXBean();
            if ((!thread.isThreadCpuTimeEnabled()) || !thread.isThreadCpuTimeSupported()){
                System.out.println("Can't take CPU measurements because thread CPU time measurement is enabled");
                canTakeCPUMeasurements = false;
            }
        }
        catch (UnsupportedOperationException Uoe){
            System.out.println("JVM does not support CPU time measurement for other threads nor for the current thread");
            canTakeCPUMeasurements = false;
        }

        if (canTakeCPUMeasurements) {
            try {
                Console console = System.console();
                if (console != null) {
                    System.out.println("-BSP Tree comparator-\n");
                    startMenu(console);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * This Thread is used to compute statistics on a specific heuristic on a specific input of segments.
     */
    public static class computeResults extends Thread{
        private Heuristics he;
        private int iterations;
        private String path;
        private Pov pov;
        private double [] result;

        /**
         * Class constructor
         * @param he the heuristic we compute results for
         * @param iterations the number of times we test the heuristic to do an average
         * @param path the path to the file containing the list of segments
         * @param pov the point of view used for the painter algorithm
         */
        private computeResults(Heuristics he, int iterations, String path, Pov pov){
            this.he = he;
            this.iterations = iterations;
            this.path = path;
            this.pov = pov;
            this.result = new double[5];
        }

        public void run(){
            ThreadMXBean simulatorMx = ManagementFactory.getThreadMXBean();
            SegmentLoader loader = new SegmentLoader();
            BSPNode root;
            Heuristic currentHeuristic;
            int i = iterations, j = iterations;
            int size = 0, height = 0, segments = 0;

            switch(this.he){
                case Inorder:
                    currentHeuristic = new InOrderHeuristic();
                    break;
                case Random:
                    currentHeuristic = new RandomHeuristic();
                    break;
                case Free_Splits:
                    currentHeuristic = new FreeSplitsHeuristic();
                    break;
                default:
                    System.out.println("Invalid heuristic given to thread ! \n");
                    return;
            }
            long start = simulatorMx.getCurrentThreadCpuTime();
            while (i > 0){
                root = currentHeuristic.createTree(loader.loadAsList(path));
                size += root.getSize();
                height += root.getHeight();
                segments += root.getSizeInSegments();
                i--;
            }
            double elapsedTime = ((simulatorMx.getCurrentThreadCpuTime()-start)/iterations)*1.0e-6;
            result[0]=(double)size/iterations;
            result[1]=(double)height/iterations;
            result[2]=(double)segments/iterations;
            result[3]=elapsedTime;

            PaintersAlgorithm painter = new PaintersAlgorithm();
            root = currentHeuristic.createTree(loader.loadAsList(path));
            start = simulatorMx.getCurrentThreadCpuTime();
            while (j > 0){
                painter.getSegmentToDraw(root, pov);
                j--;
            }
            elapsedTime = ((simulatorMx.getCurrentThreadCpuTime()-start)/iterations)*1.0e-6;
            result[4]=elapsedTime;
        }

        /**
         *
         * @return return in a array of five doubles the average results of the given heuristic
         * [0] = BSP size ; [1] = BSP height; [2] = BSP number of segments ;
         * [3] = time to create BSP tree (seconds) ; [4] = time to execute painter algorithm (seconds).
         */
        public double [] getResult(){
            return result;
        }
    }

    /**
     * If the string is an abbreviation of a path, return this path.
     * Else, checks if the given string is a correct path to a file, if not, ask to the user
     * to enter a correct path or abbreviation.
     * @param console the console in use
     * @param string the string which needs to be transformed in path or to be checked
     * @return the path associated to the string
     */
    public static String getPath(Console console, String string){
        String answer;
        switch (string){
            case("octangle"):
                answer = "../assets/first/octangle.txt";
                break;
            case("octogone"):
                answer = "../assets/first/octogone.txt";
                break;
            case("ellipsesSmall"):
                answer = "../assets/ellipses/ellipsesSmall.txt";
                break;
            case("ellipsesMedium"):
                answer = "../assets/ellipses/ellipsesMedium.txt";
                break;
            case("ellipsesLarge"):
                answer = "../assets/ellipses/ellipsesLarge.txt";
                break;
            case("randomSmall"):
                answer = "../assets/random/randomSmall.txt";
                break;
            case("randomMedium"):
                answer = "../assets/random/randomMedium.txt";
                break;
            case("randomLarge"):
                answer = "../assets/random/randomLarge.txt";
                break;
            case("randomHuge"):
                answer = "../assets/random/randomHuge.txt";
                break;
            case("rectanglesSmall"):
                answer = "../assets/rectangles/rectanglesSmall.txt";
                break;
            case("rectanglesMedium"):
                answer = "../assets/rectangles/rectanglesMedium.txt";
                break;
            case("rectanglesLarge"):
                answer = "../assets/rectangles/rectanglesLarge.txt";
                break;
            case("rectanglesHuge"):
                answer = "../assets/rectangles/rectanglesHuge.txt";
                break;
            default:
                File file = new File(string);
                if (file.isFile()){
                    answer = string;
                }
                else{
                    String response = console.readLine("Please enter a valid input : ");
                    return getPath(console, response);
                }
                break;
        }
        return answer;
    }

    /**
     * Check if a string can be transformed in a integer, if not, ask to the user
     * to enter a good value.
      * @param console the console in use
     * @param string the string which needs to be transformed in integer
     * @return the value of the integer given in the string
     */
    public static int getIntFromString(Console console, String string){
        if(string.matches("\\d+")){
            return Integer.parseInt(string);
        }
        else{
            String response = console.readLine("Please enter a integer : ");
            return getIntFromString(console, response);
        }
    }

    /**
     * Starts the user interface to test the heuristics
     * @param console the console in use
     */
    public static void startMenu(Console console){
        try {
            System.out.println("You can choose files from the following ones or enter a relative path to a new one :");
            System.out.println("[Current directory : " + System.getProperty("user.dir") + "]");
            System.out.println("");

            System.out.printf(" %-17s || %-17s \n", "octangle", "octogone");
            System.out.printf(" %-17s || %-17s || %-17s \n", "ellipsesSmall", "ellipsesMedium", "ellipsesLarge");
            System.out.printf(" %-17s || %-17s || %-17s || %-17s \n", "randomSmall", "randomMedium", "randomLarge", "randomHuge");
            System.out.printf(" %-17s || %-17s || %-17s || %-17s \n", "rectanglesSmall", "rectanglesMedium", "rectanglesLarge", "rectanglesHuge");

            System.out.println("");
            String response = console.readLine("Please enter what you want : ");
            String path = getPath(console, response);
            System.out.println("You entered the following path : " + path + "\n");
            System.out.println("We will now test the selected file and make averages on results");
            String test_iter = console.readLine("Please choose a number of iterations for testing : ");
            iterations = getIntFromString(console, test_iter);
            System.out.println("Here are the results for the chosen file : \n");

            currentHeuristic = Heuristics.Inorder;
            myThread = new computeResults(currentHeuristic, iterations, path, pov);
            myThread.start();
            myThread.join();
            double[] result1 = myThread.getResult();

            currentHeuristic = Heuristics.Random;
            myThread = new computeResults(currentHeuristic, iterations, path, pov);
            myThread.start();
            myThread.join();
            double[] result2 = myThread.getResult();

            currentHeuristic = Heuristics.Free_Splits;
            myThread = new computeResults(currentHeuristic, iterations, path, pov);
            myThread.start();
            myThread.join();
            double[] result3 = myThread.getResult();

            String header = String.format("  %-22s|  %-9s| %-10s| %-8s| %-8s| %-8s|",
                    "      [Nom]", " [Size]", " [Height]", "[Segments]", "[BSP creation time (ms)]", "[Painter Algorithm (ms)]");
            String inorder = String.format("%-24s|%11.0f|%11.0f|%11.0f|%25.5f|%25.5f|",
                    "In order Heuristic", result1[0], result1[1], result1[2], result1[3], result1[4]);

            String random = String.format("%-24s|%11.0f|%11.0f|%11.0f|%25.5f|%25.5f|",
                    "Random Heuristic", result2[0], result2[1], result2[2], result2[3], result2[4]);

            String free_split = String.format("%-24s|%11.0f|%11.0f|%11.0f|%25.5f|%25.5f|",
                    "Free-Splits Heuristic", result3[0], result3[1], result3[2], result3[3], result3[4]);

            System.out.println(header);
            System.out.println(inorder);
            System.out.println(random);
            System.out.println(free_split);

            System.out.println("");
            response = console.readLine("Do you want to do a new test or quit ?  [test/quit] :  ");
            restartMenu(console, response);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Starts the user interface to test the heuristics if the response equals "test",
     * Ends it if the response equals "response", else ask to the user to enter a valid instruction
     * @param console the console in use
     * @param response the user's response
     */
    public static void restartMenu(Console console, String response){
        if (response.equals("test")){
            System.out.println("");
            startMenu(console);
        }
        else if(response.equals("quit")){
            return;
        }
        else{
            response = console.readLine("Please enter a valid response : ");
            restartMenu(console, response);
        }
    }
}
