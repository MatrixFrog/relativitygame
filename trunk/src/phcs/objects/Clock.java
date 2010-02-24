package phcs.objects;

import static java.lang.Math.cos;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;
import static phcs.Relativity.gamma;

import java.awt.Color;
import java.awt.Graphics;

import phcs.PhysicalObject;

public class Clock extends PhysicalObject {

  /** How fast the clock runs in its own rest frame, in degrees per timestep */
  private static final double CLOCK_SPEED = 2;
  private final double handLength;
  private double clockDegrees = 0;

  public Clock() {
    this(300, 30, 50, 50, 0, 0);
  }

  public Clock(double x, double y, int width, int height, double vx, double vy) {
    super(x, y, width, height, vx, vy);
    handLength = min(width, height)/2;
  }

  @Override
  public void paint(Graphics g) {
    g.setColor(Color.BLACK);
    g.drawOval(getX(), getY(), getWidth(), getHeight());
    g.drawLine(getCenter().x, getCenter().y,
        (int) (getCenter().x + handLength*sin(toRadians(clockDegrees))),
        (int) (getCenter().y - handLength*cos(toRadians(clockDegrees))));
  }

  @Override
  public void reset() {
    super.reset();
    clockDegrees = 0;
  }

  @Override
  public void update() {
    super.update();
    clockDegrees = (clockDegrees + gamma(getSpeed())*CLOCK_SPEED) % 360;
  }
}
