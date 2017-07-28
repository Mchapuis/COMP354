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
    private JButton doneButton = null;
    JLabel instructions = null;
    JPanel cardDescription = null;

    ArrayList<Card> cardsToSelectFrom = null;
    ArrayList<Integer> cardsSelected = null;
    int currentCard = 0;

    CardPickerWindow(String instructions, ArrayList<Card> cardsToSelectFrom, boolean fullscreen){
        cpFrame = new JFrame("Card Picker");
        prevButton = new JButton("Previous Card");
        nextButton = new JButton("Next Card");
        selectButton = new JButton("Select");
        doneButton = new JButton("Done");
        this.instructions = new JLabel("<html>"+instructions+"</html>");
        this.cardsToSelectFrom = cardsToSelectFrom;
        this.cardsSelected = new ArrayList<>();

        cpFrame.setResizable(false);
        cpFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        GameWindow.centreWindow(cpFrame, WINDOW_WIDTH, WINDOW_HEIGHT);

        if(fullscreen){
            cpFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            cpFrame.setUndecorated(true);
            cpFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        }

        prevButton.setEnabled(false);
        nextButton.setEnabled(cardsToSelectFrom.size() > 1);

        prevButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                currentCard--;
                showCard(currentCard);
                prevButton.setEnabled(currentCard > 0);
                nextButton.setEnabled(currentCard < cardsToSelectFrom.size() - 1);
                selectButton.setEnabled(! cardsSelected.contains(currentCard));
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
                selectButton.setEnabled(! cardsSelected.contains(currentCard));
            }
        });

        selectButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                selectButton.setEnabled(false);
                cardsSelected.add(currentCard);

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

        doneButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String side = "none";
                String type = "cardselector";
                int index = -1;

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
        p3.add(doneButton);
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
}
