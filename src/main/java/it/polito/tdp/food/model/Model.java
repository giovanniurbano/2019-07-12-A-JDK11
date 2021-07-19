package it.polito.tdp.food.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private List<Food> vertici;
	private Map<Integer, Food> idMap;
	
	private Simulator sim;
	
	public Model() {
		this.dao = new FoodDao();
	}
	
	public String creaGrafo(int porzioni) {
		this.grafo = new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		this.vertici = this.dao.getVertici(porzioni);
		this.idMap = new HashMap<Integer, Food>();
		for(Food f : this.vertici)
			this.idMap.put(f.getFood_code(), f);
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//archi
		List<Adiacenza> archi = this.dao.getAdiacenze(idMap);
		for(Adiacenza a : archi)
			Graphs.addEdge(this.grafo, a.getF1(), a.getF2(), a.getPeso());
		
		return String.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}

	public Graph<Food, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}

	public List<Food> getVertici() {
		return vertici;
	}

	public List<Adiacenza> getBestN(Food f, int n) {
		List<Adiacenza> res = new LinkedList<Adiacenza>();
		Set<DefaultWeightedEdge> vicini = this.grafo.edgesOf(f);
		for(DefaultWeightedEdge vicino : vicini) {
			double peso = this.grafo.getEdgeWeight(vicino);
			Food source = this.grafo.getEdgeSource(vicino);
			Food target = this.grafo.getEdgeTarget(vicino);
			
			if(source.equals(f))
				res.add(new Adiacenza(source, target, peso));
			else if(target.equals(f))
				res.add(new Adiacenza(target, source, peso));
		}
		Collections.sort(res);
		if(res.size() > n-1)
			return res.subList(0, n);
		else
			return res;
	}
	
	public void simula(Food f, int k) {
		this.sim = new Simulator(f, k, this);
	}

	public int getNCibi() {
		return this.sim.getNPreparati();
	}

	public Double getTtot() {
		return this.sim.getTempoTotale();
	}
	
}
