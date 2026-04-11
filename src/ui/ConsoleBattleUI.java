package ui;

import actions.Action;
import actions.BasicAttack;
import actions.DefendAction;
import actions.ItemAction;
import actions.SpecialSkillAction;
import combatants.Combatant;
import combatants.Enemy;
import combatants.Player;
import effects.StatusEffect;
import engine.BattleContext;
import items.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// SRP: handles battle display and prompts only
public class ConsoleBattleUI implements BattleUI {
    private final Scanner scanner;

    public ConsoleBattleUI(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void displayBattleStart() {
        System.out.println("============================================================");
        System.out.println("                  BATTLE BEGINS!");
        System.out.println("============================================================");
    }

    @Override
    public void displayRoundHeader(int round) {
        System.out.println();
        System.out.println("============================================================");
        System.out.printf("                     ROUND %d%n", round);
        System.out.println("============================================================");
    }

    @Override
    public Action promptPlayerAction(Player player, BattleContext ctx) {
        List<? extends Combatant> aliveEnemies = ctx.getAliveEnemies();

        System.out.println();
        System.out.println("--- " + player.getName() + "'s Turn ---");
        System.out.printf("[HP: %d/%d]", player.getCurrentHp(), player.getMaxHp());

        if (!player.getActiveEffects().isEmpty()) {
            System.out.print(" [Effects: ");
            List<String> names = new ArrayList<>();
            for (StatusEffect e : player.getActiveEffects()) names.add(e.getName());
            System.out.print(String.join(", ", names));
            System.out.print("]");
        }
        System.out.println();

        System.out.println("Choose an action:");
        System.out.println("  1. Basic Attack");
        System.out.println("  2. Defend");

        if (player.hasItems()) {
            System.out.print("  3. Use Item [");
            List<String> itemNames = new ArrayList<>();
            for (Item it : player.getItems()) itemNames.add(it.getName());
            System.out.print(String.join(", ", itemNames));
            System.out.println("]");
        } else {
            System.out.println("  3. Use Item [No items remaining]");
        }

        if (player.canUseSpecialSkill()) {
            System.out.println("  4. Special Skill [" + player.getSpecialSkillName() + " - READY]");
        } else {
            System.out.println("  4. Special Skill [" + player.getSpecialSkillName()
                    + " - Cooldown: " + player.getSpecialSkillCooldown() + " round(s)]");
        }

        while (true) {
            int choice = readIntInRange("Enter choice (1-4): ", 1, 4);

            if (choice == 1) {
                Combatant target = promptTargetSelection(player, aliveEnemies);
                return new BasicAttack(target);

            } else if (choice == 2) {
                return new DefendAction();

            } else if (choice == 3) {
                if (!player.hasItems()) {
                    System.out.println("No items available! Choose another action.");
                    continue;
                }
                List<Item> items = player.getItems();
                System.out.println("Select item:");
                for (int i = 0; i < items.size(); i++) {
                    System.out.printf("  %d. %s - %s%n", i + 1,
                            items.get(i).getName(), items.get(i).getDescription());
                }
                int itemChoice = readIntInRange("Enter item number: ", 1, items.size()) - 1;
                Item selectedItem = items.get(itemChoice);

                List<Combatant> itemTargets = new ArrayList<>();
                if (selectedItem.requiresTarget(player)) {
                    Combatant t = promptTargetSelection(player, aliveEnemies);
                    if (t != null) itemTargets.add(t);
                }

                return new ItemAction(player, itemChoice, itemTargets);

            } else if (choice == 4) {
                if (!player.canUseSpecialSkill()) {
                    System.out.println("Special Skill is on cooldown! Choose another action.");
                    continue;
                }
                List<Combatant> skillTargets = new ArrayList<>();
                if (player.requiresTargetForSpecialSkill()) {
                    Combatant t = promptTargetSelection(player, aliveEnemies);
                    if (t != null) skillTargets.add(t);
                }
                return new SpecialSkillAction(player, skillTargets);
            }
        }
    }

    @Override
    public Combatant promptTargetSelection(Player player, List<? extends Combatant> targets) {
        if (targets.isEmpty()) {
            System.out.println("No targets available!");
            return null;
        }
        if (targets.size() == 1) {
            System.out.println("Auto-targeting: " + targets.get(0).getName());
            return targets.get(0);
        }
        System.out.println("Select target:");
        for (int i = 0; i < targets.size(); i++) {
            Combatant c = targets.get(i);
            String stunned = c.isStunned() ? " [STUNNED]" : "";
            System.out.printf("  %d. %s (HP: %d/%d)%s%n",
                    i + 1, c.getName(), c.getCurrentHp(), c.getMaxHp(), stunned);
        }
        int choice = readIntInRange("Enter target number: ", 1, targets.size()) - 1;
        return targets.get(choice);
    }

    @Override
    public void displayEndOfRound(BattleContext ctx, int round) {
        Player player = ctx.getPlayer();
        System.out.println();
        System.out.println("--- End of Round " + round + " Status ---");
        System.out.printf("Player: %s | HP: %d/%d%n",
                player.getName(), player.getCurrentHp(), player.getMaxHp());

        if (!player.getActiveEffects().isEmpty()) {
            System.out.print("  Active Effects: ");
            List<String> names = new ArrayList<>();
            for (StatusEffect e : player.getActiveEffects()) names.add(e.getName());
            System.out.println(String.join(", ", names));
        }

        System.out.print("  Items: ");
        if (player.hasItems()) {
            List<String> itemNames = new ArrayList<>();
            for (Item it : player.getItems()) itemNames.add(it.getName());
            System.out.println(String.join(", ", itemNames));
        } else {
            System.out.println("None");
        }

        if (player.getSpecialSkillCooldown() == 0) {
            System.out.println("  Special Skill [" + player.getSpecialSkillName() + "]: READY");
        } else {
            System.out.println("  Special Skill [" + player.getSpecialSkillName()
                    + "]: Cooldown " + player.getSpecialSkillCooldown() + " round(s)");
        }

        System.out.println("Enemies:");
        for (Enemy e : ctx.getActiveEnemies()) {
            if (e.isAlive()) {
                String stunned = e.isStunned() ? " [STUNNED]" : "";
                System.out.printf("  %s: HP %d/%d%s%n",
                        e.getName(), e.getCurrentHp(), e.getMaxHp(), stunned);
            } else {
                System.out.printf("  %s: X (Defeated)%n", e.getName());
            }
        }
        System.out.println("------------------------------------------------------------");
    }

    @Override
    public void displayTurnOrder(List<Combatant> turnOrder) {
        System.out.print("Turn Order: ");
        List<String> entries = new ArrayList<>();
        for (Combatant c : turnOrder) {
            entries.add(c.getName() + " (SPD:" + c.getSpeed() + ")");
        }
        System.out.println(String.join(" -> ", entries));
    }

    @Override
    public void displayBackupSpawn(List<Enemy> backup) {
        System.out.println();
        System.out.println("!!! BACKUP ENEMIES HAVE ARRIVED !!!");
        for (Enemy e : backup) {
            System.out.printf("  + %s appeared! [HP:%d ATK:%d DEF:%d SPD:%d]%n",
                    e.getName(), e.getMaxHp(), e.getAttack(), e.getDefense(), e.getSpeed());
        }
        System.out.println();
    }

    @Override
    public void displayGameResult(BattleContext ctx, boolean playerWon) {
        Player player = ctx.getPlayer();
        int rounds = ctx.getCurrentRound();

        System.out.println();
        System.out.println("============================================================");
        if (playerWon) {
            System.out.println("                    *** VICTORY! ***");
            System.out.println("============================================================");
            System.out.printf("Congratulations, %s! You defeated all enemies!%n", player.getName());
            System.out.printf("Remaining HP: %d/%d | Total Rounds: %d%n",
                    player.getCurrentHp(), player.getMaxHp(), rounds);
            // Remaining items
            System.out.print("Remaining Items: ");
            if (player.hasItems()) {
                List<String> itemNames = new ArrayList<>();
                for (Item it : player.getItems()) itemNames.add(it.getName() + ": 1");
                System.out.println(String.join(" | ", itemNames));
            } else {
                System.out.println("None");
            }
        } else {
            System.out.println("                    *** DEFEAT! ***");
            System.out.println("============================================================");
            System.out.printf("Defeated. Don't give up, try again!%n");
            long enemiesRemaining = ctx.getAliveEnemies().size();
            System.out.printf("Enemies remaining: %d | Total Rounds Survived: %d%n",
                    enemiesRemaining, rounds);
        }
        System.out.println("============================================================");
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public boolean promptContinue(int continuesRemaining) {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("                  *** CONTINUE? ***");
        System.out.println("============================================================");
        System.out.printf("  Continues remaining: %d/%d%n", continuesRemaining, BattleContext.MAX_CONTINUES);
        System.out.println("  Your HP will be fully restored. Enemy HP stays as-is.");
        System.out.println();
        System.out.println("  1. Continue");
        System.out.println("  2. Give Up");
        System.out.println("============================================================");
        int choice = readIntInRange("Enter choice (1-2): ", 1, 2);
        return choice == 1;
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
}
