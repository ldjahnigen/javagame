import java.util.Random;
import java.util.ArrayList;
import java.lang.Math;

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

  public int[][] bresenham(int startx, int starty, int endx, int endy) {
    int dx = Math.abs(endx - startx);
    int dy = Math.abs(endy - starty);
    int sx = startx < endx ? 1 : -1;
    int sy = starty < endy ? 1 : -1;
    int err = dx - dy;
    int x = startx;
    int y = starty;
    int[][] coordinates = new int[Math.max(dx, dy) + 1][2];
    int i = 0;
    while (true) {
        coordinates[i][0] = x;
        coordinates[i][1] = y;
        i++;
        if (x == endx && y == endy) {
            break;
        }
        int e2 = 2 * err;
        if (e2 > -dy) {
            err = err - dy;
            x = x + sx;
        }
        if (e2 < dx) {
            err = err + dx;
            y = y + sy;
        }
    }
    return coordinates;
  }

  public void connectDoors(int startx, int starty, int endx, int endy, char[][] map) {
    Random rand = new Random();
    int choice = rand.nextInt(0, 2);
    int cornerx = 0;
    int cornery = 0;

    if (choice == 0) {
      cornerx = endx;
      cornery = starty;
    } else if (choice == 1) {
      cornerx = startx; 
      cornery = endy;
    }
    int[][] points1 = bresenham(startx, starty, cornerx, cornery);
    int[][] points2 = bresenham(cornerx, cornery, endx, endy);
    int x = 0;
    int y = 0;
    for (int i = 0; i < points1.length; i++) {
      x = points1[i][0];
      y = points1[i][1];

      map[y][x] = '.';
    }
    for (int i = 0; i < points2.length; i++) {
      x = points2[i][0];
      y = points2[i][1];

      map[y][x] = '.';
    }
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
    ArrayList<Dungeon> dungeons = new ArrayList<Dungeon>();

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
      dungeon.center = new int[] {xcoord + (width / 2), ycoord + (height / 2)};

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
      
      // outline with '#'
      for (int ii = 0; ii < dungeon.map[0].length; ii++) {
        dungeon.map[0][ii] = ' ';
        dungeon.map[dungeon.map.length - 1][ii] = ' ';
      }
      for (int ii = 0; ii < dungeon.map.length; ii++) {
        dungeon.map[ii][0] = ' ';
        dungeon.map[ii][dungeon.map[0].length - 1] = ' ';
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
      dungeons.add(dungeon);
    }
  
    // place the halls
    for (int i = 0; i < dungeons.size(); i++) {
      try {
      connectDoors(
          dungeons.get(i).center[0],
          dungeons.get(i).center[1],
          dungeons.get(i + 1).center[0],  
          dungeons.get(i + 1).center[1],
          map
          );
      } catch (IndexOutOfBoundsException e) {
      connectDoors(
          dungeons.get(i).center[0],
          dungeons.get(i).center[1],
          dungeons.get(0).center[0],  
          dungeons.get(0).center[1],
          map
          );
      }
    }

    // place 'H's to indicate walls
    char[] snapshot = new char[8];
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        if (Character.toString(map[i][j]).equals(" ")) {
          snapshot[0] = map[i - 1][j];
          snapshot[1] = map[i + 1][j];
          snapshot[2] = map[i][j - 1];
          snapshot[3] = map[i][j + 1];
          snapshot[4] = map[i - 1][j - 1];
          snapshot[5] = map[i + 1][j + 1];
          snapshot[6] = map[i - 1][j + 1];
          snapshot[7] = map[i + 1][j - 1];
          for (char c : snapshot) {
            if (Character.toString(c).equals(".")) {
              map[i][j] = 'H';
            }
          }
        }
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
