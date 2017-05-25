import javax.swing.*;
import java.awt.*;

public class GUICard {
    public JPanel card;
    private JButton button;
    private JLabel description;

    GUICard(JButton b, String description){
        this.button = b;
        this.description = new JLabel("<html>"+description);
        card = new JPanel();
        card.setLayout(new GridLayout(0,1));

        JPanel p1 = new JPanel(new FlowLayout());
        JPanel p2 = new JPanel(new FlowLayout());

        p1.add(this.button);
        p2.add(this.description);

        card.add(p1);
        card.add(p2);

    }

    public void updateDescription(String d){
        this.description.setText("<html>"+d);
    }


}
