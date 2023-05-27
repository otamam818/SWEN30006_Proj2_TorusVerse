package src.monster;

import src.PacActor;

public class MonsterFacade implements ActorAdapter {
  private final AbstractMonster[] allMonsters;
  public MonsterFacade() {
    // NOTE: This may be subject to change due to how TorusVerse works
    this.allMonsters = new AbstractMonster[] {
      new Troll(),
      new TX5()
    };
  }

  public void reset() {
    for (AbstractMonster monster : allMonsters) {
      monster.actor.removeSelf();
    }
  }

  @Override
  public void setSeed(int seed) {
    for (AbstractMonster monster : allMonsters) {
      monster.setSeed(seed);
    }
  }

  @Override
  public void setSlowDown(int factor) {
    for (AbstractMonster monster : allMonsters) {
      monster.setSlowDown(factor);
    }
  }

  @Override
  public void handleEndOfGame() {
    for (AbstractMonster monster : allMonsters) {
      monster.handleEndOfGame();
    }
  }

  @Override
  public void setupActorLocations() {
    for (AbstractMonster monster : allMonsters) {
      monster.setupActorLocations();
    }
  }

  public boolean hasPacmanCollided() {
    PacActor pacActor = PacActor.getInstance();
    for (AbstractMonster currentMonster : allMonsters) {
      if (currentMonster.collidesWith(pacActor)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void handleStartOfGame(int seed) {
    for (AbstractMonster currentMonster : allMonsters) {
      currentMonster.setSeed(seed);
      currentMonster.setSlowDown(3);
      if (currentMonster instanceof TX5) {
        ((TX5) currentMonster).stopMoving(5);
      }
    }
  }
}
