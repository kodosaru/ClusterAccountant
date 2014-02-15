/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;

import java.sql.*;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.logging.*;
import java.util.Map;
import java.util.TreeMap;
/**
 *
 * @author donj
 */
public class Database {
	protected ContributionArrayList contributionArrayList;
	protected PrincipalInvestigatorHashMap piHashMap;
	protected ProcessorTypeArrayList processorTypeArrayList;
    protected UtilizationArray utilizationArray;
	protected ProjectHashMap projectHashMap;
	protected Map<String,Float> prjctHashMap;
	protected Map<String,Float> ownerHashMap;
	
    public Database(){
        try {
            //Register the JDBC driver for MySQL.
            Class.forName("com.mysql.jdbc.Driver");

            //Get a connection to the database
            Global.con = DriverManager.getConnection( Global.mySQLServer.getURL(),
					Global.mySQLServer.getUserID(), Global.mySQLServer.getPassword());

            //Display URL and connection information
            if(Global.debug > 2){
                System.out.println("URL: " + Global.mySQLServer.getURL());
                System.out.println("Connection: " + Global.con);
            }
       
        } catch (Exception e) {
            System.err.println("Error: "+e.getMessage());
            System.exit(1);
        }
        contributionArrayList=new ContributionArrayList();
		processorTypeArrayList = new ProcessorTypeArrayList();
		projectHashMap = new ProjectHashMap();
        piHashMap=new PrincipalInvestigatorHashMap();
        contributionArrayList.sumAllContributions(piHashMap);
		ownerHashMap = new HashMap<String,Float>();
		prjctHashMap = new HashMap<String,Float>();
        if (Global.debug > 2) {
           System.out.println();
           contributionArrayList.printRows();
           System.out.println();
           piHashMap.printRows();
           System.out.println();
           processorTypeArrayList.printRows();
           System.out.println();
           projectHashMap.printRows();
           System.out.println();
        }
    }

    protected void close(){
        try {
            Global.con.close();
        } catch (Exception e) {
            System.err.println("Error: "+e.getMessage());
        }
    }

    protected void sumSUChargeAccountingLog(Boolean pp) {
		HashMap<String,Float> tempPrjctHashMap = new HashMap<String,Float>();
		HashMap<String,Float> tempOwnerHashMap = new HashMap<String,Float>();
		projectHashMap.zeroSUSums();
        projectHashMap.totalServiceUnits = 0.0F;
		piHashMap.zeroSUSums();
        piHashMap.totalServiceUnits = 0.0F;


        String ssd = null;
        String sed = null;
        Long esd = 0L;
        Long eed = 0L; 

        if(pp) {
            ssd = Global.strStartDatePP;
            sed = Global.strEndDatePP;
            esd = Global.epochStartDatePP;
            eed = Global.epochEndDatePP; 
        }
        else {
            ssd = Global.strStartDate;
            sed = Global.strEndDate;
            esd = Global.epochStartDate;
            eed = Global.epochEndDate; 
        }
        utilizationArray = new UtilizationArray(esd, eed);
        utilizationArray.calcMaxComputationUnitsPerInterval(processorTypeArrayList);

        try {
            BufferedReader readbuffer;
            String strRead;
            Accounting acct;
            Pattern p = Pattern.compile(":");

			readbuffer = new BufferedReader(new FileReader(Global.acctfile));
            acct = new Accounting();
            if(Global.reportType.equalsIgnoreCase("export"))
                System.out.printf("%22s\t%8s\t%8s\t%10s\t%10s\t%14s\t%8s\n",
                        "hostname", "group", "owner", "start_time",
                        "end_time", "cpu", "sucharge");
            while ((strRead = readbuffer.readLine()) != null) {
                String splitarray[] = p.split(strRead);
                
                if (strRead.charAt(0) != '#') {
                    if (splitarray.length == 43) {
                        acct.setAttributes(splitarray);

						// Print record
						if (Global.debug > 0) {
							acct.printAttributes();
						}

						// Attribute report for spreadsheet import
						if(Global.reportType.equalsIgnoreCase("export")) {
							acct.printShortAttributes(processorTypeArrayList);
						}
						
						// Reject bad record
						if(acct.getEndTime() != 0L || acct.getCpu() != 0.0F) {

							// Interval reduction
							if(acct.testAttributes(ssd, sed, esd, eed)){
								acct.sumSUCharge(processorTypeArrayList, projectHashMap,
										piHashMap, utilizationArray,
										tempPrjctHashMap, tempOwnerHashMap);
							}

						} // end reject bad record 
                    } else if (Global.debug > 0) {
                        System.err.printf(
                                "Error: SGE accounting record has"
                                        + splitarray.length
                                        + " fields instead of 43");
                    }
                }
            } // end while
            readbuffer.close();
 
			// Sort hash maps
    		prjctHashMap = new TreeMap<String, Float>(tempPrjctHashMap);
    		ownerHashMap = new TreeMap<String, Float>(tempOwnerHashMap);
			
       } catch (FileNotFoundException e) { 
            System.err.println(
                    "Error: Unable to find file SGE account file: " + Global.acctfile);
        } catch (IOException e) { 
            System.err.println("Error: "+e.getMessage());
        }  // end try-catch
		utilizationArray.divideByMaxComputationUnitsPerInterval();

    } // end method

    protected void sumSUChargeDatabase(Boolean pp) {
		HashMap<String,Float> tempOwnerHashMap = new HashMap<String,Float>();
		HashMap<String,Float> tempPrjctHashMap = new HashMap<String,Float>();
		projectHashMap.zeroSUSums();
        projectHashMap.totalServiceUnits = 0.0F;
		piHashMap.zeroSUSums();
        piHashMap.totalServiceUnits = 0.0F;

        String ssd = null;
        String sed = null;
        Long esd = 0L;
        Long eed = 0L; 

        if(pp) {
            ssd = Global.strStartDatePP;
            sed = Global.strEndDatePP;
            esd = Global.epochStartDatePP;
            eed = Global.epochEndDatePP; 
        }
        else {
            ssd = Global.strStartDate;
            sed = Global.strEndDate;
            esd = Global.epochStartDate;
            eed = Global.epochEndDate; 
        }
        utilizationArray = new UtilizationArray(esd, eed);
        utilizationArray.calcMaxComputationUnitsPerInterval(processorTypeArrayList);

        PreparedStatement pst = null;
        ResultSet rs = null;
        AccountingExtract acctEx;
        acctEx = new AccountingExtract();

        // Lookup accounting records
        try {
            pst = acctEx.testAttributes(pst, ssd, sed, esd, eed);
            rs = pst.executeQuery();

            // Print export version report heading
            if(Global.reportType.equalsIgnoreCase("export"))
                System.out.printf("%22s\t%8s\t%8s\t%10s\t%10s\t%14s\t%8s\n",
                        "hostname", "group", "owner", "start_time",
                        "end_time", "cpu", "sucharge");

            // Loop through previous period accounting records 
            while(rs.next()){
                acctEx.setAttributes(rs.getString("hostname"), rs.getString("_group"),
                        rs.getString("owner"), rs.getString("job_number"),
                        rs.getLong("start_time"), rs.getLong("end_time"),
                        rs.getString("task_number"), rs.getFloat("cpu"),
                        rs.getString("pe_taskid"));
             
						// Print record
						if (Global.debug > 0) {
							acctEx.printAttributes();
						}

						// Attribute report for spreadsheet import
						if(Global.reportType.equalsIgnoreCase("export")) {
							acctEx.printShortAttributes(processorTypeArrayList);
						}
						
						// Reject bad record
						if(acctEx.getEndTime() != 0L || acctEx.getCpu() != 0.0F) {

							// Interval reduction
							acctEx.sumSUCharge(processorTypeArrayList, projectHashMap,
									piHashMap, utilizationArray,
									tempPrjctHashMap, tempOwnerHashMap);

						} // end reject bad record 
            } // end while

			// Sort hash maps
    		prjctHashMap = new TreeMap<String, Float>(tempPrjctHashMap);
    		ownerHashMap = new TreeMap<String, Float>(tempOwnerHashMap);

        } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (pst != null)
                    pst.close();
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        } 
		utilizationArray.divideByMaxComputationUnitsPerInterval();

    } // end method

    protected void printReport(Boolean pp) {
		if(Global.accountingLog)
			sumSUChargeAccountingLog(pp);
		else
			sumSUChargeDatabase(pp);
        System.out.println("");
        piHashMap.printContributionReport();
        System.out.println("\n");
        projectHashMap.printReport();
        System.out.println("\n");
        piHashMap.printReport();
        System.out.println("\n");
		if(Global.debug > 2) {
          utilizationArray.printDebug(ProcessorTypeArrayList.Cluster.SCLUSTER);
          utilizationArray.printDebug(ProcessorTypeArrayList.Cluster.PCLUSTER);
		}
        utilizationArray.printReport();
    } // end method

    protected void printReportByGroup() {
		if(Global.accountingLog)
			sumSUChargeAccountingLog(false);
		else
			sumSUChargeDatabase(false);
		Iterator iterator = prjctHashMap.entrySet().iterator();
        System.out.printf("Contributions Units by Group\n");
        System.out.printf("%18s  %8s\n\n", "PI", "Units");
		Float suSum = 0.0F;
        while(iterator.hasNext()){
			Map.Entry<String,Float> me = (Map.Entry<String,Float>) iterator.next();
			suSum += me.getValue();
            System.out.printf("%18s  %8.2f\n", me.getKey(), me.getValue()/3600.00);
        }

        System.out.printf("\nContribution Unit Total: %.2f", suSum);
    } // end method

    protected void extractRecords() {
        String strRead;
        Pattern p = Pattern.compile(":");
        ResultSet rst = null;
        Long offset = 0L;
        AccountingExtract acctEx = new AccountingExtract();
        PreparedStatement pst = null;
        Statement st = null; 
		Integer reccnt = 0;
        try {
            File acctfile = new File(Global.extractfile); 
            String filename =  acctfile.getName();
			RandomAccessFile raf = new RandomAccessFile(acctfile,"r");
			Long len = raf.length();
            try { 
                pst = Global.con.prepareStatement( "insert into accounting("
                        + "job_number,hostname,_group,owner,start_time,end_time,"
                        + "cpu,task_number,pe_taskid)"
                        + "values(?,?,?,?,?,?,?,?,?)");
                st = Global.con.createStatement(); 
                rst = st.executeQuery("select offset from fileposition where filename = \""+
                        filename+"\"");
                if(rst.next())
                    offset = rst.getLong("offset");
				else
					offset = 0L;
            } catch (SQLException ex) {
                    Logger lgr = Logger.getLogger(Database.class.getName());
                    lgr.log(Level.SEVERE, ex.getMessage(), ex);
            } 

			if(offset.equals(len)) {
				// There are no new records; exit method
                raf.close();
				if(Global.debug > 1)
					System.out.printf("%d records added\n",reccnt);
				return;
			}
			else {
				// Jump past old records
            	raf.seek(offset);
			}
            while ((strRead = raf.readLine()) != null) {
                String sa[] = p.split(strRead);
                
                if (strRead.charAt(0) != '#') {
                    if (sa.length == 43) {
                        acctEx.setAttributes (sa[1], sa[2], sa[3], sa[5],
                        Long.parseLong(sa[9]), Long.parseLong(sa[10]), sa[35],
                        Float.parseFloat(sa[36]), sa[41]);

						// Reject bad record
						if(acctEx.getEndTime() != 0L || acctEx.getCpu() != 0.0F) {
                            try {
                                pst.setString(1, acctEx.getJobNumber());
                                pst.setString(2, acctEx.getHostname());
                                pst.setString(3, acctEx.getGroup());
                                pst.setString(4, acctEx.getOwner());
                                pst.setLong(5, acctEx.getStartTime());
                                pst.setLong(6, acctEx.getEndTime());
                                pst.setFloat(7, acctEx.getCpu());
                                pst.setString(8, acctEx.getTaskNumber());
                                pst.setString(9, acctEx.getPeTaskid());
								try {
									if(Global.debug > 1) {
										System.out.printf("start: %s key: %s %s %s\n",
												DateConvert.epochToStrLong(acctEx.getStartTime()*1000L),
												acctEx.getJobNumber(),acctEx.getTaskNumber(),
                                                acctEx.getPeTaskid());
										System.out.printf("end: %s hostname: %s group: "+
                                                "%s owner: %s cpu: %4.2f\n\n",
												DateConvert.epochToStrLong(acctEx.getEndTime()*1000L),
												acctEx.getHostname(),acctEx.getGroup(),
                                                acctEx.getOwner(),acctEx.getCpu());
									}
                                	pst.executeUpdate();
								} catch (Exception e) {
									System.out.println("Error: " + e.getMessage());
								}
								reccnt++;
								if (Global.debug > 0 && reccnt == 1)
									System.out.println("Record count:        ");
								if(Global.debug > 0) 
									System.out.printf("%6d\n", reccnt);
                            } catch (SQLException ex) {
                                    Logger lgr = Logger.getLogger(Database.class.getName());
                                    lgr.log(Level.SEVERE, ex.getMessage(), ex);
                            } 
						} // end reject bad record 
                    } else {
                        System.err.printf(
                                "Error: SGE accounting record has"
                                        + sa.length
                                        + " fields instead of 43");
                    }
                }
            } // end while
            try {
          		pst = Global.con.prepareStatement( "replace into fileposition("
                    + "filename,offset) values(?,?)");
                pst.setString(1, acctfile.getName());
                pst.setLong(2, raf.getFilePointer());
                pst.executeUpdate(); 
                raf.close();
				if(reccnt > 1 && Global.debug > 1)
					System.out.printf("%d records added\n",reccnt);
				return;
            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(Database.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        } catch (FileNotFoundException e) { 
            System.err.println(
                    "Error: Unable to find file SGE account file: " + Global.acctfile);
        } catch (IOException e) { 
            System.err.println("Error: "+e.getMessage());
        }  // end try-catch
    } // end method

} // end class
