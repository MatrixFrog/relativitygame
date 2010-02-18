package phcs.gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import phcs.PhysicalObject;

public class VelocitySlider extends JSlider implements ChangeListener {

  private PhysicalObject obj;

  public VelocitySlider(PhysicalObject obj) {
    super(0, 100, 0);
    this.obj = obj;
    setMajorTickSpacing(5);
    setMinorTickSpacing(1);
    setSnapToTicks(true);
    setPaintTicks(true);
    setPaintLabels(true);
    addChangeListener(this);
  }

  public void stateChanged(ChangeEvent e) {
    obj.setVelocity(((double) ((JSlider) e.getSource()).getValue()) / 100, 0);
  }
}
