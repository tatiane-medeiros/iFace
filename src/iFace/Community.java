package iFace;
import java.util.ArrayList; 

public class Community {
	public String name;
	private User admin;
	
	private ArrayList<User> members;	
	public String description;
	private ArrayList<Message> messages;
	private ArrayList<Message> requests;
	public Community(String name,User admin){
		this.name = name;
		this.admin = admin;
		this.members = new ArrayList<>();
		this.requests = new ArrayList<>();
		this.messages = new ArrayList<>();
		this.addMember(admin);
	}
	void setDescription(String description){
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public User getAdmin() {
		return admin;
	}
	public void setAdmin(User admin) {
		this.admin = admin;
	}
	public void showInfo(){
		System.out.println(name);
		System.out.println(description);
		System.out.println("Membros ["+members.size()+"]:");
		for(int t=0; t< members.size(); t++){
			System.out.println("  "+members.get(t).getName());
		}
	}
	public void addMember(User member){
		members.add(member);
		member.joinCommunity(this);
	}
	private void deleteMember(User user){
		for(int t=0; t< members.size(); t++){
			if(members.get(t) == user){
			members.remove(t);
			break;
			}
		}
	}
	public void removeMember(User member){
		member.exitCommunity(this);
		this.deleteMember(member);
	}
	public void removeAll(){
		while(members.size() > 0){
			members.get(0).exitCommunity(this);
			members.remove(0);
		}
	}
	public void addRequest(User user){
		
		this.requests.add(new Message(user, null, "Quer participar da comunidade."));
	}
	public void receiveMessage(Message message){
		this.messages.add(message);
	}
	public void showMessages(){
		for(int i = 0; i<this.messages.size(); i++){
			this.messages.get(i).show();
		}
	}
	
	public boolean isMember(User user){
		for(int t=0; t< members.size(); t++){
			if(members.get(t) == user) return true;			
		}
		return false;
	}
	public int nOfMembers(){
		return this.members.size();
	}
	public User answerUser(){
		this.requests.get(0).show();
		User user = this.requests.get(0).getOrigination();
		this.requests.remove(0);
		return user;
	}
	
	public String toString(){
		//versao windows
		String data = name+"\r\n"+description+"\r\n"+members.size();
		for(int i=0;i<members.size(); i++){
			data = data.concat("\r\n"+members.get(i).getName());
		}
		data = data.concat("\r\n"+messages.size());
		for(int i=0;i<messages.size(); i++){
			data = data.concat("\r\n"+messages.get(i).toString());
		}
		return data;
	}
	
}
