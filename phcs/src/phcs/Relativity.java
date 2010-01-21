package phcs;

import static common.swingutils.SwingUtils.box;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Relativity extends JFrame implements ActionListener, ChangeListener {

  private LightClock stationaryLightClock = new LightClock(30, 30);
  private LightClock movingLightClock = new LightClock();

  // Will eventually be a List<SimulationObject> or something
  private List<LightClock> simulationObjects = Arrays.asList(stationaryLightClock, movingLightClock);

  private Timer timer = new Timer(5, this);
  private JButton goButton = new JButton("Go");
  private JButton resetButton = new JButton("Reset");
  private JSlider speedSlider;

  private String instructions = "Instructions: The light clock on the left is stationary,\n" +
  		"and it takes 4 seconds for the pulse of light to make a complete cycle.\n" +
  		"You can adjust the speed of the light clock on the right using the slider, which\n" +
  		"shows the clock's speed as a percentage of the speed of light.\n" +
  		"Goal: Set the speed so that the light clock on the right makes a complete cycle in 5 seconds.";

  private JLabel speedLabel = new JLabel();
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
    speedSlider.setMajorTickSpacing(5);
    speedSlider.setMinorTickSpacing(1);
    speedSlider.setSnapToTicks(true);
    speedSlider.setPaintTicks(true);
    speedSlider.setPaintLabels(true);
  }

  public Relativity() {
    initSlider();
    setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

    goButton.addActionListener(this);
    resetButton.addActionListener(this);
    speedSlider.addChangeListener(this);

    add(panel);

    JPanel buttonPanel = new JPanel();
    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
    buttonPanel.add(goButton);
    buttonPanel.add(resetButton);
    buttonPanel.add(speedSlider);
    buttonPanel.add(speedLabel);
    this.add(buttonPanel);
    this.add(box(new JTextArea(instructions)));

    reset();

    setSize(1024, 768);
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    setVisible(true);
  }

  public static double gamma(double speed) {
    return 1/sqrt(1 - speed * speed);
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

  @Override
  public void stateChanged(ChangeEvent e) {
    if (e.getSource() == speedSlider) {
      movingLightClock.setSpeed(((double) speedSlider.getValue())/100, 0);
      speedLabel.setText("v = " + ((double) speedSlider.getValue())/100 + "c");
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
    for (LightClock lc : simulationObjects) {
      lc.reset();
    }
    timer.stop();
    speedSlider.setEnabled(true);
    repaint();
  }

  public static void main(String[] args) {
    useDefaultLookAndFeel();
    useDialogExceptionHandler();
    new Relativity();
  }
}
