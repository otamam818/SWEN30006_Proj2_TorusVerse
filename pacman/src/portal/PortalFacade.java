package src.portal;

import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;
import src.Game;
import src.portal.DarkGoldPortals;
import src.portal.DarkGrayPortals;
import src.portal.WhitePortals;
import src.portal.YellowPortals;
import src.utility.PacManGameGrid;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PortalFacade {
  private final AbstractPortals darkGoldPortals;
  private final AbstractPortals darkGrayPortals;
  private final AbstractPortals whitePortals;
  private final AbstractPortals yellowPortals;

  private Integer count;
  private List<Location> portalLocations;

  public PortalFacade() {
    this.darkGoldPortals = new DarkGoldPortals();
    this.darkGrayPortals = new DarkGrayPortals();
    this.whitePortals = new WhitePortals();
    this.yellowPortals = new YellowPortals();
    this.portalLocations = null;
    this.count = null;
  }

  public List<Location> getPortalLocations() {
    return portalLocations;
  }

  public List<Location>[] getPortals() {
    List<Location>[] portals = new List[4];
    portals[0] = whitePortals.getLocations();
    portals[1] = yellowPortals.getLocations();
    portals[2] = darkGoldPortals.getLocations();
    portals[3] = darkGrayPortals.getLocations();

    return portals;
  }

  public int getCount() {
    if (this.count == null) {
      this.count = setupCount();
    }
    return this.count;
  }

  private int setupCount() {
    int nbVertCells = Game.getInstance().getNbVertCells();
    int nbHorzCells = Game.getInstance().getNbHorzCells();
    boolean noDarkGoldPortals = darkGoldPortals.getLocations().size() == 0;
    boolean noDarkGrayPortals = darkGrayPortals.getLocations().size() == 0;
    boolean noWhitePortals = whitePortals.getLocations().size() == 0;
    boolean noYellowPortals = yellowPortals.getLocations().size() == 0;
    PacManGameGrid grid = Game.getInstance().getGrid();
    int portalsCount = 0;
    for (int y = 0; y < nbVertCells; y++) {
      for (int x = 0; x < nbHorzCells; x++) {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 5 && noWhitePortals || a == 6 && noYellowPortals
        || a == 7 && noDarkGoldPortals || a == 8 && noDarkGrayPortals) { // Pill
          portalsCount++;
        }
      }
    }

    if (!noWhitePortals) {
      portalsCount += whitePortals.getLocations().size();
    }
    if (!noYellowPortals) {
      portalsCount += yellowPortals.getLocations().size();
    }
    if (!noDarkGoldPortals) {
      portalsCount += darkGoldPortals.getLocations().size();
    }
    if (!noDarkGrayPortals) {
      portalsCount += darkGrayPortals.getLocations().size();
    }

    return portalsCount;
  }
  public void setupPortalLocations() {
    List<Location> finalLocations = new ArrayList<Location>();
    int nbVertCells = Game.getInstance().getNbVertCells();
    int nbHorzCells = Game.getInstance().getNbHorzCells();
    PacManGameGrid grid = Game.getInstance().getGrid();
    boolean noDarkGoldPortals = darkGoldPortals.getLocations().size() == 0;
    boolean noDarkGrayPortals = darkGrayPortals.getLocations().size() == 0;
    boolean noWhitePortals = whitePortals.getLocations().size() == 0;
    boolean noYellowPortals = yellowPortals.getLocations().size() == 0;
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 7) {
          darkGoldPortals.getLocations().add(location);
          finalLocations.add(location);
        }
        if (a == 8) {
          darkGrayPortals.getLocations().add(location);
          finalLocations.add(location);
        }
        if (a == 5) {
          whitePortals.getLocations().add(location);
          finalLocations.add(location);
        }
        if (a == 6) {
          yellowPortals.getLocations().add(location);
          finalLocations.add(location);
        }
      }
    }
    if (!noWhitePortals) {
      finalLocations.addAll(whitePortals.getLocations());
    }
    if (!noYellowPortals) {
      finalLocations.addAll(yellowPortals.getLocations());
    }
    if (!noDarkGoldPortals) {
      finalLocations.addAll(darkGoldPortals.getLocations());
    }
    if (!noDarkGrayPortals) {
      finalLocations.addAll(darkGrayPortals.getLocations());
    }

    this.portalLocations = finalLocations;
  }

  /**
   * The function used to fill portal-related cells in the Game instance
   * to be used in the `Game` class
   * @param a
   * @param bg
   * @param location
   */
  public void fillCell(int a, GGBackground bg, Location location) {
    boolean noDarkGoldPortals = darkGoldPortals.getLocations().size() == 0;
    boolean noDarkGrayPortals = darkGrayPortals.getLocations().size() == 0;
    boolean noWhitePortals = whitePortals.getLocations().size() == 0;
    boolean noYellowPortals = yellowPortals.getLocations().size() == 0;
    if (a == 7 && noDarkGoldPortals) {
      darkGoldPortals.putPiece(bg, location);
    }
    if (a == 8 && noDarkGrayPortals) {
      darkGrayPortals.putPiece(bg, location);
    }
    if (a == 5 && noWhitePortals) {
      whitePortals.putPiece(bg, location);
    }
    if (a == 6 && noYellowPortals) {
      yellowPortals.putPiece(bg, location);
    }
  }

  public void putLocatedPortals(GGBackground bg) {
    AbstractPortals[] relevantPortals = new AbstractPortals[] {
      this.darkGoldPortals,
      this.darkGrayPortals,
      this.whitePortals,
      this.yellowPortals
    };

    for (AbstractPortals relevantPortal : relevantPortals) {
      for (Location location : relevantPortal.getLocations()) {
        relevantPortal.putPiece(bg, location);
      }
    }
  }

  public void removeItem(String type,Location location){
    AbstractPortals chosenPortal;
    switch (type) {
      case "dark-gold" -> chosenPortal = this.darkGoldPortals;
      case "dark-gray" -> chosenPortal = this.darkGrayPortals;
      case "white" -> chosenPortal = this.whitePortals;
      case "yellow" -> chosenPortal = this.yellowPortals;
      default -> throw new RuntimeException("Invalid use case for portalFacade.removeItem");
    }
    chosenPortal.removeItem(type, location);
  }
}
