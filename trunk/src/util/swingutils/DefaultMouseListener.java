package util.swingutils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A MouseListener that simply does nothing. If you only need to implement one or two methods,
 * your MouseListener can extend this class instead of implementing all the MouseListener methods.
 */
public class DefaultMouseListener implements MouseListener {
  public void mouseClicked(MouseEvent e) {
    // do nothing
  }

  public void mouseEntered(MouseEvent e) {
    // do nothing
  }

  public void mouseExited(MouseEvent e) {
    // do nothing
  }

  public void mousePressed(MouseEvent e) {
    // do nothing
  }

  public void mouseReleased(MouseEvent e) {
    // do nothing
  }
}