import java.util.Random;

public class MapGenerator {
  String file;

  public MapGenerator(String file_) {
    file = file_; 
  } 

  public void printCharray(char[][] c) {
    String text = ""; 
    for (int i = 0; i < c.length; i++) {
      for (int j = 0; j < c[0].length; j++) {
        text += c[i][j];
      }
      text += '\n';
    }
    System.out.println(text);
  }

  public void generateMap(int x, int y) {
    char[][] map = new char[y][x];
    
    // fill everything with ' '
    for (int i = 0; i < y; i++) {
      for (int j = 0; j < x; j++) {
        map[i][j] = ' ';
      }
    }
    
    // outline map with border
    for (int i = 0; i < map[0].length; i++) {
      map[0][i] = 'H';
      map[map.length - 1][i] = 'H';
    }
    for (int i = 0; i < map.length; i++) {
      map[i][0] = 'H';
      map[i][map[0].length - 1] = 'H';
    }


    // begin dungeon placement loop
    int dungeon_count = 20;
    for (int i = 0; i < dungeon_count; i++) {

      // define dimensions
      Random rand = new Random();
      int width = rand.nextInt(4, 25);
      int height = rand.nextInt(4, 25);
      int xcoord = rand.nextInt(x - 1);
      int ycoord = rand.nextInt(y - 1);

      // make sure the dungeon fits in the map
      if ((xcoord + width) > map[0].length) {
        width -= (xcoord + width) - map[0].length; 
      }

      if ((ycoord + height) > map.length) {
        height -= (ycoord + height) - map.length; 
      }
      
      // create char[][] to hold pattern
      char[][] dungeon = new char[height][width];

      // fill the pattern with '.'
      for (int ii = 0; ii < height; ii++) {
        for (int j = 0; j < width; j++) {
          dungeon[ii][j] = '.';
        }
      }
      
      // outline with '#'
      for (int ii = 0; ii < dungeon[0].length; ii++) {
        dungeon[0][ii] = 'H';
        dungeon[dungeon.length - 1][ii] = 'H';
      }
      for (int ii = 0; ii < dungeon.length; ii++) {
        dungeon[ii][0] = 'H';
        dungeon[ii][dungeon[0].length - 1] = 'H';
      }

      // check for overlaps at the corners
      String nw = Character.toString(map[ycoord][xcoord]);
      String ne = Character.toString(map[ycoord + height - 1][xcoord + width - 1]);
      String sw = Character.toString(map[ycoord + height - 1][xcoord]);
      String se = Character.toString(map[ycoord][xcoord + width - 1]);

      if (nw.equals(".") || nw.equals("H")) {
        continue;
      }
      if (ne.equals(".") || ne.equals("H")) {
        continue;
      }
      if (se.equals(".") || se.equals("H")) {
        continue;
      }
      if (sw.equals(".") || sw.equals("H")) {
        continue;
      }
      
      // place finished dungeon on the map  
      int xcoordi = xcoord;
      int ycoordi = ycoord;

      for (int ii = 0; ii < dungeon.length; ii++) {
        for (int j = 0; j < dungeon[0].length; j++) {
          map[ycoordi][xcoordi] = dungeon[ii][j];
          xcoordi++;
        }
        ycoordi++;
        xcoordi = xcoord; 
      }
    }

    String mapstring = "";
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        mapstring += map[i][j];
      }
      mapstring += '\n';
    }
    FileHound f = new FileHound();
    f.fileWrite(mapstring, file);
  }
}
