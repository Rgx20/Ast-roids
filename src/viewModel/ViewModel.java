package viewModel;

import game.Projectile;
import game.Asteroid;
import game.Space;
import game.Spaceship;
import views.View;

import java.util.List;


/**
 * An object of this class is responsible for the communication between the
 * state of the game (Space, the model), and the view of the game (View).
 * Ideally, Space and View should ignore each other (but to avoid boilerplate
 * we may sometimes use the space from the view). The view should only contain
 * rendering methods and event handlers, while the model should contain
 * all the data and the logic of the application.
 */
public class ViewModel {


  private Space gameState; // the model
  private View view; // the view

  public ViewModel(Space space, View view) {
    this.gameState = space;
    this.view = view;
  }


  /**
   * Update the model and view after time delay dt.
   * @param dt the time delay in seconds.
   */
  public void tick(double dt) {
    gameState.update(dt);
    view.render();
  }


  /**
   * Command to start the main engine of the player's spaceship
   */
  public void startSpaceshipMainEngine() {
    gameState.getSpaceship().startMainEngine();
  }

  /**
   * Command to stop the main engine of the player's spaceship
   */
  public void stopSpaceshipMainEngine() {
    gameState.getSpaceship().stopMainEngine();
  }

  /**
   * Command to start the recoil engine of the player's spaceship
   */
  public void startSpaceshipRecoilEngine() { gameState.getSpaceship().startRecoilEngine(); }

  /**
   * Command to stop the recoil engine of the player's spaceship
   */
  public void stopSpaceshipRecoilEngine() { gameState.getSpaceship().stopRecoilEngine(); }

  /**
   * Command to start the left lateral engine of the player's spaceship
   */
  public void startSpaceshipLeftLateralEngine() { gameState.getSpaceship().startLeftLateralEngine(); }

  /**
   * Command to stop the left lateral engine of the player's spaceship
   */
  public void stopSpaceshipLeftLateralEngine() { gameState.getSpaceship().stopLeftLateralEngine(); }

  /**
   * Command to start the right lateral engine of the palyer's spaceship
   */
  public void startSpaceshipRightLateralEngine() { gameState.getSpaceship().startRightLateralEngine(); }

  /**
   * Command to stop the right lateral engine of the player's spaceship
   */
  public void stopSpaceshipRightLateralEngine() { gameState.getSpaceship().stopRightLateralEngine(); }

  /**
   * @return whether the main engine of the spaceship is on
   */
  public boolean isSpaceshipMainEngineOn() { return gameState.getSpaceship().isMainEngineOn(); }

  /**
   * @return whether the recoil engine of the spaceship is on
   */
  public boolean isSpaceshipRecoilEngineOn() { return gameState.getSpaceship().isRecoilEngineOn(); }

  /**
   * @return whether the left lateral engine of the spaceship is on
   */
  public boolean isSpaceshipLeftLateralEngineOn() { return gameState.getSpaceship().isLeftLateralEngineOn(); }

  /**
   * @return whether the right lateral engine of the spaceship is on
   */
  public boolean isSpaceshipRightLateralEngineOn() { return gameState.getSpaceship().isRightLateralEngineOn();}

  /**
   * @return whether the spaceship is invulnerable
   */
  public boolean isSpaceshipInvulnerable() {
    return gameState.getSpaceship().isInvulnerable();
  }

  /**
   * @return whether the game is over
   */
  public boolean isGameOver() {
    return gameState.isGameOver();
  }


  /**
   * @return the list of asteroids to display
   */
  public List<Asteroid> getAsteroids() {
    return gameState.getAsteroids();
  }

  /**
   * @return the list of projectiles to display
   */
  public List<Projectile> getProjectiles() {
    return gameState.getProjectiles();
  }


  /**
   * @return the state of the spaceship
   */
  public Spaceship getSpaceship() {
    return gameState.getSpaceship();
  }

  /**
   * @return the fuel percentage in the spaceship's tank
   */
  public double getSpaceshipFuelPercentage() { return gameState.getSpaceship().getFuelPercentage(); }

  /**
   * @return the current score
   */
  public double getScore() {
    return gameState.getScore().getValue();
  }

  /**
   * @return the invulnerability time of the spaceship
   */
  public double getSpaceshipInvulnerabilityTime() {
    return gameState.getSpaceship().getInvulnerabilityTime();
  }

  /**
   * @return the remaining life of the spaceship
   */
  public int getSpaceshipLife() {
    return gameState.getSpaceship().getLife();
  }

  public void fireSpaceshipGun() {
    gameState.addProjectile(getSpaceship().fire());
  }

  public int getScoreMultiplier() { return gameState.getScore().getMultiplier(); }
}
