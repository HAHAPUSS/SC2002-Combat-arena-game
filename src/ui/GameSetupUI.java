package ui;

import combatants.Player;
import combatants.Warrior;
import combatants.Wizard;
import items.Item;
import items.Potion;
import items.PowerStone;
import items.SmokeBomb;
import level.Level;
import level.LevelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// SRP: handles all pre-battle setup interactions
public class GameSetupUI {
    private final Scanner scanner;

    public GameSetupUI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void displayWelcome() {
        System.out.println("============================================================");
        System.out.println("       SC2002 TURN-BASED COMBAT ARENA");
        System.out.println("============================================================");
        System.out.println("Welcome, brave adventurer! Choose your champion and battle");
        System.out.println("through waves of enemies to claim victory!");
        System.out.println("============================================================");
        System.out.println();
    }

    public Player selectPlayer() {
        System.out.println("--- SELECT YOUR CHARACTER ---");
        System.out.println("1. Warrior");
        System.out.println("   HP: 260 | ATK: 40 | DEF: 20 | SPD: 30");
        System.out.println("   Special: Shield Bash - Attack enemy + Stun for 2 turns (CD: 3)");
        System.out.println("2. Wizard");
        System.out.println("   HP: 200 | ATK: 50 | DEF: 10 | SPD: 20");
        System.out.println("   Special: Arcane Blast - Hit ALL enemies. Kills grant +10 ATK (CD: 3)");
        System.out.println();

        int choice = readIntInRange("Enter choice (1-2): ", 1, 2);
        String playerName = readNonEmptyString("Enter your name: ");

        Player player = (choice == 1) ? new Warrior(playerName) : new Wizard(playerName);
        System.out.println("You chose: " + player.getDescription());
        System.out.println();
        return player;
    }

    public List<Item> selectItems() {
        System.out.println("--- SELECT YOUR ITEMS (Choose 2, duplicates allowed) ---");
        System.out.println("1. Potion       - Heal 100 HP (capped at max HP)");
        System.out.println("2. Power Stone  - Trigger special skill effect (does NOT affect cooldown)");
        System.out.println("3. Smoke Bomb   - Enemy attacks deal 0 damage for 2 rounds");
        System.out.println();

        List<Item> selected = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            int choice = readIntInRange("Select item " + i + " (1-3): ", 1, 3);
            Item item = createItem(choice);
            selected.add(item);
            System.out.println("  -> Item " + i + ": " + item.getName() + " selected.");
        }
        System.out.println();
        return selected;
    }

    public Level selectLevel() {
        System.out.println("--- SELECT DIFFICULTY ---");
        System.out.println("1. Easy   - Level 1 (Easy): 3 Goblins | No Backup");
        System.out.println("           Goblin: HP=55 ATK=35 DEF=15 SPD=25");
        System.out.println("2. Medium - Level 2 (Medium): 1 Goblin + 1 Wolf | Backup: 2 Wolves");
        System.out.println("           Goblin: HP=55 ATK=35 DEF=15 SPD=25");
        System.out.println("           Wolf:   HP=40 ATK=45 DEF=5  SPD=35");
        System.out.println("3. Hard   - Level 3 (Hard): 2 Goblins | Backup: 1 Goblin + 2 Wolves");
        System.out.println("           Goblin: HP=55 ATK=35 DEF=15 SPD=25");
        System.out.println("           Wolf:   HP=40 ATK=45 DEF=5  SPD=35");
        System.out.println();

        int choice = readIntInRange("Enter choice (1-3): ", 1, 3);
        Level level = LevelFactory.create(choice);
        System.out.println("Selected: " + level.getDescription());
        System.out.println();
        return level;
    }

    // Runs the full setup loop with confirmation + back support
    // Returns [player, items, level] as a SetupResult
    public SetupResult runSetup() {
        while (true) {
            Player player = selectPlayer();
            List<Item> items = selectItems();
            Level level = selectLevel();

            // Confirmation screen
            System.out.println("--- CONFIRM YOUR SETUP ---");
            System.out.println("  Player : " + player.getDescription());
            System.out.print("  Items  : ");
            List<String> itemNames = new ArrayList<>();
            for (Item it : items) itemNames.add(it.getName());
            System.out.println(String.join(", ", itemNames));
            System.out.println("  Level  : " + level.getDescription());
            System.out.println();
            System.out.println("  1. Confirm and Start");
            System.out.println("  2. Go Back and Change");
            System.out.println();

            int choice = readIntInRange("Enter choice (1-2): ", 1, 2);
            if (choice == 1) {
                return new SetupResult(player, items, level);
            }
            System.out.println();
            System.out.println("Going back to setup...");
            System.out.println();
        }
    }

    // Simple data carrier for setup results
    public static class SetupResult {
        public final Player player;
        public final List<Item> items;
        public final Level level;

        public SetupResult(Player player, List<Item> items, Level level) {
            this.player = player;
            this.items = items;
            this.level = level;
        }
    }

    // Returns: 1 = same setup replay, 2 = new game, 3 = exit
    public int promptReplay() {
        System.out.println();
        System.out.println("What would you like to do?");
        System.out.println("  1. Play Again (same setup)");
        System.out.println("  2. New Game (different setup)");
        System.out.println("  3. Exit");
        int choice = readIntInRange("Enter choice (1-3): ", 1, 3);
        if (choice == 3) {
            System.out.println("Thanks for playing! Goodbye!");
        }
        return choice;
    }

    private Item createItem(int choice) {
        switch (choice) {
            case 1: return new Potion();
            case 2: return new PowerStone();
            case 3: return new SmokeBomb();
            default: return new Potion();
        }
    }

    private int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) return value;
                System.out.printf("Please enter a number between %d and %d.%n", min, max);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }
    }

    private String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) return line;
            System.out.println("Name cannot be empty.");
        }
    }
}
