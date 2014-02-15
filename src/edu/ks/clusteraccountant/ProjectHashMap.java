/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;
import java.sql.*;
import java.util.Map.Entry;
import java.util.logging.*;
import java.util.regex.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.io.*;
/**
 *
 * @author donj
 */
public class ProjectHashMap extends HashMap <String,Project> {
    protected PreparedStatement pst = null;
    protected ResultSet rs = null;
    protected static final long serialVersionUID = 1L;
    private HashSet<String> projectAccessSet = new HashSet<String>();
    protected Float totalServiceUnits = 0.0F;

    ProjectHashMap(){
        try {
            pst = Global.con.prepareStatement("SELECT * FROM project");
            rs = pst.executeQuery();

            while(rs.next()){
                put(rs.getString("groupid"), new Project(rs.getInt("gid"),
                        rs.getString("groupid"), rs.getString("pi_owner_userid")));
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
        // Confirm the projects listed in MySQL data are the same as in
        // /etc/security/access.conf
        validateProjectHashMap();
    }

   private void validateProjectHashMap() {
        String REGEX = ":";
        Pattern p = Pattern.compile(REGEX);
        Project project;

        try {
            FileReader readfile;
            BufferedReader readbuffer;
            String strRead;
            Accounting acct;

            readfile = new FileReader(Global.accessfile);
            readbuffer = new BufferedReader(readfile);
            while ((strRead = readbuffer.readLine()) != null) {
                String splitarray[] = p.split(strRead);

                if (strRead.charAt(0) != '#') {
                    if (splitarray.length == 3) {
                        String groupid = splitarray[1].trim();
                        projectAccessSet.add(groupid);
                    } else {
                        System.err.printf(
                                "Error: SGC access.conf record has"
                                        + splitarray.length
                                        + " fields instead of 3");
                    }
                }
            }
            readbuffer.close();
        } catch (FileNotFoundException e) { 
            System.err.println(
                    "Error: Unable to find file SGE account file: " + Global.accessfile);
        } catch (IOException e) { 
            System.err.println("Error: "+e.getMessage());
        } 

        // Collect group IDs from cluster_accountant.project table
        Set<String> mysqlSet = keySet(); 
        
        Set<String> defunctProjects = new HashSet<String>();
        Set<String> missingProjects = new HashSet<String>();
        defunctProjects.addAll(mysqlSet);
        missingProjects.addAll(projectAccessSet);
        // Print projects in /etc/security/access.conf, but not in the MySQL Project table
        if (!projectAccessSet.containsAll(mysqlSet)) {
            defunctProjects.removeAll(projectAccessSet);
            Iterator<String> dpi = defunctProjects.iterator();
            while(dpi.hasNext())
                System.err.println("Error: Group with GID: \"" + dpi.next()
                        + "\" is no longer part of SCLUSTER" );
        }
        // Print projects in the MySQL project table, but not in /etc/security/access.conf
        if (!mysqlSet.containsAll(projectAccessSet)) {
            missingProjects.removeAll(mysqlSet);
            Iterator<String> mpi = missingProjects.iterator();
            while(mpi.hasNext())
                System.err.println("Error: Group with GID: \"" + mpi.next() 
                        + "\" is missing from the database" );
        }
    }

    protected void printRows(){
        Iterator<Entry<String, Project>> iterator = entrySet().iterator();
        while(iterator.hasNext()){
           iterator.next().getValue().print();
        }
    } 
    
    protected void zeroSUSums(){
        Iterator<Entry<String, Project>> iterator = entrySet().iterator();
        while(iterator.hasNext()){
           iterator.next().getValue().suSum = 0.0F;
        }
    } 
	
    protected void printReport() {
        Iterator<Entry<String, Project>> iterator = entrySet().iterator();
        Project p;
		double suSumHr;
        System.out.printf("Interval Service Units by Project\n");
        System.out.printf("%14s  %8s\n", "Project", "Units");
        while(iterator.hasNext()){
           p = iterator.next().getValue();
		   suSumHr = p.suSum / 3600.0F;
           System.out.printf("%14s  %8.2f\n", p.groupid, suSumHr);
        }
        System.out.printf("Service Unit Total: %.2f",totalServiceUnits / 3600.0F);
    } 

}
