import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PlayerSelect extends JFrame implements ActionListener, FocusListener {
    //Fancy Border
    Border compound = BorderFactory.createCompoundBorder(BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder());
    
    //Class reveal functionality
    String[] classTitles = {"Character Class", "Arsonist", "Assassin","Brewer", "Cultivator","Deity","Emperor", "Farmer", "Gambler", "Highwayman", "Mountaineer", "Pirate", "Serf", "Settler", "Shepherd", "Woodsman"};
    String[] colors = {"Building Color","Blue", "Orange", "Red", "White"};
    String[] actions = {"",
            "Tile Arson - Remove Production",
            "No Trading - Remove Opponent Knights",
            "Confound - Cause Players to Fail Actions",
            "Cultivate - Double Tile Production",
            "Quadruple Resources - Control Roll",
            "Double Resources",
            "Double Sheep & Wheat - No Brick & Ore",
            "Double Resources - Chance of Net Loss",
            "Steal Action - Collect Stolen Resources",
            "Double Brick & Ore - No Sheep & Wheat",
            "Pillage Port - 1:1 TradeIn - Double Costs",
            "Every Cost is Doubled",
            "Normal Character - No Special Features",
            "Quadruple Sheep - No Other Resources",
            "Quadruple Lumber - No Other Resources"};

    String[] classDescriptions = {"",
            "You are an arsonist, and you leave a wake of destruction wherever you go. Naturally, you play fast and loose and frequently cause 'accidents'. As a result, once per turn, you may set fire to a tile, preventing it from getting resources for a turn. However, you must use a resource of the type the tile could produce as 'kindling' to start the fire. Note that a fire will last an entire turn cycle. Prepare to be hated.",
            "You are a murderer, and you've honed your skills through years of royal contracts. People, however, don't trust you because of your swarthy demeanor and empty eyes. As a result, you cannot trade with other players. But, your skills as a contract killer allow you to remove a single knight card from a random player at the cost of one random resource.",
            "You are a manufacturer of alcohol and you have a way with people. You are deceptive and can convince people of anything. As a result, you can confound other players and cause them to potentially fail their actions during their next turn. However, you need to create your brews at the cost of three wheat per confounding.",
            "You have a way with the land, and your hands till the earth quickly. The have a way with the earth; as a result, you can make a tile produce double its normal resources for the turn. But, your methods are risky in that they require some 'fertilizer'. You must pay one resource the tile could produce to get your methods off the ground.",
            "You are a literal god and you've come to Catan as an experiment to see how long it would take to completely rule over the land. Your mighty touch makes any tile prosper beyond reason and you will create with reckless abandon. All tiles produce quadruple the resources for you and you can control any roll you make. Prepare to win.\n(Super Easy Mode)",
            "You are a tyrant with a mercurial temper and as such, your servile population fears you. To avoid death, they contribute to your resource stores frequently to ensure you are sated and pacified. As a result, when you produce resources, you produce twice as much.\n(Easy Mode)",
            "You have devoted yourself to the farm, and as a result, you produce twice as much grain and wool as normal. However, you are a stranger to building, and so you cannot produce brick or ore.",
            "You are a gambler who plays fast and loose, and you don't know when to quit. Fortunately, you happen to be very lucky with your resources, and as such, produce double as normal. However, you don't always win. As a result, every time a roll is made, you have a 20% chance to lose one of every resource. Note that this effect can make you go into debt (i.e. you can have negative resources) and will therefore make you unable to trade with other players or ports, or make 4:1 exchanges.",
            "You are a highwayman, a creature that slithers around the underbelly of society. By day, you till the fields and cultivate your lands; by night, you rob the innocent on the open roads. As a result, whenever a robber steals from a player, you get the same resources that are stolen. Also, you may use your 'steal' action during your turn: doing so, you can steal from any player a resource of your choice. However, you may not produce during the next round of rolls. Also, if that player does not have the resource you choose, you get nothing. Finally, you cannot be stolen from.",
            "You live in the mountains. Rocks and stones are more familiar than your family and friends. As a result, you produce twice as much brick and ore as normal. However, you cannot relate to the civilized world, and so you cannot produce grain and wool.",
            "You've relegated yourself to seafaring and pillaging. That's right: you're a pirate. So, you are accustomed to haggling and barbarism; as a result, any time you would trade resources into the communal pool, it is a 1-for-1 deal. However, people don't trust you, so you must pay double for everything. But your pillaging and rampaging can do some damage: one per game, you can permanently remove a port.",
            "You are the lowest of the low. Society has long held you down, and today is no different. You're a serf, and everyone looks down on you. You are a laborer and people see you as such; as a result, no one goes easy on you. Everything costs you double as normal.\n(Hard Mode)",
            "You are a normal Settler of Catan: you have access to all ports (if active), you receive the standard amount of resources, and you can build using standard prices.\n(Normal Mode)",
            "You live amongst the sheep; you know nothing but sheep. You forgot common language, but you have sheep. As a result, you produce four times normal wool; and, once per game, you can permanently turn a random non-sheep producing tile into a sheep-producing tile at the cost of one sheep resource. But given your status as a depraved sheep-loving-hermit, you can produce nothing else.",
            "You left society long ago to live in the woods. It's where you belong, and you would rather forsake your kind than to ever return. All you need is an axe and a forest. As a result, you produce four times lumber than normal, but you can produce nothing else."};

    //Frame Assets
    JPanel upperPanel = new JPanel(new GridLayout(1,2));
    JComboBox<String> classBox = new JComboBox<>();
    JTextArea descriptionArea = new JTextArea();
    JScrollPane descriptionPane = new JScrollPane(descriptionArea);
    JPanel descriptionPanel = new JPanel(new BorderLayout());
    JPanel lowerPanel = new JPanel(new GridLayout(1,2));
    JPanel holder = new JPanel(new BorderLayout());
    JLabel imageLabel = new JLabel("",SwingConstants.CENTER);
    JComboBox<String> colorBox = new JComboBox<>();
    JTextField nameField = new JTextField("Name Your Player",SwingConstants.CENTER);
    JButton confirmButton = new JButton("    Confirm Character    ");
    JPanel southPanel = new JPanel(new BorderLayout());
    Color color = new Color(100,100,100);
    JLabel specialAbility = new JLabel("");

    //Global Variables
    BeginGame bgReference;
    int referenceNumber;
    boolean loadedIn=false;
    boolean submitted;

    public PlayerSelect(){}

    public PlayerSelect(BeginGame bgReference, int referenceNumber, boolean submitted){
        this.setResizable(false);
        this.bgReference=bgReference;
        this.referenceNumber=referenceNumber;
        this.submitted=submitted;
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
        southPanel.setOpaque(true);
            southPanel.setBackground(Color.WHITE);
            southPanel.add(confirmButton,BorderLayout.EAST);
                confirmButton.setBorder(compound);
                confirmButton.addActionListener(this);
                southPanel.add(specialAbility,BorderLayout.CENTER);
                    specialAbility.setBorder(compound);
                    specialAbility.setFont(new Font(imageLabel.getFont().getName(),Font.BOLD,11));
                    specialAbility.setBackground(Color.WHITE);
                    specialAbility.setOpaque(true);

        for (String classTitle : classTitles)
            classBox.addItem(classTitle);

        for (String s : colors)
            colorBox.addItem(s);
    }
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==classBox) {
            if (loadedIn) {
                if (bgReference.base.isSelected()) {
                    for (PlayerSelect player : bgReference.playerCreation) {
                        player.descriptionArea.setText(classDescriptions[classBox.getSelectedIndex()]);
                        player.classBox.setSelectedIndex(classBox.getSelectedIndex());

                        if (player.classTitles[classBox.getSelectedIndex()].equals("Gambler") || classTitles[classBox.getSelectedIndex()].equals("Highwayman") || player.classTitles[classBox.getSelectedIndex()].equals("Assassin") || player.classTitles[classBox.getSelectedIndex()].equals("Arsonist") || player.classTitles[classBox.getSelectedIndex()].equals("Cultivator")|| player.classTitles[classBox.getSelectedIndex()].equals("Deity")||player.classTitles[classBox.getSelectedIndex()].equals("Pirate")||player.classTitles[classBox.getSelectedIndex()].equals("Brewer"))
                            player.descriptionArea.select(0, 0);

                        player.imageLabel.setIcon(new ImageIcon((classBox.getSelectedIndex() == 0) ? "ClassTitles/Nameless.png" : "ClassTitles/" + classBox.getSelectedItem() + ".jpg"));
                        player.specialAbility.setText((classBox.getSelectedIndex()!=0)?" Ability: "+actions[classBox.getSelectedIndex()]:"");
                    }
                    return;
                }
            }
            descriptionArea.setText(classDescriptions[classBox.getSelectedIndex()]);

            if (!classTitles[classBox.getSelectedIndex()].equals("Gambler") && !classTitles[classBox.getSelectedIndex()].equals("Highwayman") && !classTitles[classBox.getSelectedIndex()].equals("Assassin") && !classTitles[classBox.getSelectedIndex()].equals("Arsonist") && !classTitles[classBox.getSelectedIndex()].equals("Cultivator") && !classTitles[classBox.getSelectedIndex()].equals("Deity") && !classTitles[classBox.getSelectedIndex()].equals("Pirate") && !classTitles[classBox.getSelectedIndex()].equals("Brewer")) {
                classBox.getSelectedIndex();
            }
            descriptionArea.select(0, 0);

            imageLabel.setIcon(new ImageIcon((classBox.getSelectedIndex() == 0) ? "ClassTitles/Nameless.png" : "ClassTitles/" + classBox.getSelectedItem() + ".jpg"));
            specialAbility.setText((classBox.getSelectedIndex()!=0)?" Ability: "+actions[classBox.getSelectedIndex()]:"");
        }
        else if(e.getSource()==confirmButton) {
            if (!nameField.getText().equals("Name Your Player")){
                //Generate new player
                if (classBox.getSelectedIndex() == 0 && colorBox.getSelectedIndex() != 0)
                    JOptionPane.showMessageDialog(this, "You must select your character's class.", "Class Error", 1, new ImageIcon("Resources/Catan_Icon.png"));

                else if (classBox.getSelectedIndex() != 0 && colorBox.getSelectedIndex() == 0)
                    JOptionPane.showMessageDialog(this, "You must select your character's color.", "Color Error", 1, new ImageIcon("Resources/Catan_Icon.png"));

                else if (classBox.getSelectedIndex() == 0 && colorBox.getSelectedIndex() == 0)
                    JOptionPane.showMessageDialog(this, "You must select your character's color and class.", "Color & Class Error", 1, new ImageIcon("Resources/Catan_Icon.png"));

                else {
                    //Testing Player
                    //Player newPlayer = new Player(Objects.requireNonNull(colorBox.getSelectedItem()).toString(), nameField.getText(), Objects.requireNonNull(classBox.getSelectedItem()).toString(), new ArrayList<Index>(), new ArrayList<DevelopmentCard>(), new ArrayList<DevelopmentCard>(), -100, 100, 100, 100, 100, 0, false, false, false,referenceNumber,99,99,99,false,0,false);

                    int before = bgReference.catanPlayerList.size();
                    Player newPlayer = new Player(Objects.requireNonNull(colorBox.getSelectedItem()).toString(), nameField.getText(), Objects.requireNonNull(classBox.getSelectedItem()).toString(), new ArrayList<Index>(), new ArrayList<DevelopmentCard>(), new ArrayList<DevelopmentCard>(), 0, 0, 0, 0, 0, 0, false, false, false,referenceNumber,4,5,15,false,0,false);
                    bgReference.addPlayer(newPlayer,referenceNumber);

                    if(bgReference.base.isSelected())
                        Arrays.stream(bgReference.playerCreation).forEach(player -> player.classBox.setEnabled(false));

                    if(before!=bgReference.catanPlayerList.size())
                        JOptionPane.showMessageDialog(bgReference,"A new character, "+newPlayer.getName()+" the "+newPlayer.getClassTitle()+", has been created.","Character Created",1,new ImageIcon("Resources/Catan_Icon.png"));
                    this.submitted=true;
                }
            }
            else
                JOptionPane.showMessageDialog(this,"You didn't name your character. Choose an appropriate name.","Name Error", JOptionPane.QUESTION_MESSAGE, new ImageIcon("Resources/Catan_Icon.png"));
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
