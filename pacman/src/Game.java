// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.*;
import src.monster.ActorAdapter;
import src.monster.MonsterFacade;
import src.monster.MonsterType;
import src.monster.TX5;
import src.pill.PillFacade;
import src.utility.GameCallback;
import src.utility.PacManGameGrid;

import java.awt.*;
import java.util.Properties;

public class Game extends GameGrid
{
  private static Game gameSingleton = null;
  private final static int nbHorzCells = 20;
  private final static int nbVertCells = 11;
  protected PacManGameGrid grid = new PacManGameGrid(nbHorzCells, nbVertCells);

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
    setTitle("[PacMan in the TorusVerse]");

    //Setup for auto test
    PacActor pacActor = PacActor.getInstance();
    MonsterFacade monsterFacade = new MonsterFacade();
    pillFacade = new PillFacade();

    ActorAdapter[] actorAdapter = new ActorAdapter[] {
            pacActor,
            monsterFacade,
    };

    GGBackground bg = getBg();
    grid.draw(bg);

    //Setup Random seeds
    seed = Integer.parseInt(properties.getProperty("seed"));
    for (ActorAdapter actor : actorAdapter) {
      actor.handleStartOfGame(seed);
    }

    // NEW
    // For loop over ActorAdapter where MonsterFacade has a setSeed function
    // MonsterFacade.setSeed(seed)
    for (ActorAdapter actor : actorAdapter) {
      actor.setupActorLocations();
    }

    //Run the game
    doRun();
    show();
    // Loop to look for collision in the application thread
    // This makes it improbable that we miss a hit
    boolean hasPacmanBeenHit = false;
    boolean hasPacmanEatAllPills;

    pillFacade.setupPillAndItemsLocations();
    int maxPillsAndItems = pillFacade.getCount();

    do {
      hasPacmanBeenHit = hasPacmanBeenHit || monsterFacade.hasPacmanCollided();
      hasPacmanEatAllPills = pacActor.getNbPills() >= maxPillsAndItems;
      delay(10);
    } while(!hasPacmanBeenHit && !hasPacmanEatAllPills);
    delay(120);

    Location loc = pacActor.getLocation();
    for (ActorAdapter actor : actorAdapter) {
      actor.handleEndOfGame();
    }

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
