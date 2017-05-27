import javax.swing.*;
import java.awt.*;

public class GUICard {
	
    public JPanel cardDisplay;
    public JButton button;
    public JLabel description;
    public Card card;

    public GUICard(Card c){
    	this.card = c;
    	
    	cardDisplay = new JPanel();
    	cardDisplay.setPreferredSize(new Dimension(100, 120));
    	cardDisplay.setBorder(BorderFactory.createLineBorder(Color.black));
    	cardDisplay.setLayout(new GridLayout(0,1));
    	
        this.button = new JButton(c.getName());
        
        this.description = new JLabel(c.getDescription());

        cardDisplay.add(this.button);
        cardDisplay.add(this.description);
    }
    
    public GUICard(String buttonText, String description){
    	this.card = null;
    	
    	cardDisplay = new JPanel();
    	cardDisplay.setPreferredSize(new Dimension(100, 120));
    	cardDisplay.setBorder(BorderFactory.createLineBorder(Color.black));
    	cardDisplay.setLayout(new GridLayout(0,1));
    	
        this.button = new JButton(buttonText);
        this.description = new JLabel(description);

        cardDisplay.add(this.button);
        cardDisplay.add(this.description);
    }

}
