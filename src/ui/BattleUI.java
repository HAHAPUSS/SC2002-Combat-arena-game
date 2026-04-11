package ui;

import actions.Action;
import combatants.Combatant;
import combatants.Enemy;
import combatants.Player;
import engine.BattleContext;

import java.util.List;

// ISP / DIP / SRP: focused interface for battle display and prompts only
public interface BattleUI {
    void displayBattleStart();
    void displayRoundHeader(int round);
    Action promptPlayerAction(Player player, BattleContext ctx);
    Combatant promptTargetSelection(Player player, List<? extends Combatant> targets);
    void displayEndOfRound(BattleContext ctx, int round);
    void displayTurnOrder(List<Combatant> turnOrder);
    void displayBackupSpawn(List<Enemy> backup);
    void displayGameResult(BattleContext ctx, boolean playerWon);
    void displayMessage(String message);
    boolean promptContinue(int continuesRemaining);
}
