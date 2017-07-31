
public class HumanPlayer extends Player {

	public HumanPlayer(String deckFile){
		cardManager = new CardManager(deckFile);
	}

	public void setup(){
	    //select active pokemon
		GameEngine.log("Select a Pokémon in your hand to be your active Pokémon");
		boolean hasSelectedActivePokemon = false;
        while(!hasSelectedActivePokemon){
            GameEngine.waitForInput();
            Message m = GameEngine.queue.remove();

            if(m.getType() == Message.ButtonType.HAND && m.getSide() == Message.Side.PLAYER){
                int index = m.getIndex();
                Card c = cardManager.getHand().get(index);
                if(c instanceof PokemonCard && ((PokemonCard) c).getCat() == PokemonCard.Category.BASIC){
                        GameEngine.w.displayCard(c, true, false, false, false, false, false, false, false, false);
                }
                else{
                    GameEngine.w.displayCard(c, false, false, false, false, false, false, false, false, false);
                }
            }
            else if(m.getType() == Message.ButtonType.MAKEACTIVE){
                PokemonCard c = (PokemonCard) GameEngine.w.getDisplayedCard();
                cardManager.setActivePokemon(c);
                GameEngine.updateGUI();
                GameEngine.w.displayCard(c, false, false, false, false, false, false, false, false, false);
                GameEngine.log("Made " + c.getName() + " active Pokémon.");
                hasSelectedActivePokemon = true;
            }
            else{
                GenericCard g = new GenericCard("Hidden","<html>You can not look at this until the end of the setup phase. To end the setup phase, select a pokemon from your hand to be your active pokemon. Then click on your active pokemon.</html>");
                GameEngine.w.displayCard(g,false,false,false,false,false,false,false,false,false);
            }
        }

        //select bench pokemon
        GameEngine.log("Select Pokémon in your hand to be benched. Click on your active Pokémon when you're done.");

        boolean doneBenchingPokemon = false;
        while(!doneBenchingPokemon){
            GameEngine.waitForInput();
            Message m = GameEngine.queue.remove();

            if(m.getType() == Message.ButtonType.HAND && m.getSide() == Message.Side.PLAYER){
                int index = m.getIndex();
                Card c = cardManager.getHand().get(index);
                if(c instanceof PokemonCard && ((PokemonCard) c).getCat() == PokemonCard.Category.BASIC){
                    GameEngine.w.displayCard(c, false, true, false, false, false, false, false, false, false);
                }
                else{
                    GameEngine.w.displayCard(c, false, false, false, false, false, false, false, false,false);
                }
            }
            else if(m.getType() == Message.ButtonType.ADDTOBENCH){
                PokemonCard c = (PokemonCard) GameEngine.w.getDisplayedCard();
                cardManager.getBench().add(c);
                cardManager.getHand().remove(c);
                GameEngine.updateGUI();
                GameEngine.w.displayCard(c, false, false, false, false, false, false, false, false,false);
                GameEngine.log("Added " + c.getName() + " to bench");

                if(cardManager.getBench().size() == 5){
                    doneBenchingPokemon = true;
                }
            }
            else if(m.getType() == Message.ButtonType.ACTIVE && m.getSide() == Message.Side.PLAYER){
                doneBenchingPokemon = true;
            }
            else{
                GenericCard g = new GenericCard("Hidden","<html>You can not look at this until the end of the setup phase. To end the setup phase, select a pokemon from your hand to be your active pokemon. Then click on your active pokemon.</html>");
                GameEngine.w.displayCard(g,false,false,false,false,false,false,false,false,false);
            }
        }
	}

	public void takeActions(){
		GameEngine.waitForInput();

		Message m = GameEngine.queue.remove();

		switch(m.getType()){
            case ATTACK:
                attack(m.getIndex());
                break;
            case ADDTOBENCH:
                addToBenchFromHand();
                break;
            case ATTACHENERGY:
                attachEnergy();
                break;
            case LETAIPLAY:
                GameEngine.player.turnOver = true;
                break;
            case MAKEACTIVE:
                //I think this might not need to be implemented.
                break;
            case RETREAT:
                retreatActive();
                GameEngine.w.displayCard(new Message("player", "active", 0));
                break;
            case PLAYITEM:
            	playItem();
                GameEngine.w.displayCard(new Message("player", "active", 0));
            	break;
            case EVOLVE:
            	evolve();
                GameEngine.w.displayCard(new Message("player", "active", 0));
            	break;
            default:
                GameEngine.w.displayCard(m);
        }
	}

	public void playItem(){
		if(!hasPlayedSupportCard){
            TrainerCard t = (TrainerCard) GameEngine.w.getDisplayedCard();
            cardManager.removeCardFromHand(t);
            cardManager.addToDiscard(t);
            GameEngine.updateGUI();
            t.getAbility().use(Ability.Player.PLAYER);
            GameEngine.log("Used trainer card " + t.getName());
            hasPlayedSupportCard = true;
        }
        else{
		    GameEngine.log("You have already played one trainer card this turn and can't play another.");
        }
	}

	public void evolve(){
		PokemonCard nextStage = (PokemonCard) GameEngine.w.getDisplayedCard();
		PokemonCard initialPokemon = GameEngine.choosePokemonCard(this, Ability.Target.YOUR_POKEMON);

		if(nextStage.getEvolvesFrom().equals(initialPokemon.getName())){
		    nextStage.setEvolvedFrom(initialPokemon);
		    nextStage.energy = initialPokemon.energy;
		    if(nextStage.getEnergyToRetreat() == 0){
		        nextStage.setEnergyToRetreat(initialPokemon.getEnergyToRetreat());
            }

		    if(initialPokemon == getActivePokemon()){
		        setActivePokemon(nextStage);
            }
            else{
		        getBench().remove(initialPokemon);
		        getBench().add(nextStage);
            }

            cardManager.removeCardFromHand(nextStage);

            GameEngine.log(initialPokemon.getName() + " has evolved into " + nextStage.getName() + "!");
        }
        else{
		    GameEngine.log(nextStage.getName() + " only evolves from " + nextStage.getEvolvesFrom());
		    return;
        }
	}

	public void attachEnergy(){
        if(! this.hasPlacedEnergy){
            EnergyCard e = (EnergyCard) GameEngine.w.getDisplayedCard();
            PokemonCard p = GameEngine.choosePokemonCard(this, Ability.Target.YOUR_POKEMON);
            cardManager.attachEnergy(e,p);
            this.hasPlacedEnergy = true;
        }
    }
    public void addToBenchFromHand(){
        PokemonCard c = (PokemonCard) GameEngine.w.getDisplayedCard();
        cardManager.getBench().add(c);
        cardManager.getHand().remove(c);
        GameEngine.updateGUI();
        GameEngine.w.displayCard(c, false, false, false, false, false, false, false, false, false);
        GameEngine.log("Added " + c.getName() + " to bench");
    }
    public boolean chooseNewActivePokemon(){
        if(cardManager.getBench().size() == 0){
            return false;
        }
        else{
            PokemonCard newActive = GameEngine.choosePokemonCard(this, Ability.Target.YOUR_BENCH);
            setActivePokemon(newActive);
            return true;
        }
    }

}