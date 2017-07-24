import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class CardPickerWindow{

    final static int WINDOW_HEIGHT = 850;
    final static int WINDOW_WIDTH = 450;

    JFrame cpFrame = null;
    private JButton prevButton = null;
    private JButton nextButton = null;
    private JButton selectButton = null;
    JLabel instructions = null;
    JPanel cardDescription = null;

    ArrayList<Card> cardsToSelectFrom = null;
    int currentCard = 0;

    CardPickerWindow(String instructions, ArrayList<Card> cardsToSelectFrom, boolean fullscreen){
        cpFrame = new JFrame("Card Picker");
        prevButton = new JButton("Previous Card");
        nextButton = new JButton("Next Card");
        selectButton = new JButton("Select");
        this.instructions = new JLabel(instructions);
        this.cardsToSelectFrom = cardsToSelectFrom;

        cpFrame.setResizable(false);
        cpFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        cpFrame.setUndecorated(true);
        cpFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        GameWindow.centreWindow(cpFrame, WINDOW_WIDTH, WINDOW_HEIGHT);

        if(fullscreen){
            cpFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        prevButton.setEnabled(false);

        prevButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                currentCard--;
                showCard(currentCard);
                prevButton.setEnabled(currentCard > 0);
                nextButton.setEnabled(currentCard < cardsToSelectFrom.size() - 1);
            }
        });

        nextButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                currentCard++;
                showCard(currentCard);
                prevButton.setEnabled(currentCard > 0);
                nextButton.setEnabled(currentCard < cardsToSelectFrom.size() - 1);
            }
        });

        selectButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String side = "none";
                String type = "cardselector";
                int index = currentCard;

                synchronized(GameEngine.lock){
                    Message message = new Message(side, type, index);
                    GameEngine.queue.add(message);
                    GameEngine.lock.notifyAll();
                }
            }
        });

        cpFrame.setSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
        cpFrame.setLayout(new GridBagLayout());

        JPanel p1 = new JPanel();
        p1.add(this.instructions);
        cardDescription = new JPanel(new GridBagLayout());
        cardDescription.add(new JLabel("empty :("));
        JPanel p3 = new JPanel();
        p3.add(prevButton);
        p3.add(selectButton);
        p3.add(nextButton);

        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridy = 0;
        c1.ipady = 100;
        c1.anchor = GridBagConstraints.NORTH;
        cpFrame.add(p1, c1);

        GridBagConstraints c2 = new GridBagConstraints();
        c2.fill = GridBagConstraints.BOTH;
        c2.gridy = 1;
        c2.weighty = 1;
        cpFrame.add(cardDescription, c2);

        GridBagConstraints c3 = new GridBagConstraints();
        c3.fill = GridBagConstraints.HORIZONTAL;
        c3.gridy = 2;
        c3.anchor = GridBagConstraints.SOUTH;
        cpFrame.add(p3, c3);

        if(this.cardsToSelectFrom != null && this.cardsToSelectFrom.size() > 0){
            showCard(0);
        }
    }

    public void display(){
        cpFrame.setVisible(true);
    }
    public void close(){
        cpFrame.setVisible(false);
    }

    private void showCard(int index){
        cardDescription.removeAll();
        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridy = 0;
        c1.anchor = GridBagConstraints.NORTH;
        cardDescription.add(new JLabel(cardsToSelectFrom.get(index).getName()),c1);
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridy = 1;
        c2.fill = GridBagConstraints.BOTH;
        c2.weighty = 1;
        cardDescription.add(new JLabel(cardsToSelectFrom.get(index).getDescription()),c2);

        cardDescription.repaint();
        cardDescription.validate();
    }



    public static void main(String [] args){
        Ability a1 = Ability.parseAbilitiesLine("Electroslug:dam:target:opponent-active:90");
        Ability a2 = Ability.parseAbilitiesLine("Bite:dam:target:opponent-active:10");
        Ability a3 = Ability.parseAbilitiesLine("Fury Attack:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40,cond:flip:dam:target:opponent-active:40");

        PokemonCard p = new PokemonCard();
        p.addAbility(a1);
        p.addAbility(a2);
        p.setName("poki");

        PokemonCard p2 = new PokemonCard();
        p2.addAbility(a3);
        p2.setName("mans");

        ArrayList<Card> list = new ArrayList<>();
        list.add(p);
        list.add(p2);

        CardPickerWindow cp = new CardPickerWindow("some instructions blah blah blah blah blah blah blah blah blah ", list, GameEngine.displayGameInFullScreen);
        cp.display();

    }
}
