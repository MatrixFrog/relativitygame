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

public class TunnelTest extends JFrame implements ActionListener {

  private Tunnel tunnel;
  private Timer timer = new Timer(5, this);
  private JButton leftGateToggleButton = new JButton("Toggle left gate");
  private JButton rightGateToggleButton = new JButton("Toggle right gate");

  private JPanel panel = new JPanel() {
    @Override
    public void paint(Graphics g) {
      super.paint(g);
      tunnel.paint(g);
    }
  };

  public TunnelTest(Tunnel tunnel) {
    setTitle(getClass().getSimpleName());
    this.tunnel = tunnel;

    setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
    add(panel);
    initButtons();

    setSize(800, 600);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    setVisible(true);
    timer.start();
  }

  private void initButtons() {
    leftGateToggleButton.addActionListener(this);
    rightGateToggleButton.addActionListener(this);
    add(leftGateToggleButton);
    add(rightGateToggleButton);
  }

  public static void main(String[] args) {
    useDefaultLookAndFeel();
    useDialogExceptionHandler();
    new TunnelTest(new Tunnel());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == timer) {
      tunnel.update();
      repaint();
    }
    else if (e.getSource() == leftGateToggleButton) {
      tunnel.toggleLeftGate();
    }
    else if (e.getSource() == rightGateToggleButton) {
      tunnel.toggleRightGate();
    }
  }

}
