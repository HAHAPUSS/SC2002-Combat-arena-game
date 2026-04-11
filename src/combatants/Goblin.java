package combatants;

public class Goblin extends Enemy {
    public Goblin(String name) {
        super(name, 55, 35, 15, 25);
    }

    @Override
    public String getDescription() {
        return String.format("%s [Goblin | HP:%d ATK:%d DEF:%d SPD:%d]",
                name, maxHp, attack, defense, speed);
    }
}
