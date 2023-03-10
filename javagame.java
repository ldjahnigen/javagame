import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class javagame {
  public static void main(String[] args) {
    GUI gui = new GUI();
  }
}

class KeyFrame extends JFrame implements KeyListener {
    
    public JPanel panel;
    public Player player;
    public char[][] map;
    public JFrame frame;

    public KeyFrame(JFrame frame_, String title, int width, int height, JPanel panel_, Player player_, char[][] map_) {
        panel = panel_;
        map = map_;
        player = player_;

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
            map = player.updateMap();
            remap(map); 
          }
        }
        else if (keyCode == 38) {
          if (player.y != 0) {
            player.up();
            map = player.updateMap();
            remap(map); 
          }
        }
        else if (keyCode == 37) {
          if (player.x != 0) {
            player.left();
            map = player.updateMap();
            remap(map); 
          }
        }
        else if (keyCode == 39) {
          if (player.x != 7) {
            player.right();
            map = player.updateMap();
            remap(map); 
        }
      }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
      // don't do anything
    }
}

class GUI {
  public GUI() {
    //initialize frame
    JFrame frame = new JFrame("Snake");

    // create panel with GridLayout
    JPanel panel = new JPanel(new GridLayout(8, 8));

    char[][] map = {{'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'}};

    JLabel[][] labels = new JLabel[8][8];
    Player player = new Player(4, 4);
    map = player.updateMap();
    for (int i = 0; i < 8; i++) { 
      for (int j = 0; j < 8; j++) {
        String text = "" + map[i][j];
        labels[i][j] = new JLabel(text);
        panel.add(labels[i][j]);
        labels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
        labels[i][j].setVerticalAlignment(SwingConstants.CENTER);
      }
    }

    KeyFrame keyframe = new KeyFrame(frame, "Snake", 120, 100, panel, player, map);

  }
}


class Player {
  int x;
  int y;
    
  public Player(int x_, int y_) {
    x = x_;
    y = y_;
  }
  
  public void up() {
    y--;
  }

  public void down() {
    y++;
  }

  public void left() {
    x--;
  }

  public void right() {
    x++;
  }

  @Override
  public String toString() {
    return Integer.toString(x) + Integer.toString(y); 
  }

  public char[][] updateMap() {
    char[][] newmap = {{'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'}};
    newmap[y][x] = 'P';
    return newmap;
  }
}
