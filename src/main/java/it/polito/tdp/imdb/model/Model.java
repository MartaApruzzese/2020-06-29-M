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
	private List<Director> best;
	private int totaleCondivisi;
	

	private int max;
	
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
	
	public List<DirettoriAdiacenti> getRegistiAdiacenti(Director source){
		//Creo una lista di adiacente dove c'e il direttore selezionato
		List<DirettoriAdiacenti> result= new ArrayList<>();
		for(Director d: Graphs.neighborListOf(this.grafo, source)){
			result.add(new DirettoriAdiacenti(d, this.grafo.getEdgeWeight(this.grafo.getEdge(source, d))));
		}
		
		Collections.sort(result, new Comparator<DirettoriAdiacenti>(){

			@Override
			public int compare(DirettoriAdiacenti o1, DirettoriAdiacenti o2) {
				
				return (int) (-1*( o1.getPeso()-o2.getPeso()));
			}
		});
		return result;
	}
	
	
	
	public List<Director> getVertici(){
		return this.vertici;
	}
	
	
	public int getNumArchi() {
		return this.grafo.edgeSet().size();
	}
	
	
	/**
	 * RISOLUZIONE DEL PUNTO 2: RICORSIONE
	 */
	
	public List<Director> calcolaPercorso(int x, Director d){
		this.best=new ArrayList<>();
		this.max=x;
		this.totaleCondivisi=0;
		List<Director> parziale= new ArrayList<>();
		parziale.add(d);
		
		cerca(parziale,0);
		return best;
	}

	private void cerca(List<Director> parziale, int tot) {
		//Condizione di uscita
		/*if(calcolaSomma(parziale)>max) {
			return;
		}
		
		//ricorsività
		for(Director d: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(d)) {
				parziale.add(d);
				cerca(parziale);
				parziale.remove(d);
			}
		}
		
		//Condizione di best
		if(parziale.size()>best.size()) {
			best= new ArrayList<>(parziale);
			totaleCondivisi=calcolaSomma(parziale);
		}*/
		
		
		//RIFO
		
		//Ricorsione
		for(Director d: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			if(!parziale.contains(d)) {
				tot=tot+(int)this.grafo.getEdgeWeight(this.grafo.getEdge(d,parziale.get(parziale.size()-1)));
				parziale.add(d);
				
				if(tot<=this.max) { //condizione di terminazione
					cerca(parziale,tot);
					parziale.remove(d);
				}else
					return;
				
			}
		}
		
		//Condizione migliore
		if(parziale.size()>best.size()) {
			best= new ArrayList<>(parziale);
			totaleCondivisi=tot;
		}
		
		
	}

	private int calcolaSomma(List<Director> parziale) {
		//faccio la somma tra tutti i pesi degli archi
		int sommaTot=0;
		for(int i=parziale.size(); i<=1; i--) {
			Director source= parziale.get(i);
			Director target= parziale.get(i-1);
			double peso= this.grafo.getEdgeWeight(this.grafo.getEdge(source, target));
			sommaTot=sommaTot+(int)peso;
			
		}
		return sommaTot;
	}
	
	public int getTotaleCondivisi() {
		return totaleCondivisi;
	}
}
