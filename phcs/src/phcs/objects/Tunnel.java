package phcs.objects;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

/**
 * A tunnel with gates on either end that can open or close. The player can decide
 * the tunnel's initial state and at what time they want the tunnel to open or close.
 */
public class Tunnel extends PhysicalObject {

  private boolean leftGateOpen;
  private boolean rightGateOpen;

  public Tunnel() {
    this(300, 300, 100, 60, 0, 0);
  }

  public Tunnel(double x, double y, double width, double height, double vx, double vy) {
    super(x, y, width, height, vx, vy);
  }

  @Override
  public JPanel getControlPanel() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean isControllable() {
    return isVelocityEditable() || isLeftGateEditable() || isRightGateEditable();
  }

  @Override
  public void paint(Graphics g) {
    g.setColor(Color.BLACK);
    g.drawLine((int) x, (int) y, (int) (x+getWidth()),(int)  y);
    g.drawLine((int) x, (int) (y+getHeight()), (int) (x + getWidth()),(int) (y+getHeight()));
    paintLeftGate(g);
    paintRightGate(g);
  }

  private void paintLeftGate(Graphics g) {
    if (isLeftGateOpen()) {
      g.drawLine((int) x, (int) (y-getHeight()), (int) x, (int) y);
    }
    else {
      g.drawLine((int) x, (int) y, (int) x, (int) (y+getHeight()));
    }
  }

  private void paintRightGate(Graphics g) {
    if (isRightGateOpen()) {
      g.drawLine((int) (x+getWidth()), (int) (y-getHeight()), (int) (x+getWidth()), (int) y);
    }
    else {
      g.drawLine((int) (x+getWidth()), (int) y, (int) (x+getWidth()), (int) (y+getHeight()));
    }
  }

  @Override
  public void update() {
    // TODO Auto-generated method stub

  }

  public void toggleLeftGate() {
    leftGateOpen = !leftGateOpen;
  }

  public void toggleRightGate() {
    rightGateOpen = !rightGateOpen;
  }

  private boolean isLeftGateEditable() {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean isRightGateEditable() {
    // TODO Auto-generated method stub
    return false;
  }

  private boolean isLeftGateOpen() {
    return leftGateOpen;
  }

  private boolean isRightGateOpen() {
    return rightGateOpen;
  }

}
