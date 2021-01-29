import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomSwitch extends JPanel implements MouseListener {
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
    JLabel title = new JLabel("",SwingConstants.CENTER);
    JButton on = new JButton();
    JButton off = new JButton();
    JPanel on_off = new JPanel(new GridLayout(1,2));
    boolean state = true;
    String text;
    public CustomSwitch(String text){
        this.text=text;
        on.setEnabled(false);
        off.setEnabled(false);

        this.setLayout(new BorderLayout());
        this.add(title, BorderLayout.NORTH);
        this.setBackground(Color.WHITE);

        this.add(on_off, BorderLayout.CENTER);
        on_off.add(on);
        on_off.add(off);

        on.setBackground(Color.green);
        off.setBackground(Color.WHITE);

        this.setBorder(compound);
        on.setBorder(new TitledBorder(""));
        title.setBorder(new TitledBorder(""));
        off.setBorder(new TitledBorder(""));

        off.addMouseListener(this);
        on.addMouseListener(this);

        title.setText(text+" - ON");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getSource()==on) {
            if(!on.isEnabled()) {
                on.setBackground(Color.green);
                off.setBackground(Color.WHITE);
                title.setText(text+" - ON");
                state=true;
            }
        }
        else if(e.getSource()==off) {
            if (!off.isEnabled()) {
                off.setBackground(Color.RED);
                on.setBackground(Color.WHITE);
                title.setText(text + " - OFF");
                state = false;
            }
        }
    }

    public boolean isSelected(){
        return state;
    }

    @Override
    public void mouseClicked(MouseEvent e){}
    public void mouseReleased(MouseEvent e){}
    public void mouseEntered(MouseEvent e){}
    public void mouseExited(MouseEvent e){}
}
