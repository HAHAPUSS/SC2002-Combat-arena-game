package actions;

import combatants.Combatant;
import combatants.Player;
import engine.BattleContext;

import java.util.List;

public class ItemAction implements Action {
    private final Player player;
    private final int itemIndex;
    private final List<Combatant> preSelectedTargets;

    public ItemAction(Player player, int itemIndex, List<Combatant> preSelectedTargets) {
        this.player = player;
        this.itemIndex = itemIndex;
        this.preSelectedTargets = preSelectedTargets;
    }

    @Override
    public void execute(Combatant actor, List<Combatant> targets, BattleContext ctx) {
        player.useItem(itemIndex, preSelectedTargets, ctx);
    }

    @Override
    public String getLabel() {
        return "UseItem";
    }
}
