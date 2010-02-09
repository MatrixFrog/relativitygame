package phcs.objects;

import static java.lang.Math.hypot;
import static phcs.Relativity.gamma;

import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;

import phcs.LawsOfPhysicsException;

/**
 * An instance of this class is a physical object such as a light-clock or a
 * spaceship.
 */
public abstract class PhysicalObject {

  // Current position
  protected double x, y;

  // Object's size in its own rest frame
  private double width, height;

  // Initial position
  protected double initialX, initialY;

  // Velocity
  protected double vx, vy;

  private String name = "";

  private boolean velocityEditable = false;

  private boolean running;

  /**
   * Create an object with the given position, size, and speed.
   */
  public PhysicalObject(double x, double y, double width, double height,
      double vx, double vy) {
    this.initialX = x;
    this.initialY = y;
    this.width = width;
    this.height = height;
    setVelocityInternal(vx, vy);
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
   * Subclasses should override this method to contain any actions that should
   * happen when the simulation containing this object is reset. The overriding method
   * should start with super.reset()
   */
  public void reset() {
    running = false;
    x = initialX;
    y = initialY;
  }

  /**
   * Subclasses should override this method to contain any actions that should
   * happen when the simulation containing this object is started. The overriding method
   * should start with super.go()
   */
  public void go() {
    running = true;
  }

  public void setVelocity(double vx, double vy) {
    if (velocityEditable) {
      setVelocityInternal(vx, vy);
    }
    else {
      throw new UnsupportedOperationException(
          "This object does not allow its speed to be changed.");
    }
  }

  /**
   * Set the velocity without checking the velocityEditable flag. For internal
   * use only.
   */
  private void setVelocityInternal(double vx, double vy) {
    if (vy != 0) {
      throw new UnsupportedOperationException(
          "For now, objects can only move horizontally.");
    }
    if (hypot(vx, vy) > 1) {
      throw new LawsOfPhysicsException("Nothing can travel faster than light!");
    }
    this.vx = vx;
    this.vy = vy;
  }

  /**
   * @return true if the changing the velocity is allowed. It is false by
   *         default.
   */
  public boolean isVelocityEditable() {
    return velocityEditable;
  }

  public void setVelocityEditable(boolean speedEditable) {
    this.velocityEditable = speedEditable;
  }

  public double getSpeed() {
    return hypot(vx, vy);
  }

  public Point getCenter() {
    return new Point((int) (x + getWidth() / 2), (int) (y + getHeight() / 2));
  }

  /**
   * @return A panel containing all the necessary controls for this object. If
   *         the object is not controllable (i.e. if isControllable() returns
   *         false), this method returns <tt>null</tt>. All the controls in the
   *         panel should already have the necessary
   *         ActionListeners/ChangeListeners/etc. installed.
   */
  public abstract JPanel getControlPanel();

  public abstract boolean isControllable();

  @Override
  public String toString() {
    return String.format("%s %s at (%.2f,%.2f)", getClass().getSimpleName(), name, x, y);
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public double getWidth() {
    if (running) {
      return width / gamma(vx);
    }
    else {
      return width;
    }
  }

  public double getHeight() {
    return height;
  }
}
