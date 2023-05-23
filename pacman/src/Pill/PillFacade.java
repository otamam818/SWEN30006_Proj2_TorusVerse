package src.Pill;

import ch.aplu.jgamegrid.Location;
import src.Game;
import src.PacManGameGrid;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PillFacade {
  private final AbstractPills goldPills;
  private final AbstractPills simplePills;
  private Integer count;
  private List<Location> pillAndItemLocations;

  public PillFacade () {
    this.goldPills = new GoldPills();
    this.simplePills = new SimplePills();
    this.count = null;
    this.pillAndItemLocations = setupPillAndItemsLocations();
  }

  public int getCount() {
    if (this.count == null) {
      this.count = setupCount();
    }
    return this.count;
  }

  private int setupCount() {
    // NOTE: Consider mixing this implementation with setupPillAndItemsLocations
    int nbVertCells = Game.getInstance().getNbVertCells();
    int nbHorzCells = Game.getInstance().getNbHorzCells();
    boolean noSimplePills = simplePills.getLocations().size() == 0;
    boolean noGoldPills = goldPills.getLocations().size() == 0;
    PacManGameGrid grid = Game.getInstance().getGrid();
    int pillsAndItemsCount = 0;
    for (int y = 0; y < nbVertCells; y++) {
      for (int x = 0; x < nbHorzCells; x++) {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 1 && noSimplePills) { // Pill
          pillsAndItemsCount++;
        } else if (a == 3 && noGoldPills) { // Gold
          pillsAndItemsCount++;
        }
      }
    }

    if (!noSimplePills) {
      pillsAndItemsCount += simplePills.getLocations().size();
    }
    if (!noGoldPills) {
      pillsAndItemsCount += goldPills.getLocations().size();
    }

    return pillsAndItemsCount;
  }
  private List<Location> setupPillAndItemsLocations() {
    // NOTE: Consider mixing this implementation with setupCount
    List<Location> finalLocations = new ArrayList<Location>();
    int nbVertCells = Game.getInstance().getNbVertCells();
    int nbHorzCells = Game.getInstance().getNbHorzCells();
    PacManGameGrid grid = Game.getInstance().getGrid();
    boolean noSimplePills = simplePills.getLocations().size() == 0;
    boolean noGoldPills = goldPills.getLocations().size() == 0;
    for (int y = 0; y < nbVertCells; y++)
    {
      for (int x = 0; x < nbHorzCells; x++)
      {
        Location location = new Location(x, y);
        int a = grid.getCell(location);
        if (a == 1 && noSimplePills) {
          finalLocations.add(location);
        }
        if (a == 3 &&  noGoldPills) {
          finalLocations.add(location);
        }
        if (a == 4) {
          finalLocations.add(location);
        }
      }
    }

    if (!noSimplePills) {
      finalLocations.addAll(simplePills.getLocations());
    }
    if (!noGoldPills) {
      finalLocations.addAll(goldPills.getLocations());
    }

    return finalLocations;
  }
}
