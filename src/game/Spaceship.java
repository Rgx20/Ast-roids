package game;

import tools.Vector;

import java.util.List;

/**
 * Models a spaceship controlled by a player.
 */
public class Spaceship {

  private static final double MAIN_ENGINE_POWER = 45;
  private static final double ANGULAR_VELOCITY = 360;
  private static final double RECOIL_ENGINE_POWER = -45;
  private static final double TANK_CAPACITY = 5;
  private static final double AMOUNT_OF_FUEL_RECHARGED_PER_SECOND = 0.2;
  private static final double CONSUMPTION_OF_MAIN_ENGINE = 1;
  private static final double CONSUMPTION_OF_A_LATERAL_ENGINE = 0.3;
  private static final double CONSUMPTION_OF_RECOIL_ENGINE = 0.5;


  /**
   * The position of the center of the spaceship
   */
  private Vector position;

  /**
   * The velocity of the center of the spaceship
   */
  private Vector velocity;

  /**
   * Indicates the quantity of fuel in the tank
   */
  private double fuel;

  /**
   * The forward direction for the spaceship, encoding the rotation
   * from horizontal of its image and the direction of acceleration.
   */
  private Vector direction = new Vector(1, 0);

  /**
   * Controls if the main engine, with forward acceleration, is powered on.
   */
  private boolean isMainEngineOn = false;

  /**
   * Controls if the recoil engine is powered on.
   */
  private boolean isRecoilEngineOn = false;

  /**
   * Controls if the left lateral engine is powered on.
   */
  private boolean isLeftLateralEngineOn = false;

  /**
   *  Controls if the right lateral engine is powered on.
   */
  private boolean isRightLateralEngineOn = false;

  /**
   * Represents the remaining invulnerability time
   */
  private double invulnerabilityTime = 5;

  /**
   * @return if the spaceship is invulnerable
   */
  public boolean isInvulnerable() {
    return (invulnerabilityTime > 0);
  }

  /**
   * Makes the spaceship invulnerable
   */
  public void invulnerate(double dt) {
    if (isInvulnerable())
      invulnerabilityTime = Math.max(invulnerabilityTime,dt);
    else
      invulnerabilityTime = dt;
  }

  /**
   * Controls whether the spaceship collides an asteroid
   */
  private boolean collides = false;

  /**
   * The number of life of the spaceship
   */
  private int life;

  /**
   * @return the life of the spaceship
   */
  public int getLife() { return life; }

  /**
   * @return the position of the spaceship
   */
  public Vector getPosition() {
    return position;
  }

  /**
   * @return the angle of the spaceship in degree, where 0 is facing right.
   */
  public double getDirectionAngle() {
    return direction.angle();
  }

  /**
   * @return the acceleration of the spaceship
   */
  public Vector getAcceleration() {
    return isMainEngineOn() ? direction.multiply(MAIN_ENGINE_POWER) : (isRecoilEngineOn() ? direction.multiply(RECOIL_ENGINE_POWER) : direction.multiply(0));
  }

  /**
   * @return the current consumption of fuel of the spaceship.
   */
  public double getCurrentConsumption() {
    return (isMainEngineOn() ? 1 : 0) * CONSUMPTION_OF_MAIN_ENGINE +
            (isLeftLateralEngineOn() || isRightLateralEngineOn() ? 1 : 0) * CONSUMPTION_OF_A_LATERAL_ENGINE +
            (isRecoilEngineOn() ? 1 : 0) * CONSUMPTION_OF_RECOIL_ENGINE - AMOUNT_OF_FUEL_RECHARGED_PER_SECOND;
  }

  /**
   * @return the remaining time of the current fuel in the tank
   *
   * @param dt the time delay to simulate
   */
  public double getAutonomy(double dt) {
    return !(isMainEngineOn() || isRecoilEngineOn() || isLeftLateralEngineOn() || isRightLateralEngineOn()) ? dt : Math.min(fuel/getCurrentConsumption(), dt);
  }

  /**
   * @return the percentage of fuel in the tank
   */
  public double getFuelPercentage() { return fuel * 100 / TANK_CAPACITY; }

  /**
   * @return whether the main engine is on (forward acceleration).
   */
  public boolean isMainEngineOn() {
    return isMainEngineOn;
  }

  /**
   * @return whether the recoil engine is on.
   */
  public boolean isRecoilEngineOn() { return isRecoilEngineOn; }


  /**
   * @return if the left lateral engine is on
   */
  public boolean isLeftLateralEngineOn() { return isLeftLateralEngineOn; }

  /**
   * @return if the right lateral engine is on
   */
  public boolean isRightLateralEngineOn() { return isRightLateralEngineOn; }

  /**
   * Initially the spaceship will be positioned at the center of space.
   */
  public Spaceship() {
    this.position =
      new Vector(
        Space.SPACE_HEIGHT / 2,
        Space.SPACE_WIDTH / 2
      );
    this.velocity = new Vector(0,0);
    this.fuel = TANK_CAPACITY;
    this.life = 3;
  }


  /**
   * The spaceship is a moving object. Every now and then, its position
   * must be updated, as well as other parameters evolving with time. This
   * method simulates the effects of a delay <em>dt</em> over the spaceship.
   * For good accuracy this delay should be kept small.
   *
   * @param dt the time delay to simulate.
   */
  public void update(double dt) {
      updateVelocity(dt);
      position = position.add(velocity.multiply(dt));
      position = Space.toricRemap(position);
      updateDirection(dt);
      if (getCurrentConsumption() == - 0.2 && (fuel < TANK_CAPACITY && fuel >= 0) || getCurrentConsumption() != -0.2)
        fuel = fuel - getCurrentConsumption()*getAutonomy(dt);
      if (isInvulnerable())
        invulnerabilityTime -= dt;
      if (collides) {
        life--;
      }

  }

  /**
   * Update the velocity of the center of the velocity
   *
   * @param dt the time delay to simulate
   */
  public void updateVelocity(double dt) {
    if(isMainEngineOn() || isRecoilEngineOn())
      velocity = velocity.add(getAcceleration().multiply(getAutonomy(dt)));
    else
      velocity = velocity.multiply(0);
  }

  /**
   * Update the direction of the spaceship
   *
   * @param dt the time delay to simulate
   */
  public void updateDirection(double dt) {
    if(isLeftLateralEngineOn())
      direction = direction.rotate(ANGULAR_VELOCITY * getAutonomy(dt));
    if(isRightLateralEngineOn())
      direction = direction.rotate(-ANGULAR_VELOCITY * getAutonomy(dt));
  }

  /**
   * Switches the main engine (powering forward acceleration) on.
   */
  public void startMainEngine() {
    if(fuel == 0)
      isMainEngineOn = false;
    else
      isMainEngineOn = true;
  }

  /**
   * Switches the main engine (powering forward acceleration) off.
   */
  public void stopMainEngine() {
    isMainEngineOn = false;
  }

  /**
   * Switches the recoil engine on.
   */
  public void startRecoilEngine() {
    if(fuel == 0)
      isRecoilEngineOn = false;
    else
      isRecoilEngineOn = true;
  }

  /**
   * Switches the recoil engine off.
   */
  public void stopRecoilEngine() { isRecoilEngineOn = false; }


  /**
   *  Switches the left lateral engine on (turn the spaceship counterclockwise).
   */
  public void startLeftLateralEngine() {
    if (fuel == 0)
      isLeftLateralEngineOn = false;
    else
      isLeftLateralEngineOn = true;
  }

  /**
   * Switches the left lateral engine off (stop the spaceship from turning counterclockwise).
   */
  public void stopLeftLateralEngine() { isLeftLateralEngineOn = false; }

  /**
   * Switches the right lateral engine on (turn the spaceship clockwise).
   */
  public void startRightLateralEngine() {
    if (fuel == 0)
      isRightLateralEngineOn = false;
    else
      isRightLateralEngineOn = true;
  }

  /**
   * Switches the right lateral engine off (stop the spaceship from turning clockwise).
   */
  public void stopRightLateralEngine() { isRightLateralEngineOn = false; }

  /**
   * A list of points on the boundary of the spaceship, used
   * to detect collision with other objects.
   */
  private static final List<Vector> contactPoints =
    List.of(
      new Vector(0,0),
      new Vector(27,0),
      new Vector(14.5,1.5),
      new Vector(2,3),
      new Vector(0,18),
      new Vector(-13,18),
      new Vector(-14,2),
      new Vector(-14,-2),
      new Vector(-13,-18),
      new Vector(0,-18),
      new Vector(2,-3),
      new Vector(14.5,-1.5)
    );

  public static List<Vector> getContactPoints() {
    return contactPoints;
  }

  /**
   * @param asteroid the asteroid with which we check the collision.
   * @return whether the spaceship collides the asteroid.
   */
  public boolean collides(Asteroid asteroid) {
    for(Vector point : getContactPoints()) {
      if(asteroid.contains(point.rotate(this.getDirectionAngle()).translate(this.getPosition())) && !isInvulnerable()) {
        collides = true;
        return true;
      }
    }
    collides = false;
    return false;

  }

  /**
   * @return the remaining invulnerability time.
   */
  public double getInvulnerabilityTime() {
    return invulnerabilityTime;
  }


  public Projectile fire() {
    return new Projectile(this.position.add(this.direction.multiply(30)), this.velocity.add(this.direction.multiply(100)));
  }

}
