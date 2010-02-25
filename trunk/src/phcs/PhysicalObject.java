package phcs;

import static java.lang.Math.hypot;
import static java.lang.Math.sqrt;
import static util.MathUtils.sq;

import java.awt.Graphics;
import java.awt.Point;


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
  private boolean running;

  // TODO this is storing the same information stored in vx, vy so it might be redundant
  private ReferenceFrame restFrame = new ReferenceFrame(0, 0);

  /**
   * Create an object with the given position, size, and speed.
   */
  public PhysicalObject(double x, double y, double width, double height,
      double vx, double vy) {
    this.initialX = x;
    this.initialY = y;
    this.width = width;
    this.height = height;
    setVelocity(vx, vy);
    setName(getClass().getSimpleName());

    this.reset();
  }

  /**
   * Everything that needs to be done to draw the object goes in this method.
   */
  public abstract void paint(Graphics g);

  /**
   * All the actions that should occur once per timestep belong in this method.
   * If this is overridden in subclasses, they must call <tt>super.update();</tt>
   */
  public void update() {
    x += vx;
    y += vy;
    timeIncrement(1/gamma(getSpeed()));
  }

  /**
   * If an object is clock-like in any way (i.e. if it is supposed to change as
   * time passes, in any way other than moving through space at a constant velocity)
   * then the subclass should override this method. The time dilation effect is
   * automatically taken into account in {@link #update()}.
   */
  public void timeIncrement(double time) {
    // no action
  }

  /**
   * Subclasses should override this method to contain any actions that should
   * happen when the simulation containing this object is reset. The overriding method
   * should start with <tt>super.reset()</tt>
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

  public static double gamma(double speed) {
    return 1/sqrt(1 - sq(speed));
  }

  public static double inverseGamma(double gamma) {
    return sqrt(1 - 1/sq(gamma));
  }

  @Override
  public String toString() {
    return String.format("%s %s at (%.2f,%.2f)", getClass().getSimpleName(), name, x, y);
  }

  // TODO make a similar system for allowing/disallowing the setting of the object's initial position

  /**
   * Set the velocity without checking the velocityEditable flag. For internal
   * use only.
   */
  public void setVelocity(double vx, double vy) {
    if (vy != 0) {
      throw new UnsupportedOperationException(
          "For now, objects can only move horizontally.");
    }
    if (hypot(vx, vy) > 1) {
      throw new LawsOfPhysicsException("Nothing can travel faster than light!");
    }
    restFrame.vx = this.vx = vx;
    restFrame.vy = this.vy = vy;
  }

  public double getSpeed() {
    return hypot(vx, vy);
  }

  public Point getCenter() {
    return new Point(getX() + getWidth() / 2, getY() + getHeight() / 2);
  }

  /**
   * Set the name of this object, which is used in several places in the GUI. If you don't
   * call this method, the name will default to the name of the class.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * While there are an infinite number of frames in which any object is at rest,
   * this simply returns the one with the same origin as the primary frame.
   *
   * @return the reference frame in which this object is at rest.
   */
  public ReferenceFrame getRestFrame() {
    return restFrame;
  }

  public String getName() {
    return name;
  }

  public int getWidth() {
    if (running) {
      return (int) (width / gamma(vx));
    }
    else {
      return (int) width;
    }
  }

  public int getHeight() {
    return (int) height;
  }

  /**
   * Note: This returns an int even though the variable is stored as a double.
   */
  public int getX() {
    return (int) x;
  }

  /**
   * Note: This returns an int even though the variable is stored as a double.
   */
  public int getY() {
    return (int) y;
  }
}
