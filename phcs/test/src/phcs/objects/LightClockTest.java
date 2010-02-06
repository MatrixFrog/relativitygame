package phcs.objects;

import static util.swingutils.SwingUtils.useDefaultLookAndFeel;
import static util.swingutils.SwingUtils.useDialogExceptionHandler;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class LightClockTest extends JFrame implements ActionListener {

  private LightClock lightClock;
  private Timer timer = new Timer(5, this);
  private JButton flashButton = new JButton("Flash");

  private JPanel panel = new JPanel() {
    @Override
    public void paint(Graphics g) {
      super.paint(g);
      lightClock.paint(g);
    }
  };
  public LightClockTest(LightClock lightClock) {
    this.lightClock = lightClock;

    setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    add(panel);

    flashButton.addActionListener(this);
    add(flashButton);

    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setVisible(true);
    timer.start();
  }

  public static void main(String[] args) {
    useDefaultLookAndFeel();
    useDialogExceptionHandler();
    new LightClockTest(new LightClock());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == timer) {
      lightClock.update();
      repaint();
    }
    if (e.getSource() == flashButton) {
      lightClock.flash();
    }
  }

}
