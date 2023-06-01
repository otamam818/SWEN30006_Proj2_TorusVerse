// PacActor.java
// Used for PacMan
package src;

import ch.aplu.jgamegrid.*;
import src.editor.Constants;
import src.monster.ActorAdapter;
import src.portal.PortalFacade;
import src.utility.PacManGameGrid;

import java.awt.event.KeyEvent;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PacActor extends Actor implements GGKeyRepeatListener, MovingActor, ActorAdapter
{
  private static PacActor pacActorSingleton = null;
  private static final int nbSprites = 4;
  private int idSprite = 0;
  private int nbPills = 0;
  private int score = 0;
  private ArrayList<Location> visitedList = new ArrayList<>();
  private List<String> propertyMoves = new ArrayList<>();
  private int propertyMoveIndex = 0;
  private final int listLength = 10;
  private int seed;
  private boolean isFirstGame;
  private final Random randomiser = new Random();
  public void reset() {
    this.idSprite = 0;
    this.nbPills = 0;
    this.score = 0;
    this.visitedList = new ArrayList<>();
    this.propertyMoves = new ArrayList<>();
    this.propertyMoveIndex = 0;
    this.isFirstGame = false;
  }
  private PacActor()
  {
    super(true, Constants.ACTOR_SPRITES_FOLDER + "/pacpix.gif", nbSprites);  // Rotatable
    var properties = Game.getInstance().getProperties();
    isFirstGame = true;
    setPropertyMoves(properties.getProperty("PacMan.move"));
    setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));
  }

  public static synchronized PacActor getInstance() {
    if (pacActorSingleton == null)
      pacActorSingleton = new PacActor();
    return pacActorSingleton;
  }
  private boolean isAuto = false;

  public void setAuto(boolean auto) {
    isAuto = auto;
  }


  public void setSeed(int seed) {
    this.seed = seed;
    randomiser.setSeed(seed);
  }

  public void setPropertyMoves(String propertyMoveString) {
    if (propertyMoveString != null) {
      this.propertyMoves = Arrays.asList(propertyMoveString.split(","));
    }
  }

  public void keyRepeated(int keyCode)
  {
    if (isAuto) {
      return;
    }
    if (isRemoved())  // Already removed
      return;
    Location next = null;
    switch (keyCode) {
      case KeyEvent.VK_LEFT -> {
        next = getLocation().getNeighbourLocation(Location.WEST);
        setDirection(Location.WEST);
      }
      case KeyEvent.VK_UP -> {
        next = getLocation().getNeighbourLocation(Location.NORTH);
        setDirection(Location.NORTH);
      }
      case KeyEvent.VK_RIGHT -> {
        next = getLocation().getNeighbourLocation(Location.EAST);
        setDirection(Location.EAST);
      }
      case KeyEvent.VK_DOWN -> {
        next = getLocation().getNeighbourLocation(Location.SOUTH);
        setDirection(Location.SOUTH);
      }
    }
    next = getMoveOutOfBounds(next);
    if (next != null && canMove(next))
    {
      setLocation(next);
      eatPill(next);
      Location currPortal = checkPortal(next);
      if (!(currPortal.equals(next))) {
        setLocation(currPortal);
      }
    }
  }

  public void act()
  {
    show(idSprite);
    idSprite++;
    if (idSprite == nbSprites)
      idSprite = 0;

    if (isAuto) {
      moveInAutoMode();
    }
    Game.getInstance().getGameCallback().pacManLocationChanged(getLocation(), score, nbPills);
  }

  private Location closestPillLocation() {
    int currentDistance = 1000;
    Location currentLocation = null;
    List<Location> pillAndItemLocations = Game
            .getInstance()
            .getPillFacade()
            .getPillAndItemLocations();
    for (Location location: pillAndItemLocations) {
      int distanceToPill = location.getDistanceTo(getLocation());
      if (distanceToPill < currentDistance) {
        currentLocation = location;
        currentDistance = distanceToPill;
      }
    }

    return currentLocation;
  }

  private void followPropertyMoves() {
    String currentMove = propertyMoves.get(propertyMoveIndex);
    switch (currentMove) {
      case "R" -> turn(90);
      case "L" -> turn(-90);
      case "M" -> {
        Location next = getNextMoveLocation();
        next = getMoveOutOfBounds(next);
        if (canMove(next)) {
          setLocation(next);
          eatPill(next);
        }
      }
    }
    propertyMoveIndex++;
  }

  private void moveInAutoMode() {
    if (propertyMoves.size() > propertyMoveIndex) {
      followPropertyMoves();
      return;
    }
    Location closestPill = closestPillLocation();
    double oldDirection = getDirection();

    Location.CompassDirection compassDir =
            getLocation().get4CompassDirectionTo(closestPill);
    Location next = getLocation().getNeighbourLocation(compassDir);
    setDirection(compassDir);
    next = getMoveOutOfBounds(next);
    if (!isVisited(next) && canMove(next)) {
      setLocation(next);
    } else {
      // normal movement
      int sign = randomiser.nextDouble() < 0.5 ? 1 : -1;
      setDirection(oldDirection);
      turn(sign * 90);  // Try to turn left/right
      next = getNextMoveLocation();
      next = getMoveOutOfBounds(next);
      if (canMove(next)) {
        setLocation(next);
      } else {
        setDirection(oldDirection);
        next = getNextMoveLocation();
        next = getMoveOutOfBounds(next);
        if (canMove(next)) // Try to move forward
        {
          setLocation(next);
        } else {
          setDirection(oldDirection);
          turn(-sign * 90);  // Try to turn right/left
          next = getNextMoveLocation();
          next = getMoveOutOfBounds(next);
          if (canMove(next)) {
            setLocation(next);
          } else {
            setDirection(oldDirection);
            turn(180);  // Turn backward
            next = getNextMoveLocation();
            setLocation(next);
          }
        }
      }
    }
    eatPill(next);
    addVisitedList(next);
  }

  private void addVisitedList(Location location)
  {
    visitedList.add(location);
    if (visitedList.size() == listLength)
      visitedList.remove(0);
  }

  private boolean isVisited(Location location)
  {
    for (Location loc : visitedList)
      if (loc.equals(location))
        return true;
    return false;
  }

  private int checkOutOfBounds(int coord, int bound) {
    if (coord >= bound || coord < 0) {
      return (coord < 0) ? coord + bound : coord % bound;
    }
    return -1;
  }

  protected Location checkPortal(Location location) {
    Game game = Game.getInstance();
    List<Location>[] portalList = game.getPortalFacade().getPortals();
    for (List<Location> portal : portalList) {
      if (portal.contains(location)) {
        for (Location portalCoords : portal) {
          if (!(portalCoords.equals(location))) {
            return portalCoords;
          }
        }
      }
    }
    return location;
  }

  protected Location getMoveOutOfBounds(Location location)
  {
    if (location == null) {
      return null;
    }

    Game game = Game.getInstance();
    Location newLocation;
    int currX = location.getX();
    int currY = location.getY();
    // if newX or newY value remains as -1 after the next two statements,
    // they don't fall outside bounds.
    int newX = checkOutOfBounds(currX, game.getNumHorzCells());
    int newY = checkOutOfBounds(currY, game.getNumVertCells());

    if ((newX != -1) || (newY != -1)) {
      if (newX != -1 && newY != -1) {
        newLocation = new Location(newX, newY);
      }
      else if (newX == -1) {
        newLocation = new Location(currX, newY);
      }
      else{
        newLocation =  new Location(newX, currY);
      }
    }
    else {
      newLocation = new Location(currX, currY);
    }

//    return checkPortal(newLocation);
    return newLocation;
  }

  protected boolean canMove(Location location) {
    Color c = getBackground().getColor(location);
    boolean isPortal = !checkPortal(location).equals(location);
    return (!c.equals(Color.gray) || isPortal);
  }

  public int getNbPills() {
    return nbPills;
  }

  private void eatPill(Location location)
  {
    Color c = getBackground().getColor(location);
    String type = null;
    boolean hasActorItems = false;
    if (c.equals(Color.white))
    {
      nbPills++;
      score++;
      getBackground().fillCell(location, Color.lightGray);
      type = "pills";
    } else if (c.equals(Color.yellow)) {
      nbPills++;
      score+= 5;
      getBackground().fillCell(location, Color.lightGray);
      type = "gold";
      hasActorItems = true;
    } else if (c.equals(Color.blue)) {
      getBackground().fillCell(location, Color.lightGray);
      type = "ice";
      hasActorItems = true;
    }
    if (type != null) {
      Game
        .getInstance()
        .getGameCallback()
        .pacManEatPillsAndItems(location, type);
      if (hasActorItems) {
        Game
          .getInstance()
          .getPillFacade()
          .removeItem(type,location);
      }
    }
    String title = "[PacMan in the Multiverse] Current score: " + score;
    gameGrid.setTitle(title);
  }

  @Override
  public String getKey() {
    return "PacMan";
  }

  @Override
  public void handleEndOfGame() {
    removeSelf();
  }

  @Override
  public Boolean collidesWith(MovingActor other) {
    return MovingActor.super.collidesWith(other);
  }

  @Override
  public Location initializeLocation() {
    return MovingActor.super.initializeLocation();
  }

  @Override
  public void setupActorLocations() {
    MovingActor.super.setupActorLocations();
  }

  @Override
  public void placeActor(Location location) {
    MovingActor.super.placeActor(location);
  }

  @Override
  public void handleStartOfGame(int seed) {
    setSeed(seed);
    if (this.isFirstGame) {
      Game.getInstance().addKeyRepeatListener(this);
      Game.getInstance().setKeyRepeatPeriod(150);
    }
    setSlowDown(3);
  }
}
