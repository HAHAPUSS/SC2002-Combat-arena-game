package combatants;

import effects.StunEffect;
import engine.BattleContext;
import java.util.List;

public class Warrior extends Player {

    public Warrior(String name) {
        super(name, 260, 40, 20, 30);
    }

    @Override
    public void executeSpecialSkill(List<Combatant> targets, BattleContext ctx) {
        if (targets == null || targets.isEmpty()) {
            System.out.println(name + " -> Shield Bash: No target selected!");
            return;
        }
        Combatant target = targets.get(0);
        int atk = this.getAttack();
        int def = target.getEffectiveDefense();
        int damage = Math.max(0, atk - def);
        int hpBefore = target.getCurrentHp();
        target.takeDamage(damage);
        int hpAfter = target.getCurrentHp();

        // Apply stun (overwrite any existing stun)
        target.addEffect(new StunEffect(2));

        System.out.printf("%s -> Shield Bash -> %s: HP: %d -> %d (dmg: %d-%d=%d) | %s STUNNED (2 turns)%n",
                name, target.getName(), hpBefore, hpAfter, atk, def, damage, target.getName());
    }

    @Override
    public String getSpecialSkillName() {
        return "Shield Bash";
    }

    @Override
    public boolean requiresTargetForSpecialSkill() {
        return true;
    }

    @Override
    public String getDescription() {
        return String.format("Warrior [HP:%d ATK:%d DEF:%d SPD:%d | Special: Shield Bash]",
                maxHp, attack, defense, speed);
    }
}
