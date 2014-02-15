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
public class ProcessorTypeArrayList extends ArrayList<ProcessorType> {
    public enum Cluster { SCLUSTER, PCLUSTER;}

    ProcessorTypeArrayList(){
        PreparedStatement pst = null;
        ResultSet rs = null;
         try {
            pst = Global.con.prepareStatement("SELECT * FROM processor_type");
            rs = pst.executeQuery();
            Calendar calendar = Calendar.getInstance();
			Long date = 0L;
            while(rs.next()){
                calendar.setTime(rs.getDate("date_installed"));
                try {
                    date = DateConvert.calToEpoch(calendar);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage() );
                }
                add(new ProcessorType(rs.getInt("id"), date,
                        rs.getString("regex"), rs.getString("processor_type"),
                        rs.getInt("processor_qty"),
                        rs.getInt("node_qty"), rs.getFloat("su_charge"),
                        rs.getString("cluster"), rs.getBoolean("disabled")));
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

    protected ProcessorType lookupProcessorType(String hostname) {
        ListIterator<ProcessorType> li = listIterator();
        ProcessorType pt;
        while(li.hasNext()){
            pt = li.next();
            if(pt.p.matcher(hostname).lookingAt()) {
                if (Global.debug > 2) {
                    System.out.printf(
                            "Matched hostname: %s regex: %s sucharge: %f cluster: %s\n",
                            hostname, pt.regex, pt.suCharge, pt.cluster);
                }
                return(pt);
            }
        }
        return(null);
    }

    protected Float clusterMaxSU(Cluster cluster, Long date) {
        ListIterator<ProcessorType> li = listIterator();
        ProcessorType pt;
        Float sum = 0.0F;
        if(cluster == Cluster.SCLUSTER){
            while(li.hasNext()){
                pt = li.next();
                if(pt.cluster.equals("scluster") && pt.disabled == false && date >= pt.dateInstalled) 
                    sum += pt.processorQty * pt.nodeQty * pt.suCharge; 
            }
        }
        else {
            while(li.hasNext()){
                pt = li.next();
                if(pt.cluster.equals("katana") && pt.disabled == false && date >= pt.dateInstalled)
                    sum += pt.processorQty * pt.nodeQty * pt.suCharge; 
            }
        }
        if(Global.debug > 2)
            System.out.println("sum = " + sum);
        return(sum);
    }

    protected void printRows(){
        ListIterator<ProcessorType> iterator = listIterator();
        while(iterator.hasNext()){
           iterator.next().print();
        }
    }

}
