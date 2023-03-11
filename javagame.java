import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public class javagame {
  public static void main(String[] args) {
    GUI gui = new GUI();
  }
}


class GUI {
  public static char[][] readFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        String line;
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
    }  public char[][] updateMap(Player player) {
    char[][] newmap = {{'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'},
                    {'#', '#', '#', '#', '#', '#', '#', '#'}};
    newmap[player.y][player.x] = 'P';
    return newmap;
  }

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
    map = updateMap(player);
    for (int i = 0; i < 8; i++) { 
      for (int j = 0; j < 8; j++) {
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
