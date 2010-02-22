package phcs.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JPanel;

import phcs.PhysicalObject;

/**
 * A tunnel with gates on either end that can open or close. The player can decide
 * the tunnel's initial state and at what time(s) they want the tunnel to open or close.
 */
// TODO design a control system for the gates.
public class Tunnel extends PhysicalObject {

  private boolean leftGateOpen = true;
  private boolean rightGateOpen = true;

  public Tunnel() {
    this(300, 300, 100, 60, 0, 0);
  }

  public Tunnel(double x, double y, double width, double height, double vx, double vy) {
    super(x, y, width, height, vx, vy);
  }

  @Override
  public void paint(Graphics g) {
    g.setColor(Color.BLACK);
    g.drawLine(getX(), getY(), (getX()+getWidth()),getY());
    g.drawLine(getX(), (getY()+getHeight()), (getX() + getWidth()),(getY()+getHeight()));
    paintLeftGate(g);
    paintRightGate(g);
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

  private boolean isLeftGateEditable() {
    // TODO method stub
    return false;
  }

  private boolean isRightGateEditable() {
    // TODO method stub
    return false;
  }

}
