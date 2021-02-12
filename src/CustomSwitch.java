import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomSwitch extends JPanel implements MouseListener {
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
    JLabel title = new JLabel("",SwingConstants.CENTER);
    JTextField on = new JTextField("ON");
    JTextField off = new JTextField("OFF");
    JPanel on_off = new JPanel(new GridLayout(1,2));
    boolean state = true;
    String text;
    Color green = new Color(60,255,110);
    Color red = new Color(255,0,50);
    boolean locked = false;

    public CustomSwitch(String text){
        this.text=text;
        on.setEnabled(false);
        on.setHorizontalAlignment(JTextField.CENTER);
        on.setDisabledTextColor(Color.BLACK);
        on.setFont(new Font(this.getFont().getName(), Font.BOLD, this.getFont().getSize()));
        off.setEnabled(false);
        off.setHorizontalAlignment(JTextField.CENTER);
        off.setDisabledTextColor(Color.BLACK);
        off.setFont(new Font(this.getFont().getName(), Font.BOLD, this.getFont().getSize()));

        this.setLayout(new BorderLayout());
        this.add(title, BorderLayout.NORTH);
        this.setBackground(Color.WHITE);

        this.add(on_off, BorderLayout.CENTER);
        on_off.add(on);
        on_off.add(off);

        on.setBackground(green);
        off.setBackground(Color.WHITE);

        this.setBorder(compound);
        on.setBorder(new TitledBorder(""));
        title.setBorder(new TitledBorder(""));
        off.setBorder(new TitledBorder(""));

        off.addMouseListener(this);
        on.addMouseListener(this);

        title.setText(text);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!locked) {
            if (e.getSource() == on) {
                if (!on.isEnabled()) {
                    on.setBackground(green);
                    off.setBackground(Color.WHITE);
                    state = true;
                }
            } else if (e.getSource() == off) {
                if (!off.isEnabled()) {
                    off.setBackground(red);
                    on.setBackground(Color.WHITE);
                    state = false;
                }
            }
        }
    }

    public boolean isSelected(){
        return state;
    }

    public void fixState(){
        this.locked=true;
    }

    @Override
    public void mouseClicked(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}
