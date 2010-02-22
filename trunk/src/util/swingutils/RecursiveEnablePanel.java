package util.swingutils;

import java.awt.Component;

import javax.swing.JPanel;

/**
 * A JPanel that, when you call setEnabled() on it, automatically calls
 * setEnabled() (with the same argument) on all the components inside it.
 */
public class RecursiveEnablePanel extends JPanel {
  @Override
  public void setEnabled(boolean enable) {
    super.setEnabled(enable);
    for (Component c : getComponents()) {
      c.setEnabled(enable);
    }
  }
}
