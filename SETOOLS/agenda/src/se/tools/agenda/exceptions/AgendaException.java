package se.tools.agenda.exceptions;

public class AgendaException extends Exception {
	
	@Override
	public String getMessage() {
		return "A error handling the agenda was detected";
	}

}
