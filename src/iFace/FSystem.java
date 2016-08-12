package iFace;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FSystem {
	private User currentUser;
	private int currentIndex;
	private ArrayList<User> users;
	private ArrayList<Community> communities;
	Scanner sc = new Scanner(System.in);
	
	public FSystem(){
		this.users = new ArrayList<>();
		this.communities  = new ArrayList<>();
	}
	public void createUser(){
		String nEmail;
		String nName;
		String nPass;
		System.out.println("Digite seu email:");
		nEmail = sc.next();
		if(this.searchByEmail(nEmail) == -1){
			System.out.println("Digite seu nome:");
			sc.nextLine();
			nName = sc.nextLine();
			System.out.println("Digite sua senha:");
			nPass = sc.next();
			
			users.add(new User(nName, nEmail, nPass));
			System.out.println(nName+" adicionado.");
		} else System.err.println("Email ja cadastrado.\n\n");
	}
	
	private void login(){
		String nEmail;
		String nPass;
		
		System.out.println("Digite seu email:");
		nEmail = sc.next();
		int aux = this.searchByEmail(nEmail);
		System.out.println("Digite sua senha:");
		nPass = sc.next();
		if(aux!=-1){
			
			if(this.users.get(aux).authentic(nPass)){
				System.out.println(this.users.get(aux).getName() + " logado.");
				this.currentUser = this.users.get(aux);
				this.currentIndex = aux;
			}
			//else System.err.println("senha incorreta.\n");
		}
		//else System.err.println("login inválido.\n");	
		
		
	}
	public void homepage(){
		int option = 0;
		do{
			System.out.println("\n***** iFace ******\n");
			System.out.println("  1 - Criar conta");
			System.out.println("  2 - Entrar");
			System.out.println("  0 - Sair");
			try{
			option = sc.nextInt();
			
			switch(option){
			case 1:
				createUser();
				break;
			case 2:	
				try{
				login();
				userPanel();
				} catch(NullPointerException e){
					System.err.println("Login ou senha incorreto.\n");
				}
				
				break;
			case 0:
				break;
			default:
				System.err.println("entrada invalida.\n");
				break;
			}
			
			} catch(InputMismatchException e){
				System.err.println("entrada invalida.\n");
				sc.nextLine();				
			}
			
		}while(option != 0);
	}
	
	protected void userPanel(){
		int option = 0;
		do{
			System.out.println("-- " +this.currentUser.getName()+" --");
			System.out.println("  1 - Minha conta");
			System.out.println("  2 - Amigos");
			System.out.println("  3 - Comunidades");
			System.out.println("  4 - Mensagens");
			System.out.println("  5 - Excluir conta");
			System.out.println("  0 - Sair");
			try{
			option = sc.nextInt();
			switch(option){
			case 1:
				this.acountOptions();
				break;
			case 2:
				this.friendOptions();
				break;
			case 3:
				this.communityOptions();
				break;
			case 4:
				this.messageBox();
				break;
			case 5:
				System.out.println("Tem certeza que deseja remover sua conta?(s/n)");
				String op = sc.next();
				if(op.equals("s")){
					currentUser.clearFriends();
					currentUser.clearCommunities();
					logout();
					this.users.remove(currentIndex);
					System.out.println("Conta Removida.");
					option = 0;
				}				
				break;
			case 0:
				logout();
				break;
			default:
				break;
			}
			} catch(InputMismatchException e){
				//System.out.println("entrada invalida");
				System.err.println("entrada invalida\n");
				sc.nextLine();				
			}
		}while(option != 0);
	}
	
	private void logout(){
		this.currentUser = null;
	}
	
	protected void acountOptions(){
		int option;
		do{
			System.out.println("\n-- "+this.currentUser.getName()+" --");
			System.out.println("  1 - Exibir info.");
			System.out.println("  2 - Editar");
			System.out.println("  0 - Voltar\n");
			
			option = sc.nextInt();
			switch(option){
			case 1:
				currentUser.print();
				break;
			case 2:
				this.acountSettings();
				break;
			
			default:
				break;
			}
		}while(option != 0);
	}
	
	protected void acountSettings(){
		int option;
		String aux;
		do{
			System.out.println("\n-- "+this.currentUser.getName()+" --");
			System.out.println("  1 - Alterar nome");
			System.out.println("  2 - Alterar email");
			System.out.println("  3 - Alterar senha");
			System.out.println("  0 - Voltar\n");
			
			option = sc.nextInt();
			switch(option){
			case 1:
				System.out.println("Digite o novo nome:");
				sc.nextLine();
				aux = sc.nextLine();
				this.currentUser.setName(aux);
				System.out.println("Nome alterado para "+this.currentUser.getName());
				break;
			case 2:
				System.out.println("Digite o novo email:");
				aux = sc.next();
				if(searchByEmail(aux) == -1){
					this.currentUser.setEmail(aux);
					System.out.println("Email alterado para "+this.currentUser.getEmail());
				} else System.out.println("Este email ja possui conta.");
				break;
			case 3:
				System.out.println("Digite a nova senha:");
				aux = sc.next();
				System.out.println("Digite sua senha anterior:");
				String pass = sc.next();
				this.currentUser.changePassword(aux, pass);
				break;
			default:
				break;
			}
		}while(option != 0);
	}
	
	protected void friendOptions(){
		int option;
		String aux; int id;
		do{
			System.out.println("\n-- "+this.currentUser.getName()+" --");
			System.out.println("  1 - Adicionar um amigo");
			System.out.println("  2 - Ver lista de amigos");
			System.out.println("  3 - Responder solicitaçoes");
			System.out.println("  4 - Desfazer amizade");
			System.out.println("  0 - Voltar\n");
			
			option = sc.nextInt();
			switch(option){
			case 1:
				System.out.println("Digite o nome:");
				sc.nextLine();
				aux = sc.nextLine();
				id = this.searchByName(aux);
				if(id != -1 && id != currentIndex){					
					this.users.get(id).request(currentUser);
					System.out.println("Solicitação enviada para "+aux);
				}else System.out.println("Usuario não encontrado.");
				break;
			case 2:
				currentUser.listFriends();
				break;
			case 3:
				if(currentUser.hasRequest()){
					User f = currentUser.answerRequest();
					System.out.println("Aceitar?(s/n)");
					String a = sc.next();
					if(a.equals("s")){
						currentUser.addFriend(f);
						System.out.println("Amigo adicionado!");
					} else System.out.println("Solicitaçao removida.");
				} else System.out.println("Sem solicitaçoes.");
				break;
			case 4:
				System.out.println("Desfazer Amizade.\nDigite o nome:");
				sc.nextLine();
				aux = sc.nextLine();
				id = this.searchByName(aux);
				if(id != -1){
					currentUser.removeFriend(this.users.get(id));
					System.out.println(this.users.get(id).getName() +" removido.");
				} else System.out.println("Usuario não encontrado.");
				break;
			default:
				break;
			}
			}while(option !=0);
	
	}
	
	protected void communityOptions(){
		int option;
		String aux; int id;
		do{
			System.out.println("\n-- "+this.currentUser.getName()+" --");
			System.out.println("  1 - Criar uma comunidade");
			System.out.println("  2 - Ver minhas comunidades");
			System.out.println("  3 - Entrar em uma comunidade");
			System.out.println("  0 - Voltar\n");
			
			option = sc.nextInt();
			switch(option){
			case 1:
				System.out.println("Digite o nome da Comunidade:");
				sc.nextLine();
				aux = sc.nextLine();
				id = searchCommName(aux);
				if(id == -1){
					this.communities.add(new Community(aux, currentUser));
					System.out.println("Descriçao da comunidade:");
				
					aux = sc.nextLine();
					this.communities.get(communities.size() -1).setDescription(aux);
					System.out.println("Comunidade criada.");
				} else System.out.println("Uma comunidade com esse nome ja existe.");
				break;
			case 2:
				currentUser.showCommunities();
				
				break;
			case 3:
				System.out.println("Digite o nome da comunidade a ser pesquisada:");
				sc.nextLine();
				aux = sc.nextLine();
				id = searchCommName(aux);
				if(id != -1){
					Community curr = this.communities.get(id);
					int op;
					if(currentUser == curr.getAdmin()){
						do{
							System.out.println("\n-- "+curr.getName()+" --");
							System.out.println("1 - Exibir info.");
							System.out.println("2 - Adicionar usuario");
							System.out.println("3 - Remover usuario");
							System.out.println("4 - Responder solicitações");
							System.out.println("5 - Excluir comunidade");
							System.out.println("0 - Sair");
							op = sc.nextInt();
							switch(op){
							case 1:
								curr.showInfo();
								break;
							case 2:
								System.out.println("Adicionar usuario.\nDigite o nome do usuario:");
								sc.nextLine();
								aux = sc.nextLine();
								id = this.searchByName(aux);
								if(id != -1 && !curr.isMember(this.users.get(id))){
									curr.addMember(this.users.get(id));
									System.out.println("Usuario adicionado.");
								}
								break;
							case 3:
								System.out.println("Remover usuario.\nDigite o nome do usuario:");
								sc.nextLine();
								aux = sc.nextLine();
								id = this.searchByName(aux);
								if(id != -1 && id != currentIndex){
									curr.removeMember(this.users.get(id));
									System.out.println("Usuario removido.");
								}
								break;
							case 4:
								User user = curr.answerUser();
								System.out.println("Aceitar? (s/n)");
								aux = sc.next();
								if(sc.equals("s")){
									curr.addMember(user);
								}
								
							}
							
						}while(op!= 0);
					}
					else if(curr.isMember(currentUser)){
						curr.showInfo();
						System.out.println("Sair da comunidade?(s/n)");
						aux = sc.next();
						if(aux.equals("s")){
							curr.removeMember(currentUser);
							System.out.println("comunidade removida.");
						}
					}
					else{
						System.out.println(curr.getName() +"\n"+curr.nOfMembers()+" membros");
						System.out.println("Entrar na comundade?(s/n)");
						aux = sc.next();
						if(aux.equals("s")){
							curr.addRequest(currentUser);
						}
					}
				} else System.out.println("Comunidade não encontrada.");
				
				break;
			default:
				break;
			
			}
		}while(option != 0);
	}
	
	protected void messageBox(){
		int op;
		String aux, text;
		do{
			System.out.println("\n-- "+currentUser.getName()+" --");
			System.out.println("1 - Exibir mensagens");
			System.out.println("2 - Enviar mensagem");
			System.out.println("0 - Sair");
			op = sc.nextInt();
			switch(op){
			case 1:
				currentUser.showMessages();
				break;
			case 2:
				System.out.println("Digite o nome do destino:");
				sc.nextLine();
				aux = sc.nextLine();
				int i = currentUser.searchFriend(aux);
				if(i!=-1) {
					System.out.println("Digite a mensagem:");
					//sc.nextLine();
					text = sc.nextLine();
					Message message = new Message(currentUser, currentUser.friendAt(i), text);
					currentUser.friendAt(i).sendMessage(message);
					currentUser.sendMessage(message);
					System.out.println("Mensagem enviada.");
				} else System.out.println("Você não pode enviar mensagem para esse usuario.");
				break;
			default: break;
			}
			
		}while(op!= 0);
	}
	
	private int searchByEmail(String email){
		for(int i=0; i< this.users.size(); i++){
			
			if(this.users.get(i).getEmail().equals(email)) return i;
		}
		return -1;
	}
	private int searchByName(String name){
		for(int i=0; i< this.users.size(); i++){
			
			if(this.users.get(i).getName().equals(name)) return i;
		}
		return -1;
	}
	private int searchCommName(String name){
		for(int i=0; i< this.communities.size(); i++){
			
			if(this.communities.get(i).getName().equals(name)) return i;
		}
		return -1;
	}
	
	public void saveData(){
		try {
			File dir = new File( "data");			 
			if(!dir.mkdir() ){	
				int i;
				FileWriter file = new FileWriter(new File(dir, "users"));
				PrintWriter writeFile = new PrintWriter(file);
				
				for(i=0; i<this.users.size(); i++){
					writeFile.println(this.users.get(i).toString());
				}					
				file.close();
				writeFile.close();
				
				file = new FileWriter(new File(dir, "communities"));
				writeFile = new PrintWriter(file);
				for(i=0; i<this.communities.size(); i++){
					writeFile.println(this.communities.get(i).toString());
				}
				file.close();
				writeFile.close();
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}		
	}
	
	public void readData(){
		try {
			File dir = new File( "data");
			 
			if(!dir.mkdir() ){
				//Abrindo users
				FileReader file = new FileReader(new File(dir, "users"));
				BufferedReader readFile = new BufferedReader(file);
				String line1,line2,line3 ;
				do{				
				line1 = readFile.readLine();
				if(line1 == null) break;
				line2 = readFile.readLine();
				line3 = readFile.readLine();
				this.users.add(new User(line3,line1,line2));
				
				}while(line1 != null);
				file.close();
				readFile.close();
				//Abrindo communities
				file = new FileReader(new File(dir, "communities"));
				readFile = new BufferedReader(file);
				int n;
				do{
					line1 = readFile.readLine();
					if(line1 == null) break;
					line2 = readFile.readLine();
					line3 = readFile.readLine();
					n = Integer.parseInt(line3);
					line3 = readFile.readLine();
					n--;
					Community c = new Community(line1, users.get(searchByName(line3)));
					while(n>0){
						line3 = readFile.readLine();
						c.addMember(users.get(searchByName(line3)));
						n--;
					}
					c.setDescription(line2);
					communities.add(c);
					
				}while(line1!=null);
				file.close();
				readFile.close();
			}
			
		} catch (IOException e) {
			//e.printStackTrace();
			
		}
	}
	
}
