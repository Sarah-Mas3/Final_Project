public class Player {
  private int x,y;
  private int health;

// the initial values for the player starting point and health
 public Player(){
     reset();
 }

// to move the player inside the map
  public void move(String dir){
     switch (dir.toLowerCase()){
         case "up" -> x--;
         case "down" -> x++;
         case "left" -> y--;
         case "right" -> y++;
     }
  }

// incase the player wanted to get outside the map
    public void reverseMove(String dir){
      switch (dir.toLowerCase()){
          case "up" -> x++;
          case "down" -> x--;
          case "left" -> y++;
          case "right" -> y--;

      }
    }


// to use if the player fell in the trap or moved to the next square
  public void decreaseHealth(int amount){
     health-= amount;
  }

  public void increaseHealth(int amount){
     health+= amount;
  }


// to restart the game
    public void reset() {
        x = 0;
        y = 0;
        health = 100;
    }


// checking the health percentage so the game can continue
    public boolean isAlive() {
        return health > 0;
    }

// getters for the attributes
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getHealth() {
        return health;
    }

    // Setters
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

}
