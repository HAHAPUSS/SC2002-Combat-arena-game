package items;

import combatants.Combatant;
import combatants.Player;
import engine.BattleContext;

import java.util.List;

public interface Item {
    void use(Player player, List<Combatant> targets, BattleContext ctx);
    String getName();
    boolean isUsed();
    String getDescription();
    default boolean requiresTarget(Player player) { return false; }
}
