package phcs;

import static common.MathUtils.sq;
import static java.lang.Math.abs;
import static java.lang.Math.hypot;
import static java.lang.Math.sqrt;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

public class LightClock {

  private Dimension size;
  private double x, y;
  private double vx, vy;

  private List<Flash> flashes;
  private String info = "";

  public LightClock() {
    this(0, 0);
  }

  public LightClock(double x, double y) {
    this(x, y, 50, 200);
  }

  public LightClock(double x, double y, int width, int height) {
    this(x, y, width, height, 0, 0);
  }

  public LightClock(double x, double y, int width, int height, double vx, double vy) {
    setPosition(x, y);
    setSpeed(vx, vy);
    size = new Dimension(width, height);
    this.reset();
  }

  public double getTopX() {
    return x + size.width / 2;

  }
  public double getTopY() {
    return y;
  }

  public double getBottomX() {
    return x + size.width / 2;
  }
  public double getBottomY() {
      return y + size.height;
  }

  /**
   * Release a flash of light from the top of the light clock
   */
  public void flash() {
    double flash_vy = sqrt(1 - sq(vx));
    Flash flash = new Flash(getTopX(), getTopY(), vx, flash_vy);
    flashes.add(flash);
  }

  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    Rectangle r = new Rectangle((int) x, (int) y, size.width, size.height);
    g2.setColor(Color.BLACK);
    g2.draw(r);

    g.drawString(info, (int) x, (int) y);

    for (Flash flash : flashes) {
      flash.paint(g);
    }
  }

  public void update() {
    x += vx;
    y += vy;

    for (Flash flash : flashes) {
      if (flash.getY() >= this.getBottomY()) {
        flash.reflectVertical();
      }
      flash.update();
    }
    if (!flashes.isEmpty()) {
      Flash firstFlash = flashes.get(0);
      if (firstFlash.getY() <= this.getTopY()) {
        flashes.remove(firstFlash);
        info = ((double) firstFlash.getLifetime())/100 + "";

        // TODO this obviously belongs somewhere else
        if (abs(firstFlash.getLifetime() - 500) < 2) {
          info += " (You win!)";
        }

      }
    }
  }

  public void setPosition(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public void setSpeed(double vx, double vy) {
    if (vy != 0) {
      throw new UnsupportedOperationException("For now, light clocks can only move horizontally.");
    }
    if (hypot(vx, vy) >= 1) {
      throw new LawsOfPhysicsException("The light clock cannot travel faster than c!");
    }
    this.vx = vx;
    this.vy = vy;
  }

  public void reset() {
    flashes = new LinkedList<Flash>();
    info = "";
  }

}
