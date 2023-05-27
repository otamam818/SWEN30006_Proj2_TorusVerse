package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import src.utility.PacManGameGrid;

import java.util.Optional;
import java.util.Properties;

public interface MovingActor {
  void setSeed(int seed);

  void setSlowDown(int factor);

  Location getLocation();

  String getKey();

  default Boolean collidesWith(MovingActor other) {
    return this.getLocation().equals(other.getLocation());
  }

  default Location initializeLocation() {
    Properties property;
    if (Game.getInstance().getGrid().getOrigin() == PacManGameGrid.GridOrigin.DEFAULT) {
      property = Game
        .getInstance()
        .getProperties();
    } else {
      Optional<Properties> propOption = Game.getInstance().getGrid().getGridProps();
      assert propOption.isPresent();
      property = propOption.get();
    }

    if (!property.containsKey(getKey() + ".location")) {
      throw new RuntimeException(getKey() + " does not exist in the grid.");
    }
    String[] locations = property
            .getProperty(getKey() + ".location")
            .split(",");
    int x = Integer.parseInt(locations[0]);
    int y = Integer.parseInt(locations[1]);

    return new Location(x, y);
  }

  void handleEndOfGame();

  default void setupActorLocations() {
    Location location = initializeLocation();
    assert this instanceof Actor;
    placeActor(location);
  }

  default void placeActor(Location location) {
    Game
      .getInstance()
      .addActor((Actor) this, location);
  }
}
