import java.util.Random;
import java.util.ArrayList;

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

  public char[][] jahnigen(int x1, int y1, int x2, int y2, char[][] map) {
    float m = (y2 - y1) / (x2 - x1);
    System.out.println(m);
    return map;
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
    int placed_dungeons = 0;
    for (int i = 0; i < dungeon_count; i++) {

      // define dimensions
      Random rand = new Random();
      int width = rand.nextInt(5, 25);
      int height = rand.nextInt(5, 25);
      int xcoord = rand.nextInt(x - 1);
      int ycoord = rand.nextInt(y - 1);

      // make sure the dungeon fits in the map
      if ((xcoord + width) > map[0].length) {
        width -= (xcoord + width) - map[0].length; 
      }

      if ((ycoord + height) > map.length) {
        height -= (ycoord + height) - map.length; 
      }
      
      // make sure it still fits in the bounds of dungeon size
      if (height < 4) {
        continue;
      }
      if (width < 4) {
        continue;
      }

      // create dungeon object to hold pattern
      Dungeon dungeon = new Dungeon(new char[height][width]); 

      String nw = Character.toString(map[ycoord][xcoord]);
      String ne = Character.toString(map[ycoord + height - 1][xcoord + width - 1]);
      String sw = Character.toString(map[ycoord + height - 1][xcoord]);
      String se = Character.toString(map[ycoord][xcoord + width - 1]);

      // fill the pattern with '.'
      for (int ii = 0; ii < height; ii++) {
        for (int j = 0; j < width; j++) {
          dungeon.map[ii][j] = '.';
        }
      }
      
      // outline with '#' and place doors
      for (int ii = 0; ii < dungeon.map[0].length; ii++) {
        dungeon.map[0][ii] = 'H';
        dungeon.map[dungeon.map.length - 1][ii] = 'H';
      }
      for (int ii = 0; ii < dungeon.map.length; ii++) {
        dungeon.map[ii][0] = 'H';
        dungeon.map[ii][dungeon.map[0].length - 1] = 'H';
      }
     
      int side = 1;
      int door_spot = 0;
      switch (side) {
        case (0):
          door_spot = rand.nextInt(1, dungeon.map[0].length - 2); 
          dungeon.map[0][door_spot] = '>';
          break;

        case (1):
          door_spot = rand.nextInt(1, dungeon.map.length - 2); 
          dungeon.map[door_spot][0] = '>';
          break;

        case (2):
          door_spot = rand.nextInt(1, dungeon.map[0].length - 2); 
          dungeon.map[0][door_spot] = '>';
          break;

        case (3):
          door_spot = rand.nextInt(1, dungeon.map[0].length - 2); 
          dungeon.map[0][door_spot] = '>';
          break;
      }


      // check for overlaps at the corners
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

      for (int ii = 0; ii < dungeon.map.length; ii++) {
        for (int j = 0; j < dungeon.map[0].length; j++) {
          map[ycoordi][xcoordi] = dungeon.map[ii][j];
          xcoordi++;
        }
        ycoordi++;
        xcoordi = xcoord; 
      }
      placed_dungeons++;
    }
   
    // begin hall placement loop
    for (int i = 0; i < placed_dungeons; i++) {
      // select which tiles to use as doors
      Random rand = new Random();
      int side = rand.nextInt(0, 3);
      switch (side) {
        case (0):
           
          break;

        case (1):

          break;

        case (2):

          break;

        case (3):

          break;
      }
    }

    // write the finished map to the file
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
