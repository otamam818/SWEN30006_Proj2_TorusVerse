package src.monster;

import ch.aplu.jgamegrid.Location;

public class TX5 extends AbstractMonster {
  public TX5() {
    super();
  }

  @Override
  protected Location chooseNextLocation(double oldDirection, Location next) {
    next = getMoveOutOfBounds(next);
    if (!isVisited(next) && canMove(next)) {
      actor.setLocation(next);
    } else {
      next = doRandomWalk(oldDirection);
    }
    return next;
  }

  @Override
  public String getKey() {
    return "TX5";
  }

  @Override
  protected MonsterType setupMonsterType() {
    return MonsterType.TX5;
  }
}
