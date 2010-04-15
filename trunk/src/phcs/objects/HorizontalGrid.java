package phcs.objects;

import java.awt.Color;
import java.awt.Graphics;

public class HorizontalGrid implements Grid {

  private double increment = 50;
  private double x, y, width, height;

  public HorizontalGrid() {
    this(0, 295, 800, 10);
  }

  public HorizontalGrid(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public void paint(Graphics g) {
    g.setColor(Color.BLACK);
    g.drawLine(
        getX()             , getY() + getHeight()/2,
        getX() + getWidth(), getY() + getHeight()/2
    );
    for (int x = this.getX(); x < (this.x + this.getWidth()); x += increment) {
      g.drawLine(x, getY(), x, getY() + getHeight());
      g.drawString("" + x, x, getY() + getHeight() + 15);
    }
  }

  public int getX() {
    return (int) x;
  }

  public int getY() {
    return (int) y;
  }

  public int getWidth() {
    return (int) width;
  }

  public int getHeight() {
    return (int) height;
  }
}
