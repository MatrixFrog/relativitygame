package phcs.objects;

import java.awt.Color;
import java.awt.Graphics;

public class VerticalGrid implements Grid {

  private double _x, _y, width, height;

  public VerticalGrid() {
    this(50, 0, 15, 500);
  }

  public VerticalGrid(double x, double y, double width, double height) {
    this._x = x;
    this._y = y;
    this.width = width;
    this.height = height;
  }

  public void paint(Graphics g) {
    g.setColor(Color.BLACK);
    g.drawLine(
        getX() + getWidth()/2, getY(),
        getX() + getWidth()/2, getY() + getHeight()
    );
    for (int y = this.getY(); y < (this.getY() + this.getHeight()); y += increment) {
      g.drawLine(getX(), y, getX() + getWidth(), y);
      g.drawString("" + y, getX() + getWidth() + 10, y);
    }
  }

  public int getX() {
    return (int) _x;
  }

  public int getY() {
    return (int) _y;
  }

  public int getWidth() {
    return (int) width;
  }

  public int getHeight() {
    return (int) height;
  }
}
