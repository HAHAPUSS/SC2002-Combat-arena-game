package engine;

import combatants.Combatant;
import combatants.Enemy;
import strategies.TurnOrderStrategy;
import ui.BattleUI;

import java.util.ArrayList;
import java.util.List;


// DIP: depends only on abstractions (BattleUI, TurnOrderStrategy, BattleContext)
public class BattleEngine {
    private final BattleContext ctx;
    private final TurnOrderStrategy turnOrderStrategy;
    private final BattleUI ui;

    public BattleEngine(BattleContext ctx, TurnOrderStrategy turnOrderStrategy, BattleUI ui) {
        this.ctx = ctx;
        this.turnOrderStrategy = turnOrderStrategy;
        this.ui = ui;
    }

    // Returns true if player won, false if defeated
    public boolean run() {
        ui.displayBattleStart();

        while (!ctx.isGameOver()) {
            int round = ctx.getCurrentRound() + 1;
            ctx.setCurrentRound(round);

            ui.displayRoundHeader(round);

            List<Combatant> turnOrder = turnOrderStrategy.determineTurnOrder(ctx.getAllCombatants());
            ui.displayTurnOrder(turnOrder);

            ctx.getPlayer().setTookTurnThisRound(false);

            for (Combatant combatant : turnOrder) {

                if (!combatant.isAlive()) {
                    if (combatant.isStunned()) {
                        ui.displayMessage(String.format("%s -> ELIMINATED: Skipped | Stun expires",
                                combatant.getName()));
                        combatant.decrementStun();
                    }
                    continue;
                }

                if (combatant.isStunned()) {
                    combatant.decrementStun();
                    if (!combatant.isStunned()) {
                        ui.displayMessage(String.format("%s -> STUNNED: Turn skipped | Stun expires",
                                combatant.getName()));
                    } else {
                        ui.displayMessage(String.format("%s -> STUNNED: Turn skipped",
                                combatant.getName()));
                    }
                    continue;
                }

                // LSP: no instanceof — each Combatant knows how to take its own turn
                combatant.takeTurn(ctx, ui);

                if (!ctx.getPlayer().isAlive()) {
                    if (ctx.hasContinues() && ui.promptContinue(ctx.getContinuesRemaining())) {
                        ctx.useContinue();
                        ctx.getPlayer().revive();
                        ui.displayMessage(String.format("%s revived! HP fully restored. (%d continue(s) remaining)",
                                ctx.getPlayer().getName(), ctx.getContinuesRemaining()));
                    } else {
                        break;
                    }
                }

                // Trigger backup spawn when last initial enemy dies
                if (ctx.allInitialEnemiesDefeated() && !ctx.isBackupSpawned()
                        && !ctx.getBackupEnemies().isEmpty()) {
                    List<Enemy> backup = new ArrayList<>(ctx.getBackupEnemies());
                    ctx.triggerBackupSpawn();
                    ui.displayBackupSpawn(backup);
                }
            }

            // Tick round-based effects after all turns
            ctx.getPlayer().tickRoundEffects();
            for (Enemy e : ctx.getActiveEnemies()) {
                e.tickRoundEffects();
            }

            ui.displayEndOfRound(ctx, round);

            ctx.getPlayer().decrementCooldownIfActed();
        }

        boolean playerWon = ctx.getPlayer().isAlive();
        ui.displayGameResult(ctx, playerWon);
        return playerWon;
    }
}
