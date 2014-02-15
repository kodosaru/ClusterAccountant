/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;
import java.sql.*;
import java.util.logging.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Calendar;
/**
 *
 * @author donj
 */
public class ContributionArrayList extends ArrayList<Contribution> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ContributionArrayList(){
        PreparedStatement pst = null;
        ResultSet rs = null;

         try {
            pst = Global.con.prepareStatement("SELECT * FROM contribution");
            rs = pst.executeQuery();
            Calendar calendar = Calendar.getInstance();
			Long date = 0L;
            while(rs.next()){
                calendar.setTime(rs.getDate("date_contributed"));
                try {
                    date = DateConvert.calToEpoch(calendar);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                add(new Contribution(rs.getInt("id"),date,
                        rs.getString("contributor_userid"),
                        rs.getString("credited_to_userid"),
                        rs.getString("host_name"), rs.getString("processor_type"),
                        rs.getInt("no_of_processors"), rs.getInt("split_percentage"),
                        rs.getFloat("su_charge")));
            }

        } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        } 
    }

    protected void printRows(){
        ListIterator<Contribution> iterator = listIterator();
        while(iterator.hasNext()){
           iterator.next().print();
        }
    }

    protected void sumAllContributions(PrincipalInvestigatorHashMap pihm){
        Contribution c;
        PrincipalInvestigator pi;
        ListIterator<Contribution> iterator = listIterator();
        while(iterator.hasNext()){
            c = iterator.next();
            pi = pihm.get(c.creditedToUserID); 
            pi.contributionSum += c.sumContribution();
            // Don't include exempt PI's contribution in the total
            if(!pi.exempt)
                pihm.totalContributionUnits += c.sumContribution(); 
        }
    }
  }
