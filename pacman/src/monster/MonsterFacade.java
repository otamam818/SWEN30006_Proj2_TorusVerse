package src.monster;

public class MonsterFacade implements ActorAdapter {
  private final AbstractMonster[] allMonsters;
  public MonsterFacade() {
    // NOTE: This may be subject to change due to how TorusVerse works
    this.allMonsters = new AbstractMonster[] {
      new Troll(),
      new TX5()
    };
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
}
