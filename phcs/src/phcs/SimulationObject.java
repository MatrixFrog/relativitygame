package phcs;

import static java.lang.Math.hypot;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

public abstract class SimulationObject {

  // TODO encapsulate all protected fields and make them private?

  protected Dimension size;

  // Current position
  protected double x, y;

  // Initial position
  protected double initialX, initialY;

  // Velocity
  protected double vx, vy;

  private boolean velocityEditable = false;

  /**
   * Create an object with the given position, size, and speed.
   */
  public SimulationObject(double x, double y, int width, int height, double vx, double vy) {
    setInitialPosition(x, y);
    this.vx = vx;
    this.vy = vy;
    size = new Dimension(width, height);
    this.reset();
  }

  /**
   * Everything that needs to be done to draw the object goes in this method.
   */
  public abstract void paint(Graphics g);

  /**
   * All the actions that should occur once per timestep belong in this method.
   */
  public abstract void update();

  /**
   *
   */
  public abstract void reset();

  /**
   * What happens to this object when the user clicks "Go" -- by default, no action.
   * If there is an action that should happen, this method should be overridden.
   */
  public void go() {

  }

  public void setInitialPosition(double x, double y) {
    this.initialX = x;
    this.initialY = y;
  }

  public void setVelocity(double vx, double vy) {
    if (!velocityEditable) {
      throw new UnsupportedOperationException("This LightClock does not allow its speed to be changed.");
    }
    if (vy != 0) {
      throw new UnsupportedOperationException("For now, light clocks can only move horizontally.");
    }
    if (hypot(vx, vy) > 1) {
      throw new LawsOfPhysicsException("The light clock cannot travel faster than c!");
    }
    this.vx = vx;
    this.vy = vy;
  }

  /**
   * @return true if the changing the velocity is allowed. It is false by default.
   */
  public boolean isVelocityEditable() {
    return velocityEditable;
  }

  public void setVelocityEditable(boolean speedEditable) {
    this.velocityEditable = speedEditable;
  }

  /**
   * @return A panel containing all the necessary controls for this object. If the object
   * is not controllable (i.e. if isControllable() returns false), this method returns
   * <tt>null</tt>. All the controls in the panel should already have the necessary
   * ActionListeners/ChangeListeners/etc. installed.
   */
  public abstract JPanel getControlPanel();

  public abstract boolean isControllable();
}
