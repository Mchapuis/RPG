public class EnergyCard extends Card {
	
	private enum Type{
		COLORLESS, OTHER
	}
	
	private int ID;
	private String name;
	private String description;
	private Type type;

	public EnergyCard(String type){
		this.ID = 0;
		this.name = "Energy";
		
		if (type.equals("COLORLESS")){
			this.type = Type.COLORLESS;
			this.description = "Colorless";
		} else {
			this.type = Type.OTHER;
			this.description = "Other";
		}
	}
	
	public int getID() {
		return this.ID;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public String getSimpleDescription(){
		return this.description;
	}
	
	public String getType(){
		return this.type.toString();
	}
	
	public void setID(int ID){
		this.ID = ID;
	}
	
}