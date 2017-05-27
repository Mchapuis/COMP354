import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainWindow implements ActionListener {
    public static final int BENCH_SIZE = 5;
    public enum Side{
            player,
            ai
    }
    public enum Location{
        hand,
        bench,
        active
    }

    JFrame mainFrame = null;

    public ArrayList<GUICard> playerBench = null;
    public ArrayList<GUICard> AIBench = null;

    public GUICard playerActivePokemon = null;
    public GUICard AIActivePokemon = null;

    public ArrayList<GUICard> playerHand = null;
    public ArrayList<GUICard> AIHand = null;

    public JPanel pHandContainer = null;
    public JPanel pBenchContainer = null;
    public JPanel pActivePokemonContainer = null;
    public JPanel pSidebar = null;
    public JLabel pSidebarTitle = null;
    public JLabel pSidebarText = null;
    public JPanel pLeftSidebar = null;
    public JPanel playerSide = null;
    public JPanel AIHandContainer = null;
    public JPanel AIBenchContainer = null;
    public JPanel AIActivePokemonContainer = null;
    public JPanel AISidebar = null;
    public JLabel AISidebarTitle = null;
    public JLabel AISidebarText = null;
    public JPanel AILeftSidebar = null;
    public JPanel AISide = null;
    public JLabel instructions = null;

    MainWindow(){
        //Set window properties
        mainFrame = new JFrame("354 Pokemon Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(new Dimension(1600, 850));
        mainFrame.setLayout(new GridBagLayout()); 
        
        GridBagConstraints constraints = new GridBagConstraints();

        //bench buttons
        playerBench = new ArrayList<GUICard>();
        AIBench = new ArrayList<GUICard>();
        
        for(int i = 0; i < BENCH_SIZE; i++){
        	playerBench.add(new GUICard(new PokemonCard()));
        	AIBench.add(new GUICard(new PokemonCard()));
        }

        //active pokemon buttons
        playerActivePokemon = new GUICard(new PokemonCard());
        playerActivePokemon.button.addActionListener(this);
        pActivePokemonContainer = new JPanel();
        pActivePokemonContainer.add(playerActivePokemon.cardDisplay);
        AIActivePokemon = new GUICard(new PokemonCard());
        AIActivePokemon.button.addActionListener(this);
        AIActivePokemonContainer = new JPanel();
        AIActivePokemonContainer.add(AIActivePokemon.cardDisplay);

        //hand buttons
        playerHand = new ArrayList<GUICard>();
        AIHand = new ArrayList<GUICard>();
        for(int i = 0; i < 7; i++){
            playerHand.add(new GUICard(new PokemonCard()));
            AIHand.add(new GUICard(new PokemonCard()));
        }

        
        AILeftSidebar = new JPanel();
        AILeftSidebar.setPreferredSize(new Dimension(200, 375));
        /*AILeftSidebar.setBorder(BorderFactory.createLineBorder(Color.black));*/
        GUICard AIDiscard = new GUICard("Discard", "");
        AILeftSidebar.add(AIDiscard.cardDisplay);
        GUICard AIDeck = new GUICard("Deck", "");
        AILeftSidebar.add(AIDeck.cardDisplay);
        GUICard AIPrizeCards = new GUICard("Prize cards", "");
        AILeftSidebar.add(AIPrizeCards.cardDisplay);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 0;
        mainFrame.add(AILeftSidebar, constraints);
        
        AISide = new JPanel();
        AISide.setPreferredSize(new Dimension(1150, 375));
        /*AISide.setBorder(BorderFactory.createLineBorder(Color.black));*/
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        mainFrame.add(AISide, constraints);
        
        AISidebar = new JPanel(new GridLayout(0, 1));
        AISidebar.setPreferredSize(new Dimension(200, 375));
        AISidebar.setBorder(BorderFactory.createLineBorder(Color.black));
        AISidebarTitle = new JLabel();
		AISidebar.add(AISidebarTitle);
		AISidebarText = new JLabel();
		AISidebar.add(AISidebarText);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 2;
        constraints.gridy = 0;
        mainFrame.add(AISidebar, constraints);
        
        pLeftSidebar = new JPanel();
        pLeftSidebar.setPreferredSize(new Dimension(200, 375));
        /*pLeftSidebar.setBorder(BorderFactory.createLineBorder(Color.black));*/
        GUICard pPrizeCards = new GUICard("Prize cards", "");
        pLeftSidebar.add(pPrizeCards.cardDisplay);
        GUICard pDeck = new GUICard("Deck", "");
        pLeftSidebar.add(pDeck.cardDisplay);
        GUICard pDiscard = new GUICard("Discard", "");
        pLeftSidebar.add(pDiscard.cardDisplay);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        mainFrame.add(pLeftSidebar, constraints);
        
        playerSide = new JPanel();
        playerSide.setPreferredSize(new Dimension(1150, 375));
        /*playerSide.setBorder(BorderFactory.createLineBorder(Color.black));*/
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 1;
        mainFrame.add(playerSide, constraints);
        
        pSidebar = new JPanel(new GridLayout(0, 1));
        pSidebar.setPreferredSize(new Dimension(200, 375));
        pSidebar.setBorder(BorderFactory.createLineBorder(Color.black));
        pSidebarTitle = new JLabel();
		pSidebar.add(pSidebarTitle);
		pSidebarText = new JLabel();
		pSidebar.add(pSidebarText);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 2;
        constraints.gridy = 1;
        mainFrame.add(pSidebar, constraints);
        
        instructions = new JLabel("Instructions");
        instructions.setPreferredSize(new Dimension(1500, 30));
        /*instructions.setBorder(BorderFactory.createLineBorder(Color.black));*/
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
        mainFrame.add(instructions, constraints);

        //add buttons to player side
        playerSide.setLayout(new GridLayout(0,1));

        //active
        playerSide.add(pActivePokemonContainer);

        //bench
        pBenchContainer = new JPanel();
        playerSide.add(pBenchContainer);
        for(GUICard c : playerBench){
            pBenchContainer.add(c.cardDisplay);
            c.button.addActionListener(this);
        }

        //hand
        pHandContainer = new JPanel();
        playerSide.add(pHandContainer);
        for(GUICard c: playerHand){
            pHandContainer.add(c.cardDisplay);
            c.button.addActionListener(this);
        }

        //add buttons to AI side
        AISide.setLayout(new GridLayout(0,1));

        //hand
        AIHandContainer = new JPanel();
        AISide.add(AIHandContainer);
        for(GUICard c: AIHand){
            AIHandContainer.add(c.cardDisplay);
            c.button.addActionListener(this);
        }

        //bench
        AIBenchContainer = new JPanel();
        AISide.add(AIBenchContainer);
        for(GUICard c : AIBench){
            AIBenchContainer.add(c.cardDisplay);
            c.button.addActionListener(this);
        }

        //active
        AISide.add(AIActivePokemonContainer);
    }

    public void display(){
        mainFrame.setVisible(true);
    }

    public void addCard(){

    }

    public void removeCard(){

    }
    
    @Override
	public void actionPerformed(ActionEvent e) {
    	String buttonName = e.getActionCommand();
    	JPanel buttonParent = (JPanel)((JButton)e.getSource()).getParent();
    	JPanel containingPanel = (JPanel)(buttonParent.getParent().getParent());
    	Component[] children = buttonParent.getComponents();
    	String description = "";
    	
    	for (int i = 0; i < children.length; i++){
    		if (children[i] instanceof JLabel){
    			description = ((JLabel)children[i]).getText();
    		}
    	}
    	
    	if (containingPanel.equals(AISide)){
    		AISidebarTitle.setText(buttonName);
        	AISidebarText.setText(description);
    	} else {
    		pSidebarTitle.setText(buttonName);
        	pSidebarText.setText(description);
    	}
	}
    
    public void updateAIHandContainer(){
    	AIHandContainer.removeAll();    	
    	for (GUICard card : AIHand){
    		AIHandContainer.add(card.cardDisplay);
            card.button.addActionListener(this);
    	}
    }
    
    public void updatePlayerHandContainer(){
    	pHandContainer.removeAll();    	
    	for (GUICard card : playerHand){
    		pHandContainer.add(card.cardDisplay);
            card.button.addActionListener(this);
    	}
    }

}
