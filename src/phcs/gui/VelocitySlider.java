package phcs.gui;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import phcs.PhysicalObject;

public class VelocitySlider extends JSlider implements ChangeListener {

  private PhysicalObject obj;

  public VelocitySlider(PhysicalObject obj) {
    this(obj, 0);
  }

  public VelocitySlider(PhysicalObject obj, double initialValue) {
    super(0, 100, (int) (initialValue*100));
    this.obj = obj;
    obj.setVelocity(initialValue, 0);
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
