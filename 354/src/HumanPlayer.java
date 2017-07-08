
public class HumanPlayer extends Player {

	public HumanPlayer(){
		cardManager = new CardManager();
	}

	public void setup(){
	    //select active pokemon
		GameEngine.w.updateInstructions("Select a Pokémon in your hand to be your active Pokémon");
		boolean hasSelectedActivePokemon = false;
        while(!hasSelectedActivePokemon){
            GameEngine.waitForInput();
            Message m = GameEngine.queue.remove();

            if(m.getType() == Message.ButtonType.HAND && m.getSide() == Message.Side.PLAYER){
                int index = m.getIndex();
                Card c = cardManager.getHand().get(index);
                if(c instanceof PokemonCard && ((PokemonCard) c).getCat() == PokemonCard.Category.BASIC){
                        GameEngine.w.displayCard(c, true, false, false, false, false, false);
                }
                else{
                    GameEngine.w.displayCard(c, false, false, false, false, false, false);
                }
            }
            else if(m.getType() == Message.ButtonType.MAKEACTIVE){
                PokemonCard c = (PokemonCard) GameEngine.w.getDisplayedCard();
                cardManager.setActivePokemon(c);
                GameEngine.w.updatePlayerSide();
                GameEngine.w.displayCard(c, false, false, false, false, false, false);
                GameEngine.w.updateInstructions("Made " + c.getName() + " active Pokémon.");
                hasSelectedActivePokemon = true;
            }
        }

        //select bench pokemon
        GameEngine.w.updateInstructions("Select Pokémon in your hand to be benched. Click on your active Pokémon when you're done.");

        boolean doneBenchingPokemon = false;
        while(!doneBenchingPokemon){
            GameEngine.waitForInput();
            Message m = GameEngine.queue.remove();

            if(m.getType() == Message.ButtonType.HAND && m.getSide() == Message.Side.PLAYER){
                int index = m.getIndex();
                Card c = cardManager.getHand().get(index);
                if(c instanceof PokemonCard && ((PokemonCard) c).getCat() == PokemonCard.Category.BASIC){
                    GameEngine.w.displayCard(c, false, true, false, false, false, false);
                }
                else{
                    GameEngine.w.displayCard(c, false, false, false, false, false, false);
                }
            }
            else if(m.getType() == Message.ButtonType.ADDTOBENCH){
                PokemonCard c = (PokemonCard) GameEngine.w.getDisplayedCard();
                cardManager.getBench().add(c);
                cardManager.getHand().remove(c);
                GameEngine.w.updatePlayerSide();
                GameEngine.w.displayCard(c, false, false, false, false, false, false);
                GameEngine.w.updateInstructions("Added " + c.getName() + " to bench");

                if(cardManager.getBench().size() == 5){
                    doneBenchingPokemon = true;
                }
            }
            else if(m.getType() == Message.ButtonType.ACTIVE && m.getSide() == Message.Side.PLAYER){
                doneBenchingPokemon = true;
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
                break;
            default:
                GameEngine.w.displayCard(m);
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
        GameEngine.w.updatePlayerSide();
        GameEngine.w.displayCard(c, false, false, false, false, false, false);
        GameEngine.w.updateInstructions("Added " + c.getName() + " to bench");
    }
    public void retreatActive(){
        if(! this.getActivePokemon().hasEnoughEnergyForRetreat()){
            GameEngine.w.updateInstructions(getActivePokemon().getName() + " does not have enough energy to retreat.");
        }
        else if(this.getActivePokemon().getStatus() ==  Status.ASLEEP){
            GameEngine.w.updateInstructions(getActivePokemon().getName() + " is asleep and can't retreat.");
        }
        else if(this.getActivePokemon().getStatus() == Status.PARALYZED){
            GameEngine.w.updateInstructions(getActivePokemon().getName() + " is paralyzed and can't retreat.");
        }
        else{
            PokemonCard replacement = GameEngine.choosePokemonCard(this, Ability.Target.YOUR_BENCH);
            GameEngine.w.updateInstructions(getActivePokemon().getName() + " has been replaced with " + replacement.getName());
            this.getActivePokemon().applyStatus(Status.NORMAL);
            this.retreatPokemon(replacement);
            hasRetreatedActivePokemon = true;
        }
    }
    public void attack(int attackIndex){
        Ability ability = getActivePokemon().getAbilities().get(attackIndex);

        if(!getActivePokemon().hasEnoughEnergyForAttack(attackIndex)){
            GameEngine.w.updateInstructions(getActivePokemon().getName() + " does not have enough energy to attack.");
        }
        else if(getActivePokemon().getStatus() == Status.ASLEEP){
            GameEngine.w.updateInstructions(getActivePokemon().getName() + " is asleep and cannot attack.");
        }
        else if(getActivePokemon().getStatus() == Status.PARALYZED){
            GameEngine.w.updateInstructions(getActivePokemon().getName() + " is paralyzed and cannot attack.");
        }
        else{
            GameEngine.w.updateInstructions(getActivePokemon().getName() + " used ability " + ability.name);
            ability.use(Ability.Player.PLAYER);
            turnOver = true;
        }
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