import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.awt.FontFormatException;
import java.io.IOException;

public class FontLoader {
  public FontLoader() {
  }

  public Font loadFont(String location) {
    File fontFile = new File(location);
    try {
      Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
      font = font.deriveFont(Font.BOLD, 14f);
      return font;
    } catch (FontFormatException e) {
      
    } catch (IOException e) {

    }
    return null;
  }
}
