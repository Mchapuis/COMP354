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
    public JPanel sidebar = null;
    public JLabel sidebarIndex = null;
    public JLabel sidebarTitle = null;
    public JLabel sidebarText = null;
    public JButton makeActiveButton = null;
    public JButton attachButton = null;
    public JButton attack1 = null;
    public JButton attack2 = null;
    public JButton attack3 = null;
    public JPanel playerLeftSidebar = null;
    public JPanel playerSide = null;
    
    public JPanel AIHandContainer = null;
    public JPanel AIBenchContainer = null;
    public JPanel AIActivePokemonContainer = null;
    public JPanel AILeftSidebar = null;
    public JPanel AISide = null;
    
    public JLabel instructions = null;
    private boolean hasSelectedActive = false;
    private boolean hasClickedAttach = false;
    private boolean hasAttachedEnergy = false;
    private int energySourceIndex;
    
    public class GenericButtonActionListener implements ActionListener{
    	
    	public void actionPerformed(ActionEvent e)
        {
        	String buttonName = e.getActionCommand();
        	JPanel buttonParent = (JPanel)((JButton)e.getSource()).getParent();
        	Component[] children = buttonParent.getComponents();
        	String description = "";
        
        	for (int i = 0; i < children.length; i++){
        		if (children[i] instanceof JLabel){
        			description = ((JLabel)children[i]).getText();
        		}
        	}
        	
        	makeActiveButton.setVisible(false);
    		attachButton.setVisible(false);
    		sidebarTitle.setText(buttonName);
        	sidebarText.setText(description);
    		
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
        	
        	sidebarIndex.setText(String.valueOf(index));
        	
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
        		//type = playerBench.get(index).getClass().toString();
        	}
        	
        	if (type.equals("class PokemonCard")){
        		attack1.setVisible(true);
        		attack2.setVisible(true);
        		attack3.setVisible(true);
        	}
        	
        	if (buttonParent.getParent().equals(playerActivePokemonContainer)){
        		attack1.setEnabled(true);
        		if (playerActivePokemon.attacks.size() > 1)
        			attack2.setEnabled(true);
        		if (playerActivePokemon.attacks.size() > 2)
        			attack3.setEnabled(true);
        	}
        	     	
        	if (!hasSelectedActive && type.equals("class PokemonCard")){
        		makeActiveButton.setVisible(true);
        	} else if (!hasClickedAttach && type.equals("class EnergyCard") && hasSelectedActive && !hasAttachedEnergy){
        		attachButton.setVisible(true);
        		energySourceIndex = index;
        	} else if (hasClickedAttach && type.equals("class PokemonCard")){
        		attachEnergy(targetLocation, targetIndex, energySourceIndex);
        	} else if (hasSelectedActive && hasAttachedEnergy){

        	}
        }
    	
    }
    
    public class AttackButtonActionListener implements ActionListener{
    	
    	public void actionPerformed(ActionEvent e){
    		int index;
    		if (e.getSource().equals(attack1))
    			index = 0;
    		else if (e.getSource().equals(attack2))
    			index = 1;
    		else
    			index = 2;
    		
    		boolean success = player.attack(index, autoPlayer);
    		
    		if (success){
    			updateAISide();
    			instructions.setText("Your turn is done.");
    			attack1.setVisible(false);
    			attack2.setVisible(false);
    			attack3.setVisible(false);
    		} else {
    			instructions.setText("Your pokemon doesn't have enough energy for that attack.");
    		}
    	}
    	
    }

   
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
        AISide.setPreferredSize(new Dimension(1050, 375));
        /*AISide.setBorder(BorderFactory.createLineBorder(Color.black));*/
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        mainFrame.add(AISide, constraints);
        
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
        playerSide.setPreferredSize(new Dimension(1050, 375));
        /*playerSide.setBorder(BorderFactory.createLineBorder(Color.black));*/
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 1;
        mainFrame.add(playerSide, constraints);
        
        sidebar = new JPanel(new GridBagLayout());
        sidebar.setPreferredSize(new Dimension(300, 750));
        sidebar.setBorder(BorderFactory.createLineBorder(Color.black));
        sidebarIndex = new JLabel();
        sidebarIndex.setVisible(false);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        sidebar.add(sidebarIndex, constraints);
        sidebarTitle = new JLabel();
        sidebarTitle.setPreferredSize(new Dimension(295, 15));
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 3;
		sidebar.add(sidebarTitle, constraints);
		sidebarText = new JLabel();
		sidebarText.setPreferredSize(new Dimension(295, 600));
		constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;
		sidebar.add(sidebarText, constraints);
		attack1 = new JButton("Attack 1");
		attack1.setEnabled(false);
		attack1.setVisible(false);
		attack1.addActionListener(new AttackButtonActionListener());
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		sidebar.add(attack1, constraints);
		attack2 = new JButton("Attack 2");
		attack2.setEnabled(false);
		attack2.setVisible(false);
		attack2.addActionListener(new AttackButtonActionListener());
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		sidebar.add(attack2, constraints);
		attack3 = new JButton("Attack 3");
		attack3.setEnabled(false);
		attack3.setVisible(false);
		attack3.addActionListener(new AttackButtonActionListener());
		constraints.gridx = 2;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		sidebar.add(attack3, constraints);
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
	    		
	    		instructions.setText("(Optional) Click on an energy card to select it. Then click \"Attach to a pokemon\" in the sidebar on the right. If you don't want to attach energy, click on your active pokemon to see its attacks.");
		    }
		});
		constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 3;
		sidebar.add(makeActiveButton, constraints);
		attachButton = new JButton("Attach to a pokemon");
		attachButton.setVisible(false);
		attachButton.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	hasClickedAttach = true;
	    		instructions.setText("Now click a pokemon you want to attach the energy card to.");
		    }
		});
		constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 3;
		sidebar.add(attachButton, constraints);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.gridwidth = 1;
        mainFrame.add(sidebar, constraints);
        
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
    
    public void updateAISide(){
    	updateAIActivePokemon();
    }
    
    public void updateAIActivePokemon(){
    	AIActivePokemonContainer.removeAll();
    	AIActivePokemon = autoPlayer.cardManager.getActivePokemon();
    	AIActivePokemonContainer.add(createJPanelFromCard(AIActivePokemon));
    	updateAIHand();
    }
    
    public void updateAIHand(){
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
    	
    	hasClickedAttach = false;
    	instructions.setText("Click on your active pokemon to see its attacks.");
    	
		sidebarTitle.setText("");
    	sidebarText.setText("");
    	attack1.setVisible(false);
		attack2.setVisible(false);
		attack3.setVisible(false);
    	hasAttachedEnergy = true;
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
    	button.addActionListener(new GenericButtonActionListener());
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
    	button.addActionListener(new GenericButtonActionListener());
    	JLabel description = new JLabel(descriptionString);
    	card.add(button);
    	card.add(description);
    	return card;
    }
}