package effects;

public class DefendEffect implements StatusEffect {
    private int roundsRemaining;

    public DefendEffect() {
        this.roundsRemaining = 2;
    }

    @Override
    public void tick() {
        roundsRemaining--;
    }

    @Override
    public boolean isExpired() {
        return roundsRemaining <= 0;
    }

    @Override
    public String getName() {
        return "Defend";
    }

    @Override
    public int getBonusDefense() {
        return 10;
    }

    public int getRoundsRemaining() {
        return roundsRemaining;
    }
}
