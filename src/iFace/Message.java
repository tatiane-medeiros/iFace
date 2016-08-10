package iFace;

public class Message {
	private String text;
	private User origination;	
	private User destination;
	public Message(User origination, User destination, String text){
		this.origination = origination;
		this.destination = destination;
		this.text = text;
	}
	public User getOrigination() {
		return origination;
	}
	
	public void show(){
		System.out.println(this.origination.getName()+":\n"+this.text+"\n");
	}
	
}