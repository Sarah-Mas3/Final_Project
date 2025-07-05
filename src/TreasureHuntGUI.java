import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TreasureHuntGUI extends JFrame {
    private Game game;
    private JPanel mapPanel;
    private JLabel[][] mapLabels;
    private JLabel healthLabel, hintLabel;
    private int gridSize;

    public TreasureHuntGUI() {
        showLevelSelection(); // Start by selecting level
    }

    private void showLevelSelection() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "Choose a difficulty level",
                "Treasure Hunt",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                options,
                options[0]
        );

        Level level;
        switch (choice) {
            case 1 -> level = new Level(6, 3, 4);
            case 2 -> level = new Level(8, 6, 6);
            default -> level = new Level(4, 2, 2);
        }

        game = new Game(level);
        gridSize = level.getSize();
        setupGameUI();
    }

    private void setupGameUI() {
        setTitle("Treasure Hunt");
        setSize(800, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        mapPanel = new JPanel(new GridLayout(gridSize, gridSize));
        mapLabels = new JLabel[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                mapLabels[i][j] = new JLabel("", SwingConstants.CENTER);
                mapLabels[i][j].setFont(new Font("SansSerif", Font.BOLD, 22));
                mapLabels[i][j].setOpaque(true);
                mapLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY));
                mapLabels[i][j].setBackground(Color.WHITE);
                mapPanel.add(mapLabels[i][j]);
            }
        }

        JPanel topPanel = new JPanel(new BorderLayout());
        healthLabel = new JLabel("Health: 100", SwingConstants.CENTER);
        healthLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(healthLabel, BorderLayout.NORTH);

        hintLabel = new JLabel("Welcome to Treasure Hunt!", SwingConstants.CENTER);
        hintLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        topPanel.add(hintLabel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new GridLayout(2, 3));
        controlPanel.add(new JLabel());
        controlPanel.add(createMoveButton("↑", "up"));
        controlPanel.add(new JLabel());
        controlPanel.add(createMoveButton("←", "left"));
        controlPanel.add(createMoveButton("↓", "down"));
        controlPanel.add(createMoveButton("→", "right"));
        add(controlPanel, BorderLayout.SOUTH);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(e -> {
            dispose();
            SwingUtilities.invokeLater(() -> new TreasureHuntGUI());
        });
        add(restartButton, BorderLayout.EAST);

        add(mapPanel, BorderLayout.CENTER);
        setVisible(true);
        updateMap();
    }

    private JButton createMoveButton(String label, String direction) {
        JButton button = new JButton(label);
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.addActionListener(e -> handleMove(direction));
        return button;
    }

    private void handleMove(String direction) {
        Player player = game.getPlayer();
        Map map = game.getMap();

        player.move(direction);
        int x = player.getX();
        int y = player.getY();

        if (!map.isInsideMap(x, y)) {
            player.reverseMove(direction);
            JOptionPane.showMessageDialog(this, "You can't move outside the map!");
            return;
        }

        player.decreaseHealth(10);

        if (map.isTrap(x, y)) {
            player.decreaseHealth(20);
            JOptionPane.showMessageDialog(this, "🪤 You fell into a trap! -20 health.");
            hintLabel.setText("Hint: " + map.getTrapMessage(x, y));
        } else if (map.isFirstAid(x, y)) {
            player.increaseHealth(15);
            JOptionPane.showMessageDialog(this, "💊 You found a first aid kit! +15 health.");
            hintLabel.setText("Hint: " + map.getFirstAidMessage(x, y));
        } else if (map.isTreasure(x, y)) {
            JOptionPane.showMessageDialog(this, "🎯 You found the treasure! You win!");
            hintLabel.setText("Congratulations! You found the treasure!");
        } else {
            hintLabel.setText("Hint: " + map.getHintMessage(x, y));
        }

        if (!player.isAlive()) {
            JOptionPane.showMessageDialog(this, "💀 You lost all your health. Game Over.");
        }

        updateMap();
    }
    private void updateMap() {
        Player player = game.getPlayer();
        Map map = game.getMap();
        int px = player.getX();
        int py = player.getY();


        for (int i = 0; i < map.getSize(); i++) {
            for (int j = 0; j < map.getSize(); j++) {
                JLabel cell = mapLabels[i][j];
                cell.setText("");
                cell.setBackground(Color.DARK_GRAY);
                cell.setForeground(Color.WHITE);
            }
        }

        JLabel playerCell = mapLabels[px][py];

        if (map.isTreasure(px, py)) {
            playerCell.setBackground(Color.YELLOW);
            playerCell.setText("C");
        } else if (map.isTrap(px, py)) {
            playerCell.setBackground(Color.RED);
            playerCell.setText("T");
        } else if (map.isFirstAid(px, py)) {
            playerCell.setBackground(Color.GREEN);
            playerCell.setText("F");
        } else {
            playerCell.setBackground(Color.BLUE);
            playerCell.setText("P");
        }

        healthLabel.setText("Health: " + player.getHealth());
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(TreasureHuntGUI::new);
    }
}
