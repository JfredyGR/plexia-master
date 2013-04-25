package se.tools.agenda;

import se.tools.agenda.exceptions.AgendaException;

public class Agenda {

	public Agenda() {
	}

	public boolean add(Event e) {
		return true;
	}

	public boolean delete(String name) {
		return true;
	}

	public String findEvent(Date d) {
		return null;
	}

	public Date initDate(String name) throws AgendaException {
		return null;
	}

	public Date endDate(String name) throws AgendaException {
		return null;
	}

	public boolean intended(String nom) {
		return true;
	}

}
