package phcs.objects;

import static java.lang.Math.cos;
import static java.lang.Math.min;
import static java.lang.Math.sin;
import static java.lang.Math.toRadians;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class Clock extends PhysicalObject {

  private final int handLength;
  double clockDegrees = 0;

  public Clock() {
    this(30, 30);
  }

  public Clock(double x, double y) {
    this(x, y, 50, 50, 0, 0);
  }

  public Clock(double x, double y, int width, int height, double vx, double vy) {
    super(x, y, width, height, vx, vy);
    handLength = min(width, height)/2;
  }

  @Override
  public JPanel getControlPanel() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isControllable() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void paint(Graphics g) {
    g.setColor(Color.BLACK);
    g.drawOval((int) x, (int) y, size.width, size.height);
    g.drawLine(getCenter().x, getCenter().y,
        (int) (getCenter().x + handLength*sin(toRadians(clockDegrees))),
        (int) (getCenter().y - handLength*cos(toRadians(clockDegrees))));
  }

  @Override
  public void reset() {
    clockDegrees = 0;
  }

  @Override
  public void update() {
    // TODO multiply by gamma or something
    clockDegrees = (clockDegrees + 2) % 360;
  }
}
