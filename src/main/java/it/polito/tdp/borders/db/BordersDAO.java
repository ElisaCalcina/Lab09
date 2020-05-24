package it.polito.tdp.borders.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.borders.model.Border;
import it.polito.tdp.borders.model.Country;

public class BordersDAO {

	public void loadAllCountries(Map<Integer, Country>idMap) {

		String sql = "SELECT ccode, StateAbb, StateNme FROM country ORDER BY StateAbb";
		
		
		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				if(!idMap.containsKey(rs.getInt("ccode"))) {
					Country country= new Country(rs.getInt("ccode"), rs.getString("StateAbb"), rs.getString("StateNme"));
					idMap.put(rs.getInt("ccode"), country);
				}
			}
				
			
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Border> getCountryPairs(int anno, Map<Integer, Country>idMap) {
		String sql= "SELECT state1no, state2no " + 
					"FROM contiguity " + 
					"WHERE conttype=1 AND YEAR <= ?" ;
		List<Border> result= new ArrayList<Border>();
		
		try {
			Connection conn= ConnectDB.getConnection();
			PreparedStatement st= conn.prepareStatement(sql);
			st.setInt(1,anno);
			
			ResultSet rs=st.executeQuery();
			while(rs.next()) {
				Country c1=idMap.get(rs.getInt("state1no"));
				Country c2= idMap.get(rs.getInt("state2no"));
				
				if(c1!=null && c2!=null) {
					result.add(new Border(c1, c2));
				}
			}
			
		}catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore in GET COUNTRY PAIRS");
			throw new RuntimeException("Error Connection Database");
		}
		
		return result;
	}
}
