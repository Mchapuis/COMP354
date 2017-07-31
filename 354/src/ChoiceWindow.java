import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChoiceWindow{
    final static int WINDOW_HEIGHT = 250;
    final static int WINDOW_WIDTH = 400;

    JFrame cpFrame = null;
    private JButton yesButton = null;
    private JButton noButton = null;
    JLabel instructions = null;

    ChoiceWindow(String instructions){
        cpFrame = new JFrame("Choice");
        yesButton = new JButton("Yes");
        noButton = new JButton("No");
        this.instructions = new JLabel("<html>"+instructions+"</html>");

        cpFrame.setResizable(false);
        cpFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        GameWindow.centreWindow(cpFrame, WINDOW_WIDTH, WINDOW_HEIGHT);

        if(GameEngine.displayGameInFullScreen){
            cpFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            cpFrame.setUndecorated(true);
            cpFrame.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
        }

        yesButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String side = "none";
                String type = "choice";

                synchronized(GameEngine.lock){
                    Message message = new Message(side, type, 1);
                    GameEngine.queue.add(message);
                    GameEngine.lock.notifyAll();
                }
            }
        });

        noButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                String side = "none";
                String type = "choice";

                synchronized(GameEngine.lock){
                    Message message = new Message(side, type, 0);
                    GameEngine.queue.add(message);
                    GameEngine.lock.notifyAll();
                }
            }
        });


        cpFrame.setSize(new Dimension(WINDOW_WIDTH,WINDOW_HEIGHT));
        cpFrame.setLayout(new GridBagLayout());

        JPanel p1 = new JPanel();
        p1.add(this.instructions);
        JPanel p3 = new JPanel();
        p3.add(yesButton);
        p3.add(noButton);

        GridBagConstraints c1 = new GridBagConstraints();
        c1.fill = GridBagConstraints.HORIZONTAL;
        c1.gridy = 0;
        c1.ipady = 100;
        c1.anchor = GridBagConstraints.NORTH;
        cpFrame.add(p1, c1);

        GridBagConstraints c3 = new GridBagConstraints();
        c3.fill = GridBagConstraints.HORIZONTAL;
        c3.gridy = 2;
        c3.anchor = GridBagConstraints.SOUTH;
        cpFrame.add(p3, c3);
    }

    public void display(){
        cpFrame.setVisible(true);
    }
    public void close(){
        cpFrame.setVisible(false);
    }

    public static void main(String[] args){
        ChoiceWindow w = new ChoiceWindow("instructions of some sort");
        w.display();
    }

}
