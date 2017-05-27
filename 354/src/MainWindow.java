import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainWindow {
    public static final int BENCH_SIZE = 5;
    public JFrame mainFrame = null;
    
    public AIPlayer autoPlayer;
    public HumanPlayer player;

    public ArrayList<PokemonCard> playerBench;
    public ArrayList<PokemonCard> AIBench;
    public ArrayList<Card> playerHand;
    public ArrayList<Card> AIHand;
    public PokemonCard playerActivePokemon;
    public PokemonCard AIActivePokemon;
    
    public JPanel playerHandContainer = null;
    public JPanel playerBenchContainer = null;
    public JPanel playerActivePokemonContainer = null;
    public JPanel playerSidebar = null;
    public JLabel playerSidebarIndex = null;
    public JLabel playerSidebarTitle = null;
    public JLabel playerSidebarText = null;
    public JButton makeActiveButton = null;
    public JButton attachButton = null;
    public JPanel playerLeftSidebar = null;
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
    private boolean hasSelectedActive = false;
    private boolean attachClicked = false;	
    private int energySourceIndex;
    
    public MainWindow(AIPlayer autoPlayer, HumanPlayer player){
    	this.autoPlayer = autoPlayer;
    	this.player = player;
    	
        //Set window properties
        mainFrame = new JFrame("354 Pokemon Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(new Dimension(1600, 850));
        mainFrame.setLayout(new GridBagLayout()); 
        
        GridBagConstraints constraints = new GridBagConstraints();

        //bench buttons
        playerBench = player.cardManager.bench;
        AIBench = autoPlayer.cardManager.bench;
        
        //active pokemon buttons
        playerActivePokemonContainer = new JPanel();
        playerActivePokemonContainer.add(createJPanelFromStrings("Undefined", "No description"));
        
        AIActivePokemonContainer = new JPanel();
        AIActivePokemonContainer.add(createJPanelFromStrings("Undefined", "No description"));

        //hand buttons
        playerHand = player.cardManager.hand;
        AIHand = autoPlayer.cardManager.hand;
        
        AILeftSidebar = new JPanel();
        AILeftSidebar.setPreferredSize(new Dimension(200, 375));
        /*AILeftSidebar.setBorder(BorderFactory.createLineBorder(Color.black));*/
        JPanel AIDiscard = createJPanelFromStrings("Discard", "");
        AILeftSidebar.add(AIDiscard);
        JPanel AIDeck = createJPanelFromStrings("Deck", "");
        AILeftSidebar.add(AIDeck);
        JPanel AIPrizeCards = createJPanelFromStrings("Prize cards", "");
        AILeftSidebar.add(AIPrizeCards);
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
        AISidebarTitle.setBorder(BorderFactory.createLineBorder(Color.red));
		AISidebar.add(AISidebarTitle);
		AISidebarText = new JLabel();
		AISidebar.add(AISidebarText);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 2;
        constraints.gridy = 0;
        mainFrame.add(AISidebar, constraints);
        
        playerLeftSidebar = new JPanel();
        playerLeftSidebar.setPreferredSize(new Dimension(200, 375));
        /*playerLeftSidebar.setBorder(BorderFactory.createLineBorder(Color.black));*/
        JPanel playerPrizeCards = createJPanelFromStrings("Prize cards", "");
        playerLeftSidebar.add(playerPrizeCards);
        JPanel playerDeck = createJPanelFromStrings("Deck", "");
        playerLeftSidebar.add(playerDeck);
        JPanel playerDiscard = createJPanelFromStrings("Discard", "");
        playerLeftSidebar.add(playerDiscard);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;
        mainFrame.add(playerLeftSidebar, constraints);
        
        playerSide = new JPanel();
        playerSide.setPreferredSize(new Dimension(1150, 375));
        /*playerSide.setBorder(BorderFactory.createLineBorder(Color.black));*/
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 1;
        mainFrame.add(playerSide, constraints);
        
        playerSidebar = new JPanel(new GridLayout(0, 1));
        playerSidebar.setPreferredSize(new Dimension(200, 375));
        playerSidebar.setBorder(BorderFactory.createLineBorder(Color.black));
        playerSidebarIndex = new JLabel();
        playerSidebarIndex.setVisible(false);
        playerSidebar.add(playerSidebarIndex);
        playerSidebarTitle = new JLabel();
		playerSidebar.add(playerSidebarTitle);
		playerSidebarText = new JLabel();
		playerSidebar.add(playerSidebarText);
		makeActiveButton = new JButton("Make Active Pokemon");
		makeActiveButton.setVisible(false);
		makeActiveButton.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	JPanel buttonParent = (JPanel)((JButton)e.getSource()).getParent();
	    		Component[] children = buttonParent.getComponents();
	    		
	    		int index = Integer.parseInt(((JLabel)children[0]).getText());
	    		setPlayerActivePokemon(index);
	    		removeFromPlayerHand(index);
	    		makeActiveButton.setVisible(false);
	    		hasSelectedActive = true;
	    		
	    		instructions.setText("Click on an energy card to select it. Then click \"Attach to a pokemon\" in the sidebar on the right.");
		    }
		});
		playerSidebar.add(makeActiveButton);
		attachButton = new JButton("Attach to a pokemon");
		attachButton.setVisible(false);
		attachButton.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	attachClicked = true;
	    		instructions.setText("Now click a pokemon you want to attach the energy card to.");
		    }
		});
		playerSidebar.add(attachButton);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 2;
        constraints.gridy = 1;
        mainFrame.add(playerSidebar, constraints);
        
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
        playerSide.add(playerActivePokemonContainer);

        //bench
        playerBenchContainer = new JPanel();
        playerSide.add(playerBenchContainer);
        for(int i = 0; i < BENCH_SIZE; i++){
            playerBenchContainer.add(createJPanelFromStrings("Undefined", "No description"));
        }

        //hand
        playerHandContainer = new JPanel();
        playerSide.add(playerHandContainer);
        for(Card c: playerHand){
            playerHandContainer.add(createJPanelFromCard(c));
        }

        //add buttons to AI side
        AISide.setLayout(new GridLayout(0,1));

        //hand
        AIHandContainer = new JPanel();
        AISide.add(AIHandContainer);
        for(Card c: AIHand){
            AIHandContainer.add(createJPanelFromCard(c));
        }

        //bench
        AIBenchContainer = new JPanel();
        AISide.add(AIBenchContainer);
        for(int i = 0; i < BENCH_SIZE; i++){
            AIBenchContainer.add(createJPanelFromStrings("Undefined", "No description"));
        }

        //active
        AISide.add(AIActivePokemonContainer);
    }

    public void display(){
        mainFrame.setVisible(true);
    }
    
    public void updateAIActivePokemon(AIPlayer autoPlayer){
    	AIActivePokemonContainer.removeAll();
    	AIActivePokemon = autoPlayer.cardManager.getActivePokemon();
    	AIActivePokemonContainer.add(createJPanelFromCard(AIActivePokemon));
    	updateAIHand(autoPlayer);
    }
    
    public void updateAIHand(AIPlayer autoPlayer){
    	AIHandContainer.removeAll();
    	AIHand = autoPlayer.cardManager.hand;
    	for (Card c : AIHand){
    		AIHandContainer.add(createJPanelFromCard(c));
    	}
    }
    
    public void setPlayerActivePokemon(int index){
    	playerActivePokemonContainer.removeAll();
    	playerActivePokemon = (PokemonCard)playerHand.get(index);
    	player.cardManager.setActivePokemon(playerActivePokemon);
    	playerActivePokemonContainer.add(createJPanelFromCard(playerActivePokemon));
    	playerActivePokemonContainer.invalidate();
    	playerActivePokemonContainer.validate();
    	playerActivePokemonContainer.repaint();
    }
    
    public void removeFromPlayerHand(int index){
    	playerHandContainer.remove(index);
    	playerHandContainer.invalidate();
    	playerHandContainer.validate();
    	playerHandContainer.repaint();
    }
    
    public void attachEnergy(String targetLocation, int targetIndex, int sourceIndex){
    	System.out.println(sourceIndex);
    	EnergyCard energy = (EnergyCard)playerHand.get(sourceIndex);
    	PokemonCard pokemon;
    	if (targetLocation.equals("active")){
    		pokemon = playerActivePokemon;
    	} else {
    		pokemon = (PokemonCard)playerHand.get(targetIndex);
    	}
    	player.cardManager.attachEnergy(energy, pokemon);
    	removeFromPlayerHand(sourceIndex);
    	
    	updatePlayerActivePokemon();
    	playerActivePokemonContainer.invalidate();
    	playerActivePokemonContainer.validate();
    	playerActivePokemonContainer.repaint();
    	
    	attachClicked = false;
    	instructions.setText("Click on your active pokemon to see its attacks.");
    	
		playerSidebarTitle.setText("");
    	playerSidebarText.setText("");
    }
    
    public void updatePlayerActivePokemon(){
    	playerActivePokemonContainer.removeAll();
    	playerActivePokemonContainer.add(createJPanelFromCard(playerActivePokemon));
    }

    public JPanel createJPanelFromCard(Card c){
    	JPanel card = new JPanel();
    	card.setPreferredSize(new Dimension(100, 120));
    	card.setBorder(BorderFactory.createLineBorder(Color.black));
    	card.setLayout(new GridLayout(0,1));
    	
    	JButton button = new JButton(c.getName());
    	button.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
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
	        		makeActiveButton.setVisible(false);
	        		attachButton.setVisible(false);
	        		playerSidebarTitle.setText(buttonName);
	            	playerSidebarText.setText(description);
	        		
	        		int index = -1;
	            	int i = 0;
	            	Component[] cardsInContainer = buttonParent.getParent().getComponents();
	            	for (Component c : cardsInContainer){
	            		JPanel card = (JPanel)c;
	            		Component[] cardComponents = card.getComponents();
	            		if (cardComponents[0].equals(e.getSource())){
	            			index = i;
	            		}
	            		i++;
	            	}
	            	
	            	playerSidebarIndex.setText(String.valueOf(index));
	            	
	            	String type = "";
	            	String targetLocation = "";
	            	int targetIndex = 0;
	            	if (buttonParent.getParent().equals(playerActivePokemonContainer)){
	            		type = playerActivePokemon.getClass().toString();
	            		targetLocation = "active";
	            		targetIndex = 0;
	            	} else if (buttonParent.getParent().equals(playerHandContainer)){
	            		type = playerHand.get(index).getClass().toString();
	            	} else {
	            		// deal with bench click here
	            	}
	            	     	
	            	if (!hasSelectedActive && type.equals("class PokemonCard")){
	            		makeActiveButton.setVisible(true);
	            	} else if (!attachClicked && type.equals("class EnergyCard")){
	            		attachButton.setVisible(true);
	            		energySourceIndex = index;
	            	} else if (attachClicked && type.equals("class PokemonCard")){
	            		attachEnergy(targetLocation, targetIndex, energySourceIndex);
	            	}
	        		
	        	}
		    }
		});
    	JLabel description = new JLabel(c.getDescription());
    	card.add(button);
    	card.add(description);
    	return card;
    }
    
    public JPanel createJPanelFromStrings(String buttonName, String descriptionString){
    	JPanel card = new JPanel();
    	card.setPreferredSize(new Dimension(100, 120));
    	card.setBorder(BorderFactory.createLineBorder(Color.black));
    	card.setLayout(new GridLayout(0,1));
    	
    	JButton button = new JButton(buttonName);
    	button.addActionListener(new ActionListener()
		{
    		public void actionPerformed(ActionEvent e)
		    {
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
	        		makeActiveButton.setVisible(false);
	        		attachButton.setVisible(false);
	        		
	        		int index = -1;
	            	int i = 0;
	            	Component[] cardsInContainer = buttonParent.getParent().getComponents();
	            	for (Component c : cardsInContainer){
	            		JPanel card = (JPanel)c;
	            		Component[] cardComponents = card.getComponents();
	            		if (cardComponents[0].equals(e.getSource())){
	            			index = i;
	            		}
	            		i++;
	            	}
	            	
	            	playerSidebarIndex.setText(String.valueOf(index));
	            	
	            	String type = "";
	            	String targetLocation = "";
	            	int targetIndex = 0;
	            	if (buttonParent.getParent().equals(playerActivePokemonContainer)){
	            		type = playerActivePokemon.getClass().toString();
	            		targetLocation = "active";
	            		targetIndex = 0;
	            	} else if (buttonParent.getParent().equals(playerHandContainer)){
	            		type = playerHand.get(index).getClass().toString();
	            	} else {
	            		// deal with bench click here
	            	}
	            	     	
	            	if (!hasSelectedActive && type.equals("class PokemonCard")){
	            		makeActiveButton.setVisible(true);
	            	} else if (!attachClicked && type.equals("class EnergyCard")){
	            		attachButton.setVisible(true);
	            		energySourceIndex = index;
	            	} else if (attachClicked && type.equals("class PokemonCard")){
	            		attachEnergy(targetLocation, targetIndex, energySourceIndex);
	            	}
	        		
	        		playerSidebarTitle.setText(buttonName);
	            	playerSidebarText.setText(description);
	        	}
		    }
		});
    	JLabel description = new JLabel(descriptionString);
    	card.add(button);
    	card.add(description);
    	return card;
    }
}
