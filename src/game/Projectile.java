package game;
import tools.Vector;

public class Projectile {
    /**
     * The position of the projectile
     */
    private Vector position;

    /**
     * The velocity of the projectile
     */
    private Vector velocity;

    /**
     * The lifetime of the projectile
     */
    private double lifetime;

    /**
     * Initializes a projectile which has initially 10 like lifetime
     */
    public Projectile(Vector position, Vector velocity) {
        this.position = position;
        this.velocity = velocity;
        this.lifetime = 10;
    }

    /**
     * @return the position the position of the projectile
     */
    public Vector getPosition() {
        return position;
    }

    /**
     *  This method simulates the effects of a delay <em>dt</em> over the projectile.
     *   For good accuracy this delay should be kept small.
     *
     *  @param dt the time delay to simulate.
     */
    public void update(double dt) {
        lifetime = lifetime - dt;
        position = position.add(velocity.multiply(dt));
    }

    /**
     * @return whether the lifetime has not expired
     */
    public boolean isAlive() {
        return lifetime > 0;
    }

    public boolean Hits(Asteroid asteroid) {
        return asteroid.contains(this.position);
    }



}
