import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomSwitch extends JPanel implements MouseListener {
    Border etched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);

    JLabel title = new JLabel("",SwingConstants.CENTER);
    JTextField on = new JTextField("ON");
    JTextField off = new JTextField("OFF");
    JPanel on_off = new JPanel(new GridLayout(1,2));
    boolean state;
    String text;
    Color green = new Color(60,255,110);
    Color red = new Color(255,0,50);
    boolean locked = false;

    public CustomSwitch(String text, boolean startState){
        this.text=text;
        this.state=startState;
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

        if(state) {
            on.setBackground(green);
            off.setBackground(Color.WHITE);
        }
        else{
            on.setBackground(Color.WHITE);
            off.setBackground(red);
        }

        this.setBorder(etched);
        on.setBorder(new BevelBorder(BevelBorder.LOWERED));
        title.setBorder(etched);
        off.setBorder(etched);

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
                    off.setBorder(etched);
                    on.setBorder(new BevelBorder(BevelBorder.LOWERED));
                    state = true;
                }
            } else if (e.getSource() == off) {
                if (!off.isEnabled()) {
                    off.setBackground(red);
                    on.setBackground(Color.WHITE);
                    on.setBorder(etched);
                    off.setBorder(new BevelBorder(BevelBorder.LOWERED));
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
        on_off.remove((state)?off:on);
        on_off.revalidate();
    }

    @Override
    public void mouseClicked(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}
