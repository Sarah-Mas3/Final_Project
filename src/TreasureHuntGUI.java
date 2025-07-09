// Importing required Swing and AWT classes for GUI
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Main class extending JFrame for GUI window
public class TreasureHuntGUI extends JFrame {
    private Game game; // Game object managing the logic
    private JPanel mapPanel; // Panel that holds the map grid
    private JLabel[][] mapLabels; // 2D array of labels representing each cell
    private JLabel healthLabel, hintLabel; // Labels to show player health and hints
    private int gridSize; // Size of the map grid

    // Constructor: starts with level selection
    public TreasureHuntGUI() {
        showLevelSelection();
    }

    // Method to display a dialog for selecting game difficulty
    private void showLevelSelection() {
        String[] options = {"Easy", "Medium", "Hard"}; // Difficulty options
        int choice = JOptionPane.showOptionDialog(
                this, // Parent component
                "Choose a difficulty level", // Message
                "Treasure Hunt", // Title
                JOptionPane.DEFAULT_OPTION, // Option type
                JOptionPane.PLAIN_MESSAGE, // Message type
                null, // Icon
                options, // Option choices
                options[0] // Default option
        );

        // Create a Level object based on selected difficulty
        Level level;
        switch (choice) {
            case 1 -> level = new Level(6, 3, 4); // Medium
            case 2 -> level = new Level(8, 6, 6); // Hard
            default -> level = new Level(4, 2, 2); // Easy 
        }

        // Initialize the game with the selected level
        game = new Game(level);
        gridSize = level.getSize();
        setupGameUI(); // Setup the main game interface
    }

    // Method to build and display the main game window
    private void setupGameUI() {
        setTitle("Treasure Hunt"); // Set window title
        setSize(800, 800); // Set window size
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Close on exit
        setLayout(new BorderLayout()); // Use BorderLayout for layout

        mapPanel = new JPanel(new GridLayout(gridSize, gridSize)); // Create grid layout for map
        mapLabels = new JLabel[gridSize][gridSize]; // Initialize label array

        // Create each cell in the grid
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                mapLabels[i][j] = new JLabel("", SwingConstants.CENTER); // Centered text
                mapLabels[i][j].setFont(new Font("SansSerif", Font.BOLD, 22)); // Font settings
                mapLabels[i][j].setOpaque(true); // Make label background visible
                mapLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY)); // Add border
                mapLabels[i][j].setBackground(Color.WHITE); // Default background
                mapPanel.add(mapLabels[i][j]); // Add label to map panel
            }
        }

        // Create top panel for health and hint labels
        JPanel topPanel = new JPanel(new BorderLayout());
        healthLabel = new JLabel("Health: 100", SwingConstants.CENTER); // Initial health
        healthLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(healthLabel, BorderLayout.NORTH);

        hintLabel = new JLabel("Welcome to Treasure Hunt!", SwingConstants.CENTER); // Initial hint
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        topPanel.add(hintLabel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH); // Add to the top of the window

        // Create control buttons (arrow directions)
        JPanel controlPanel = new JPanel(new GridLayout(2, 3)); // 2x3 grid for arrows
        controlPanel.add(new JLabel()); // Empty placeholder
        controlPanel.add(createMoveButton("↑", "up")); // Up button
        controlPanel.add(new JLabel());
        controlPanel.add(createMoveButton("←", "left")); // Left button
        controlPanel.add(createMoveButton("↓", "down")); // Down button
        controlPanel.add(createMoveButton("→", "right")); // Right button
        add(controlPanel, BorderLayout.SOUTH); // Add to bottom of window

        // Restart button on the right
        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            dispose(); // Close current window
            SwingUtilities.invokeLater(() -> new TreasureHuntGUI()); // Start new game
        });
        add(restartButton, BorderLayout.EAST);

        add(mapPanel, BorderLayout.CENTER); // Add map in the center
        setVisible(true); // Make window visible
        updateMap(); // Display initial player location
    }

    // Create a movement button with a label and direction
    private JButton createMoveButton(String label, String direction) {
        JButton button = new JButton(label); // Create button
        button.setFont(new Font("Arial", Font.BOLD, 24)); // Set font
        button.addActionListener(e -> handleMove(direction)); // Add move logic
        return button;
    }

    // Handle movement logic when a direction button is pressed
    private void handleMove(String direction) {
        Player player = game.getPlayer();
        Map map = game.getMap();

        player.move(direction); // Move player
        int x = player.getX(); // New x position
        int y = player.getY(); // New y position

        // Check if move is outside the map
        if (!map.isInsideMap(x, y)) {
            player.reverseMove(direction); // Undo move
            JOptionPane.showMessageDialog(this, "You can't move outside the map!");
            return;
        }

        // Check for trap
        if (map.isTrap(x, y)) {
            player.decreaseHealth(20); // Decrease health
            JOptionPane.showMessageDialog(this, "🪤 You fell into a trap! -20 health.");
            hintLabel.setText("Hint: " + map.getTrapMessage(x, y)); // Show trap hint
        }
        // Check for first aid
        else if (map.isFirstAid(x, y)) {
            player.increaseHealth(15); // Increase health
            JOptionPane.showMessageDialog(this, "💊 You found a first aid kit! +15 health.");
            hintLabel.setText("Hint: " + map.getFirstAidMessage(x, y));
        }
        // Check for treasure
        else if (map.isTreasure(x, y)) {
            JOptionPane.showMessageDialog(this, "🎯 You found the treasure! You win!");
            hintLabel.setText("Congratulations! You found the treasure!");
        }
        // Empty cell
        else {
            player.decreaseHealth(10); // Decrease health slightly
            hintLabel.setText("Hint: " + map.getHintMessage(x, y)); // Show general hint
        }

        // Check if player lost all health
        if (!player.isAlive()) {
            JOptionPane.showMessageDialog(this, "💀 You lost all your health. Game Over.");
        }

        updateMap(); // Refresh the map display
    }

    // Update the GUI map to reflect the player's position and visible items
    private void updateMap() {
        Player player = game.getPlayer();
        Map map = game.getMap();
        int px = player.getX();
        int py = player.getY();

        // Cover the whole map in dark color
        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                JLabel cell = mapLabels[i][j];
                cell.setText(""); // Clear text
                cell.setBackground(Color.DARK_GRAY); // Hide cell
                cell.setForeground(Color.WHITE);
            }
        }

        // Show only the player's current cell
        JLabel playerCell = mapLabels[px][py];

        if (map.isTreasure(px, py)) {
            playerCell.setBackground(Color.YELLOW);
            playerCell.setText("C"); // C for Chest
        } else if (map.isTrap(px, py)) {
            playerCell.setBackground(Color.RED);
            playerCell.setText("T"); // T for Trap
        } else if (map.isFirstAid(px, py)) {
            playerCell.setBackground(Color.GREEN);
            playerCell.setText("F"); // F for First Aid
        } else {
            playerCell.setBackground(Color.BLUE);
            playerCell.setText("P"); // P for Player
        }

        // Update health display
        healthLabel.setText("Health: " + player.getHealth());
    }

    // Main method to launch the GUI application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TreasureHuntGUI::new);
    }
}
