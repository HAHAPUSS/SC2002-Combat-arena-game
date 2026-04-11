package actions;

import combatants.Combatant;
import combatants.Enemy;
import engine.BattleContext;

import java.util.ArrayList;
import java.util.List;

public class BasicAttack implements Action {
    private final Combatant preSelectedTarget;

    public BasicAttack() {
        this.preSelectedTarget = null;
    }

    public BasicAttack(Combatant target) {
        this.preSelectedTarget = target;
    }

    @Override
    public void execute(Combatant actor, List<Combatant> targets, BattleContext ctx) {
        // Use pre-selected target if provided (player-initiated), else use targets list (enemy-initiated)
        List<Combatant> resolved = targets;
        if (preSelectedTarget != null) {
            resolved = new ArrayList<>();
            resolved.add(preSelectedTarget);
        }
        if (resolved == null || resolved.isEmpty()) {
            System.out.println(actor.getName() + " has no target!");
            return;
        }
        Combatant target = resolved.get(0);
        int atk = actor.getAttack();
        int def = target.getEffectiveDefense();

        // If actor is an enemy and target (player) has smoke bomb protection, deal 0
        if (actor instanceof Enemy && target.hasSmokeBombProtection()) {
            int hpBefore = target.getCurrentHp();
            System.out.printf("%s -> BasicAttack -> %s: HP: %d -> %d (dmg: BLOCKED by Smoke Bomb)%n",
                    actor.getName(), target.getName(), hpBefore, hpBefore);
        } else {
            int damage = Math.max(0, atk - def);
            int hpBefore = target.getCurrentHp();
            target.takeDamage(damage);
            int hpAfter = target.getCurrentHp();
            System.out.printf("%s -> BasicAttack -> %s: HP: %d -> %d (dmg: %d-%d=%d)%n",
                    actor.getName(), target.getName(), hpBefore, hpAfter, atk, def, damage);
        }
    }

    @Override
    public String getLabel() {
        return "BasicAttack";
    }
}
