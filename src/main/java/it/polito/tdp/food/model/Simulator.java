package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;


import it.polito.tdp.food.model.Event.EventType;

public class Simulator {
	//coda degli eventi
	private PriorityQueue<Event> queue;
	
	//modello del mondo
	private Map<Integer, Boolean> libera; //data una stazione mi dice se è libera
	private Map<Integer, Double> tempoRichiesto; //data una stazione mi dice il tempo richiesto per la preparazione in corso
	private List<Food> preparati;
	
	//parametri input
	private int k;
	
	private Model model;
	
	//valori output
	private double tempoTotale;
	
	public Simulator(Food partenza, int k, Model model) {
		this.k = k;
		this.model = model;
		
		this.tempoTotale = 0.0;
		
		//inizializzo il mondo
		this.preparati = new ArrayList<Food>();
		this.tempoRichiesto = new HashMap<Integer, Double>();
		this.libera = new HashMap<Integer, Boolean>();
		for(int i=0; i<this.k; i++) 
			this.libera.put(i, true);
		
		//inizializzo la coda
		this.queue = new PriorityQueue<Event>();
		List<Adiacenza> vicini = this.model.getBestN(partenza, k);
		if(vicini.isEmpty()) {
			throw new IllegalArgumentException();
		}
		int s = 0;
		for(Adiacenza vicino : vicini) {
			this.queue.add(new Event(0.0, s, vicino.getF2(), EventType.IN_PREPARAZIONE));
			this.tempoRichiesto.put(s, vicino.getPeso());
		}
		
		//lancio la simulazione
		this.run();
	}

	private void run() {
		while(this.queue.size() > 0) {
			Event e = this.queue.poll();
			this.processEvent(e);
		}
	}

	private void processEvent(Event e) {
		switch(e.getTipo()) {
		case IN_PREPARAZIONE:
			this.preparati.add(e.getFood());
			this.libera.replace(e.getStazione(), false);
			this.tempoTotale += this.tempoRichiesto.get(e.getStazione());
			this.queue.add(new Event(this.tempoRichiesto.get(e.getStazione()), e.getStazione(), e.getFood(), EventType.FINITO));
			break;
			
		case FINITO:
			List<Adiacenza> vicini = this.model.getBestN(e.getFood(), 100);
			if(vicini.isEmpty()) {
				this.libera.replace(e.getStazione(), true);
			}
			else {
				for(int i=0; i<vicini.size(); i++) {
					Food next = vicini.get(i).getF2();
					if(!this.preparati.contains(next)) {
						this.queue.add(new Event(e.getT(), e.getStazione(), next, EventType.IN_PREPARAZIONE));
						this.tempoRichiesto.replace(e.getStazione(), vicini.get(i).getPeso());
						break;
					}
				}
				this.libera.replace(e.getStazione(), true);
			}
			break;
		}
	}

	public double getTempoTotale() {
		return tempoTotale;
	}
	
	public int getNPreparati() {
		return this.preparati.size();
	}
	
}
