package src.pill;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.GGBackground;
import ch.aplu.jgamegrid.Location;
import src.Game;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Describes a set of pills of any kind, differentiated by their
 * `key` value
 */
public abstract class AbstractPills {
  private final List<Location> locations = new ArrayList<>();
  private final Optional<List<Actor>> actorPieces;
  public AbstractPills() {
    // Ice does not exist in any properties file
    if (!getKey().equals("Ice")) {
      addParsedLocations();
    }

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
    String fileName = "sprites/" + getKey().toLowerCase() + ".png";
    Actor gold = new Actor(fileName);
    actorPieces.get().add(gold);
    gameInstance.addActor(gold, location);
  }

  private void addParsedLocations() {
    String propertyName = getKey().concat(".location");
    String pillsLocationString = Game
            .getInstance()
            .getProperties()
            .getProperty(propertyName);
    if (pillsLocationString != null) {
      String[] singlePillLocationStrings = pillsLocationString.split(";");
      for (String singlePillLocationString: singlePillLocationStrings) {
        String[] locationStrings = singlePillLocationString.split(",");
        locations.add(new Location(Integer.parseInt(locationStrings[0]), Integer.parseInt(locationStrings[1])));
      }
    }
  }

  private boolean hasActorPieces() {
    HashSet<String> acceptedPieces = new HashSet<>() {{
      add("Ice");
      add("Gold");
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
