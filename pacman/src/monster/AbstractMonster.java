package src.monster;

import ch.aplu.jgamegrid.Actor;
import ch.aplu.jgamegrid.Location;
import src.Game;
import src.MovingActor;
import src.PacActor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public abstract class AbstractMonster implements MovingActor {
  protected final Actor actor;
  private final ArrayList<Location> visitedList = new ArrayList<Location>();
  private final int listLength = 10;
  private boolean stopMoving = false;
  private int seed = 0;

  private final MonsterType type;
  private final Random randomiser = new Random(0);
  public AbstractMonster() {
    this.type = setupMonsterType();
    this.actor = new Actor("sprites/" + type.getImageName()) {
      @Override
      public void act() {
        doAct();
      }
    };
  }

  public void stopMoving(int seconds) {
    this.stopMoving = true;
    // Instantiate Timer Object
    Timer timer = new Timer();
    int SECOND_TO_MILLISECONDS = 1000;
    final AbstractMonster monster = this;
    timer.schedule(new TimerTask() {
      @Override
      public void run() {
        monster.stopMoving = false;
      }
    }, (long) seconds * SECOND_TO_MILLISECONDS);
  }

  public void setSeed(int seed) {
    this.seed = seed;
    randomiser.setSeed(seed);
  }

  public void setStopMoving(boolean stopMoving) {
    this.stopMoving = stopMoving;
  }


  public void doAct() {
    if (stopMoving) {
      return;
    }
    walkApproach();
    if (actor.getDirection() > 150 && actor.getDirection() < 210)
      actor.setHorzMirror(false);
    else
      actor.setHorzMirror(true);
  }

  private void walkApproach()
  {
    Location pacLocation = PacActor.getInstance().getLocation();
    double oldDirection = actor.getDirection();

    // Walking approach:
    // TX5: Determine direction to pacActor and try to move in that direction. Otherwise, random walk.
    // Troll: Random walk.
    Location.CompassDirection compassDir =
            actor.getLocation().get4CompassDirectionTo(pacLocation);
    Location next = actor.getLocation().getNeighbourLocation(compassDir);
    actor.setDirection(compassDir);
    next = chooseNextLocation(oldDirection, next);
    Game.getInstance().getGameCallback().abstractMonsterLocationChanged(this);
    addVisitedList(next);
  }

  protected abstract Location chooseNextLocation(double oldDirection, Location next);

  protected Location doRandomWalk(double oldDirection) {
    Location next;
    // Random walk
    int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
    actor.setDirection(oldDirection);
    actor.turn(sign * 90);  // Try to turn left/right
    next = actor.getNextMoveLocation();
    if (canMove(next))
    {
      actor.setLocation(next);
    }
    else
    {
      actor.setDirection(oldDirection);
      next = actor.getNextMoveLocation();
      if (canMove(next)) // Try to move forward
      {
        actor.setLocation(next);
      }
      else
      {
        actor.setDirection(oldDirection);
        actor.turn(-sign * 90);  // Try to turn right/left
        next = actor.getNextMoveLocation();
        if (canMove(next))
        {
          actor.setLocation(next);
        }
        else
        {

          actor.setDirection(oldDirection);
          actor.turn(180);  // Turn backward
          next = actor.getNextMoveLocation();
          actor.setLocation(next);
        }
      }
    }
    return next;
  }

  public MonsterType getType() {
    return type;
  }

  private void addVisitedList(Location location)
  {
    visitedList.add(location);
    if (visitedList.size() == listLength)
      visitedList.remove(0);
  }

  protected boolean isVisited(Location location)
  {
    for (Location loc : visitedList)
      if (loc.equals(location))
        return true;
    return false;
  }

  protected boolean canMove(Location location)
  {
    Game game = Game.getInstance();
    Color c = actor.getBackground().getColor(location);
    if (c.equals(Color.gray) || location.getX() >= game.getNumHorzCells()
            || location.getX() < 0 || location.getY() >= game.getNumVertCells() || location.getY() < 0)
      return false;
    else
      return true;
  }

  protected abstract MonsterType setupMonsterType();
  public Location getLocation() {
    return actor.getLocation();
  }

  @Override
  public void setSlowDown(int factor) {
    actor.setSlowDown(factor);
  }

  @Override
  public void handleEndOfGame() {
    setStopMoving(true);
  }
}
