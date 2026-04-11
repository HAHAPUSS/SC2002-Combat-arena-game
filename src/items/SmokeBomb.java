package items;

import combatants.Combatant;
import combatants.Player;
import effects.SmokeBombEffect;
import engine.BattleContext;

import java.util.List;

public class SmokeBomb implements Item {
    private boolean used = false;

    @Override
    public void use(Player player, List<Combatant> targets, BattleContext ctx) {
        player.getActiveEffects().removeIf(e -> e.immune());
        player.addEffect(new SmokeBombEffect());
        used = true;
        System.out.printf("%s -> Smoke Bomb: Enemy attacks deal 0 damage for 2 rounds!%n",
                player.getName());
    }

    @Override
    public String getName() { return "Smoke Bomb"; }

    @Override
    public boolean isUsed() { return used; }

    @Override
    public String getDescription() { return "Enemy attacks deal 0 damage for 2 rounds"; }
}
