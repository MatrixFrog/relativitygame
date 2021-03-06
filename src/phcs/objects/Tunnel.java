package phcs.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import phcs.PhysicalObject;
import phcs.gui.TunnelController;

/**
 * A tunnel with gates on either end that can open or close. The player can decide
 * the tunnel's initial state and at what time(s) they want the tunnel to open or close.
 */
public class Tunnel extends PhysicalObject {

  private boolean leftGateOpen = true;
  private boolean rightGateOpen = true;
  private TunnelController controller;
  private boolean leftGateOpenByDefault;
  private boolean rightGateOpenByDefault;

  public Tunnel() {
    this(300, 100, 100, 60, 0, 0);
  }

  public Tunnel(double x, double y, double width, double height, double vx, double vy) {
    super(x, y, width, height, vx, vy);
    setDefaultGateStates(true, true);
  }

  /**
   * If you don't call this method at all, then it is
   * called automatically with the arguments (true, true)
   *
   * @param leftGateOpenByDefault sets whether the left gate is open by default (starts open)
   * @param rightGateOpenByDefault sets whether the right gate is open by default (starts open)
   *
   */
  public void setDefaultGateStates(boolean leftGateOpenByDefault, boolean rightGateOpenByDefault) {
    this.leftGateOpenByDefault = leftGateOpenByDefault;
    this.rightGateOpenByDefault = rightGateOpenByDefault;
  }

  public void setController(TunnelController controller) {
    this.controller = controller;
    controller.setTunnel(this);
  }

  @Override
  public void reset() {
    super.reset();
    if (controller != null) {
      controller.reset();
    }
    leftGateOpen = leftGateOpenByDefault;
    rightGateOpen = rightGateOpenByDefault; // TODO allow for different defaults
  }

  @Override
  public void paint(Graphics g) {
    g.setColor(Color.BLACK);
    g.drawLine(getX(), getY(), (getX()+getWidth()),getY());
    g.drawLine(getX(), (getY()+getHeight()), (getX() + getWidth()),(getY()+getHeight()));
    paintLeftGate(g);
    paintRightGate(g);
  }

  @Override
  public void update() {
    super.update();
    controller.update();
  }

  private void paintLeftGate(Graphics g) {
    paintGate(g, true, leftGateOpen);
  }

  private void paintRightGate(Graphics g) {
    paintGate(g, false, rightGateOpen);
  }

  private void paintGate(Graphics g, boolean left, boolean open) {
    g.setColor(left ? Color.BLUE : Color.RED);
    int gateX = left ? getX() : getX() + getWidth();

    g.fillOval(gateX-2, getY()-2, 4, 4);
    g.fillOval(gateX-2, getY()+getHeight()-2, 4, 4);
    if (open) {
      int openX = left ? gateX - getHeight()/2 : gateX + getHeight()/2;
      Line2D upperGate = new Line2D.Double(gateX, y, openX, y);
      Line2D lowerGate = new Line2D.Double(gateX, y+getHeight(), openX, y+getHeight());

      ((Graphics2D) g).draw(upperGate);
      ((Graphics2D) g).draw(lowerGate);
    }
    else {
      g.drawLine(gateX, getY(), gateX, (getY()+getHeight()));
    }
  }

  public void toggleLeftGate() {
    leftGateOpen = !leftGateOpen;
  }

  public void toggleRightGate() {
    rightGateOpen = !rightGateOpen;
  }

  public boolean isLeftGateOpen() {
    return leftGateOpen;
  }

  public boolean isRightGateOpen() {
    return rightGateOpen;
  }
}
