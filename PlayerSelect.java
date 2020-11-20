import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerSelect extends JFrame implements ActionListener {
    //Fancy Border
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());

    //Class reveal functionality
    String[] classTitles = {"Class","Settler","Farmer","Mountaineer","Shepard","Pirate","Woodsman","Serf"};
    String[] colors = {"Building Color","Orange","Red","Blue","White"};
    String[] classDescriptions = {"A description of your class choice will be here.",
                                  "The standard character: you have access to all ports, can trade as normal, and build as normal. No bonuses or penalties here.",
                                  "You have devoted yourself to the farm, and as a result, you produce twice as much grain and wool as normal. However, you are a stranger to building, and so you cannot produce brick or ore.",
                                  "You live in the mountains. Rocks and stones are more familiar than your family and friends. As a result, you produce twice as much brick and ore as normal. However, you cannot relate to the civilized world, and so you cannot produce grain and wool.",
                                  "You live amongst the sheep; you know nothing but sheep. You forgot common language, but you have sheep. As a result, you produce four times normal wool; but given your status as a depraved sheep-loving-hermit, you can produce nothing else.",
                                  "You've relegated yourself to seafaring and pillaging. That's right: you're a pirate. So, you are accustomed to haggling and barbarism; as a result, any time you would trade resources into the communal pool, it is a 1-for-1 deal. However, people don't trust you, so you must pay double for everything.",
                                  "You left society long ago to live in the woods. It's where you belong, and you would rather forsake your kind than to ever return. All you need is an axe and a forest. As a result, you produce four times lumber than normal, but you can produce nothing else.",
                                  "You are the lowest of the low. Society has long held you down, and today is no different. You're a serf, and everyone looks down on you. You are a laborer and people see you as such; as a result, no one goes easy on you. Everything costs you double as normal."};

    //Frame Assets
    JPanel upperPanel = new JPanel(new GridLayout(1,2));
    JComboBox classBox = new JComboBox();
    JTextArea descriptionArea = new JTextArea("");
    JScrollPane descriptionPane = new JScrollPane(descriptionArea);
    JPanel descriptPanel = new JPanel(new BorderLayout());
    JPanel lowerPanel = new JPanel(new GridLayout(1,2));
    JPanel holder = new JPanel(new BorderLayout());
    JLabel imageLabel = new JLabel("",SwingConstants.CENTER);
    JComboBox colorBox = new JComboBox();
    JTextField nameField = new JTextField("",SwingConstants.CENTER);

    public PlayerSelect(){
        this.add(holder);
            holder.setBorder(compound);
            holder.add(upperPanel,BorderLayout.CENTER);
            upperPanel.setBorder(compound);
            holder.add(lowerPanel,BorderLayout.NORTH);
            upperPanel.add(descriptPanel);
            descriptPanel.add(classBox, BorderLayout.NORTH);
            classBox.setBorder(compound);
            classBox.addActionListener(this);
            descriptPanel.add(descriptionPane,BorderLayout.CENTER);
            descriptionPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setBorder(compound);
            descriptionArea.setLineWrap(true);
            upperPanel.add(imageLabel);
            imageLabel.setBorder(compound);
            lowerPanel.add(nameField);
            nameField.setBorder(compound);
            lowerPanel.add(colorBox);
            colorBox.setBorder(compound);
            colorBox.addActionListener(this);

        for(int x=0; x<classTitles.length; x++)
            classBox.addItem(classTitles[x]);

        for(int x=0;x<colors.length; x++)
            colorBox.addItem(colors[x]);

        this.setBounds(100,100,450,290);
        this.setVisible(true);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==classBox) {
            descriptionArea.setText(classDescriptions[classBox.getSelectedIndex()]);
            imageLabel.setIcon(new ImageIcon("ClassTitles/" + classBox.getSelectedItem() + (classBox.getSelectedItem().equals("Settler") || classBox.getSelectedItem().equals("Pirate")?".png":".jpg")));
        }
    }

    public static void main(String[] args){
        new PlayerSelect();
    }
}
