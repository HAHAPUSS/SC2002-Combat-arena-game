package actions;

import combatants.Combatant;
import effects.DefendEffect;
import engine.BattleContext;

import java.util.List;

public class DefendAction implements Action {

    @Override
    public void execute(Combatant actor, List<Combatant> targets, BattleContext ctx) {
        // Remove any existing DefendEffect before adding a new one (refresh)
        actor.getActiveEffects().removeIf(e -> e instanceof DefendEffect);
        actor.addEffect(new DefendEffect());
        System.out.printf("%s -> Defend: +10 DEF for 2 rounds (Effective DEF: %d)%n",
                actor.getName(), actor.getEffectiveDefense());
    }

    @Override
    public String getLabel() {
        return "Defend";
    }
}
