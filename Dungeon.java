import java.util.ArrayList;

public class Dungeon {
  char[][] map;
  int[] center = {0, 0};
  
  public Dungeon(char[][] map_) {
    map = map_;
  } 
  @Override
  public String toString() {
    String text = "";
    for (int i = 0; i < map.length; i++) {
      for (int j = 0; j < map[0].length; j++) {
        text += map[i][j];
      }
      text += '\n';
    }
    return text;
  }
}
