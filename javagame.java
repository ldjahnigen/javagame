import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class javagame {
  public static void main(String[] args) {
    GUI gui = new GUI(128, 64);
  }
}

class GUI {
  public int height;
  public int width;
  public char[][] map;

  public static char[][] readFile(String filename) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(filename));
    String line = "";
    int numRows = 0;
    int maxCols = 0;
    while ((line = reader.readLine()) != null) {
        numRows++;
        maxCols = Math.max(maxCols, line.length());
    }
    reader.close();

    char[][] charArray = new char[numRows][maxCols];
    reader = new BufferedReader(new FileReader(filename));
    int row = 0;
    while ((line = reader.readLine()) != null) {
        char[] chars = line.toCharArray();
        System.arraycopy(chars, 0, charArray[row], 0, chars.length);
        row++;
    }
    reader.close();

    return charArray;
  }  

  public char[][] updateMap(Player player) {
    try {
      char[][] newmap = readFile("map.txt"); 
      newmap[player.y][player.x] = 'P';
      return newmap;
    } catch (IOException e) {
      return null;
    }
  }

  public void remap(KeyFrame frame, char[][] map) {
    frame.panel.removeAll();
    JLabel[] labels = new JLabel[width];
    for (int i = 0; i < height; i++) { 
      String text = "";
      for (int j = 0; j < width; j++) {
        text += map[i][j];  
      }
      labels[i] = new JLabel(text);
      frame.panel.add(labels[i]);
      labels[i].setHorizontalAlignment(SwingConstants.CENTER);
      labels[i].setVerticalAlignment(SwingConstants.CENTER);
      frame.panel.repaint();
      frame.panel.revalidate();
    }
  }

  public GUI(int width_, int height_) {
    width = width_;
    height = height_;

    //initialize frame
    JFrame frame = new JFrame("Snake");

    // create panel with GridLayout
    JPanel panel = new JPanel(new GridLayout(width, height));

    JLabel[][] labels = new JLabel[height][width];
    Player player = new Player(0, 0);
    map = updateMap(player);
    for (int i = 0; i < height; i++) { 
      for (int j = 0; j < width; j++) {
        String text = "" + map[i][j];
        labels[i][j] = new JLabel(text);
        panel.add(labels[i][j]);
        labels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
        labels[i][j].setVerticalAlignment(SwingConstants.CENTER);
      }
    }

    KeyFrame keyframe = new KeyFrame(frame, "Snake", 120, 100, panel, player, this);

  }
}

class KeyFrame extends JFrame implements KeyListener { 
  private static final long serialVersionUID = 1L;
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
    
    @Override
    public void keyTyped(KeyEvent e) {
      // don't do anything
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // This method is called when a key is pressed down
        int keyCode = e.getKeyCode();
        if (keyCode == 40) {
          if (player.y != gui.height - 1) {
            player.down();
            map = gui.updateMap(player);
            gui.remap(this, map); 
          }
        }
        else if (keyCode == 38) {
          if (player.y != 0) {
            player.up();
            map = gui.updateMap(player);
            gui.remap(this, map); 
          }
        }
        else if (keyCode == 37) {
          if (player.x != 0) {
            player.left();
            map = gui.updateMap(player);
            gui.remap(this, map); 
          }
        }
        else if (keyCode == 39) {
          if (player.x != gui.width - 1) {
            player.right();
            map = gui.updateMap(player);
            gui.remap(this, map); 
        }
      }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
      // don't do anything
    }
}
