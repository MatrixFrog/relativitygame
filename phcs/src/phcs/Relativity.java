package phcs;

import static common.swingutils.SwingUtils.useDefaultLookAndFeel;
import static common.swingutils.SwingUtils.useDialogExceptionHandler;
import static java.lang.Math.sqrt;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Relativity extends JFrame implements ActionListener, ChangeListener {

  private LightClock stationaryLightClock = new LightClock(30, 30);
  private LightClock movingLightClock = new LightClock();

  // Will eventually be a List<SimulationObject> or something
  private List<LightClock> simulationObjects = Arrays.asList(stationaryLightClock, movingLightClock);

  private Timer timer = new Timer(10, this);
  private JButton goButton = new JButton("Go");
  private JButton resetButton = new JButton("Reset");
  private JSlider speedSlider;

  private JPanel panel = new JPanel() {
    @Override
    public void paint(Graphics g) {
      for (LightClock lc : simulationObjects) {
        lc.paint(g);
      }
    }
  };

  private void initSlider() {
    speedSlider = new JSlider(0, 100, 0);
    speedSlider.setMajorTickSpacing(10);
    speedSlider.setPaintTicks(true);
    speedSlider.setPaintLabels(true);
  }

  public Relativity() {
    initSlider();

    goButton.addActionListener(this);
    resetButton.addActionListener(this);
    speedSlider.addChangeListener(this);

    setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    add(panel);

    JPanel buttonPanel = new JPanel();
    buttonPanel.add(goButton);
    buttonPanel.add(resetButton);
    buttonPanel.add(speedSlider);
    this.add(buttonPanel);

    reset();

    setSize(1024, 768);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }

  public static double gamma(double speed) {
    return sqrt(1 - speed * speed);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == goButton) {
      go();
    }
    if (e.getSource() == resetButton) {
      reset();
    }
    else if (e.getSource() == timer) {
      for (LightClock lc : simulationObjects) {
        lc.update();
      }
      repaint();
    }
  }

  private void go() {
    speedSlider.setEnabled(false);
    timer.start();
    stationaryLightClock.flash();
    movingLightClock.flash();
  }

  private void reset() {
    movingLightClock.setPosition(100, 30);
    movingLightClock.reset();
    timer.stop();
    speedSlider.setEnabled(true);
    repaint();
  }

  public static void main(String[] args) {
    useDefaultLookAndFeel();
    useDialogExceptionHandler();
    new Relativity();
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    if (e.getSource() == speedSlider) {
      movingLightClock.setSpeed(((double) speedSlider.getValue())/100, 0);
    }
  }
}
