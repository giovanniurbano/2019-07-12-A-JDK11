package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	public Model() {
		this.dao = new FoodDao();
	}
	
	public String creaGrafo(int porzioni) {
		this.grafo = new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		//vertici
		this.idMap = new HashMap<Integer, Food>();
		this.vertici = this.dao.getVertici(porzioni);
		for(Food f : vertici)
			this.idMap.put(f.getFood_code(), f);
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		//archi
		List<Adiacenza> archi = this.dao.getAdiacenze(porzioni, idMap);
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

	public List<Adiacenza> getViciniCalorie(Food f) {
		List<Food> vicini = Graphs.neighborListOf(this.grafo, f);
		List<Adiacenza> topFive = new ArrayList<Adiacenza>();
		for(Food vicino : vicini) {
			DefaultWeightedEdge e = this.grafo.getEdge(f, vicino);
			topFive.add(new Adiacenza(f, vicino, this.grafo.getEdgeWeight(e)));
		}
		
		Collections.sort(topFive);
		
		return topFive.subList(0, 5);
	}
	
	
}
