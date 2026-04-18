package PacmanGame;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame frame=new JFrame("Pac-Man Game");
        GamePanel panel=new GamePanel();

        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        panel.requestFocusInWindow();
    }
}