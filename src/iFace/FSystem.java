package iFace;

import java.util.ArrayList;
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
		} else System.out.println("Email ja cadastrado.");
	}
	
	public void login(){
		String nEmail;
		String nPass;
		System.out.println("Digite seu email:");
		nEmail = sc.next();
		int aux = this.searchByEmail(nEmail);
		if(aux!=-1){
			System.out.println("Digite sua senha:");
			nPass = sc.next();
			if(this.users.get(aux).authentic(nPass)){
				System.out.println(this.users.get(aux).getName() + " logado.");
				this.currentUser = this.users.get(aux);
				this.currentIndex = aux;
			}
			else System.out.println("senha incorreta.");
		}
		else {
			System.out.println("login inválido.");
			//exception
		}
		
	}
	public void homepage(){
		int option;
		do{
			System.out.println("\n***** iFace ******\n");
			System.out.println("  1 - Criar conta");
			System.out.println("  2 - Entrar");
			System.out.println("  0 - Sair");
			
			option = sc.nextInt();
			switch(option){
			case 1:
				createUser();
				break;
			case 2:
				login();
				userPanel();
				break;
			default:
				//saveData();
				break;
			}
		}while(option != 0);
	}
	
	public void userPanel(){
		int option;
		do{
			System.out.println("-- " +this.currentUser.getName()+" --");
			System.out.println("  1 - Minha conta");
			System.out.println("  2 - Amigos");
			System.out.println("  3 - Comunidades");
			System.out.println("  4 - Mensagens");
			System.out.println("  5 - Excluir conta");
			System.out.println("  0 - Sair");
		
			option = sc.nextInt();
			switch(option){
			case 1:
				this.acountOptions();
				break;
			case 2:
				this.friendOptions();
				break;
			case 3:
				//
				break;
			case 4:
				//
				break;
			case 5:
				logout();
				this.users.remove(currentIndex);
				System.out.println("Conta Removida.");
				option = 0;
				break;
			case 0:
				logout();
				break;
			default:
				break;
			}
		}while(option != 0);
	}
	
	public void logout(){
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
		String aux;
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
				int id = this.searcByName(aux);
				if(id != -1){
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
				break;
			default:
				break;
			}
			}while(option !=0);
	
	}
	
	protected int searchByEmail(String email){
		for(int i=0; i< this.users.size(); i++){
			
			if(this.users.get(i).getEmail().equals(email)) return i;
		}
		return -1;
	}
	protected int searcByName(String name){
		for(int i=0; i< this.users.size(); i++){
			
			if(this.users.get(i).getName().equals(name)) return i;
		}
		return -1;
	}
	
	public void saveData(){
		try {
			File dir = new File( "data");			 
			if(!dir.mkdir() ){				
			FileWriter file = new FileWriter(new File(dir, "users"));
			PrintWriter writeFile = new PrintWriter(file);
			String aux;
			for(int i=0; i<this.users.size(); i++){
				writeFile.println(this.users.get(i).toString());
			}					
			file.close();
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
		
	}
	
	public void readData(){
		try {
			File dir = new File( "data");
			 
			if(!dir.mkdir() ){
				
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
			}
			
		} catch (IOException e) {
			//e.printStackTrace();
			
		}
	}
	
}
