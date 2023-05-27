package src.monster;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import src.Game;
import src.MovingActor;

public interface ActorAdapter {
  void setSeed(int seed);

  void setSlowDown(int factor);

  void handleEndOfGame();

  void setupActorLocations();

  default void placeActor(Location location) {
    Game
      .getInstance()
      .addActor((Actor) this, location);
  }
}
