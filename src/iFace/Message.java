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
	public User getDestination() {
		return destination;
	}
	
	public void show(){
		System.out.println(this.origination.getName()+":\n"+this.text+"\n");
	}
	public String toString(){
		//versao windows
		String data = this.origination.getName()+"\r\n"+this.text;
		return data;
	}
	public String toSave(){
		//versao windows
		String data = "\r\n"+this.origination.getName()+"\r\n"+ this.destination.getName()+"\r\n"+this.text;
		return data;
	}
	
}