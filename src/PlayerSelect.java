import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Objects;

public class PlayerSelect extends JFrame implements ActionListener, FocusListener {
    //Fancy Border
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());

    //Class reveal functionality
    String[] classTitles = {"Class","Settler","Emperor","Serf","Farmer","Mountaineer","Shepard","Pirate","Woodsman","Gambler"};
    String[] colors = {"Building Color","Orange","Red","Blue","White"};
    String[] classDescriptions = {"A description of your class choice will be here.",
                                  "You are a normal Settler of Catan: you have access to all ports (if active), you receive the standard amount of resources, and you can build using standard prices. \n(Normal Mode)",
                                  "You are a tyrant with a mercurial temper and as such, your servile population fears you. To avoid death, they contribute to your resource stores frequently to ensure you are sated and pacified. As a result, when you produce resources, you produce twice as much. \n(Easy Mode)",
                                  "You are the lowest of the low. Society has long held you down, and today is no different. You're a serf, and everyone looks down on you. You are a laborer and people see you as such; as a result, no one goes easy on you. Everything costs you double as normal.  \n(Hard Mode)",
                                  "You have devoted yourself to the farm, and as a result, you produce twice as much grain and wool as normal. However, you are a stranger to building, and so you cannot produce brick or ore.",
                                  "You live in the mountains. Rocks and stones are more familiar than your family and friends. As a result, you produce twice as much brick and ore as normal. However, you cannot relate to the civilized world, and so you cannot produce grain and wool.",
                                  "You live amongst the sheep; you know nothing but sheep. You forgot common language, but you have sheep. As a result, you produce four times normal wool; but given your status as a depraved sheep-loving-hermit, you can produce nothing else.",
                                  "You've relegated yourself to seafaring and pillaging. That's right: you're a pirate. So, you are accustomed to haggling and barbarism; as a result, any time you would trade resources into the communal pool, it is a 1-for-1 deal. However, people don't trust you, so you must pay double for everything.",
                                  "You left society long ago to live in the woods. It's where you belong, and you would rather forsake your kind than to ever return. All you need is an axe and a forest. As a result, you produce four times lumber than normal, but you can produce nothing else.",
                                  "You are a gambler who plays fast and loose, and you don't know when to quit. Fortunately, you happen to be very lucky with your resources, and as such, produce double as normal. However, you don't always win. As a result, every time a roll is made, you have a 20% chance to lose one of every resource. Note that this effect can make you go into debt (i.e. you can have negative resources)."};
    //Frame Assets
    JPanel upperPanel = new JPanel(new GridLayout(1,2));
    JComboBox<String> classBox = new JComboBox<>();
    JTextArea descriptionArea = new JTextArea();
    JScrollPane descriptionPane = new JScrollPane(descriptionArea);
    JPanel descriptionPanel = new JPanel(new BorderLayout());
    JPanel lowerPanel = new JPanel(new GridLayout(1,2));
    JPanel holder = new JPanel(new BorderLayout());
    JLabel imageLabel = new JLabel("",SwingConstants.CENTER);
    JComboBox<String> colorBox = new JComboBox<String>();
    JTextField nameField = new JTextField("Name Your Player",SwingConstants.CENTER);
    JButton confirmButton = new JButton("    Confirm Character    ");
    JPanel southPanel = new JPanel(new BorderLayout());
    JLabel fillout = new JLabel("",SwingConstants.CENTER);
    Color color = new Color(100,100,100);

    //Global Variables
    BeginGame bgReference;
    int referenceNumber;

    public PlayerSelect(BeginGame bgReference, int referenceNumber){
        this.bgReference=bgReference;
        this.referenceNumber=referenceNumber;
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        this.add(holder);
            holder.add(upperPanel,BorderLayout.CENTER);
            holder.add(lowerPanel,BorderLayout.NORTH);
            upperPanel.add(descriptionPanel);
            descriptionPanel.add(classBox, BorderLayout.NORTH);
            classBox.setBorder(compound);
            classBox.addActionListener(this);
            descriptionPanel.add(descriptionPane,BorderLayout.CENTER);
            descriptionPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            descriptionPane.setBorder(compound);
            descriptionArea.setWrapStyleWord(true);
            descriptionArea.setBorder(compound);
            descriptionArea.setEditable(false);
            descriptionArea.setLineWrap(true);
            upperPanel.add(imageLabel);
            imageLabel.setBorder(compound);
            lowerPanel.add(nameField);
            nameField.setBorder(compound);
            nameField.addFocusListener(this);
            lowerPanel.add(colorBox);
            colorBox.setBorder(compound);
            colorBox.addActionListener(this);
            nameField.setForeground(color);
            descriptionArea.requestFocus();
        this.add(southPanel, BorderLayout.SOUTH);
            southPanel.add(confirmButton,BorderLayout.EAST);
                confirmButton.setBorder(compound);
                confirmButton.addActionListener(this);
                southPanel.add(fillout,BorderLayout.CENTER);
                    fillout.setBorder(compound);

        for (String classTitle : classTitles)
            classBox.addItem(classTitle);

        for (String s : colors)
            colorBox.addItem(s);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==classBox) {
            descriptionArea.setText(classDescriptions[classBox.getSelectedIndex()]);

            if(classTitles[classBox.getSelectedIndex()].equals("Gambler"))
                descriptionArea.select(0,0);

            imageLabel.setIcon(new ImageIcon("ClassTitles/" + classBox.getSelectedItem() +".jpg"));
        }
        else if(e.getSource()==confirmButton) {
            if (!nameField.getText().equals("Name Your Player")){
                //Generate new player
                if (classBox.getSelectedIndex() == 0 && colorBox.getSelectedIndex() != 0)
                    JOptionPane.showMessageDialog(this, "That isn't a valid class. Choose a class.", "Class Error", 3, new ImageIcon("Resources/Catan_Icon.png"));

                else if (classBox.getSelectedIndex() != 0 && colorBox.getSelectedIndex() == 0)
                    JOptionPane.showMessageDialog(this, "That isn't a valid color. Choose a color.", "Color Error", 3, new ImageIcon("Resources/Catan_Icon.png"));

                else if (classBox.getSelectedIndex() == 0 && colorBox.getSelectedIndex() == 0)
                    JOptionPane.showMessageDialog(this, "You didn't choose a valid color or a valid class. Choose both.", "Color & Class Error", 3, new ImageIcon("Resources/Catan_Icon.png"));

                else {
                    //Testing Player
                    //Player newPlayer = new Player(Objects.requireNonNull(colorBox.getSelectedItem()).toString(), nameField.getText(), Objects.requireNonNull(classBox.getSelectedItem()).toString(), new ArrayList<Index>(), new ArrayList<DevelopmentCard>(), new ArrayList<DevelopmentCard>(), 100, 100, 100, 100, 100, 0, false, false, false,referenceNumber);

                    Player newPlayer = new Player(Objects.requireNonNull(colorBox.getSelectedItem()).toString(), nameField.getText(), Objects.requireNonNull(classBox.getSelectedItem()).toString(), new ArrayList<Index>(), new ArrayList<DevelopmentCard>(), new ArrayList<DevelopmentCard>(), 0, 0, 0, 0, 0, 0, false, false, false,referenceNumber);
                    bgReference.addPlayer(newPlayer,referenceNumber);
                }
            }
            else
                JOptionPane.showMessageDialog(this,"You didn't name your character. Choose an appropriate name.","Name Error",3, new ImageIcon("Resources/Catan_Icon.png"));
        }
    }

    public void focusGained(FocusEvent e){
        if(e.getSource() == nameField && nameField.getText().equals("Name Your Player")){
            nameField.setText("");
            nameField.setForeground(Color.black);
        }
    }

    public void focusLost(FocusEvent e){
        if(e.getSource() == nameField && nameField.getText().equals("")){
            nameField.setText("Name Your Player");
            nameField.setForeground(color);
        }
    }
}