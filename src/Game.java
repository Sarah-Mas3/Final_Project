public class Game {
  private Map map;
  private Player player;
  private Level currentLevel;

  public Game( Level level){
      this.currentLevel = level;
      map = new Map(level);
      player = new Player();
  }

    public Map getMap() {
        return map;
    }

    public Player getPlayer() {
        return player;
    }

}
