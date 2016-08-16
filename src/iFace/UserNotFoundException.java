package iFace;

@SuppressWarnings("serial")
public class UserNotFoundException extends Exception {

	public UserNotFoundException() {
		super("Usuário não encontrado.");
	}

}
