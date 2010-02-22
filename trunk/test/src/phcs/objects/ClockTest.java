package phcs.objects;

import static util.swingutils.SwingUtils.useDefaultLookAndFeel;
import static util.swingutils.SwingUtils.useDialogExceptionHandler;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class ClockTest extends JFrame implements ActionListener {

  private Clock clock1 = new Clock(10, 10, 50, 50, 0, 0);
  private Clock clock2 = new Clock(10, 80, 50, 50, 0.3, 0);
  private Timer timer = new Timer(5, this);

  private JPanel panel = new JPanel() {
    @Override
    public void paint(Graphics g) {
      clock1.paint(g);
      clock2.paint(g);
    }
  };

  public ClockTest() {
    setTitle(getClass().getSimpleName());

    setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    add(panel);

    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setVisible(true);
    timer.start();
  }

  public static void main(String[] args) {
    useDefaultLookAndFeel();
    useDialogExceptionHandler();
    new ClockTest();
  }

  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == timer) {
      clock1.update();
      clock2.update();
      repaint();
    }
  }

}
