package items;

import combatants.Combatant;
import combatants.Player;
import engine.BattleContext;

import java.util.List;

public class Potion implements Item {
    private boolean used = false;

    @Override
    public void use(Player player, List<Combatant> targets, BattleContext ctx) {
        int hpBefore = player.getCurrentHp();
        player.heal(100);
        int hpAfter = player.getCurrentHp();
        used = true;
        System.out.printf("%s -> Potion: HP: %d -> %d (+%d healed)%n",
                player.getName(), hpBefore, hpAfter, hpAfter - hpBefore);
    }

    @Override
    public String getName() { return "Potion"; }

    @Override
    public boolean isUsed() { return used; }

    @Override
    public String getDescription() { return "Heal 100 HP (capped at max HP)"; }
}
