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
import java.util.ArrayList;

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
  public Tile[][] labels;
  public int pixelwidth;
  public int pixelheight;
  public int ver_interval;
  public int hor_interval;
  public JPanel panel;
  public Color light = Color.white;
  public Color dark = Color.black;
  public Color seen = Color.gray;

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

  public char[][] updateMap(Player player, char[][] map, int lasty, int lastx, Tile[][] labels) {
    map[lasty][lastx] = '.';
    map[player.y][player.x] = 'P';
    labels[lasty][lastx].setText(Character.toString(map[lasty][lastx]));
    labels[lasty][lastx].setForeground(dark);
    labels[player.y][player.x].setText("P");
    labels[player.y][player.x].setForeground(Color.magenta);
    losTrace(player, labels);
    return map;
  }

  public void losTrace(Player player, Tile[][] labels) {
    // if it has been seen, make it gray
    for (int i = 0; i < labels.length; i++) {
      for (int j = 0; j < labels[0].length; j++) {
        if (labels[i][j].wasSeen) {
          labels[i][j].setForeground(seen);
        }
      }
    }
    // if a label is in a 12x12 grid, make it white or purple if it is in the center
    for (int i = player.y - 6; i < player.y + 6; i++) {
      for (int j = player.x - 6; j < player.x + 6; j++) {
        if (i == player.y && j == player.x) {
          labels[i][j].setForeground(Color.magenta);
          labels[i][j].wasSeen = true;
        } else {
          labels[i][j].setForeground(light);
          labels[i][j].wasSeen = true;
        }
      }
    }
  }

  public GUI(int width_, int height_) {
    // define dimensions
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
    labels = new Tile[height][width];

    // put the map into a char[][]
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
        labels[i][j] = new Tile(Character.toString(map[i][j]));
        labels[i][j].setForeground(dark);
        labels[i][j].setBackground(dark);
        labels[i][j].setOpaque(true);
        labels[i][j].setBounds(hor_interval * j + 92, ver_interval * i, 10, 10);
        panel.add(labels[i][j]);
      }
    }

    // add info labels  
    JLabel health = new JLabel("Health: ");
    health.setForeground(Color.white);
    health.setFont(font);
    health.setBounds(95, 735, 200, 100);
    panel.add(health);
    JLabel health_val = new JLabel(Integer.toString(player.health));
    health_val.setForeground(Color.white);
    health_val.setFont(font);
    health_val.setBounds(195, 735, 200, 100);
    panel.add(health_val);

    // place the player and create a keyframe
    map[player.y][player.x] = 'P';
    labels[player.y][player.x].setText("P");
    labels[player.y][player.x].setForeground(Color.magenta);
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
            if (Character.toString(map[player.y][player.x]).equals(".")) {
              map = gui.updateMap(player, map, y, x, gui.labels);
            } else {
              player.up();
            }
          }
        }
        else if (keyCode == 38) {
          if (player.y != 0) {
            int y = player.y;
            int x = player.x;
            player.up();
            if (Character.toString(map[player.y][player.x]).equals(".")) {
              map = gui.updateMap(player, map, y, x, gui.labels);
            } else {
              player.down();
            }
          }
        }
        else if (keyCode == 37) {
          if (player.x != 0) {
            int y = player.y;
            int x = player.x;
            player.left();
            if (Character.toString(map[player.y][player.x]).equals(".")) {
              map = gui.updateMap(player, map, y, x, gui.labels);
            } else {
              player.right();
            }
          }
        }
        else if (keyCode == 39) {
          if (player.x != gui.width - 1) {
            int y = player.y;
            int x = player.x;
            player.right();
            if (Character.toString(map[player.y][player.x]).equals(".")) {
              map = gui.updateMap(player, map, y, x, gui.labels);
            } else {
              player.left();
            }
        }
      }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
      // don't do anything
    }
}

class Tile extends JLabel {
  boolean wasSeen = false;
  public Tile(String text) {
    this.setText(text);
  }
}
