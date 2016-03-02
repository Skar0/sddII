package be.ac.umons.gui;

/**
 * Created by clement on 2/27/16.
 */
public class Gui {

    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        MainPanel menu = new MainPanel();

        frame.setContentPane(menu);
    }
}
