package src.monster;

import ch.aplu.jgamegrid.Location;
import src.MonsterType;

public class Troll extends AbstractMonster {
  public Troll() {
    super();
  }

  @Override
  protected Location chooseNextLocation(double oldDirection, Location next) {
    return doRandomWalk(oldDirection);
  }

  @Override
  protected MonsterType setupMonsterType() {
    return MonsterType.Troll;
  }
}
