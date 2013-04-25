package se.tools.agenda.exceptions;

public class EventException extends Exception {
  	

	@Override
	public String getMessage() {
		return "A error creating the event was detected";
	}
	
}
