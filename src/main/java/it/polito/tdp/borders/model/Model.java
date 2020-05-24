package it.polito.tdp.borders.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.jgrapht.traverse.GraphIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {
	private SimpleGraph<Country,DefaultEdge> grafo;
	private Map<Integer, Country> idMap;
	private BordersDAO dao;
	private Map<Country, Country> visita;
	private List<Country> nazioni;
	

	public Model() {
		idMap= new HashMap<Integer, Country>();
		dao= new BordersDAO();
		grafo= new SimpleGraph<Country, DefaultEdge>(DefaultEdge.class);
		dao.loadAllCountries(idMap);

	}
	
	
	public void creaGrafo(int anno) {	
	/*	for(Border b:dao.getCountryPairs(anno, idMap)) {
			DefaultEdge e= grafo.getEdge(b.c1, b.c2);
			if(e==null) {
				Graphs.addEdgeWithVertices(grafo, b.getC1(), b.getC2());
			}
		}*/
		
		for (Border b : dao.getCountryPairs(anno, idMap)) {
			grafo.addVertex(b.getC1());
			grafo.addVertex(b.getC2());
			grafo.addEdge(b.getC1(), b.getC2());
		}
		
		nazioni = new ArrayList<Country>(grafo.vertexSet());
		Collections.sort(nazioni);
	}
	
	public int numeroVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int numeroArchi() {
		return this.grafo.edgeSet().size();
	}

	public List<Border> getBorder(int anno){
		return this.dao.getCountryPairs(anno, idMap);
	}
	
	public int gradoVertice(Country c) {
		return this.grafo.degreeOf(c);
	}
	
	public int numComponentiConnesse() {
		ConnectivityInspector<Country, DefaultEdge> c= new ConnectivityInspector<>(grafo);
		return c.connectedSets().size();
	}
	
	public Collection<Country> getCountry(){
		//return this.grafo.vertexSet();
		return nazioni;
	}
	
	public List<Country> trovaVicini(Country c){
		List<Country> vicini= new ArrayList<Country>();
		visita= new HashMap<Country,Country>();
		visita.put(c, null);
		
		GraphIterator<Country, DefaultEdge> bfv= new BreadthFirstIterator<>(grafo, c);
		bfv.addTraversalListener(new TraversalListener<Country, DefaultEdge>(){

			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {				
			}

			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {				
			}

			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
				Country c1=grafo.getEdgeSource(e.getEdge());
				Country c2=grafo.getEdgeTarget(e.getEdge());
				
				if(!visita.containsKey(c1) && visita.containsKey(c2)) {
					visita.put(c1, c2);
				}
				else if(!visita.containsKey(c2) && visita.containsKey(c1)) {
					visita.put(c2, c1);
				}
				
			}

			@Override
			public void vertexTraversed(VertexTraversalEvent<Country> e) {				
			}

			@Override
			public void vertexFinished(VertexTraversalEvent<Country> e) {				
			}
			
		});
		
		//SOLUZIONE CORRETTA CON RIMOZIONE DEL VERTICE DI PARTENZA
		/*while(bfv.hasNext()) {
			vicini.add(bfv.next());
		}
		
		vicini.remove(c);
		return vicini;*/
		
		//SOLUZIONE SENZA RIMOZIONE DEL VERTICE DI PARTENZA
		
		while(bfv.hasNext()) {
			bfv.next();
		}
		if(!visita.containsKey(c)) {
			return null;
		}
		
		for(Country co: visita.keySet()) {
			vicini.add(co);
		}
		return vicini;
	}
	
	public Graph<Country, DefaultEdge> getGrafo(){
		return this.grafo;
	}
}
