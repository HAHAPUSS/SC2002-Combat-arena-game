package combatants;

public class Wolf extends Enemy {
    public Wolf(String name) {
        super(name, 40, 45, 5, 35);
    }

    @Override
    public String getDescription() {
        return String.format("%s [Wolf | HP:%d ATK:%d DEF:%d SPD:%d]",
                name, maxHp, attack, defense, speed);
    }
}
