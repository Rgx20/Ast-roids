package game;

import tools.Polygon;
import tools.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Models an asteroid. An asteroid is a polygonal shape with velocity
 * and angular velocity. They have currently no acceleration so they travel
 * in straight lines. Their positions must be updated frequently using
 * the <em>update</em> method.
 */
public class  Asteroid {

  /**
   * The position of the center of the asteroid.
   */
  private Vector position;

  /**
   * The velocity of the center of the asteroid.
   */
  private Vector velocity;

  /**
   * A measure of the rotation of the asteroid since its creation.
   */
  private double angle;

  /**
   * The angle in degree by which the asteroid rotates each second.
   */
  private double angularVelocity;

  /**
   * The shape of the asteroid, the center of the asteroid is the
   * center of its shape.
   */
  private final Polygon shape;

  /**
   * The size of the asteroid (in an arbitrary but fixed unit)
   */
  private final double size;

  /**
   * @return the position of the center of the asteroid.
   */
  public Vector getPosition() {
    return position;
  }

  /**
   * @return the velocity of the center of the asteroid.
   */
  public Vector getVelocity() {
    return velocity;
  }

  /**
   * @return the angular velocity of the asteroid around its angle.
   */
  public double getAngularVelocity() {
    return angularVelocity;
  }

  /**
   * @return a factor of size of the asteroid.
   */
  public double getSize() {
    return size;
  }

  /**
   * @return the shape of the asteroid, with same center as the asteroid.
   */
  public Polygon getShape() {
    return shape.rotate(angle).translate(position);
  }


  /**
   * @param center          the center of the asteroid
   * @param shape           the shape with center (0,0) of the asteroid
   * @param velocity        the velocity (in pixel per second) of the asteroid
   * @param angularVelocity the angular velocity (in degree per second) of the asteroid
   * @param size            the relative size of the asteroid.
   */
  public Asteroid(Vector center,
                  Polygon shape,
                  Vector velocity,
                  double angularVelocity,
                  double size) {
    this.position = center;
    this.shape = shape;
    this.angle = 0;
    this.velocity = velocity;
    this.angularVelocity = angularVelocity;
    this.size = size;
  }


  /**
   * Asteroids move over time. To simulate the movement, their positions
   * and other physical properties must be updated regularly. This method
   * simulates the effect of a small time delay <em>dt</em> upon the asteroid.
   *
   * @param dt the time delay to simulate.
   */
  public void update(double dt) {
    position = position.add(velocity.multiply(dt));
    position = Space.toricRemap(position);
    angle = angle + angularVelocity * dt;
  }

  public boolean contains(Vector point) {
    return getShape().contains(point);
  }

  public List<Asteroid> fragments() {
    List<Asteroid> asteroidFragments = new ArrayList<>();
    Asteroid fragment;
    for (int i = 0; i < Space.ASTEROID_FRAGMENT_COUNT; i++) {
      fragment = Space.generator.asteroid(this.getPosition(), this.getSize()*Space.RATIO);
      if (fragment.getSize() == Space.LIMIT_ASTEROID_SIZE) {
        asteroidFragments.add(fragment);
      }
    }
    return asteroidFragments;
  }

}
