package src.utility;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import src.editor.Constants;
import src.editor.Tile;
import src.editor.TileManager;
import src.grid.Grid;
import src.grid.GridModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GameXMLHandler {
  private final File selectedFile;
  private Grid model;
  public GameXMLHandler(File selectedFile) {
    this.selectedFile = selectedFile;
    handleFile();
  }

  public void handleFile() {
    SAXBuilder builder = new SAXBuilder();
    try {
      if (selectedFile.canRead() && selectedFile.exists()) {
        Document document = null;
        document = (Document) builder.build(selectedFile);

        Element rootNode = document.getRootElement();

        List sizeList = rootNode.getChildren("size");
        Element sizeElem = (Element) sizeList.get(0);
        int height = Integer.parseInt(sizeElem
                .getChildText("height"));
        int width = Integer
                .parseInt(sizeElem.getChildText("width"));
        List<Tile> tiles = TileManager.getTilesFromFolder(Constants.TORUS_FOLDER);
        Grid model = new GridModel(width, height, tiles.get(0).getCharacter());

        List rows = rootNode.getChildren("row");
        for (int y = 0; y < rows.size(); y++) {
          Element cellsElem = (Element) rows.get(y);
          List cells = cellsElem.getChildren("cell");

          for (int x = 0; x < cells.size(); x++) {
            Element cell = (Element) cells.get(x);
            String cellValue = cell.getText();

            char tileNr = 'a';
            if (cellValue.equals("PathTile"))
              tileNr = 'a';
            else if (cellValue.equals("WallTile"))
              tileNr = 'b';
            else if (cellValue.equals("PillTile"))
              tileNr = 'c';
            else if (cellValue.equals("GoldTile"))
              tileNr = 'd';
            else if (cellValue.equals("IceTile"))
              tileNr = 'e';
            else if (cellValue.equals("PacTile"))
              tileNr = 'f';
            else if (cellValue.equals("TrollTile"))
              tileNr = 'g';
            else if (cellValue.equals("TX5Tile"))
              tileNr = 'h';
            else if (cellValue.equals("PortalWhiteTile"))
              tileNr = 'i';
            else if (cellValue.equals("PortalYellowTile"))
              tileNr = 'j';
            else if (cellValue.equals("PortalDarkGoldTile"))
              tileNr = 'k';
            else if (cellValue.equals("PortalDarkGrayTile"))
              tileNr = 'l';
            else
              tileNr = '0';

            model.setTile(x, y, tileNr);
          }
        }

        // String mapString = model.getMapAsString();
        this.model = model;
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  public Grid getModel() {
    return model;
  }
}
