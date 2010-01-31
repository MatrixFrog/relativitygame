package phcs;

import static common.MathUtils.sq;
import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class LightClock extends SimulationObject {

  private List<Flash> flashes;
  private String info = "";

  public LightClock() {
    this(0, 0);
  }

  /**
   * Create a LightClock at the given position
   */
  public LightClock(double x, double y) {
    this(x, y, 50, 200);
  }

  /**
   * Create a LightClock with the given position and size
   */
  public LightClock(double x, double y, int width, int height) {
    super(x, y, width, height, 0, 0);
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

  @Override
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

  @Override
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
        info = ((double) firstFlash.getLifetime()) / 100 + "";

        // TODO this obviously belongs somewhere else
        if (abs(firstFlash.getLifetime() - 500) < 2) {
          info += " (You win!)";
        }

      }
    }
  }

  @Override
  public void reset() {
    x = initialX;
    y = initialY;
    flashes = new LinkedList<Flash>();
    info = "";
  }

  @Override
  public void go() {
    flash();
  }

  // TODO put a title on this panel somehow
  @Override
  public JPanel getControlPanel() {
    if (isVelocityEditable()) {
      JPanel ctrlPanel = new JPanel();
      final JSlider slider = new JSlider(0, 100, 0);
      slider.setMajorTickSpacing(5);
      slider.setMinorTickSpacing(1);
      slider.setSnapToTicks(true);
      slider.setPaintTicks(true);
      slider.setPaintLabels(true);
      slider.addChangeListener(new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e) {
          setVelocity(((double) slider.getValue()) / 100, 0);
        }
      });
      ctrlPanel.add(slider);

      ctrlPanel.setBorder(BorderFactory.createTitledBorder("Light Clock"));

      return ctrlPanel;
    }
    else {
      return null;
    }
  }

  @Override
  public boolean isControllable() {
    return isVelocityEditable();
  }
}
