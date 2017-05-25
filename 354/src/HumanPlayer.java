public class HumanPlayer extends Player {

	public HumanPlayer(){
		cardManager = new CardManager();
	}
	
	public void selectActivePokemon(){
		//prompt user to click on a pokemon from their hand
		PokemonCard selectedPokemon;
		
		cardManager.setActivePokemon(selectedPokemon);
	}
	
	public void playTurn(){
		//prompt user to click on an energy card
		EnergyCard selectedEnergy;
		
		//prompt user to click on the active pokemon (for now)
		PokemonCard selectedPokemon;
		
		cardManager.attachEnergy(selectedEnergy, selectedPokemon);
	}
	
}