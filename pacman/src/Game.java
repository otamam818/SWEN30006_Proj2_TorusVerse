// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.*;
import src.Pill.PillFacade;
import src.utility.GameCallback;

import java.awt.*;
import java.util.ArrayList;
import java.util.Properties;

public class Game extends GameGrid
{
  private static Game gameSingleton = null;
  private final static int nbHorzCells = 20;
  private final static int nbVertCells = 11;
  protected PacManGameGrid grid = new PacManGameGrid(nbHorzCells, nbVertCells);

  protected PacActor pacActor = new PacActor(this);
  private Monster troll = new Monster(this, MonsterType.Troll);
  private Monster tx5 = new Monster(this, MonsterType.TX5);


  private GameCallback gameCallback;
  private Properties properties;
  private int seed = 30006;
  private PillFacade pillFacade;

  private Game() {
    super(nbHorzCells, nbVertCells, 20, false);
    this.properties = null;
  }

  public void setInitSettings(GameCallback gameCallback, Properties properties) {
    this.gameCallback = gameCallback;
    this.properties = properties;
  }

  public void build()
  {
    //Setup game
    assert gameCallback != null : "gameCallback not initialized";
    assert properties != null : "properties not initialized";
    setSimulationPeriod(100);
    setTitle("[PacMan in the Torusverse]");

    //Setup for auto test
    pacActor.setPropertyMoves(properties.getProperty("PacMan.move"));
    pacActor.setAuto(Boolean.parseBoolean(properties.getProperty("PacMan.isAuto")));
    pillFacade = new PillFacade();

    GGBackground bg = getBg();
    grid.draw(bg);

    //Setup Random seeds
    seed = Integer.parseInt(properties.getProperty("seed"));
    pacActor.setSeed(seed);
    troll.setSeed(seed);
    tx5.setSeed(seed);
    addKeyRepeatListener(pacActor);
    setKeyRepeatPeriod(150);
    troll.setSlowDown(3);
    tx5.setSlowDown(3);
    pacActor.setSlowDown(3);
    tx5.stopMoving(5);
    setupActorLocations();

    //Run the game
    doRun();
    show();
    // Loop to look for collision in the application thread
    // This makes it improbable that we miss a hit
    boolean hasPacmanBeenHit;
    boolean hasPacmanEatAllPills;

    pillFacade.setupPillAndItemsLocations();
    int maxPillsAndItems = pillFacade.getCount();

    
    do {
      hasPacmanBeenHit = troll.getLocation().equals(pacActor.getLocation()) ||
              tx5.getLocation().equals(pacActor.getLocation());
      hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;
      delay(10);
    } while(!hasPacmanBeenHit && !hasPacmanEatAllPills);
    delay(120);

    Location loc = pacActor.getLocation();
    troll.setStopMoving(true);
    tx5.setStopMoving(true);
    pacActor.removeSelf();

    String title = "";
    if (hasPacmanBeenHit) {
      bg.setPaintColor(Color.red);
      title = "GAME OVER";
      addActor(new Actor("sprites/explosion3.gif"), loc);
    } else if (hasPacmanEatAllPills) {
      bg.setPaintColor(Color.yellow);
      title = "YOU WIN";
    }
    setTitle(title);
    gameCallback.endOfGame(title);

    doPause();
  }

  public Properties getProperties() {
    return properties;
  }


  public static synchronized Game getInstance() {
    if (gameSingleton == null)
      gameSingleton = new Game();
    return gameSingleton;
  }

  public GameCallback getGameCallback() {
    return gameCallback;
  }

  private void setupActorLocations() {
    String[] trollLocations = this.properties.getProperty("Troll.location").split(",");
    String[] tx5Locations = this.properties.getProperty("TX5.location").split(",");
    String[] pacManLocations = this.properties.getProperty("PacMan.location").split(",");
    int trollX = Integer.parseInt(trollLocations[0]);
    int trollY = Integer.parseInt(trollLocations[1]);

    int tx5X = Integer.parseInt(tx5Locations[0]);
    int tx5Y = Integer.parseInt(tx5Locations[1]);

    int pacManX = Integer.parseInt(pacManLocations[0]);
    int pacManY = Integer.parseInt(pacManLocations[1]);

    addActor(troll, new Location(trollX, trollY), Location.NORTH);
    addActor(pacActor, new Location(pacManX, pacManY));
    addActor(tx5, new Location(tx5X, tx5Y), Location.NORTH);
  }

  public int getNumHorzCells(){
    return this.nbHorzCells;
  }
  public int getNumVertCells(){
    return this.nbVertCells;
  }

  public PillFacade getPillFacade() {
    return pillFacade;
  }

  public PacManGameGrid getGrid() {
    return grid;
  }
}
