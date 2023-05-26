package src.pill;

import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;
import src.Game;
import src.PacManGameGrid;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PillFacade {
  private final AbstractPills goldPills;
  private final AbstractPills simplePills;
  private final AbstractPills icePills;
  private Integer count;
  private List<Location> pillAndItemLocations;

  public PillFacade () {
    this.goldPills = new GoldPills();
    this.simplePills = new SimplePills();
    this.icePills = new IcePills();
    this.pillAndItemLocations = null;
    this.count = null;
  }

  public List<Location> getPillAndItemLocations() {
    return pillAndItemLocations;
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
  public void setupPillAndItemsLocations() {
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
          icePills.getLocations().add(location);
        }
      }
    }

    if (!noSimplePills) {
      finalLocations.addAll(simplePills.getLocations());
    }
    if (!noGoldPills) {
      finalLocations.addAll(goldPills.getLocations());
    }

    this.pillAndItemLocations = finalLocations;
  }

  /**
   * The function used to fill pill-related cells in the Game instance
   * to be used in the `Game` class
   * @param a
   * @param bg
   * @param location
   */
  public void fillCell(int a, GGBackground bg, Location location) {
    if (a > 0)
      bg.fillCell(location, Color.lightGray);
    if (a == 1 && simplePills.getLocations().size() == 0) { // Pill
      simplePills.putPiece(bg, location);
    } else if (a == 3 && goldPills.getLocations().size() == 0) { // Gold
      goldPills.putPiece(bg, location);
    } else if (a == 4) {
      icePills.putPiece(bg, location);
    }
  }

  public void putLocatedPills(GGBackground bg) {
    AbstractPills[] relevantPills = new AbstractPills[] {
            this.goldPills,
            this.simplePills
    };

    for (AbstractPills relevantPill : relevantPills) {
      for (Location location : relevantPill.getLocations()) {
        relevantPill.putPiece(bg, location);
      }
    }
  }

  public void removeItem(String type,Location location){
    AbstractPills chosenPill;
    switch (type) {
      case "gold" -> chosenPill = this.goldPills;
      case "ice" -> chosenPill = this.icePills;
      default -> throw new RuntimeException("Invalid use case for pillFacade.removeItem");
    }
    chosenPill.removeItem(type, location);
  }
}
