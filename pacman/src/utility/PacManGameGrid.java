// PacGrid.java
package src.utility;

import ch.aplu.jgamegrid.*;
import src.Game;

import java.awt.*;
import java.util.*;

public class PacManGameGrid
{
  private int nbHorzCells;
  private int nbVertCells;
  private int[][] mazeArray;
  private GridOrigin origin;

  private Optional<Properties> gridProps;

  public enum GridOrigin {
    DEFAULT, XML
  }

  public PacManGameGrid(int nbHorzCells, int nbVertCells)
  {
    this.nbHorzCells = nbHorzCells;
    this.nbVertCells = nbVertCells;
    mazeArray = new int[nbVertCells][nbHorzCells];
    this.origin = GridOrigin.DEFAULT;
    this.gridProps = Optional.empty();
    String maze =
      "xxxxxxxxxxxxxxxxxxxx" + // 0
      "x....x....g...x....x" + // 1
      "xgxx.x.xxxxxx.x.xx.x" + // 2
      "x.x.......i.g....x.x" + // 3
      "x.x.xx.xx  xx.xx.x.x" + // 4
      "x......x    x......x" + // 5
      "x.x.xx.xxxxxx.xx.x.x" + // 6
      "x.x......gi......x.x" + // 7
      "xixx.x.xxxxxx.x.xx.x" + // 8
      "x...gx....g...x....x" + // 9
      "xxxxxxxxxxxxxxxxxxxx";// 10

    // Copy structure into integer array
    toIntArray(nbHorzCells, nbVertCells, maze);
  }

  public PacManGameGrid(GameXMLHandler mazeHandler)
  {
    this.nbHorzCells = mazeHandler.getModel().getWidth();
    this.nbVertCells = mazeHandler.getModel().getHeight();
    mazeArray = new int[nbVertCells][nbHorzCells];
    String maze = mazeHandler
            .getModel()
            .getMapAsString()
            .replace("\n", "");
    this.origin = GridOrigin.XML;
    this.gridProps = Optional.of(new Properties());

    toIntArray(nbHorzCells, nbVertCells, maze);
  }

  private void toIntArray(int nbHorzCells, int nbVertCells, String maze) {
    Map<Character, String> propAppendChars = new HashMap<>() {{
      put('f', "PacMan");
      put('g', "Troll");
      put('h', "TX5");
    }};
    for (int i = 0; i < nbVertCells; i++)
    {
      for (int k = 0; k < nbHorzCells; k++) {
        int value;
        final char chosenChar = maze.charAt(nbHorzCells * i + k);
        switch (origin) {
          case DEFAULT -> value = toInt(chosenChar);
          case XML -> {
            if (propAppendChars.containsKey(chosenChar)) {
              assert gridProps.isPresent();
              gridProps.get().setProperty(
                propAppendChars.get(chosenChar) + ".location",
                k + "," + i
              );
              value = 2;
            } else {
              value = toIntXML(chosenChar);
            }
          }
          default -> throw new RuntimeException("Not implemented");
        }
        mazeArray[i][k] = value;
      }
    }
  }

  public int getCell(Location location)
  {
    return mazeArray[location.y][location.x];
  }
  private int toInt(char c) {
    if (c == 'x')
      return 0;
    if (c == '.')
      return 1;
    if (c == ' ')
      return 2;
    if (c == 'g')
      return 3;
    if (c == 'i')
      return 4;
    return -1;
  }

  private int toIntXML(char c) {
    // For the WallTile
    if (c == 'b')
      return 0;
    // For PillTile
    if (c == 'c')
      return 1;
    // For the PathTile
    if (c == 'a')
      return 2;
    // For the GoldTile
    if (c == 'd')
      return 3;
    // For the IceTile
    if (c == 'e')
      return 4;
    throw new RuntimeException("Not implemented for: " + Character.toString(c));
  }

  public void draw(GGBackground bg)
  {
    bg.clear(Color.gray);
    bg.setPaintColor(Color.white);
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        bg.setPaintColor(Color.white);
        Location location = new Location(x, y);
        int a = Game
          .getInstance()
          .getGrid()
          .getCell(location);
        Game
          .getInstance()
          .getPillFacade()
          .fillCell(a, bg, location);
      }
    }

    Game
      .getInstance()
      .getPillFacade()
      .putLocatedPills(bg);
  }

  public GridOrigin getOrigin() {
    return origin;
  }

  public Optional<Properties> getGridProps() {
    return gridProps;
  }
}
