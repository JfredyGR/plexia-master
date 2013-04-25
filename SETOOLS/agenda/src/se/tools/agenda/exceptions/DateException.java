package se.tools.agenda.exceptions;

public class DateException extends Exception {

	@Override
	public String getMessage() {
		return "A error creating the date was detected";
	}
}
