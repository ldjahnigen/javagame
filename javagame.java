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

    public KeyFrame(String title, int width, int height, JLabel[] labels, Player player_, char[][] map_) {
        panel = new JPanel();
        for (JLabel l : labels) {
          panel.add(l);
        }
        
        map = map_;
        player = player_;

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
        if (keyCode == 38) {
          player.up();
          player.updateMap(map);
          System.out.println(player.toString());
        }
        else if (keyCode == 40) {
          player.down();
          System.out.println(player.toString());
        }
        else if (keyCode == 37) {
          player.left();
          System.out.println(player.toString());
        }
        else if (keyCode == 39) {
          player.right();
          System.out.println(player.toString());
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
    // create panel
    JPanel panel = new JPanel(new GridLayout(0, 1));

    char[][] map = {{'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'}};

    JLabel[] labels = new JLabel[8];
    Player player = new Player(4, 4);
    map = player.updateMap(map);
    for (int i = 0; i < 8; i++) { 
      String text = "";
      for (int j = 0; j < 8; j++) {
        text += map[i][j];  
      }
      labels[i] = new JLabel(text);
      panel.add(labels[i]);
      labels[i].setHorizontalAlignment(SwingConstants.CENTER);
      labels[i].setVerticalAlignment(SwingConstants.CENTER);
    }
    KeyFrame frame = new KeyFrame("Snake", 100, 221, labels, player, map);

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
    y++;
  }

  public void down() {
    y--;
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

  public char[][] updateMap(char[][] map) {
    char[][] newmap = map;
    newmap[y][x] = 'P';

    return newmap;
  }
}
