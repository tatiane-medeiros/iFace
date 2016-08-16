package iFace;
import java.util.ArrayList;

public class User {
	private String name;
	private String email;
	private String password;
	int id;
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
		else System.err.println("Senha incorreta.\n");
	}
	
	public void print(){
		System.out.println("-- "+this.name+" --");
		System.out.println("Email: "+this.email);
		System.out.println("Numero de amigos: "+this.friends.size());
		System.out.println("Numero de comunidades: "+this.communities.size());
	}
	
	public void listFriends(){
		if(this.friends.size() == 0) System.out.println("Nenhum amigo.");
		for(int i=0; i<this.friends.size(); i++){
			System.out.println(this.friends.get(i).getName());
		}
	}
	
	public void request(User user){
		this.requests.add(new Message(user, this, "Enviou uma solicitação de amizade."));
	}
	protected void newFriend(User user){
		this.friends.add(user);
	}
	public void addFriend(User user){
		if(!this.isFriend(user)){
			this.newFriend(user);
			user.newFriend(this);
		}
		
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
	private void noFriend(User user) throws IndexOutOfBoundsException{
		for(int i=0; i<this.friends.size(); i++){
			if(this.friends.get(i) == user){
				this.friends.remove(i);
				break;
			}
		}		
	}
	public void removeFriend(User user){
		if(this.isFriend(user)){
		user.noFriend(this);
		this.noFriend(user);
		}
		else throw new NullPointerException("Este usuario não está na sua lista de amigos.\n");
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
	public String fprintFriends(){
		String aux = ""+this.friends.size();
		for(int i=0; i<this.friends.size();i++){
			aux = aux.concat("\r\n"+this.friends.get(i).getName());
		}
		return aux;
	}
	public User friendAt(int index){
		return this.friends.get(index);
	}
	public void showCommunities(){
		System.out.println(this.communities.size()+" comunidades:\n");
		for(int i=0; i<this.communities.size(); i++){
			Community curr =this.communities.get(i);
			System.out.println(curr.getName()+"\n"+curr.description+"\nMembros: "+curr.nOfMembers()+"\n");
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
		for (int i=0; i<this.messages.size(); i++){
			this.messages.get(i).show();
			//System.out.println("\n");
		}
	}
	public void sendMessage(Message message){
		this.messages.add(message);
	}
	public String fprintMessageBox(){
		String aux = messages.size()+"";
		int i;
		for(i = 0; i< messages.size(); i++){
			aux = aux.concat(messages.get(i).toSave());
		}
		aux = aux.concat("\r\n"+requests.size());
		for(i = 0; i< requests.size(); i++){
			aux = aux.concat("\r\n"+requests.get(i).getOrigination().getName());
		}
			
		
		return aux;
	}
	public boolean isFriend(String name){
		if(this.searchFriend(name) == -1) return false;
		return true;
	}
	public boolean isFriend(User user){
		for(int i=0; i<friends.size(); i++){
			if(friends.get(i) == user) return true;
		}
		return false;
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
