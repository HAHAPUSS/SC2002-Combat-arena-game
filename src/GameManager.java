import combatants.Player;
import combatants.Warrior;
import combatants.Wizard;
import engine.BattleContext;
import engine.BattleEngine;
import items.Item;
import items.Potion;
import items.PowerStone;
import items.SmokeBomb;
import level.Level;
import level.LevelFactory;
import strategies.SpeedBasedTurnOrder;
import ui.BattleUI;
import ui.ConsoleBattleUI;
import ui.GameSetupUI;
import ui.GameSetupUI.SetupResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameManager {
    private final GameSetupUI setupUI;
    private final BattleUI battleUI;

    // Stores last setup for "Play Again (same setup)"
    private String lastPlayerClass;
    private String lastPlayerName;
    private List<String> lastItemNames;
    private int lastLevelNo;

    public GameManager() {
        Scanner scanner = new Scanner(System.in);
        this.setupUI = new GameSetupUI(scanner);
        this.battleUI = new ConsoleBattleUI(scanner);
    }

    public void start() {
        setupUI.displayWelcome();

        // First game always goes through full setup with confirmation
        SetupResult setup = setupUI.runSetup();
        Player player = setup.player;
        List<Item> items = setup.items;
        Level level = setup.level;

        saveSetup(player, items, level);

        while (true) {
            // Give player fresh copies of items (single-use, need new instances)
            for (Item item : items) {
                player.addItem(item);
            }

            BattleContext ctx = new BattleContext(player, level);
            BattleEngine engine = new BattleEngine(ctx, new SpeedBasedTurnOrder(), battleUI);
            engine.run();

            int replayChoice = setupUI.promptReplay();

            if (replayChoice == 3) {
                break; // exit
            } else if (replayChoice == 1) {
                // Same setup — rebuild fresh player and items from saved info
                player = buildPlayer(lastPlayerClass, lastPlayerName);
                items = buildItems(lastItemNames);
                level = LevelFactory.create(lastLevelNo);
                System.out.println();
                System.out.println("Restarting with same setup...");
                System.out.println();
            } else {
                // New game — full setup with confirmation
                SetupResult newSetup = setupUI.runSetup();
                player = newSetup.player;
                items = newSetup.items;
                level = newSetup.level;
                saveSetup(player, items, level);
                System.out.println();
                System.out.println("Starting new game...");
                System.out.println();
            }
        }
    }

    private void saveSetup(Player player, List<Item> items, Level level) {
        lastPlayerClass = player.getClass().getSimpleName();
        lastPlayerName = player.getName();
        lastLevelNo = level.getNumber();
        lastItemNames = new ArrayList<>();
        for (Item item : items) {
            lastItemNames.add(item.getName());
        }
    }

    private Player buildPlayer(String playerClass, String name) {
        if ("Warrior".equals(playerClass)) return new Warrior(name);
        return new Wizard(name);
    }

    private List<Item> buildItems(List<String> names) {
        List<Item> items = new ArrayList<>();
        for (String name : names) {
            switch (name) {
                case "Potion":     items.add(new Potion()); break;
                case "Power Stone": items.add(new PowerStone()); break;
                case "Smoke Bomb": items.add(new SmokeBomb()); break;
            }
        }
        return items;
    }
}
