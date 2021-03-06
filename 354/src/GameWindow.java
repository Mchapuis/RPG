import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GameWindow {
	public static Object lock;
    public static Queue<Message> queue;
	
    private static final int BENCH_SIZE = 5;
    private JFrame mainFrame = null;
    private Card displayedCard;
    
    private AIPlayer autoPlayer;
    private HumanPlayer player;

    private JPanel playerHandContainer = null;
    private JPanel playerBenchContainer = null;
    private JPanel playerActivePokemonContainer = null;
    private JPanel playerLeftSidebar = null;
    private JPanel playerDiscard = null;
    private JPanel playerDeck = null;
    private JPanel playerPrizeCards = null;
    private JPanel playerSide = null;
    
    private JLabel instructions = null;
    private JPanel sidebar = null;
    private JLabel sidebarIndex = null;
    private JLabel sidebarTitle = null;
    private JLabel sidebarText = null;
    private JButton makeActiveButton = null;
    private JButton addToBenchButton = null;
    private JButton attachButton = null;
    private JButton attack1 = null;
    private JButton attack2 = null;
    private JButton attack3 = null;
    private JButton letAIPlay = null;
    private JButton retreatButton = null;
    private JButton playItemButton = null;
    private JButton evolveButton = null;
	private JButton selectButton = null;
    
    private JPanel AIHandContainer = null;
    private JPanel AIBenchContainer = null;
    private JPanel AIActivePokemonContainer = null;
    private JPanel AILeftSidebar = null;
    private JPanel AIDiscard = null;
    private JPanel AIDeck = null;
    private JPanel AIPrizeCards = null;
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
    		} else if (container.equals(AIHandContainer)){
    			side = "AI";
    			type = "hand";
    		} else {
    			if (buttonParent.equals(AIDeck)){
    				side = "AI";
    				type = "deck";
    			} else if (buttonParent.equals(AIDiscard)){
    				side = "AI";
    				type = "discard";
    			} else if (buttonParent.equals(AIPrizeCards)){
    				side = "AI";
    				type = "prizecards";
    			} else if (buttonParent.equals(playerDeck)){
    				side = "player";
    				type = "deck";
    			} else if (buttonParent.equals(playerDiscard)){
    				side = "player";
    				type = "discard";
    			} else {
    				side = "player";
    				type = "prizecards";
    			}
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

    //Constructor
    public GameWindow(AIPlayer autoPlayer, HumanPlayer player, boolean fullscreen){
    	this.autoPlayer = autoPlayer;
    	this.player = player;
    	
        //Set window properties
        mainFrame = new JFrame("354 Pokemon Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(new Dimension(1800, 850));
        mainFrame.setResizable(false);
        mainFrame.setLayout(new GridBagLayout());

		if(fullscreen){
			mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			mainFrame.setUndecorated(true);
		}


        GridBagConstraints constraints = new GridBagConstraints();

        //active pokemon buttons
        playerActivePokemonContainer = new JPanel();
        playerActivePokemonContainer.add(createJPanelFromStrings("", "No description"));
        
        AIActivePokemonContainer = new JPanel();
        AIActivePokemonContainer.add(createJPanelFromStrings("", "No description"));

        AILeftSidebar = new JPanel();
        AILeftSidebar.setPreferredSize(new Dimension(200, 375));
        AIDiscard = createJPanelFromPile(autoPlayer.getDiscard(), "Discard");
        AILeftSidebar.add(AIDiscard);
        AIDeck = createJPanelFromPile(autoPlayer.getDeck(), "Deck");
        AILeftSidebar.add(AIDeck);
        AIPrizeCards = createJPanelFromPile(autoPlayer.getPrizeCards(), "Prize Cards");
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
        playerPrizeCards = createJPanelFromPile(player.getPrizeCards(), "Prize Cards");
        playerLeftSidebar.add(playerPrizeCards);
        playerDeck = createJPanelFromPile(player.getDeck(), "Deck");
        playerLeftSidebar.add(playerDeck);
        playerDiscard = createJPanelFromPile(player.getDiscard(), "Discard");
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
        sidebarTitle.setHorizontalAlignment(SwingConstants.CENTER);
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
		attack1.setEnabled(true);
		attack1.setVisible(false);
		attack1.addActionListener(new AttackButtonActionListener());
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		sidebar.add(attack1, constraints);
		
		attack2 = new JButton("Attack 2");
		attack2.setEnabled(true);
		attack2.setVisible(false);
		attack2.addActionListener(new AttackButtonActionListener());
		constraints.gridx = 1;
		constraints.gridy = 3;
		constraints.gridwidth = 1;
		sidebar.add(attack2, constraints);
		
		attack3 = new JButton("Attack 3");
		attack3.setEnabled(true);
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
		
		addToBenchButton = new JButton("Add pokemon to bench");
		addToBenchButton.setVisible(false);
		addToBenchButton.addActionListener(new ActionListener()
		{
		    public void actionPerformed(ActionEvent e)
		    {
		    	String side = "player";
	    		String type = "addtobench";
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
		sidebar.add(addToBenchButton, constraints);
		
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

        retreatButton = new JButton("Retreat pokemon");
		retreatButton.setVisible(false);
		retreatButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String side = "player";
	    		String type = "retreat";
	    		int index = 0;
	    		
	            synchronized(lock){
	            	Message message = new Message(side, type, index);
	                queue.add(message);
	                lock.notifyAll();
	            }
			}
		});
		constraints.gridx = 0;
        constraints.gridy = 7;
        constraints.gridwidth = 3;
        sidebar.add(retreatButton, constraints);
        
        playItemButton = new JButton("Play item");
		playItemButton.setVisible(false);
		playItemButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String side = "player";
	    		String type = "playitem";
	    		int index = 0;
	    		
	            synchronized(lock){
	            	Message message = new Message(side, type, index);
	                queue.add(message);
	                lock.notifyAll();
	            }
			}
		});
		constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = 3;
        sidebar.add(playItemButton, constraints);
        
        evolveButton = new JButton("Evolve");
		evolveButton.setVisible(false);
		evolveButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String side = "player";
	    		String type = "evolve";
	    		int index = 0;
	    		
	            synchronized(lock){
	            	Message message = new Message(side, type, index);
	                queue.add(message);
	                lock.notifyAll();
	            }
			}
		});
		constraints.gridx = 0;
        constraints.gridy = 8;
        constraints.gridwidth = 3;
        sidebar.add(evolveButton, constraints);

		selectButton = new JButton("Select");
		selectButton.setVisible(false);
		selectButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				String side = "player";
				String type = "select";
				int index = 0;

				synchronized(lock){
					Message message = new Message(side, type, index);
					queue.add(message);
					lock.notifyAll();
				}
			}
		});
		constraints.gridx = 0;
		constraints.gridy = 8;
		constraints.gridwidth = 3;
		sidebar.add(selectButton, constraints);

		letAIPlay = new JButton("End Turn");
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
		constraints.gridy = 9;
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
            playerBenchContainer.add(createJPanelFromStrings("", "No description"));
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
            AIBenchContainer.add(createJPanelFromStrings("", "No description"));
        }

        //active
        AISide.add(AIActivePokemonContainer);
    }

	public void display(){
		mainFrame.setVisible(true);
	}
	public void close(){
		mainFrame.setVisible(false);
	}

	//Update GUI information entities
    public void updateInstructions(String text){
    	instructions.setText("             " + text);
	}
    public void displayCard(Card card, boolean showMakeActive, boolean showAddToBench, boolean showAttachToPokemon, boolean showAttacks, boolean showLetAIPlay, boolean showRetreat, boolean showPlayItem, boolean showEvolve, boolean showSelect){
    	if (card != null) {
	    	sidebarTitle.setText(card.getName());
	    	sidebarText.setText(card.getDescription());
	    	
	    	makeActiveButton.setVisible(showMakeActive);
	    	addToBenchButton.setVisible(showAddToBench);
	    	attachButton.setVisible(showAttachToPokemon);
			attack1.setVisible(showAttacks);
			attack2.setVisible(showAttacks && player.getActivePokemon().getAbilities().size() > 1);
			attack3.setVisible(showAttacks && player.getActivePokemon().getAbilities().size() > 2);
	    	letAIPlay.setVisible(showLetAIPlay);
	    	playItemButton.setVisible(showPlayItem);
	    	retreatButton.setVisible(showRetreat);
	    	evolveButton.setVisible(showEvolve);
	    	selectButton.setVisible(showSelect);
    	} else {
    		sidebarTitle.setText("");
	    	sidebarText.setText("");
    	}
    	
    	displayedCard = card;
    	
    	sidebar.invalidate();
    	sidebar.validate();
    	sidebar.repaint();
    }
    public void displayCard(Message m){
        Card cardToBeDisplayed = null;
        CardManager sourceCardManager = null;
        boolean isPlayers = false;

        if(m.getSide() == Message.Side.PLAYER){
           sourceCardManager = player.cardManager;
           isPlayers = true;
        }
        else{
            sourceCardManager = autoPlayer.cardManager;
        }

        boolean isCat1Pokemon;
        switch(m.getType()){
            case ACTIVE:
                cardToBeDisplayed = sourceCardManager.getActivePokemon();
                isCat1Pokemon = cardToBeDisplayed instanceof PokemonCard && ((PokemonCard) cardToBeDisplayed).getCat() == PokemonCard.Category.BASIC;
                displayCard(cardToBeDisplayed, false, false, false, isPlayers, true, isPlayers, false, false,false);
                break;
            case BENCH:
                if(m.getIndex() < sourceCardManager.getBench().size()){
                    cardToBeDisplayed = sourceCardManager.getBench().get(m.getIndex());
                    isCat1Pokemon = cardToBeDisplayed instanceof PokemonCard && ((PokemonCard) cardToBeDisplayed).getCat() == PokemonCard.Category.BASIC;
                    displayCard(cardToBeDisplayed, false, false, false, false, true, false, false, false, false);
                }
                break;
            case HAND:
                if(m.getIndex() < sourceCardManager.getHand().size()){
                    cardToBeDisplayed = sourceCardManager.getHand().get(m.getIndex());
                    isCat1Pokemon = cardToBeDisplayed instanceof PokemonCard && ((PokemonCard) cardToBeDisplayed).getCat() == PokemonCard.Category.BASIC;
                    boolean isCat2Pokemon = cardToBeDisplayed instanceof PokemonCard && ((PokemonCard) cardToBeDisplayed).getCat() == PokemonCard.Category.STAGEONE;
                    boolean canAttachEnergy = cardToBeDisplayed instanceof EnergyCard && player.hasPlacedEnergy == false;
                    boolean isTrainerCard = cardToBeDisplayed instanceof TrainerCard;
                    displayCard(cardToBeDisplayed, false, isCat1Pokemon && isPlayers, canAttachEnergy, false, true, false, isTrainerCard && isPlayers, isCat2Pokemon && isPlayers, false);
                }
                break;
            default:
            	cardToBeDisplayed = createCardFromMessage(m);
            	displayCard(cardToBeDisplayed, false, false, false, false, false, false, false, false, false);
            	break;
        }
    }

    //-----Update GUI Buttons
    public void updateAll(){
    	updateAISide();
    	updatePlayerSide();
    	updateLeftSidebar();
	}

	//Update AI buttons
    public void updateAISide(){
    	updateAIActivePokemon();
    	updateAIHand();
    	updateAIBench();
    }
    public void updateAIActivePokemon(){
    	AIActivePokemonContainer.removeAll();
    	AIActivePokemonContainer.add(createJPanelFromCard(autoPlayer.getActivePokemon()));

    	AIActivePokemonContainer.invalidate();
    	AIActivePokemonContainer.validate();
    	AIActivePokemonContainer.repaint();
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
    public void updateAIBench(){
    	AIBenchContainer.removeAll();
		int i = 0;
		for (Card c : autoPlayer.getBench()){
			AIBenchContainer.add(createJPanelFromCard(c));
			i++;
		}
		for (; i < 5; i++){
			AIBenchContainer.add(createJPanelFromCard(null));
		}
		AIBenchContainer.invalidate();
		AIBenchContainer.validate();
		AIBenchContainer.repaint();
    }

    //Update Player buttons
    public void updatePlayerSide(){
    	updatePlayerActivePokemon();
    	updatePlayerHand();
    	updatePlayerBench();
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
    public void updatePlayerBench(){
    	playerBenchContainer.removeAll();
    	int i = 0;
    	for (Card c : player.getBench()){
    		playerBenchContainer.add(createJPanelFromCard(c));
    		i++;
    	}
    	for (; i < 5; i++){
    		playerBenchContainer.add(createJPanelFromCard(null));
    	}
    	playerBenchContainer.invalidate();
    	playerBenchContainer.validate();
    	playerBenchContainer.repaint();
    }

    //Update Pile buttons
	public void updateLeftSidebar(){
		AILeftSidebar.invalidate();
		AILeftSidebar.validate();
		AILeftSidebar.repaint();
		playerLeftSidebar.invalidate();
		playerLeftSidebar.validate();
		playerLeftSidebar.repaint();
	}
	//-----

	//Create GUI entities
    public JPanel createJPanelFromCard(Card c){
    	JPanel card = new JPanel();
    	card.setPreferredSize(new Dimension(100, 120));
    	card.setLayout(new GridLayout(0,1));
    	
    	JButton button;
    	
    	if (c != null){
	    	button = new JButton(c.getName());
    	} else {
    		button = new JButton("");
    	}
    	button.addActionListener(new GenericButtonActionListener());
    	card.add(button);
    	return card;
    }
    public JPanel createJPanelFromStrings(String buttonName, String descriptionString){
    	JPanel card = new JPanel();
    	card.setPreferredSize(new Dimension(100, 120));
    	card.setLayout(new GridLayout(0,1));
    	
    	JButton button = new JButton(buttonName);
    	button.addActionListener(new GenericButtonActionListener());
    	card.add(button);
    	return card;
    }
    public JPanel createJPanelFromPile(ArrayList<Card> pile, String buttonName){
    	JPanel card = new JPanel();
    	card.setPreferredSize(new Dimension(100, 120));
    	card.setLayout(new GridLayout(0,1));
    	
    	JButton button = new JButton(buttonName);
    	button.addActionListener(new GenericButtonActionListener());
    	card.add(button);
    	return card;
    }
	public Card createCardFromMessage(Message m){
		CardManager sourceCardManager;
		if (m.getSide() == Message.Side.AI){
			sourceCardManager = autoPlayer.cardManager;
		} else {
			sourceCardManager = player.cardManager;
		}

		GenericCard card;
		if (m.getType() == Message.ButtonType.DECK){
			card = new GenericCard(sourceCardManager.getDeck().size());
		} else if (m.getType() == Message.ButtonType.DISCARD){
			//make list of discarded cards
			String list = "<html>";
			for(Card c : sourceCardManager.getDiscard()){
				list += c.getName() + "<br/>";
			}
			list += "</html>";
			card = new GenericCard(Integer.toString(sourceCardManager.getDiscard().size()), list);
		} else {
			card = new GenericCard(sourceCardManager.getPrizeCards().size());
		}

		return card;
	}

	//Getters
	public Card getDisplayedCard(){
		return this.displayedCard;
	}

	//the following function was modified from https://stackoverflow.com/questions/144892/how-to-center-a-window-in-java
	public static void centreWindow(Window frame, int window_width, int window_height) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2) - window_width/2;
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2) - window_height/2;
		frame.setLocation(x, y);
	}

}