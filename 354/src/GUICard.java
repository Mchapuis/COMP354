import javax.swing.*;
import java.awt.*;

public class GUICard {
	
    public JPanel card;
    private JButton button;
    private JLabel description;

    public GUICard(Card c){
    	card = new JPanel();
    	card.setPreferredSize(new Dimension(100, 120));
    	card.setBorder(BorderFactory.createLineBorder(Color.black));
    	card.setLayout(new GridLayout(0,1));
    	
        this.button = new JButton(c.getName());
        this.description = new JLabel("<html>" + c.getDescription());

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
