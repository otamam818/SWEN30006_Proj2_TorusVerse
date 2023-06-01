// PacMan.java
// Simple PacMan implementation
package src;

import ch.aplu.jgamegrid.*;
import src.editor.Constants;
import src.monster.ActorAdapter;
import src.monster.MonsterFacade;
import src.monster.MonsterType;
import src.monster.TX5;
import src.pill.PillFacade;
import src.portal.PortalFacade;
import src.utility.GameCallback;
import src.utility.GameXMLHandler;
import src.utility.PacManGameGrid;

import java.awt.*;
import java.io.File;
import java.util.Optional;
import java.util.Properties;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Game extends GameGrid
{
  private static Game gameSingleton = null;
  private final static int nbHorzCells = 20;
  private final static int nbVertCells = 11;
  protected PacManGameGrid grid = null;

  private GameCallback gameCallback;
  private Properties properties;
  private int seed = 30006;
  private PillFacade pillFacade;
  private PortalFacade portalFacade;
  private MonsterFacade monsterFacade;

  private Game() {
    super(nbHorzCells, nbVertCells, 20, false);
    this.properties = null;
    this.gameCallback = null;
  }

  private ScheduledExecutorService scheduler;

  public void setInitSettings(GameCallback gameCallback, Properties properties, Optional<File> chosenFile) {
    this.gameCallback = gameCallback;
    this.properties = properties;
    if (chosenFile.isEmpty()) {
      // TODO: call a 'default' map
      this.grid = new PacManGameGrid(nbHorzCells, nbVertCells);
    } else {
      // TODO: parse file to call a custom map
      GameXMLHandler handler = new GameXMLHandler(chosenFile.get());
      this.grid = new PacManGameGrid(handler);
    }
  }

  public void reset() {
    PacActor.getInstance().reset();
//    pillFacade.removeAllPills();
//    portalFacade.setupPortalLocations();
    monsterFacade.reset();
//    getBg().clear();
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
    monsterFacade = new MonsterFacade();
    pillFacade = new PillFacade();
    portalFacade = new PortalFacade();

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

    portalFacade.setupPortalLocations();

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
      Actor explosion = new Actor(Constants.ACTOR_SPRITES_FOLDER + "/explosion3.gif");
      addActor(explosion, loc);
      scheduler = Executors.newSingleThreadScheduledExecutor();
      scheduler.schedule(() -> {
        removeActor(explosion);
      }, 800, TimeUnit.MILLISECONDS);
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

  public PortalFacade getPortalFacade() {
    return  portalFacade;
  }

  public PacManGameGrid getGrid() {
    return grid;
  }
}
