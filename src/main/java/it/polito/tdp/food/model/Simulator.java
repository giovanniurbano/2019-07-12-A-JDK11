package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Simulator {
	//coda degli eventi
	private Map<Integer, Food> lavorazioni;
	private List<Food> preparati;
	
	//modello mondo
	private Model model;
	private int max;
	
	//parametri input
	private int k;
	private Food partenza;
	
	//valori output
	private Double tempoImpiegato;
	
	public Simulator(Model model, int k, Food f) {
		this.model = model;
		this.k = k;
		this.partenza = f;
		
		this.preparati = new ArrayList<Food>();
		
		this.tempoImpiegato = 0.0;
		
		//inizializzo le stazioni
		this.lavorazioni = new HashMap<Integer, Food>();
		this.max = 0;
		List<Adiacenza> vicini = this.model.getViciniCalorie(f, k);
		
		if(vicini.size() >= this.k)
			max = this.k;
		else
			max = vicini.size();
		
		for(int i=0; i<max; i++) {
			if(i == 0) {
				this.lavorazioni.replace(0, this.partenza);
				this.preparati.add(this.partenza);
			}
			else {
				this.lavorazioni.replace(i, vicini.get(i-1).getF2());
				this.preparati.add(vicini.get(i-1).getF2());
			}
		}
	}

	public void run() {
		int libere = 0;
		while(libere != max) {
			for(Integer stazione : this.lavorazioni.keySet()) {
				if(this.lavorazioni.get(stazione) != null) {
					List<Adiacenza> vicini = this.model.getViciniCalorie(this.lavorazioni.get(stazione), max);
					if(vicini.size() > 0) {
						Food next = vicini.get(0).getF2();
						if(!preparati.contains(next)) {
							this.lavorazioni.replace(stazione, next);
							this.preparati.add(next);
							this.tempoImpiegato += vicini.get(0).getPeso();
						}
						else {
							this.lavorazioni.replace(stazione, null);
							libere++;
						}
					}
					else {
						this.lavorazioni.replace(stazione, null);
						libere++;
					}
				}
			}
		}
	}
	
	public int getnCibi() {
		return this.preparati.size();
	}

	public Double getTempoImpiegato() {
		return tempoImpiegato;
	}
	
}
