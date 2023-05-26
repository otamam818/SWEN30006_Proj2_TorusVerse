package src.monster;

public class MonsterFacade {
  private AbstractMonster[] allMonsters;
  public MonsterFacade() {
    this.allMonsters = new AbstractMonster[] {
      new Troll(),
      new TX5()
    };
  }
}
