package src;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;

public interface MovingActor {
  void setSeed(int seed);

  void setSlowDown(int factor);

  Location getLocation();

  String getKey();

  default Boolean collidesWith(MovingActor other) {
    return this.getLocation().equals(other.getLocation());
  }

  default Location initializeLocation() {
    String[] locations = Game
            .getInstance()
            .getProperties()
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
