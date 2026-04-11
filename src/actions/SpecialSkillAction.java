package actions;

import combatants.Combatant;
import combatants.Player;
import engine.BattleContext;

import java.util.List;

public class SpecialSkillAction implements Action {
    private final Player player;
    private final List<Combatant> preSelectedTargets;

    public SpecialSkillAction(Player player, List<Combatant> preSelectedTargets) {
        this.player = player;
        this.preSelectedTargets = preSelectedTargets;
    }

    @Override
    public void execute(Combatant actor, List<Combatant> targets, BattleContext ctx) {
        player.executeSpecialSkill(preSelectedTargets, ctx);
        player.setSpecialSkillCooldown(3);
    }

    @Override
    public String getLabel() {
        return "SpecialSkill";
    }
}
