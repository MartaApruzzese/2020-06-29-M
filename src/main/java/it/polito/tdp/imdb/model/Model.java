package it.polito.tdp.imdb.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph<Director, DefaultWeightedEdge> grafo;
	private Map<Integer, Director> idMap;
	private List<Director> vertici;
	
	
	public Model() {
		this.dao= new ImdbDAO();
	}
	
	public void creaGrafo(int anno) {
		this.grafo= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMap= new HashMap<Integer, Director>();
		this.vertici= new ArrayList<>();
		
		//Popolo idMap
		for(Director d: this.dao.listAllDirectors()) {
			idMap.put(d.getId(), d);
		}
		
		//Creo i vertici
		for(Integer id: this.dao.getIdDirettoriByYear(anno)) {
			vertici.add(idMap.get(id));
		}
		Graphs.addAllVertices(this.grafo, vertici);
		
		
		//Creo gli archi
		for(Adiacenza a: this.dao.listaArchi(anno)) {
			Graphs.addEdgeWithVertices(this.grafo, idMap.get(a.getId1()), idMap.get(a.getId2()), a.getPeso());
		}
		
		
		
		
	}
	
	
	public List<Director> getVertici(){
		return this.vertici;
	}
	
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
}
