import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.awt.Color;

public class javagame {
  public static void main(String[] args) {
    MapGenerator m = new MapGenerator("map.txt");
    m.generateMap(128, 64);
    GUI gui = new GUI(128, 64);
  }
}

class GUI {
  private static final long serialVersionUID = 1L;
  public int height;
  public int width;
  public char[][] map;
  public JLabel[][] labels;
  public int pixelwidth;
  public int pixelheight;
  public int ver_interval;
  public int hor_interval;
  public JPanel panel;

  public static char[][] readFile(String filename) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(filename));
    String line = "";
    int numRows = 64;
    int maxCols = 128;
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

  public char[][] updateMap(Player player, char[][] map, int lasty, int lastx, JLabel[][] labels) {
      map[lasty][lastx] = '.';
      map[player.y][player.x] = 'P';
      labels[lasty][lastx].setText(Character.toString(map[lasty][lastx]));
      labels[player.y][player.x].setText("P");
      return map;
  }

  public GUI(int width_, int height_) {
    width = width_;
    height = height_;
    pixelwidth = 1600;
    pixelheight = 900;

    hor_interval = (pixelwidth - 100) / width;
    ver_interval = (pixelheight - 100) / height;

    //initialize frame
    JFrame frame = new JFrame("Game");

    // create panel with GridLayout along with a list to store labels
    panel = new JPanel();
    panel.setLayout(null);
    panel.setBackground(Color.black);
    labels = new JLabel[height][width];
    

    // put the map into a variable
    try {
    map = readFile("map.txt");
    } catch (IOException e) {
    }

    // find a place to put the player and place it on the map
    int xplayer = 0;
    int yplayer = 0; 
    for (int i = 0; i < height; i++) { 
      for (int j = 0; j < width; j++) {
        if (Character.toString(map[i][j]).equals(".")) {
          xplayer = j;
          yplayer = i; 
          break;
        }
      }
        if (Character.toString(map[i][xplayer]).equals(".")) {
          break;
        }
    }

    // create player
    Player player = new Player(xplayer, yplayer);

    // create font
    FontLoader fontloader = new FontLoader();
    Font font = fontloader.loadFont("Early GameBoy.ttf");

    // create and place the labels on the panel
    for (int i = 0; i < height; i++) { 
      for (int j = 0; j < width; j++) {
        String text = "" + map[i][j];
        labels[i][j] = new JLabel(text);
        labels[i][j].setForeground(Color.white);
        labels[i][j].setBackground(Color.gray);
        labels[i][j].setOpaque(true);
        labels[i][j].setBounds(hor_interval * j + 92, ver_interval * i, 10, 10);
        labels[i][j].setFont(font);
        panel.add(labels[i][j]);
      }
    }
      
    JLabel health = new JLabel("Health: ");
    health.setForeground(Color.white);
    health.setFont(font);
    health.setBounds(95, 735, 200, 100);
    panel.add(health);

    // place the player and create a keyframe
    map[player.y][player.x] = 'P';
    labels[player.y][player.x].setText("P");
    KeyFrame keyframe = new KeyFrame(frame, "Game", pixelwidth, pixelheight, panel, player, this);
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
        try {
        map = gui.readFile("map.txt");
        } catch (IOException e) {
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
          if (player.y != gui.height - 1) {
            int y = player.y;
            int x = player.x;
            player.down();
            map = gui.updateMap(player, map, y, x, gui.labels);
          }
        }
        else if (keyCode == 38) {
          if (player.y != 0) {
            int y = player.y;
            int x = player.x;
            player.up();
            map = gui.updateMap(player, map, y, x, gui.labels);
          }
        }
        else if (keyCode == 37) {
          if (player.x != 0) {
            int y = player.y;
            int x = player.x;
            player.left();
            map = gui.updateMap(player, map, y, x, gui.labels);
          }
        }
        else if (keyCode == 39) {
          if (player.x != gui.width - 1) {
            int y = player.y;
            int x = player.x;
            player.right();
            map = gui.updateMap(player, map, y, x, gui.labels);
        }
      }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
      // don't do anything
    }
}
