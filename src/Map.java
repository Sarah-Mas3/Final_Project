import java.util.*;

public class Map {
    private char[][] map;
    private HashMap<String, String> traps;
    private HashSet<String> usedPositions;
    private HashMap<String, String> firstAids;
    private int treasureX, treasureY;
    private int size;
    private Random random = new Random();

    public Map(Level level) {
        this.size = level.getSize();
        map = new char[size][size];
        traps = new HashMap<>();
        usedPositions = new HashSet<>();
         firstAids = new HashMap<>();
        initializeMap(level);
    }

    public void initializeMap(Level level) {
        for (int i = 0; i < size; i++) {
           Arrays.fill(map[i], '.');
        }

     // putting traps
        for (int i = 0; i < level.getTrapCount(); i++) {
            int x, y;
            do {
                x = random.nextInt(size);
                y = random.nextInt(size);
            } while (usedPositions.contains(x + "," + y));
            traps.put(x + "," + y, "You fell in a trap! -20 energy.");
            usedPositions.add(x + "," + y);
            map[x][y] = 'T';
        }

     // adding first aid kits
        for (int i = 0; i < level.getFirstAid(); i++) {
            int x, y;
            do {
                x = random.nextInt(size);
                y = random.nextInt(size);
            } while (usedPositions.contains(x + "," + y));
            firstAids.put(x + "," + y, "You found a first aid kit! +15 energy.");
            usedPositions.add(x + "," + y);
            map[x][y] = 'F'; // first aid kits
        }
    // adding the treasure
        int x, y;
        do {
            x = random.nextInt(size);
            y = random.nextInt(size);
        } while (usedPositions.contains(x + "," + y) || x == 0 && y == 0 );
        treasureX = x;
        treasureY = y;
        usedPositions.add(x + "," + y);
        map[x][y] = 'C'; // Treasure
    }

// checking if the player next step is inside the map
    public boolean isInsideMap(int x, int y) {
        return x >= 0 && x < size && y >= 0 && y < size;
    }

// checking if the square has a trap
    public boolean isTrap(int x, int y) {
        return traps.containsKey(x + "," + y);
    }

    public String getTrapMessage(int x, int y) {
        return traps.getOrDefault(x + "," + y, ""); // receiving the trap massege
    }

    // checking if the player found the trap
    public boolean isTreasure(int x, int y) {
        return x == treasureX && y == treasureY;
    }

    public boolean isFirstAid(int x, int y) {
        return usedPositions.contains(x + "," + y) && map[x][y] == 'F';
    }

    public String getFirstAidMessage(int x, int y){
        return firstAids.getOrDefault(x + "," + y, ""); // receiving the trap massege
    }

    // hints massage depending on the distance
    public String getHintMessage(int x, int y) {
        int distance = Math.abs(treasureX - x) + Math.abs(treasureY - y);
        if (distance == 1) return "Very close to the treasure!";
        else if (distance <= 2) return "Getting warmer!";
        else if (distance <= 4) return "Still a bit far...";
        else return "Cold... try another direction.";
    }

// Getters
    public int getSize() {
        return size;
    }

    public char[][] getMap() {
        return map;
    }
}
