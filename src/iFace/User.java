package iFace;
import java.util.ArrayList;

public class User {
	private String name;
	private String email;
	private String password;
	private ArrayList<User> friends;
	private ArrayList<Message> messages;
	private ArrayList<Community> communities;
	private ArrayList<Message> requests;
	
	public User(String name, String email, String password){
		this.email = email;
		this.name = name;
		this.password = password;
		this.friends = new ArrayList<>();
		this.communities = new ArrayList<>();
		this.messages = new ArrayList<>();
		this.requests = new ArrayList<>();
	}
	
	//login
	public boolean authentic(String pass){
		return (this.password.equals(pass));
	}
	public String toString(){
		//versao windows
		String data = email+"\r\n"+password+"\r\n"+name;
		return data;
	}
	public void changePassword(String newPass, String oldPass){
		if(authentic(oldPass)){
			this.password = newPass;
			System.out.println("Senha alterada com sucesso!");
		}
		else System.out.println("Senha incorreta.");
	}
	
	public void print(){
		System.out.println("-- "+this.name+" --");
		System.out.println("Email: "+this.email);
		System.out.println("Numero de amigos: "+this.friends.size());
		System.out.println("Numero de comunidades: "+this.communities.size());
	}
	
	public void listFriends(){
		for(int i=0; i<this.friends.size(); i++){
			System.out.println(this.friends.get(i).getName());
		}
	}
	
	public void request(User user){
		this.requests.add(new Message(user, this, "Enviou uma solicitação de amizade."));
	}
	private void newFriend(User user){
		this.friends.add(user);
	}
	public void addFriend(User user){
		this.newFriend(user);
		user.newFriend(this);
	}
	public boolean hasRequest(){
		return (this.requests.size() >0);
	}
	public User answerRequest(){
		this.requests.get(0).show();
		User aux = this.requests.get(0).getOrigination();
		this.requests.remove(0);
		return aux;
	}
	private void noFriend(User user){
		for(int i=0; i<this.friends.size(); i++){
			if(this.friends.get(i) == user){
				this.friends.remove(i);
				break;
			}
		}
	}
	public void removeFriend(User user){
		user.noFriend(this);
		this.noFriend(user);
	}
	public void clearFriends(){
		while(this.friends.size() >0){
			this.removeFriend(this.friends.get(0));
		}
	}
	public int searchFriend(String name){
		for(int i=0; i<this.friends.size(); i++){
			if(this.friends.get(i).name.equals(name)){
				return i;
			}
		}
		return -1;
	}
	public User friendAt(int index){
		return this.friends.get(index);
	}
	public void showCommunities(){
		System.out.println("Comunidades");
		for(int i=0; i<this.communities.size(); i++){
			this.communities.get(i).showInfo();
			System.out.println("\n");
		}
	}
	public void joinCommunity(Community c){
		this.communities.add(c);
	}
	public void exitCommunity(Community c){
		for(int i=0; i<this.communities.size(); i++){
			if(this.communities.get(i) == c){
				this.communities.remove(i);
				break;
			}
		}
	}
	public void removeCommunity(Community c){
		c.removeMember(this);
	}
	public void clearCommunities(){
		while(this.communities.size() >0){
			this.removeCommunity(this.communities.get(0));
		}
	}
	public void showMessages(){
		for (int i=0; i<this.messages.size() ; i++){
			System.out.println("----------------------------------");
			this.messages.get(i).show();
			System.out.println("\n");
		}
	}
	public void sendMessage(Message message){
		this.messages.add(message);
	}
	public boolean isFriend(String name){
		if(this.searchFriend(name) == -1) return false;
		return true;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
}
