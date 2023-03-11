import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class KeyFrame extends JFrame implements KeyListener { 
    public JPanel panel;
    public Player player;
    public char[][] map;
    public JFrame frame;
    public GUI gui;

    public KeyFrame(JFrame frame_, String title, int width, int height, JPanel panel_, Player player_, GUI gui_) {
        panel = panel_;
        player = player_;
        gui = gui_;
        add(panel);
        setTitle(title);
        addKeyListener(this);
        setSize(width, height);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    public void remap(char[][] map) {
      panel.removeAll();
      JLabel[] labels = new JLabel[8];
      for (int i = 0; i < 8; i++) { 
        String text = "";
        for (int j = 0; j < 8; j++) {
          text += map[i][j];  
        }
        labels[i] = new JLabel(text);
        panel.add(labels[i]);
        labels[i].setHorizontalAlignment(SwingConstants.CENTER);
        labels[i].setVerticalAlignment(SwingConstants.CENTER);
        panel.repaint();
        panel.revalidate();
    }

    }

    @Override
    public void keyTyped(KeyEvent e) {
      // don't do anything
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // This method is called when a key is pressed down
        int keyCode = e.getKeyCode();
        if (keyCode == 40) {
          if (player.y != 7) {
            player.down();
            map = gui.updateMap(player);
            remap(map); 
          }
        }
        else if (keyCode == 38) {
          if (player.y != 0) {
            player.up();
            map = gui.updateMap(player);
            remap(map); 
          }
        }
        else if (keyCode == 37) {
          if (player.x != 0) {
            player.left();
            map = gui.updateMap(player);
            remap(map); 
          }
        }
        else if (keyCode == 39) {
          if (player.x != 7) {
            player.right();
            map = gui.updateMap(player);
            remap(map); 
        }
      }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
      // don't do anything
    }
}
