package phcs;

/**
 * A reference frame is defined in terms of its velocity relative to another reference
 * frame. It's not specified what that other reference frame is.
 *
 * For now, the only reference frames we can deal with are ones that have the same
 * origin as the "main" reference frame and move at a certain velocity relative to it.
 */
public class ReferenceFrame {
  public static final ReferenceFrame DEFAULT_FRAME = new ReferenceFrame(0, 0);

  public double vx, vy;

  public ReferenceFrame(double vx, double vy) {
    this.vx = vx;
    this.vy = vy;
  }

  /**
   * @return A new reference frame with the opposite velocity of this one.
   */
  public ReferenceFrame inverted() {
    return new ReferenceFrame(-vx, -vy);
  }

  // TODO this needs to not only transform the velocity, but also move the object
  public void transform(PhysicalObject obj) {
    obj.vx = (obj.vx - this.vx) / (1 - obj.vx * this.vx);
    //obj.initialX /= PhysicalObject.gamma(this.vx);
  }

  /**
   * Equivalent to <tt>this.inverted().transformVelocity(obj)</tt>
   */
  public void untransform(PhysicalObject obj) {
    this.inverted().transform(obj);
  }

  @Override
  public String toString() {
    return String.format("v=(%.2f, %.2f)", vx, vy);
  }
}
