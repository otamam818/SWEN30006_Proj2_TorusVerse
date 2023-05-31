package src.portal;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;
import src.Game;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Describes a set of portals.
 */
public abstract class AbstractPortals {
  private final List<Location> locations = new ArrayList<>();
  private final Optional<List<Actor>> actorPieces;
  public AbstractPortals() {
    if (hasActorPieces()) {
      this.actorPieces = Optional.of(new ArrayList<>());
    } else {
      this.actorPieces = Optional.empty();
    }
  }

  /**
   * Puts a piece into the map, to be used in the PacManGameGrid function
   * @param bg The whole background of the Game
   * @param location the chosen coordinate of the background to fill
   */
  public void putPiece (GGBackground bg, Location location) {
    Game gameInstance = Game.getInstance();
    if (actorPieces.isEmpty()) {
      bg.fillCircle(gameInstance.toPoint(location), 5);
      return;
    }
    bg.setPaintColor(getPaintColor());
    bg.fillCircle(gameInstance.toPoint(location), 5);
    String fileName = "sprites/portal_" + getKey().toLowerCase() + ".png";
    Actor portal = new Actor(fileName);
    actorPieces.get().add(portal);
    gameInstance.addActor(portal, location);
  }

  private void addParsedLocations() {
    String propertyName = getKey().concat(".location");
    String portalsLocationString = Game
            .getInstance()
            .getProperties()
            .getProperty(propertyName);
    if (portalsLocationString != null) {
      String[] singlePortalLocationStrings = portalsLocationString.split(";");
      for (String singlePortalLocationString: singlePortalLocationStrings) {
        String[] locationStrings = singlePortalLocationString.split(",");
        locations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }
  }

  private boolean hasActorPieces() {
    HashSet<String> acceptedPieces = new HashSet<>() {{
      add("White");
      add("Yellow");
      add("DarkGold");
      add("DarkGray");
    }};
    return acceptedPieces.contains(getKey());
  }

  public void removeItem(String type,Location location){
    if (actorPieces.isPresent()) {
      for (Actor item : actorPieces.get()){
        if (location.getX() == item.getLocation().getX() && location.getY() == item.getLocation().getY()) {
          item.hide();
        }
      }
    }
  }

  protected abstract String getKey();
  protected abstract Color getPaintColor();

  public List<Location> getLocations() {
    return locations;
  }
}
