package it.polito.tdp.food.model;

public class Event implements Comparable<Event> {
	public enum EventType {
		IN_PREPARAZIONE,
		FINITO
	}
	
	private Double t;
	private int stazione;
	private Food food;
	private EventType tipo;
	
	
	public Event(Double t, int stazione, Food food, EventType tipo) {
		super();
		this.t = t;
		this.stazione = stazione;
		this.food = food;
		this.tipo = tipo;
	}
	
	public Food getFood() {
		return food;
	}

	public void setFood(Food food) {
		this.food = food;
	}

	public Double getT() {
		return t;
	}
	public void setT(Double t) {
		this.t = t;
	}
	public int getStazione() {
		return stazione;
	}
	public void setStazione(int stazione) {
		this.stazione = stazione;
	}
	public EventType getTipo() {
		return tipo;
	}
	public void setTipo(EventType tipo) {
		this.tipo = tipo;
	}

	
	@Override
	public String toString() {
		return "Event [t=" + t + ", stazione=" + stazione + ", food=" + food + ", tipo=" + tipo + "]";
	}

	@Override
	public int compareTo(Event o) {
		return this.t.compareTo(o.t);
	}
	
	
}
