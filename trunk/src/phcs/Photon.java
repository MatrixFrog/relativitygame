package phcs;

import static java.lang.Math.abs;
import static java.lang.Math.hypot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Photon {

  /**
   * @return The speed of light
   */
  public static double getSpeed() {
    return 1;
  }

  private double x, y;
  private double vx, vy;

  /**
   * The number of timesteps the flash has existed;
   */
  private long lifetime = 0;

  public Photon(double x, double y, double vx, double vy) {
    this.x = x;
    this.y = y;
    this.vx = vx;
    this.vy = vy;
    if (abs(hypot(vx, vy) - 1)> 0.000000001) {
      throw new IllegalArgumentException("The flash must travel at the speed of light (1)");
    }
  }

  public Photon(Point pos, double vx, double vy) {
    this(pos.x, pos.y, vx, vy);
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }

  public void update() {
    x += vx;
    y += vy;
    lifetime++;
  }

  public void paint(Graphics g) {
    g.setColor(Color.RED);
    g.fillOval((int) x-2, (int) y-2, 4, 4);
  }

  public double getY() {
    return y;
  }

  public void reflectVertical() {
    vy = -vy;
  }

  public long getLifetime() {
    return lifetime;
  }
}
