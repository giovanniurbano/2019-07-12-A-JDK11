package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Adiacenza;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM `portion`" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}

	public List<Food> getVertici(int porzioni) {
		String sql = "SELECT f.food_code, f.display_name "
				+ "FROM `portion` p, food f "
				+ "WHERE p.food_code = f.food_code "
				+ "GROUP BY f.food_code, f.display_name "
				+ "HAVING COUNT(*) <= ?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Adiacenza> getAdiacenze(int porzioni, Map<Integer, Food> idMap) {
		String sql = "SELECT f1.food_code AS f1, f2.food_code AS f2, AVG(c1.condiment_calories) AS peso "
				+ "FROM food f1, food f2, food_condiment fc1, food_condiment fc2, condiment c1, condiment c2 "
				+ "WHERE f1.food_code = fc1.food_code AND f2.food_code = fc2.food_code "
				+ "AND fc1.condiment_code = c1.condiment_code AND fc2.condiment_code = c2.condiment_code "
				+ "AND c1.condiment_code = c2.condiment_code "
				+ "AND f1.food_code < f2.food_code "
				+ "AND f1.food_code IN (SELECT f.food_code "
				+ "							FROM `portion` p, food f  "
				+ "							WHERE p.food_code = f.food_code  "
				+ "							GROUP BY f.food_code "
				+ "							HAVING COUNT(*) <= ?) "
				+ "AND f2.food_code IN (SELECT f.food_code "
				+ "							FROM `portion` p, food f  "
				+ "							WHERE p.food_code = f.food_code  "
				+ "							GROUP BY f.food_code "
				+ "							HAVING COUNT(*) <= ?) "
				+ "GROUP BY f1.food_code, f2.food_code "
				+ "HAVING peso > 0" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, porzioni);
			st.setInt(2, porzioni);
			
			List<Adiacenza> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					Food f1 = idMap.get(res.getInt("f1"));
					Food f2 = idMap.get(res.getInt("f2"));
					
					if(f1 != null && f2 != null && !f1.equals(f2)) {
						list.add(new Adiacenza(f1, f2, res.getDouble("peso")));
					}
					
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
}
