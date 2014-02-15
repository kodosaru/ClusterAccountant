/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;
import jargs.gnu.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author donj
 */
public class Main {
    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) {

        // Initialize local file path variables
		Global.initFilePaths();

        // Check server status
        if(!Global.serverAlive(Global.mySQLServer.getHostName()))
                System.exit(1);
        if(!Global.mySQLServer.getHostName().equalsIgnoreCase(
				Global.webServer.getHostName())){
            if(!Global.serverAlive(Global.webServer.getHostName()))
                System.exit(1);
        }
        
        // Read in command line parameters
        if(parseCommandLine(args) != 0)
			System.exit(1);
        
        // Open database MySQL cluster_accounting on bucwp.bu.edu and run reports
        Database db = new Database();
        if(Global.extractfile == null) {
            // Run current period text report
            if(Global.reportType.equalsIgnoreCase("text")){
				Global.printHeading();
                db.printReport(false);
            }
			else if(Global.reportType.equalsIgnoreCase("text_previous_period")){
				Global.printHeading();
                db.printReport(true);
            }
            else if(Global.reportType.equalsIgnoreCase("text_by_group")){
				Global.printHeading();
                db.printReportByGroup();
            }
            else if(Global.reportType.equalsIgnoreCase("html")){
                HTMLReport htmlrpt = new HTMLReport();
                htmlrpt.printReport(db);
				String filename = Global.reportDir+Global.reportName+".html";
				htmlrpt.writeReport(filename);
            }
            else if(Global.reportType.equalsIgnoreCase("html_by_project")){
                HTMLReport htmlrpt = new HTMLReport();
                htmlrpt.printReportByProject(db);
				String filename = Global.reportDir+Global.reportName+".html";
				htmlrpt.writeReport(filename);
            }
            else if(Global.reportType.equalsIgnoreCase("html_by_client")){
                HTMLReport htmlrpt = new HTMLReport();
                htmlrpt.printReportByOwner(db);
				String filename = Global.reportDir+Global.reportName+".html";
				htmlrpt.writeReport(filename);
            }
            else {
                System.err.printf("Error: \"%s\" is not a valid report type\n",Global.reportType);
            }
        }
        else {
            db.extractRecords();
        }

        db.close();
    }

    private static void printUsage() {
        System.out.printf("Usage: OptionTest [{-d,--debug <0,1,2 or 3}]\n" +
                "\t[{-s,--start-date <99/99/9999>}]\n" +
                "\t[{-e,--end-date <99/99/9999>}]\n" +
                "\t[{-S,--start-date-previous-period <99/99/9999>}]\n" +
                "\t[{-E,--end-date-previous-period <99/99/9999>}]\n" +
                "\t[{-p,--project <xxxxxx>}] [{-u,--user <xxxxxx>}]\n" +
                "\t[{-o, --earth-systems-only}]\n" + 
                "\t[{-t, --report-type <export,text or html>}]\n" +
                "\t[{-n, --report-name <xxxxxx>}]\n" +
                "\t[{-x, --extract <path to accounting file}]\n" + 
                "\t[{-a, --accounting-log}]\n"); 
    }

    private static Integer parseCommandLine(String[] args){
        CmdLineParser parser = new CmdLineParser(); 
        
        // Process command line parameters 
        CmdLineParser.Option debugOption = parser.addIntegerOption('d', "debug");
        CmdLineParser.Option startDateOption = parser.addStringOption('s',
                "start-date");
        CmdLineParser.Option endDateOption = parser.addStringOption('e',
                "end-date");
        CmdLineParser.Option startDatePPOption = parser.addStringOption('S',
                "start-date-previous-period");
        CmdLineParser.Option endDatePPOption = parser.addStringOption('E',
                "end-date-previous-period");
        CmdLineParser.Option projectOption = parser.addStringOption('p',
                "project");
        CmdLineParser.Option userOption = parser.addStringOption('u',
                "user");
        CmdLineParser.Option earthSystemsOnlyOption = parser.addBooleanOption('o',
                "earth-systems-only");
        CmdLineParser.Option reportTypeOption = parser.addStringOption('t',
                "report-type");
        CmdLineParser.Option reportNameOption = parser.addStringOption('n',
                "report-name");
        CmdLineParser.Option extractOption = parser.addStringOption('x',
                "extract");
        CmdLineParser.Option accountingLogOption = parser.addBooleanOption('a',
                "accounting-log");

        try {
            parser.parse(args);
        } catch (CmdLineParser.OptionException e) {
            System.err.println("Error: "+e.getMessage());
            printUsage();
            return(1);
        }
    
        // Retrieve command parameters
        Global.debug = (Integer) parser.getOptionValue(debugOption, 0);
        Global.strStartDate = (String) parser.getOptionValue(startDateOption, null);
        Global.strEndDate = (String) parser.getOptionValue(endDateOption, null);
        Global.strStartDatePP = (String) parser.getOptionValue(startDatePPOption, null);
        Global.strEndDatePP = (String) parser.getOptionValue(endDatePPOption, null);
        Global.group = (String) parser.getOptionValue(projectOption, null);
        Global.owner = (String) parser.getOptionValue(userOption, null);
        Global.earthSystemsOnly = (Boolean) parser.getOptionValue(earthSystemsOnlyOption,
                Boolean.FALSE);
        Global.reportType = (String) parser.getOptionValue(reportTypeOption, "text");
        Global.reportName = (String) parser.getOptionValue(reportNameOption, "report");
        Global.extractfile = (String) parser.getOptionValue(extractOption, null);
        Global.accountingLog = (Boolean) parser.getOptionValue(accountingLogOption,
                Boolean.FALSE);

        // Process command date paramenters
        DateConvert dc = new DateConvert();	

        try {
			Calendar epoch = Calendar.getInstance();
			// Start date
            if (Global.strStartDate == null) {
				// If date is empty, set to epoch begin date to 1/1/1970 GMT
                Global.epochStartDate = 0L;
            }
			else {
				// Convert parameter string date to epoch form
				Global.epochStartDate = DateConvert.strToEpoch(Global.strStartDate,
                        DateConvert.TimeOfDay.BEGINNING);
            }

			// End date 
            if (Global.strEndDate == null) {
				// If date is empty, set to now
				epoch = Calendar.getInstance();
				epoch = new GregorianCalendar(epoch.get(Calendar.YEAR),
                        epoch.get(Calendar.MONTH),
						epoch.get(Calendar.DAY_OF_MONTH),23,59,59);
                Global.epochEndDate = DateConvert.calToEpoch(epoch);
            }
			else {
				// Convert parameter string date to epoch form
				Global.epochEndDate = DateConvert.strToEpoch(Global.strEndDate,
                        DateConvert.TimeOfDay.ENDING);
            }
			if(Global.epochEndDate < Global.epochStartDate) {
				System.err.println("Error: End date cannot be before start date "+
                        "for current period dates");
				return(1);
			}

			// Start date previous period 
            if (Global.strStartDatePP == null) {
				// If date is empty, set to epoch begin date to 1/1/1970 GMT
                Global.epochStartDatePP = 0L;
            }
			else {
				// Convert parameter string date to epoch form
				Global.epochStartDatePP = DateConvert.strToEpoch(Global.strStartDatePP,
                        DateConvert.TimeOfDay.BEGINNING);
            }

   			// End date previous period 
            if (Global.strEndDatePP == null) {
				// If date is empty, set to now
				epoch = Calendar.getInstance();
				epoch = new GregorianCalendar(epoch.get(Calendar.YEAR),
                        epoch.get(Calendar.MONTH),
						epoch.get(Calendar.DAY_OF_MONTH),23,59,59);
                Global.epochEndDatePP = DateConvert.calToEpoch(epoch);
            }
			else {
				// Convert parameter string date to epoch form
				Global.epochEndDatePP = DateConvert.strToEpoch(Global.strEndDatePP,
                        DateConvert.TimeOfDay.ENDING);
            }
			if(Global.epochEndDatePP < Global.epochStartDatePP) {
				System.err.println("Error: End date cannot be before start date "+
                        "for previous period dates");
				return(1);
			}

        }
        catch (DateException e) {
            System.err.println("Error: "+e.getMessage());
            return(1);
        }

        if(Global.debug > 2)
            Global.printAttributes();

		return(0);
 
    }

} // end of class
