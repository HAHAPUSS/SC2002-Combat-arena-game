package effects;

public class SmokeBombEffect implements StatusEffect {
    private int roundsRemaining;

    public SmokeBombEffect() {
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
        return "SmokeBomb";
    }

    public int getRoundsRemaining() {
        return roundsRemaining;
    }

    @Override
    public boolean immune() { return true; }
}
