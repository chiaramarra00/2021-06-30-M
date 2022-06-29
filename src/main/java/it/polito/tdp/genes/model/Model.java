package it.polito.tdp.genes.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {
	
private GenesDao dao;
	
	private Graph<Integer,DefaultWeightedEdge> grafo;
	
	private List<Integer> listaMigliore;
	
	public Model() {
		dao = new GenesDao();
	}
	
	public void creaGrafo() {
		//creo il grafo
		this.grafo = 
				new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		
		//aggiungo i vertici
		Graphs.addAllVertices(this.grafo, 
				this.dao.getVertici());
		
		//aggiungo gli archi
		for(Adiacenza a : this.dao.getArchi()) {
			Graphs.addEdgeWithVertices(this.grafo, a.getC1(), 
					a.getC2(), a.getPeso());
		}
		
		System.out.println("Grafo creato!");
		System.out.println(String.format("# Vertici: %d", 
				this.grafo.vertexSet().size()));
		System.out.println(String.format("# Archi: %d", 
				this.grafo.edgeSet().size()));
	}
	
	public ArrayList<Integer> getVertici(){
		return new ArrayList<>(this.grafo.vertexSet());
	}
	
	public boolean grafoCreato() {
		if(this.grafo == null)
			return false;
		else 
			return true;
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}

	public double getPesoMin() {
		double min = 100000;
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			if (grafo.getEdgeWeight(e)<min) {
				min = grafo.getEdgeWeight(e);
			}
		}
		return min;
	}

	public double getPesoMax() {
		double max = 0;
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			if (grafo.getEdgeWeight(e)>max) {
				max = grafo.getEdgeWeight(e);
			}
		}
		return max;
	}

	public int getMaggiori(double s) {
		int numMaggiori = 0;
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			if (grafo.getEdgeWeight(e)>s) {
				numMaggiori++;
			}
		}
		return numMaggiori;
	}

	public int getMinori(double s) {
		int numMinori = 0;
		for (DefaultWeightedEdge e : grafo.edgeSet()) {
			if (grafo.getEdgeWeight(e)<s) {
				numMinori++;
			}
		}
		return numMinori;
	}
	
	public List<Integer> cercaLista(double s){
		Set<Integer> cromosomiValidi = grafo.vertexSet();
		List<Integer> parziale = new ArrayList<>();
		listaMigliore = new ArrayList<>();
		
		cerca(parziale,cromosomiValidi,s);
		
		return listaMigliore;
	}
	
	private void cerca(List<Integer> parziale, Set<Integer> cromosomiValidi, double s) {
		//controllo soluzione migliore
		if(sommaPesiArchi(parziale) > sommaPesiArchi(listaMigliore)) {
			listaMigliore = new ArrayList<>(parziale);
		}
		
		for(Integer c : cromosomiValidi) {
			if(parziale.size()==0) {
				parziale.add(c);
				cerca(parziale, getCromosomiValidi(c,s),s);
				parziale.remove(parziale.size()-1);
			}
			else if(!parziale.contains(c)) {
				parziale.add(c);
				cerca(parziale, getCromosomiValidi(c,s),s);
				parziale.remove(parziale.size()-1);
			}
		}
		
		
	}

	private Set<Integer> getCromosomiValidi(Integer c1, double s) {
		Set<Integer> result = new HashSet<Integer>();
		List<Integer> vicini = Graphs.successorListOf(grafo, c1);
		for (Integer c2 : vicini) {
			if (grafo.getEdgeWeight(grafo.getEdge(c1, c2))>s) {
				result.add(c2);
			}
		}
		return result;
	}

	private double sommaPesiArchi(List<Integer> parziale) {
		double somma = 0;
		for (int i=1; i<parziale.size(); i++)
			somma += grafo.getEdgeWeight(grafo.getEdge(parziale.get(i-1), parziale.get(i)));
		return somma;
	}

}