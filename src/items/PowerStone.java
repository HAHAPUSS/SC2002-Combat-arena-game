package items;

import combatants.Combatant;
import combatants.Player;
import engine.BattleContext;

import java.util.List;

public class PowerStone implements Item {
    private boolean used = false;

    @Override
    public void use(Player player, List<Combatant> targets, BattleContext ctx) {
        System.out.printf("%s -> Power Stone: Triggering special skill effect!%n", player.getName());
        player.executeSpecialSkill(targets, ctx);
        used = true;
        System.out.println("(Cooldown unchanged by Power Stone)");
    }

    @Override
    public String getName() { return "Power Stone"; }

    @Override
    public boolean isUsed() { return used; }

    @Override
    public String getDescription() { return "Trigger special skill effect once (does NOT affect cooldown)"; }

    @Override
    public boolean requiresTarget(Player player) {
        return player.requiresTargetForSpecialSkill();
    }
}
