package phcs.objects;

import java.awt.Color;
import java.awt.Graphics;

import phcs.PhysicalObject;

/**
 * This is not actually a physical object but it's in the 'objects' package
 * because it is drawn on the screen as though it were an object.
 */
// TODO (low priority) use http://stackoverflow.com/questions/2278474 to make this look a little nicer
public abstract class Grid {
  public static class HorizontalGrid extends PhysicalObject {

    private double increment = 50;

    public HorizontalGrid() {
      this(0, 295, 800, 10, 0, 0);
    }

    public HorizontalGrid(double x, double y, double width, double height,
        double vx, double vy) {
      super(x, y, width, height, vx, vy);
    }

    @Override
    public void paint(Graphics g) {
      g.setColor(Color.BLACK);
      g.drawLine(
          getX(),              getY() + getHeight()/2,
          getX() + getWidth(), getY() + getHeight()/2
      );
      for (int x = this.getX(); x < (this.x + this.getWidth()); x += increment) {
        g.drawLine(x, getY(), x, getY() + getHeight());
        g.drawString("" + x, x, getY() + getHeight() + 15);
      }
    }
  }

  public static class VerticalGrid extends PhysicalObject {

    private double increment = 50;

    public VerticalGrid() {
      this(395, 0, 10, 600, 0, 0);
    }

    public VerticalGrid(double x, double y, double width, double height,
        double vx, double vy) {
      super(x, y, width, height, vx, vy);
    }

    @Override
    public void paint(Graphics g) {
      g.setColor(Color.BLACK);
      g.drawLine(
          getX() + getWidth()/2, getY(),
          getX() + getWidth()/2, getY() + getHeight()
      );
      for (int y = this.getY(); y < (this.y + this.getHeight()); y += increment) {
        g.drawLine(getX(), y, getX() + getWidth(), y);
        g.drawString("" + y, getX() + getWidth() + 5, y);
      }
    }
  }

}
