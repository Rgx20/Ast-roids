package game;


import tools.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Space contains all the information determining the current state of
 * the game, and methods implementing how the game state changes, and how
 * the game ends (basically the rules of the game).
 */
public class Space {

  public static final double SPACE_WIDTH = 800;
  public static final double SPACE_HEIGHT = 800;

  public static final int INITIAL_ASTEROID_COUNT = 10;
  public static final double INITIAL_ASTEROID_SIZE = 2;
  public static final double LIMIT_ASTEROID_SIZE = 1.5;
  public static final double ASTEROID_FRAGMENT_COUNT = 3;
  public static final double RATIO = 0.75;


  /**
   * We don't want asteroids to spawn on the spaceship. This parameter
   * controls how close an asteroid can be from the spaceship initially,
   * in pixels.
   */
  private static final double STARTING_SECURITY_DISTANCE = 80;

  /**
   * An object able to create random items, like asteroids or positions.
   */
  public static final RandomGenerator generator = new RandomGenerator();


  private Spaceship spaceship;
  private List<Asteroid> asteroids;
  private Score score;
  private List<Projectile> projectiles;

  public Spaceship getSpaceship() {
    return spaceship;
  }

  public List<Asteroid> getAsteroids() {
    return asteroids;
  }

  public Score getScore() {
    return score;
  }

  public List<Projectile> getProjectiles() { return projectiles; }

  public void addProjectile(Projectile projectile) { projectiles.add(projectile); }

  public Space() {
    score = new Score();
    spaceship = new Spaceship();
    asteroids = new ArrayList<>(INITIAL_ASTEROID_COUNT);
    for (int i = 0; i < INITIAL_ASTEROID_COUNT; i++) {
      asteroids.add(generateInitialAsteroid());
    }
    projectiles = new ArrayList<>();
  }

  public void updateProjectiles(double dt) {
    for (Projectile projectile : projectiles) {
      projectile.update(dt);
    }
  }


  public void update(double dt) {
    processProjectiles(dt);
    updateScore(dt);
    updateAsteroids(dt);
    if (hasCollision())
      spaceship.invulnerate(3);
  }

  private void updateAsteroids(double dt) {
    for (Asteroid asteroid : asteroids) {
      asteroid.update(dt);
    }
    spaceship.update(dt);
  }

  private void updateScore(double dt){
    score.update(dt);
  }

  public boolean isGameOver() {
    return  spaceship.getLife() == 0;
  }


  /**
   * Generates a random asteroid with standard parameters, whose distance
   * to the spaceship is large enough.
   *
   * @return a random asteroid
   */
  public Asteroid generateInitialAsteroid() {
    Asteroid asteroid = generator.asteroid(INITIAL_ASTEROID_SIZE);
    double distanceFromSpaceship =
      asteroid.getPosition().distanceTo(spaceship.getPosition());
    if (distanceFromSpaceship < STARTING_SECURITY_DISTANCE) {
      return generateInitialAsteroid();
    }
    return asteroid;
  }


  /**
   * Because the space is toric (things leaving the window on one side
   * reappear on the other side), we need to compute the positions of items
   * leaving the screen to get them back on the other side. This method takes
   * an arbitrary vector and maps it to valid toric coordinates.
   *
   * @param position any position
   * @return the same position with canonical toric coordinates
   */
  public static Vector toricRemap(Vector position) {
    return new Vector(
      clamp(position.getX(), SPACE_WIDTH),
      clamp(position.getY(), SPACE_HEIGHT)
    );
  }


  /**
   * Used by remapPosition to compute coordinates between 0 and a bound.
   *
   * @param value the coordinate to recompute
   * @param bound the maximum value allowed for this coordinate
   * @return the corrected coordinate
   */
  private static double clamp(double value, double bound) {
    return value - Math.floor( value / bound) * bound;
  }

  /**
   * @return whether the spaceship collides at least an asteroid.
   */
  public boolean hasCollision() {
    for(Asteroid asteroid : this.getAsteroids()) {
      if(this.getSpaceship().collides(asteroid))
        return true;
    }
    return false;
  }


  private List<Projectile> getDeadProjectiles() {
    List<Projectile> deadProjectiles = new ArrayList<>();
    for (Projectile projectile : getProjectiles()) {
      if(!projectile.isAlive()) {
        deadProjectiles.add(projectile);
      }
    }
    return deadProjectiles;
  }

  private  void removeDeadProjectiles() {
    for (Projectile deadProjectile : getDeadProjectiles()) {
      getProjectiles().remove(deadProjectile);
    }
  }

  private void findProjectilesHits(Set<Projectile> hittingProjectiles, Set<Asteroid> hittedAsteroid) {
    for (Projectile projectile : getProjectiles()) {
      for (Asteroid asteroid : getAsteroids() ) {
        if (projectile.Hits(asteroid)){
          getScore().addMultiplier(score.getMultiplierModifierValue());
          getScore().notifyAsteroidHit(score.getPoints());
          getScore().putMultiplierTimeBackTo(score.getRebootTime());
          hittingProjectiles.add(projectile);
          hittedAsteroid.add(asteroid);
        }
      }
    }
  }

  private void remove(Set<Projectile> hittingProjectiles) {
    for (Projectile hittingProjectile : hittingProjectiles) {
      getProjectiles().remove(hittingProjectile);
    }
  }


  private void processProjectiles(double dt) {
    updateProjectiles(dt);
    Set<Projectile> hittingProjectiles = new HashSet<>();
    Set<Asteroid> hittedAsteroids = new HashSet<>();
    findProjectilesHits(hittingProjectiles, hittedAsteroids);
    remove(hittingProjectiles);
    fragment(hittedAsteroids);
    removeDeadProjectiles();
  }

  private void fragment(Set<Asteroid> hittedAsteroids) {
    for (Asteroid hittedAsteroid : hittedAsteroids) {
      getAsteroids().remove(hittedAsteroid);
    }
    for (Asteroid hittedAsteroid : hittedAsteroids) {
      for (Asteroid fragment : hittedAsteroid.fragments()) {
        getAsteroids().add(fragment);
      }
    }
  }


}
