
public class Message {
	public static enum Side{
		PLAYER, AI
	}
	
	public static enum ButtonType{
		HAND, BENCH, ACTIVE, MAKEACTIVE, ATTACHENERGY, ATTACK, LETAIPLAY 
	}
	
	private Side side;
	private ButtonType type;
	private int index;	
	
	public Message(String side, String type, int index){
		if (side.equals("player")){
			this.side = Side.PLAYER;
		} else {
			this.side = Side.AI;
		}
		
		if (type.equals("active")){
			this.type = ButtonType.ACTIVE;
		} else if (type.equals("bench")){
			this.type = ButtonType.BENCH;
		} else if (type.equals("hand")){
			this.type = ButtonType.HAND;
		} else if (type.equals("makeactive")){
			this.type = ButtonType.MAKEACTIVE;
		} else if (type.equals("attachenergy")){
			this.type = ButtonType.ATTACHENERGY;
		} else if (type.equals("attack")){
			this.type = ButtonType.ATTACK;
		} else if (type.equals("letAIplay")){
			this.type = ButtonType.LETAIPLAY;
		}
		
		this.index = index;
	}
	
	public Side getSide(){
		return this.side;
	}
	
	public ButtonType getType(){
		return this.type;
	}
	
	public int getIndex(){
		return this.index;
	}
}