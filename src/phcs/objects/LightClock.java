package phcs.objects;

import static java.lang.Math.sqrt;
import static util.MathUtils.sq;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import phcs.Photon;
import phcs.LightClockListener;
import phcs.PhysicalObject;

public class LightClock extends PhysicalObject implements ChangeListener {

  private List<Photon> flashes;
  private int counter;
  private List<LightClockListener> listeners = new ArrayList<LightClockListener>();

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
  public LightClock(double x, double y, double width, double height) {
    super(x, y, width, height, 0.0, 0.0);
  }

  public double getTopX() {
    return x + getWidth() / 2;

  }

  public double getTopY() {
    return y;
  }

  public double getBottomX() {
    return x + getWidth() / 2;
  }

  public double getBottomY() {
    return y + getHeight();
  }

  /**
   * Release a flash of light from the top of the light clock
   */
  public void flash() {
    double flash_vy = sqrt(1 - sq(vx));
    Photon flash = new Photon(getTopX(), getTopY(), vx, flash_vy);
    flashes.add(flash);
  }

  @Override
  public void paint(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;
    Rectangle r = new Rectangle((int) x, (int) y, getWidth(), getHeight());
    g2.setColor(Color.BLACK);
    g2.draw(r);

    g.drawString(getName(), (int) x, (int) y);

    for (Photon flash : flashes) {
      flash.paint(g);
    }
  }

  @Override
  public void update() {
    super.update();
    counter++;
    for (Photon flash : flashes) {
      if (flash.getY() >= this.getBottomY()) {
        flash.reflectVertical();
      }
      flash.update();
    }
    if (!flashes.isEmpty()) {
      Photon firstFlash = flashes.get(0);
      if (firstFlash.getY() <= this.getTopY()) {
        flashes.remove(firstFlash);
        notifyLightClockListeners();
      }
    }
  }

  @Override
  public void reset() {
    super.reset();
    counter = 0;
    flashes = new LinkedList<Photon>();
  }

  @Override
  public void go() {
    super.go();
    flash();
  }

  // TODO don't do this in this way!
  public void stateChanged(ChangeEvent e) {
    setVelocity(((double) ((JSlider) e.getSource()).getValue()) / 100, 0);
  }

  public void addLightClockListener(LightClockListener lightClockListener) {
    listeners.add(lightClockListener);
  }

  public void removeLightClockListener(LightClockListener lightClockListener) {
    listeners.remove(lightClockListener);
  }

  private void notifyLightClockListeners() {
    for (LightClockListener listener : listeners) {
      listener.cycleCompleted(counter);
    }
  }
}
