package iFace;
import java.util.ArrayList; 

public class Community {
	public String name;
	private User admin;
	private ArrayList<User> members;	
	public String description;
	private ArrayList<Message> messages;
	public Community(String name,User admin){
		this.name = name;
		this.admin = admin;
		this.members = new ArrayList<>();
		this.members.add(admin);
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
	public void showInfo(){
		System.out.println(name);
		System.out.println(description);
		System.out.println("Membros ["+members.size()+"]:");
		for(int t=0; t< members.size(); t++){
			System.out.println("  "+members.get(t).getName());
		}
	}
	public void addMember(User member){
		this.members.add(member);
	}
	
}
