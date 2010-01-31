package common.swingutils;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * A KeyListener that simply does nothing. If you only need to implement one or two methods,
 * your KeyListener can extend this class instead of implementing all three KeyListener methods.
 */
public abstract class DefaultKeyListener implements KeyListener {
  @Override public void keyPressed(KeyEvent e) {
    // do nothing
  }

  @Override public void keyReleased(KeyEvent e) {
    // do nothing
  }

  @Override public void keyTyped(KeyEvent e) {
    // do nothing
  }
}
