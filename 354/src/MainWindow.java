import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class MainWindow {
	public static Object lock;
    public static PriorityQueue<Message> queue;
	
    private static final int BENCH_SIZE = 5;
    private JFrame mainFrame = null;
    private Card displayedCard;
    
    private AIPlayer autoPlayer;
    private HumanPlayer player;

    private JPanel playerHandContainer = null;
    private JPanel playerBenchContainer = null;
    private JPanel playerActivePokemonContainer = null;
    private JPanel playerLeftSidebar = null;
    private JPanel playerSide = null;
    
    private JLabel instructions = null;
    private JPanel sidebar = null;
    private JLabel sidebarIndex = null;
    private JLabel sidebarTitle = null;
    private JLabel sidebarText = null;
    private JButton makeActiveButton = null;
    private JButton attachButton = null;
    private JButton attack1 = null;
    private JButton attack2 = null;
    private JButton attack3 = null;
    private JButton letAIPlay = null;
    
    private JPanel AIHandContainer = null;
    private JPanel AIBenchContainer = null;
    private JPanel AIActivePokemonContainer = null;
    private JPanel AILeftSidebar = null;
    private JPanel AISide = null;
    
    public class GenericButtonActionListener implements ActionListener{
    	
    	public void actionPerformed(ActionEvent e)
        {
    		String side;
    		String type;
    		int index = 0;
    		
    		JPanel buttonParent = (JPanel)((JButton)e.getSource()).getParent();
    		JPanel container = (JPanel)buttonParent.getParent();
    		
    		int i = 0;
        	Component[] cardsInContainer = container.getComponents();
        	for (Component c : cardsInContainer){
        		JPanel card = (JPanel)c;
        		Component[] cardComponents = card.getComponents();
        		if (cardComponents[0].equals(e.getSource())){
        			index = i;
        			break;
        		}
        		i++;
        	}
    		
    		if (container.equals(playerActivePokemonContainer)){
    			side = "player";
    			type = "active";
    		} else if (container.equals(AIActivePokemonContainer)){
    			side = "AI";
    			type = "active";
    		} else if (container.equals(playerBenchContainer)){
    			side = "player";
    			type = "bench";
    		} else if (container.equals(AIBenchContainer)){
    			side = "AI";
    			type = "bench";
    		} else if (container.equals(playerHandContainer)){
    			side = "player";
    			type = "hand";
    		} else {
    			side = "AI";
    			type = "hand";
    		}
    		
            synchronized(lock){
            	Message message = new Message(side, type, index);
                queue.add(message);
                lock.notifyAll();
            }
        }
    	
    }
    
    public class AttackButtonActionListener implements ActionListener{
    	
    	public void actionPerformed(ActionEvent e){
    		String side = "player";
    		String type = "attack";
    		int index;
    		
    		if (e.getSource().equals(attack1))
    			index = 0;
    		else if (e.getSource().equals(attack2))
    			index = 1;
    		else
    			index = 2;
    		
            synchronized(lock){
            	Message message = new Message(side, type, index);
                queue.add(message);
                lock.notifyAll();
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

        //active pokemon buttons
        playerActivePokemonContainer = new JPanel();
        playerActivePokemonContainer.add(createJPanelFromStrings("Undefined", "No description"));
        
        AIActivePokemonContainer = new JPanel();
        AIActivePokemonContainer.add(createJPanelFromStrings("Undefined", "No description"));

        AILeftSidebar = new JPanel();
        AILeftSidebar.setPreferredSize(new Dimension(200, 375));
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
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 1;
        constraints.gridy = 0;
        mainFrame.add(AISide, constraints);
        
        playerLeftSidebar = new JPanel();
        playerLeftSidebar.setPreferredSize(new Dimension(200, 375));
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
		    	String side = "player";
	    		String type = "makeactive";
	    		int index = 0;
	    		
	            synchronized(lock){
	            	Message message = new Message(side, type, index);
	                queue.add(message);
	                lock.notifyAll();
	            }
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
		    	String side = "player";
	    		String type = "attachenergy";
	    		int index = 0;
	    		
	            synchronized(lock){
	            	Message message = new Message(side, type, index);
	                queue.add(message);
	                lock.notifyAll();
	            }
		    }
		});
		constraints.gridx = 0;
        constraints.gridy = 5;
        constraints.gridwidth = 3;
		sidebar.add(attachButton, constraints);
		letAIPlay = new JButton("Let AI play");
		letAIPlay.setVisible(false);
		letAIPlay.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String side = "player";
	    		String type = "letAIplay";
	    		int index = 0;
	    		
	            synchronized(lock){
	            	Message message = new Message(side, type, index);
	                queue.add(message);
	                lock.notifyAll();
	            }
			}
		});
		constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 3;
        sidebar.add(letAIPlay, constraints);
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 2;
        constraints.gridy = 0;
        constraints.gridheight = 2;
        constraints.gridwidth = 1;
        mainFrame.add(sidebar, constraints);
        
        instructions = new JLabel("Instructions");
        instructions.setPreferredSize(new Dimension(1500, 30));
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
        for(Card c: player.getHand()){
            playerHandContainer.add(createJPanelFromCard(c));
        }

        //add buttons to AI side
        AISide.setLayout(new GridLayout(0,1));

        //hand
        AIHandContainer = new JPanel();
        AISide.add(AIHandContainer);
        for(Card c: autoPlayer.getHand()){
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
    
    public void updateInstructions(String text){
    	instructions.setText(text);
    }
    
    public void displayCard(Card card, boolean showMakeActive, boolean showAttachToPokemon, boolean showAttacks, boolean showLetAIPlay){
    	if (card != null) {
	    	sidebarTitle.setText(card.getName());
	    	sidebarText.setText(card.getDescription());
	    	
	    	if (showMakeActive){
	    		makeActiveButton.setVisible(true);
	    	} else {
	    		makeActiveButton.setVisible(false);
	    	}
	    	
	    	if (showAttachToPokemon){
	    		attachButton.setVisible(true);
	    	} else {
	    		attachButton.setVisible(false);
	    	}
	    	
	    	if (showAttacks){
	    		attack1.setVisible(true);
    			attack2.setVisible(true);
    			attack3.setVisible(true);
    			
    			attack1.setEnabled(true);
        		if (player.getActivePokemon().getAbilities().size() > 1)
        			attack2.setEnabled(true);
        		if (player.getActivePokemon().getAbilities().size() > 2)
        			attack3.setEnabled(true);
	    	} else {
	    		attack1.setVisible(false);
    			attack2.setVisible(false);
    			attack3.setVisible(false);
    			
    			attack1.setEnabled(false);
    			attack2.setEnabled(false);
    			attack3.setEnabled(false);
	    	}
	    	
	    	if (showLetAIPlay){
	    		letAIPlay.setVisible(true);
	    	} else {
	    		letAIPlay.setVisible(false);
	    	}
    	} else {
    		sidebarTitle.setText("Undefined");
	    	sidebarText.setText("No description");
    	}
    	displayedCard = card;
    	
    	sidebar.invalidate();
    	sidebar.validate();
    	sidebar.repaint();
    }
    
    public String getInstructions(){
    	return instructions.getText();
    }
    
    public Card getDisplayedCard(){
    	return this.displayedCard;
    }

    public void display(){
        mainFrame.setVisible(true);
    }
    
    public void updateAISide(){
    	updateAIActivePokemon();
    }
    
    public void updateAIActivePokemon(){
    	AIActivePokemonContainer.removeAll();
    	AIActivePokemonContainer.add(createJPanelFromCard(autoPlayer.getActivePokemon()));
    	updateAIHand();
    }
    
    public void updateAIHand(){
    	AIHandContainer.removeAll();
    	for (Card c : autoPlayer.getHand()){
    		AIHandContainer.add(createJPanelFromCard(c));
    	}
    	AIHandContainer.invalidate();
    	AIHandContainer.validate();
    	AIHandContainer.repaint();
    }
    
    public void setPlayerActivePokemon(){
    	playerActivePokemonContainer.removeAll();
    	playerActivePokemonContainer.add(createJPanelFromCard(player.getActivePokemon()));
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
    
    
    public void updatePlayerSide(){
    	updatePlayerActivePokemon();
    	updatePlayerHand();
    }
    
    public void updatePlayerActivePokemon(){
    	playerActivePokemonContainer.removeAll();
    	playerActivePokemonContainer.add(createJPanelFromCard(player.getActivePokemon()));
    	playerActivePokemonContainer.invalidate();
    	playerActivePokemonContainer.validate();
    	playerActivePokemonContainer.repaint();
    }
    
    public void updatePlayerHand(){
    	playerHandContainer.removeAll();
    	for (Card c : player.getHand()){
    		playerHandContainer.add(createJPanelFromCard(c));
    	}
    	playerHandContainer.invalidate();
    	playerHandContainer.validate();
    	playerHandContainer.repaint();
    }

    public JPanel createJPanelFromCard(Card c){
    	JPanel card = new JPanel();
    	card.setPreferredSize(new Dimension(100, 120));
    	card.setBorder(BorderFactory.createLineBorder(Color.black));
    	card.setLayout(new GridLayout(0,1));
    	
    	JButton button;
    	JLabel description;
    	
    	if (c != null){
	    	button = new JButton(c.getName());
	    	description = new JLabel(c.getDescription());
    	} else {
    		button = new JButton("Undefined");
    		description = new JLabel("No description");
    	}
    	button.addActionListener(new GenericButtonActionListener());
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