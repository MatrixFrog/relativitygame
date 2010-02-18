package phcs.objects;

import java.awt.Color;
import java.awt.Graphics;

import phcs.PhysicalObject;

public class Spaceship extends PhysicalObject {

  public Spaceship() {
    this(200, 100, 200, 40, 0, 0);
  }

  public Spaceship(double x, double y, double width, double height, double vx, double vy) {
    super(x, y, width, height, vx, vy);
  }

  @Override
  public void paint(Graphics g) {
    int tailWidth = getWidth()/5;
    int tailX = getX() + tailWidth;

    // tail
    g.setColor(Color.ORANGE);
    g.drawLine(getX(), getY(), getX(), getY() + getHeight());
    g.drawLine(getX(), getY(), tailX, getY() + getHeight()/2);
    g.drawLine(tailX, getY() + getHeight()/2, getX(), getY() + getHeight());

    g.setColor(Color.WHITE);
    g.fillOval(tailX, getY(), getWidth() - tailWidth, getHeight());
    g.setColor(Color.BLACK);
    g.drawOval(tailX, getY(), getWidth() - tailWidth, getHeight());
  }
}
