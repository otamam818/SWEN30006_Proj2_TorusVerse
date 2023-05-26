package src.monster;

import ch.aplu.jgamegrid.Location;
import src.MonsterType;

public class TX5 extends AbstractMonster {
  public TX5() {
    super();
  }

  @Override
  protected Location chooseNextLocation(double oldDirection, Location next) {
    if (!isVisited(next) && canMove(next)) {
      actor.setLocation(next);
    } else {
      next = doRandomWalk(oldDirection);
    }
    return next;
  }

  @Override
  protected MonsterType setupMonsterType() {
    return MonsterType.TX5;
  }
}
