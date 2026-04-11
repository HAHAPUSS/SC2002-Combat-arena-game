package effects;

public class StunEffect implements StatusEffect {
    private int turnsRemaining;

    public StunEffect(int turns) {
        this.turnsRemaining = turns;
    }
    
    //easy to change the number of stunned rounds, because it is create by warrior skill so can have flexi constructor
    public StunEffect() {
        this(2);
    }

    @Override
    public void tick() {
        turnsRemaining--;
    }

    @Override
    public boolean isExpired() {
        return turnsRemaining <= 0;
    }

    @Override
    public String getName() {
        return "Stun";
    }

    public int getTurnsRemaining() {
        return turnsRemaining;
    }

    @Override
    public boolean causeStun() { return true; }
}
