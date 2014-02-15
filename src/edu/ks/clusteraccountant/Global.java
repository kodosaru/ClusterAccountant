/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.regex.*;
/**
 *
 * @author donj */
public class Global {
    protected static Integer debug = 0;
    protected static Boolean earthSystemsOnly = false;
    protected static String strStartDate = null;
    protected static Long epochStartDate = 0L;
    protected static String strEndDate = null;
    protected static Long epochEndDate = 0L;
    protected static String strStartDatePP = null;
    protected static Long epochStartDatePP = 0L;
    protected static String strEndDatePP = null;
    protected static Long epochEndDatePP = 0L;
    protected static String group = null;
    protected static String owner = null;
    protected static String reportType = null;
    protected static String reportName = null;
	protected static String accessfile = "/etc/security/access.conf";
	protected static String extractfile = null;
	protected static String acctfile = "/usr/local/sge/sge_root/default/common/accounting";
   	protected static String templateDir = "/net/usr/local/admin/development/Cluster Accountant/templates/";
   	protected static String reportDir = "/net/usr/local/admin/development/Cluster Accountant/reports/";
    protected static Integer maxGraphIntervals = 50;
    protected static Boolean accountingLog = false;

    // Database related attributes
	protected static NetworkResource mySQLServer = new NetworkResource("mysql",
			"aether.ks.edu", 3306, "/cluster_accountant", "cadba", "999999", null); 
	protected static NetworkResource webServer = new NetworkResource("rsync",
			"electron.ks.edu", 0, "/var/www/html/cluster_accountant/", "cadba", null,
			"/net/casfsb/vol/ssrchome/active_users/donj/.ssh/cadba_id_rsa"); 
    protected static Connection con = null;
  
	protected static void initFilePaths() {
     
		try {
    		InetAddress addr = InetAddress.getLocalHost();

			// Get IP Address
			byte[] ipAddr = addr.getAddress();

			// Get hostname
			String hostname = addr.getHostName();
			if(debug > 2)
				System.out.println(hostname);
			if(hostname.equals("monkey.ks.edu")){
    			templateDir = "/mnt/my_documents/workspace/Cluster Accountant/templates";
    			reportDir = "/Users/donj/reports/";
			} else if (hostname.equals("electron")) {
    			accessfile = "C:\\cygwin\\home\\donj\\data\\access.conf";
    			acctfile = "C:\\cygwin\\home\\donj\\data\\accounting";
    			templateDir = "Q:\\my_documents\\NetBeansProjects\\Cluster Accountant\\templates\\";
    			reportDir = "C:\\cygwin\\home\\donj\\reports\\";
				webServer.setHostName("lego.kodosaru.net");
                webServer.setUserID("cadba");
				webServer.setPassword("999999,");
				mySQLServer.setHostName("lego.kodosaru.net");
                mySQLServer.setUserID("cadba");
				mySQLServer.setPassword("999999,");
			} else if (hostname.equals("proton.ks.edu") ||
                                hostname.equals("proton")) {
			} else {
				System.err.println("Error: Unknown host name used to set file paths.");
				System.exit(1);
			}

		} catch (UnknownHostException e) {
			System.err.println("Error: "+e.getMessage());
		}
	}
	// File with SGC job accounting data

	// File with list of groups/projects with SCLUSTER computer cluster access

    protected static void printAttributes(){
        System.out.printf("debug: %d\n",debug);
		String date = "";
        try {
			System.out.printf("strStartDate: %s\n", strStartDate);
			date = DateConvert.epochToStrLong(epochStartDate);
			System.out.printf("epochStartDate: %s\n", date);
			System.out.printf("strEndDate: %s\n", strEndDate);
			date = DateConvert.epochToStrLong(epochEndDate);
			System.out.printf("epochEndDate: %s\n", date);
			System.out.printf("strStartDatePP: %s\n", strStartDatePP);
			date = DateConvert.epochToStrLong(epochStartDatePP);
			System.out.printf("epochStartDatePP: %s\n", date);
			System.out.printf("strEndDatePP: %s\n", strEndDatePP);
			date = DateConvert.epochToStrLong(epochEndDatePP);
			System.out.printf("epochEndDatePP: %s\n", date);
     } catch (Exception e) {
            System.err.println("Error: "+e.getMessage());
        }
		System.out.printf("group: %s\n", group);
		System.out.printf("user: %s\n", owner);
		System.out.println("");
    }

    public static boolean serverAlive(String url) {
        String[] args = new String[6];
        String output;
        Pattern failed = null;
        Pattern unknownHost = null;
        Pattern windows = Pattern.compile("Windows");
        Pattern linux = Pattern.compile("Linux");
        Pattern macos = Pattern.compile("Mac OS");
        String os = System.getProperty("os.name");
        if(windows.matcher(os).lookingAt()){
            args[0] = "ping";
            args[1] = "-n";
            args[2] = "1";
            args[3] = "-w";
            args[4] = "1000";
            args[5] = url;
            unknownHost = Pattern.compile("Ping request could not find host");
            failed = Pattern.compile("Ping request could not find");
        }
        else if(linux.matcher(os).lookingAt()) {
            args[0] = "ping";
            args[1] = "-c";
            args[2] = "1";
            args[3] = "-w";
            args[4] = "1";
            args[5] = url;
            unknownHost = Pattern.compile("ping: unknown host");
            failed = Pattern.compile("Ping request could not find");
        }
        else if(macos.matcher(os).lookingAt()) {
            args[0] = "ping";
            args[1] = "-c";
            args[2] = "1";
            args[3] = "-t";
            args[4] = "1";
            args[5] = url;
            unknownHost = Pattern.compile("ping: cannot resolve");
            failed = Pattern.compile("100.0% packet loss");
        }
		else {
            System.err.printf("Error : Unknown operating systems %s",os);
		}
		output = SystemCaller.runCmd(args,false);
		if(unknownHost.matcher(output).lookingAt()){
				System.err.println("Error: Unknown host "+args[5]);
            return(false);
        }
        if(failed.matcher(output).lookingAt()){
            System.err.println("Error: Unable to connect to host "+args[5]);
            return(false);
        }
        return(true);
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

	protected static void printHeading() {
		System.out.println("SCLUSTER Cluster Contribution And Usage Report\n");
		System.out.println(printSubHeading());
	}

	protected static String printSubHeading() {
		try {
			// Print interval and previous months date range
			String s="", e="", spm="";
			s = DateConvert.epochToStr(epochStartDate);
			e = DateConvert.epochToStr(epochEndDate);
			spm = DateConvert.epochToStr(epochStartDatePP);
            if(Global.reportType.startsWith("text")) {
			    System.out.printf("Interval report dates from %s to %s\n",s,e);
			    System.out.printf("Previous month's report dates from %s to %s\n",spm,e);
            }
		} catch (DateException e) {
			System.err.println("Error: "+e.getMessage());
		}
		String project = (group == null) ? "\"any\"" : "\""
				+group+"\"";
		String onr = (owner == null) ? "\"any\"" : "\""
				+owner+"\"";
		String tempstr = String.format("Project equals %s and Client equals %s",
				project, onr);
		return(tempstr);
	}


}
