/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;
import java.sql.*;
import java.util.Map.Entry;
import java.util.logging.*;
import java.util.*;
/**
 *
 * @author donj
 */
public class PrincipalInvestigatorHashMap extends HashMap <String,PrincipalInvestigator> {
    protected static final long serialVersionUID = 1L;
    protected Float totalServiceUnits = 0.0F;
    protected Float totalContributionUnits = 0.0F;

    PrincipalInvestigatorHashMap(){
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = Global.con.prepareStatement("SELECT * FROM principal_investigator");
            rs = pst.executeQuery();

            while(rs.next()){
                put(rs.getString("userid"), new PrincipalInvestigator(rs.getInt("uid"),
                        rs.getString("userid"), rs.getString("first_name"),
                        rs.getString("last_name"), rs.getString("alias"),
                        rs.getString("email_address"), rs.getBoolean("exempt")));
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
        Iterator<Entry<String, PrincipalInvestigator>> iterator = entrySet().iterator();
        while(iterator.hasNext()){
           iterator.next().getValue().print();
        }
    } 

    protected void zeroSUSums(){
        Iterator<Entry<String, PrincipalInvestigator>> iterator = entrySet().iterator();
        while(iterator.hasNext()){
           iterator.next().getValue().suSum = 0.0F;
        }
    } 

    protected void printContributionReport() {
        PrincipalInvestigator pi;
        Iterator<Entry<String, PrincipalInvestigator>> iterator = entrySet().iterator();
        System.out.printf("Contributions Units by PI\n");
        System.out.printf("%18s  %8s  %8s  %6s\n", "PI", "Units",
               "Units %", "Exempt");
        while(iterator.hasNext()){
            pi = iterator.next().getValue();
            String exemptYN = pi.exempt ? "Yes" : "No";
            System.out.printf("%18s  %8.2f  %8.0f%% %6s\n", pi.alias, pi.contributionSum,
                   pi.contributionSum/totalContributionUnits*100.0F,exemptYN);
        }
        System.out.printf("Contribution Unit Total (minus exempt): %.2f",totalContributionUnits);
    } 

    protected void printReport() {
        PrincipalInvestigator pi;
        Float contributionPercent,suPercent,subscriptionPercent;
        Iterator<Entry<String, PrincipalInvestigator>> iterator = entrySet().iterator();
        System.out.printf("Interval Service Units by PI\n");
        System.out.printf("%18s  %9s  %8s  %13s\n", "PI", "Units",
               "Units %", "Subscribed %");
        while(iterator.hasNext()){
            pi = iterator.next().getValue();
            if(totalContributionUnits == 0.0F){
                    System.err.println("Error: Total contribution units equals 0");
                    return;
            }
            contributionPercent=pi.contributionSum/totalContributionUnits;
            if(totalServiceUnits > 0.0F){
                suPercent=pi.suSum/totalServiceUnits;
                subscriptionPercent=suPercent/contributionPercent;
                if(contributionPercent > 0.0F && subscriptionPercent > 0.0F)
                    System.out.printf("%18s  %8.2f  %8.0f%%  %12.0f%%\n", pi.alias, pi.suSum / 3600.0F,
                           suPercent*100.0, subscriptionPercent*100.0F);
                else if(contributionPercent == 0.0F && suPercent > 0.0F)
                    System.out.printf("%18s  %8.2f  %8.0f%%  %12s\n", pi.alias, pi.suSum / 3600.0F,
                           suPercent*100.0, "Over");
                else if(contributionPercent > 0.0F && suPercent == 0.0F)
                    System.out.printf("%18s  %8.2f  %8.0f%%  %12s\n", pi.alias, pi.suSum / 3600.0F,
                           suPercent*100.0, pi.exempt ? "N/A" : "Under");
                else if(contributionPercent == 0.0F && suPercent == 0.0F)
                    System.out.printf("%18s  %8.2f  %8.0f%%  %12s\n", pi.alias, pi.suSum / 3600.0F,
                           suPercent*100.0, "N/A");
                else
                    System.err.printf("Error: Erroneous subscription calculation for PI: "
                            +"%18s contributePercent: %8.2fi% suPercent: %8.2f%\n",
                            pi.userID, contributionPercent*100.0F,suPercent*100.0F);
                }else{
                    System.out.printf("%18s  %8s\n", pi.alias, "No SU records meet criteria");
                }
            }  // end while
            System.out.printf("Service Unit Total: %.2f",totalServiceUnits / 3600.0F);
        } // end method
    
} // end class
