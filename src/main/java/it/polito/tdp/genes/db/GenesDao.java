package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.genes.model.Adiacenza;
import it.polito.tdp.genes.model.Genes;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}

	public List<Integer> getVertici() {
		String sql = "select distinct Chromosome "
				+ "from genes "
				+ "where Chromosome!=0 "
				+ "order by Chromosome";
		List<Integer> result = new ArrayList<Integer>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(res.getInt("Chromosome"));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}

	public List<Adiacenza> getArchi() {
		String sql = "select distinct Chromosome1, Chromosome2, sum(Expression_Corr) as Peso "
				+ "from (select distinct g1.Chromosome as Chromosome1, g2.Chromosome as Chromosome2, g1.GeneID as Gene1, g2.GeneID as Gene2, Expression_Corr "
				+ "from genes g1, genes g2, interactions i "
				+ "where g1.Chromosome!=0 and g2.Chromosome!=0 and g1.Chromosome!=g2.Chromosome and g1.GeneID=i.GeneID1 and g2.GeneID=i.GeneID2 "
				+ "group by g1.Chromosome, g2.Chromosome, g1.GeneID, g2.GeneID "
				+ "order by g1.Chromosome, g2.Chromosome, g1.GeneID, g2.GeneID) as t "
				+ "group by Chromosome1, Chromosome2";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				result.add(new Adiacenza(res.getInt("Chromosome1"),res.getInt("Chromosome2"),res.getDouble("Peso")));
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	


	
}
