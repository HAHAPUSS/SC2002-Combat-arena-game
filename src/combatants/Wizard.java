package combatants;

import engine.BattleContext;

import java.util.List;

public class Wizard extends Player {

    public Wizard(String name) {
        super(name, 200, 50, 10, 20);
    }

    @Override
    public void executeSpecialSkill(List<Combatant> targets, BattleContext ctx) {
        // targets should be all alive enemies
        List<Enemy> aliveEnemies = ctx.getAliveEnemies();
        if (aliveEnemies.isEmpty()) {
            System.out.printf("%s -> Arcane Blast: No enemies to blast!%n", name);
            return;
        }

        System.out.printf("%s -> Arcane Blast -> All enemies!%n", name);
        int killCount = 0;

        for (Combatant enemy : aliveEnemies) {
            int atk = this.getAttack(); // use current ATK (may have been boosted by previous kills)
            int def = enemy.getEffectiveDefense();
            int damage = Math.max(0, atk - def);
            int hpBefore = enemy.getCurrentHp();
            enemy.takeDamage(damage);
            int hpAfter = enemy.getCurrentHp();

            System.out.printf("  -> %s: HP: %d -> %d (dmg: %d-%d=%d)%n",
                    enemy.getName(), hpBefore, hpAfter, atk, def, damage);

            if (!enemy.isAlive()) {
                killCount++;
                // Each kill adds +10 to Wizard's ATK permanently
                this.attack += 10;
                System.out.printf("  [%s defeated! Wizard ATK +10 -> now %d]%n",
                        enemy.getName(), this.attack);
            }
        }

        if (killCount == 0) {
            System.out.printf("%s -> Arcane Blast: No enemies defeated.%n", name);
        } else {
            System.out.printf("%s -> Arcane Blast: %d enem%s defeated! ATK boosted to %d%n",
                    name, killCount, killCount == 1 ? "y" : "ies", this.attack);
        }
    }

    @Override
    public String getSpecialSkillName() {
        return "Arcane Blast";
    }

    @Override
    public boolean requiresTargetForSpecialSkill() {
        return false;
    }

    @Override
    public String getDescription() {
        return String.format("Wizard [HP:%d ATK:%d DEF:%d SPD:%d | Special: Arcane Blast]",
                maxHp, attack, defense, speed);
    }
}
