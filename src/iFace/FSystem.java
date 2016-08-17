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
			System.out.println("Digite seu nome de usuario:");
			sc.nextLine();
			nName = sc.nextLine();
			while(this.searchByName(nName) !=-1){
				System.out.println("Este nome ja é usado. Digite outro nome:");
				//sc.nextLine();
				nName = sc.nextLine();
			}
			System.out.println("Digite sua senha:");
			nPass = sc.next();
			
			users.add(new User(nName, nEmail, nPass));
			System.out.println(nName+" adicionado.");
		} else System.err.println("Email ja cadastrado.\n");
	}
	
	private void login(){
		String nEmail;
		String nPass;
		
		System.out.println("Digite seu email:");
		nEmail = sc.next();
		int aux = this.searchByEmail(nEmail);
		System.out.println("Digite sua senha:");
		nPass = sc.next();	
		if(this.users.get(aux).authentic(nPass)){
			System.out.println(this.users.get(aux).getName() + " logado.");
			this.currentUser = this.users.get(aux);
			this.currentIndex = aux;
		}		
	}
	public void homepage(){
		int option = 3;
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
				}catch(IndexOutOfBoundsException e){
					System.err.println("login invalido.\n");
				} catch(NullPointerException e){
					System.err.println("Senha incorreta.\n");
				}
				
				break;
			case 0:
				break;
			default:
				System.err.println("Entrada invalida.\n");
				break;
			}
			
			} catch(InputMismatchException e){
				System.err.println("Entrada invalida.\n");
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
		int option = 0;
		do{
			System.out.println("\n-- "+this.currentUser.getName()+" --");
			System.out.println("  1 - Exibir info.");
			System.out.println("  2 - Editar");
			System.out.println("  0 - Voltar\n");
			try{
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
			}catch(InputMismatchException e){
				System.err.println("Entrada invalida.\n");
				sc.nextLine();				
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
				if(searchByName(aux) != -1) System.err.println("Ja existe um usuario com esse nome.\n");
				else{
					this.currentUser.setName(aux);
					System.out.println("Nome alterado para "+this.currentUser.getName());
				}
				break;
			case 2:
				System.out.println("Digite o novo email:");
				aux = sc.next();
				if(searchByEmail(aux) == -1){
					this.currentUser.setEmail(aux);
					System.out.println("Email alterado para "+this.currentUser.getEmail());
				} else System.err.println("Este email ja possui conta.\n");
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
		int option = 0;
		String aux; int id;
		do{
			System.out.println("\n-- "+this.currentUser.getName()+" --");
			System.out.println("  1 - Adicionar um amigo");
			System.out.println("  2 - Ver lista de amigos");
			System.out.println("  3 - Responder solicitaçoes");
			System.out.println("  4 - Desfazer amizade");
			System.out.println("  0 - Voltar\n");
			try{
				option = sc.nextInt();
				switch(option){
				case 1:
					System.out.println("Digite o nome:");
					sc.nextLine();
					aux = sc.nextLine();
					id = this.searchByName(aux);
					try{
						if(id == currentIndex){	
							System.out.println("Você não pode adicionar a si mesmo(a).");
						}else{		
							this.users.get(id).request(currentUser);
							System.out.println("Solicitação enviada para "+aux);
						}
					}catch(IndexOutOfBoundsException e){
						System.err.println("Usuario não encontrado.\n");
					}
					break;
				case 2:
					currentUser.listFriends();
					break;
				case 3:
					try{
					//if(currentUser.hasRequest()){
						User f = currentUser.answerRequest();
						System.out.println("Aceitar?(s/n)");
						String a = sc.next();
						if(a.equals("s")){
							currentUser.addFriend(f);
							System.out.println("Amigo adicionado!");
						} else System.out.println("Solicitaçao removida.");
					//} else System.out.println("Sem solicitaçoes.");
					}catch(IndexOutOfBoundsException e){
						System.err.println("Sem solicitações.\n");
					}
					break;
				case 4:
					System.out.println("Desfazer Amizade.\nDigite o nome:");
					sc.nextLine();
					aux = sc.nextLine();
					id = this.searchByName(aux);
					try{
						currentUser.removeFriend(this.users.get(id));
						System.out.println(this.users.get(id).getName() +" removido.");
					
					}catch(NullPointerException e){
						System.err.println(e.getMessage());
					}catch(IndexOutOfBoundsException e){
						System.err.println("Usuario não encontrado.\n");
					}
					
					break;
				default:
					break;
				}
			}catch(InputMismatchException e){
				System.err.println("Entrada invalida.\n");
				sc.nextLine();				
			}
			}while(option !=0);
	
	}
	
	protected void communityOptions(){
		int option = 4;
		String aux; int id;
		do{
			System.out.println("\n-- "+this.currentUser.getName()+" --");
			System.out.println("  1 - Criar uma comunidade");
			System.out.println("  2 - Ver minhas comunidades");
			System.out.println("  3 - Entrar em uma comunidade");
			System.out.println("  0 - Voltar\n");
			try{
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
				} else System.out.println("Uma comunidade com esse nome já existe.");
				break;
			case 2:
				currentUser.showCommunities();
				
				break;
			case 3:
				String op2;
				System.out.println("Digite o nome da comunidade a ser pesquisada:");
				sc.nextLine();
				aux = sc.nextLine();
				id = searchCommName(aux);
				if(id != -1){
					Community curr = this.communities.get(id);
					int op = 6;
					//Admin. da comunidade
					if(currentUser == curr.getAdmin()){
						do{
							System.out.println("\n-- "+curr.getName()+" --");
							System.out.println("1 - Exibir info.");
							System.out.println("2 - Adicionar usuario");
							System.out.println("3 - Remover usuario");
							System.out.println("4 - Responder solicitações");
							System.out.println("5 - Exibir mensagens");
							System.out.println("6 - Enviar mensagem");
							System.out.println("7 - Excluir comunidade");
							System.out.println("0 - Sair");
							try{
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
								try{
								if(id != currentIndex){
									curr.removeMember(this.users.get(id));
									System.out.println("Usuario removido.");
								}else System.out.println("O dono da comunidade não pode ser excluido.");
								}catch(IndexOutOfBoundsException e){
									System.err.println("Este usuario não está na comunidade.\n");
								}
								break;
							case 4:
								try{
									User user = curr.answerUser();
									System.out.println("Aceitar? (s/n)");
									op2 = sc.next();
									System.out.println(op2);
									if(op2.equals("s")){
										if(!curr.isMember(user)){
											curr.addMember(user);
											System.out.println("Usuario adicionado.");
										}
									}
								}catch(NullPointerException e){
									System.err.println("Sem solicitações.\n");
								}catch(IndexOutOfBoundsException e){
									System.err.println("Sem solicitações.\n");
								}
								break;
							case 5:
								System.out.println("Mensagens da comunidade:\n");
								curr.showMessages();
								break;
							case 6:
								System.out.println("Digite a mensagem:");
								sc.nextLine();
								curr.receiveMessage(new Message(currentUser, null, sc.nextLine()));
								System.out.println("\nMensagem enviada!\n");
								break;
							case 7:
								try{
									curr.removeAll();
									this.communities.remove(id);
									op = 0;
								}catch(IndexOutOfBoundsException e){
									System.err.println(id);
								}
								break;
							case 0: break;
								
							}
							} catch(InputMismatchException e){
								System.err.println("entrada invalida.\n");
								sc.nextLine();				
							}
							
						}while(op!= 0);
					}
					
					//Membro da comunidade
					else if(curr.isMember(currentUser)){
						//curr.showInfo();						
						do{
							System.out.println(" 1 - Exibir info.");
							System.out.println(" 2 - Enviar mensagem para a comunidade");
							System.out.println(" 3 - Ver mensagens da comunidade");
							System.out.println(" 4 - Sair da comunidade");
							System.out.println(" 0 - Voltar");
							
							op = sc.nextInt();
							switch(op){
							case 1:
								curr.showInfo();
								break;
							case 2:
								System.out.println("Digite a mensagem:");
								sc.nextLine();
								curr.receiveMessage(new Message(currentUser, null, sc.nextLine()));
								System.out.println("\nMensagem enviada!\n");
								break;
							case 3:
								System.out.println("Mensagens da comunidade:\n");
								curr.showMessages();
								break;
							case 4:
								System.out.println("Sair da comunidade?(s/n)");
								try{
								op2 = sc.next();
								if(op2.equals("s")){
									curr.removeMember(currentUser);
									System.out.println("comunidade removida.");
								}
								}catch(NullPointerException e){
									e.printStackTrace();
								}
								break;
							case 0: break;
							}
						}while (op!=0);
					}
					//Usuario não é membro
					else{
						System.out.println(curr.getName() +"\n"+curr.nOfMembers()+" membros");
						System.out.println("Entrar na comundade?(s/n)");
						try{
						op2 = sc.next();
						if(op2.equals("s")){
							curr.addRequest(currentUser);
							System.out.println("Solicitação enviada.\n");
						}
						}catch(NullPointerException e){
							e.printStackTrace();
						}
					}
				} else System.out.println("Comunidade não encontrada.");
				
				break;
			default:
				break;
			
			}
			
			} catch(InputMismatchException e){
				System.err.println("entrada invalida.\n");
				sc.nextLine();				
			}
		}while(option != 0);
	}
	
	protected void messageBox(){
		int op = 0;
		String aux, text;
		do{
			System.out.println("\n-- "+currentUser.getName()+" --");
			System.out.println("1 - Exibir conversa com um usuario");
			System.out.println("2 - Exibir todas as mensagens");
			System.out.println("3 - Enviar mensagem");
			System.out.println("0 - Voltar");
			
			try{
				op = sc.nextInt();
				switch(op){
				case 1:
					System.out.println("Digite o nome do usuario:");
					sc.nextLine();
					aux = sc.nextLine();
					currentUser.conversation(aux);
					break;
				case 2:
					currentUser.showMessages();
					break;
				case 3:
					System.out.println("Digite o nome do destino:");
					sc.nextLine();
					aux = sc.nextLine();
					try{
					int i = currentUser.searchFriend(aux);
					//if(i!=-1) {
						System.out.println("Digite a mensagem:");
						//sc.nextLine();
						text = sc.nextLine();
						Message message = new Message(currentUser, currentUser.friendAt(i), text);
						currentUser.friendAt(i).sendMessage(message);
						currentUser.sendMessage(message);
						System.out.println("Mensagem enviada.");
					//} else System.out.println("Você não pode enviar mensagem para esse usuario.");
					}catch(IndexOutOfBoundsException e){
						System.out.println("Você não pode enviar mensagem para esse usuario.");
					}
					break;
				case 0: break;
				}
			}catch(InputMismatchException e){
				System.err.println("Entrada invalida.\n");
				sc.nextLine();				
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
				//arquivo de usuarios
				FileWriter file = new FileWriter(new File(dir, "users"));
				PrintWriter writeFile = new PrintWriter(file);
				
				for(i=0; i<this.users.size(); i++){
					writeFile.println(this.users.get(i).toString());
				}					
				file.close();
				writeFile.close();
				//arquivo de comunidades
				file = new FileWriter(new File(dir, "communities"));
				writeFile = new PrintWriter(file);
				for(i=0; i<this.communities.size(); i++){
					writeFile.println(this.communities.get(i).toString());
				}
				file.close();
				writeFile.close();
				//arquivo de amizades
				file = new FileWriter(new File(dir, "friends"));
				writeFile = new PrintWriter(file);
				for(i=0; i<this.users.size(); i++){
					if(this.users.get(i).hasFriends()){
						writeFile.println(i);
						writeFile.println(this.users.get(i).fprintFriends());
					}
					
				}
				file.close();
				writeFile.close();
				//arquivo de mensagens e solicitações
				file = new FileWriter(new File(dir, "messagebox"));
				writeFile = new PrintWriter(file);
				for(i=0; i<this.users.size(); i++){
					if(users.get(i).hasMessages() || users.get(i).hasRequest()){
						writeFile.println(i);
						writeFile.println(this.users.get(i).fprintMessageBox());
					}
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
			File dir = new File("data");
			 
			if(!dir.mkdir() ){
				//Abrindo users
				FileReader file = new FileReader(new File(dir, "users"));
				BufferedReader readFile;
				readFile = new BufferedReader(file);
				String line1,line2,line3,line4;
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
					Community comm = new Community(line1, users.get(searchByName(line3)));
					while(n>0){
						line4 = readFile.readLine();
						comm.addMember(users.get(searchByName(line4)));
						n--;
					}
					comm.setDescription(line2);
					line3 = readFile.readLine();
					n = Integer.parseInt(line3);
					while(n>0){
						line2 = readFile.readLine();
						line4 = readFile.readLine();
						comm.receiveMessage(new Message(users.get(searchByName(line2)), null, line4));
						n--;
					}
					
					communities.add(comm);
					
				}while(line1!=null);
				file.close();
				readFile.close();
				line1 = null;
				
				//Abrindo friends
				file = new FileReader(new File(dir, "friends"));
				readFile = new BufferedReader(file);
				int i = 0;
				do{
					line1 = readFile.readLine();
					if(line1 == null) break;
					i = Integer.parseInt(line1);
					line2 = readFile.readLine();
					n = Integer.parseInt(line2);
					while(n>0){
						line3 = readFile.readLine();
						User friend = this.users.get(this.searchByName(line3));
						this.users.get(i).newFriend(friend);
						n--;
					}
				}while(line1!=null);
				file.close();
				readFile.close();
				line1 = null;
				
				//Abrindo messagebox
				file = new FileReader(new File(dir, "messagebox"));
				readFile = new BufferedReader(file);
				i = 0;
				do{
					line1 = readFile.readLine();
					if(line1 == null) break;
					i = Integer.parseInt(line1);
					line1 = readFile.readLine();
					n = Integer.parseInt(line1);
					while(n>0){
						line2 = readFile.readLine();
						line3 = readFile.readLine();
						line4 = readFile.readLine();	
						Message msg;
						if(line2.equals(users.get(i).getName())){
							msg = new Message(users.get(i), users.get(this.searchByName(line3)), line4);
						} else msg = new Message( users.get(this.searchByName(line2)), users.get(i), line4);
						this.users.get(i).sendMessage(msg);
						n--;
					}
					line2 = readFile.readLine();
					n = Integer.parseInt(line2);
					while(n>0){						
						line3 = readFile.readLine();	
						this.users.get(i).request(users.get(this.searchByName(line3)));
						n--;
					}
				}while(line1!=null);				
			}
			
		} catch (IOException e) {
			e.printStackTrace();			
		} catch (IndexOutOfBoundsException e){
			e.printStackTrace();
		} catch (NumberFormatException e){
			e.printStackTrace();
		}
	}	
}
