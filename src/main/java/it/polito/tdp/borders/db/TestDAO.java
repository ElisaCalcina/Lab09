package it.polito.tdp.borders.db;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Border;

public class TestDAO {

	public static void main(String[] args) {

		BordersDAO dao = new BordersDAO();

		Map<Integer, Country>idMap= new HashMap<>();
		dao.loadAllCountries(idMap);
		
		for(Border b: dao.getCountryPairs(1930, idMap)) {
			if(b!=null) {
				System.out.println(b.getC1().getStateAbb()+" "+b.getC2().getStateAbb());
			}
		}
		//System.out.println("Lista di tutte le nazioni:");
		//List<Country> countries = dao.loadAllCountries();
	}
}
