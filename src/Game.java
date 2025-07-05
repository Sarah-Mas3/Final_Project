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

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public void resetLevel(Level newLevel) {
        this.currentLevel = newLevel;
        this.map = new Map(newLevel);
        this.player.reset();
    }

}
