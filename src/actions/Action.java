package actions;

import combatants.Combatant;
import engine.BattleContext;

import java.util.List;

public interface Action {
    void execute(Combatant actor, List<Combatant> targets, BattleContext ctx);
    String getLabel();
}
