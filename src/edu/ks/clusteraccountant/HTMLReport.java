/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;
import java.util.Map.Entry;
import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
/**
 *
 * @author donj
 */
public class HTMLReport {
    protected ArrayList<String> report = new ArrayList<String>();

    public HTMLReport(){
        report = new ArrayList<String>();
    }

    protected void readTemplate(String filename) {
        try {
            filename = Global.templateDir+filename;
            FileReader readfile;
            BufferedReader readbuffer;
            String strRead;
            Accounting acct;

            readfile = new FileReader(filename);
            readbuffer = new BufferedReader(readfile);
            while ((strRead = readbuffer.readLine()) != null) {
                report.add(strRead);
            }
            readbuffer.close();
        } catch (IOException e) { 
            System.err.println("Error: "+e.getMessage());
        } 
    }

	private void removeLastRowComma(ArrayList report){
		Integer rptsize = report.size();
		StringBuilder lastcell = new StringBuilder(); 
		lastcell.append(report.get(rptsize-1));	
		Integer strlen = lastcell.length();
		if(lastcell.charAt(strlen-1) == ',')
			lastcell.deleteCharAt(strlen-1);	
		report.set(rptsize-1, lastcell.toString());
	}

    protected void createPieChartDataTable(ProjectHashMap phm, Boolean pp) {
        Integer size = phm.size();
        Iterator<Entry<String, Project>> iterator = phm.entrySet().iterator();
        Project p;
		double suSumHr;
        String suSumHrStr = "";
        String spc4 = "    ";
        String spc6 = "      ";
        String spc8 = "        ";
        String spc10 = "          ";
        String spc14 = "              ";
        String spc16 = "                ";

        // Begin report section
        if(pp){
            report.add(spc6+"// Create the data table for previous period, service units pie chart.");
            report.add(spc6+"var data2 = new google.visualization.DataTable(");
        }
        else{
            report.add(spc6+"// Create the data table for interval service units pie chart.");
            report.add(spc6+"var data1 = new google.visualization.DataTable(");
        }
        report.add(spc6+"{");
        report.add(spc8+"cols: [");
        report.add(spc16+"{id: 'groupid', label: 'Project', type: 'string'},");
        report.add(spc16+"{id: 'susum', label: 'Service Units', type: 'number'}");
        report.add(spc14+"],");
        report.add(spc8+"rows: ["); 
        while(iterator.hasNext()){
            p = iterator.next().getValue();
		    suSumHr = p.suSum / 3600.0D;
            suSumHrStr=String.format("%.2f", suSumHr);
			report.add(spc16+"{c:["+"{'v': '"+p.groupid+"'}, {'v': "+suSumHrStr+"}]},"); 
        }
		removeLastRowComma(report);
        report.add(spc14+"]"); 
        report.add(spc8+"}"); 
        report.add(spc6+")"); 
        report.add("");
    } 

    protected void createPieChartDataTable(PrincipalInvestigatorHashMap pihm, Boolean pp) {
        Integer size = pihm.size();
        Iterator<Entry<String, PrincipalInvestigator>> iterator = pihm.entrySet().iterator();
        PrincipalInvestigator pi;
		double suSumHr;
        String suSumHrStr = "";
        String spc4 = "    ";
        String spc6 = "      ";
        String spc8 = "        ";
        String spc10 = "          ";
        String spc14 = "              ";
        String spc16 = "                ";

        // Begin report section
        if(pp){
            report.add(spc6+"// Create the data table for previous period, service units pie chart.");
            report.add(spc6+"var data2 = new google.visualization.DataTable(");
        }
        else{
            report.add(spc6+"// Create the data table for interval service units pie chart.");
            report.add(spc6+"var data1 = new google.visualization.DataTable(");
        }
        report.add(spc8+"{");
        report.add(spc8+"cols: [");
        report.add(spc16+"{id: 'groupid', label: 'Group ID', type: 'string'},");
        report.add(spc16+"{id: 'susum', label: 'Service Units', type: 'number'}");
        report.add(spc14+"],");
        report.add(spc8+"rows: ["); 

        while(iterator.hasNext()){
            pi = iterator.next().getValue();
		    suSumHr = pi.suSum / 3600.0D;
            suSumHrStr=String.format("%.2f", suSumHr);
			report.add(spc16+"{c:["+"{'v': '"+pi.alias+"'}, {'v': "+suSumHrStr+"}]},"); 
        }
		removeLastRowComma(report);
        report.add(spc14+"]"); 
        report.add(spc8+"}"); 
        report.add(spc6+")"); 
        report.add("");
    } 

    protected void createPieChartDataTable(Map<String,Float> ghm, Boolean pp) {
        Integer size = ghm.size();
		Iterator iterator = ghm.entrySet().iterator();
		double suSumHr;
        String suSumHrStr = "";
        String spc4 = "    ";
        String spc6 = "      ";
        String spc8 = "        ";
        String spc10 = "          ";
        String spc14 = "              ";
        String spc16 = "                ";

        // Begin report section
        if(pp){
            report.add(spc6+"// Create the data table for previous period, service"
					+ "units by group pie chart.");
            report.add(spc6+"var data2 = new google.visualization.DataTable(");
        }
        else{
            report.add(spc6+"// Create the data table for interval service units"
					+ "by group pie chart.");
            report.add(spc6+"var data1 = new google.visualization.DataTable(");
        }
        report.add(spc6+"{");
        report.add(spc8+"cols: [");
        report.add(spc16+"{id: 'groupid', label: 'Project', type: 'string'},");
        report.add(spc16+"{id: 'susum', label: 'Service Units', type: 'number'}");
        report.add(spc14+"],");
        report.add(spc8+"rows: ["); 
        while(iterator.hasNext()){
			Map.Entry<String,Float> me = (Map.Entry<String,Float>) iterator.next();
		    suSumHr = me.getValue() / 3600.0D;
            suSumHrStr=String.format("%.2f", suSumHr);
			report.add(spc16+"{c:["+"{'v': '"+me.getKey()+"'}, {'v': "+suSumHrStr+"}]},"); 
        }
		removeLastRowComma(report);
        report.add(spc14+"]"); 
        report.add(spc8+"}"); 
        report.add(spc6+")"); 
        report.add("");
    }
	
	protected void createTableChartDataTable(PrincipalInvestigatorHashMap pihm, Boolean pp) {
        Integer size = pihm.size();
        PrincipalInvestigator pi;
        Iterator<Entry<String, PrincipalInvestigator>> pihmIterator;
        String spc6 = "      ";
        String spc8 = "        ";
        String spc14 = "              ";
        String spc16 = "                ";

        // Create a sorted list of PIs' aliases
        ArrayList<String> aliasList = new ArrayList<String>();
        pihmIterator = pihm.entrySet().iterator();
        while(pihmIterator.hasNext()){
           aliasList.add(pihmIterator.next().getValue().alias);
        }
        Collections.sort(aliasList);

        // Begin report section
        if(pp){
            report.add(spc6+"// Create the data table for previous period, subscription table.");
            report.add(spc6+"var data4 = new google.visualization.DataTable(");
        }
        else{
            report.add(spc6+"// Create the data table for interval" +
                    " subscription table.");
            report.add(spc6+"var data3 = new google.visualization.DataTable(");
        }
        report.add(spc8+"{");
        report.add(spc8+"cols: [");
        report.add(spc16+"{id: 'groupid', label: 'PI', type: 'string'},");
        report.add(spc16+"{id: 'subscription', label: 'Subscription', type: 'string'},");
        report.add(spc16+"{id: 'exempt', label: 'Exempt', type: 'boolean'}");
        report.add(spc14+"],");
        report.add(spc8+"rows: ["); 

        // Iterate across sorted alias list and create new sorted PI hashmap
        Integer alsize = aliasList.size();
        Boolean found = false;
        for(int i=0;i<alsize;i++) {
            found = false;
            pihmIterator = pihm.entrySet().iterator();
            while(!found && pihmIterator.hasNext()){
                pi = pihmIterator.next().getValue();
                if(aliasList.get(i).equals(pi.alias)){
                    found = true;
					if (pi.suSum > 0.0F) {
						createTableChartRow(pihm, pi);
					}
				}
			}
		}
		removeLastRowComma(report);
        report.add(spc14+"]"); 
        report.add(spc8+"}"); 
        report.add(spc6+")"); 
        report.add("");

    } // end method

    protected void createTableChartRow(PrincipalInvestigatorHashMap pihm,
            PrincipalInvestigator pi) {
        Float contributionPercent,suPercent,subscriptionPercent;
		double suSumHr;
        String suSumHrStr = "";
        String spc16 = "                ";

        String tempstr = "";
        contributionPercent=pi.contributionSum/pihm.totalContributionUnits;
        if(pihm.totalServiceUnits > 0.0F){
            String substr;
            suPercent=pi.suSum/pihm.totalServiceUnits;
            subscriptionPercent=suPercent/contributionPercent;
            if(pi.exempt) {
                tempstr = spc16 + "{c:[" + "{'v': '" + pi.alias + "'}," +
                        " {'v': '" + "N/A" + "'}, {'v': " + pi.exempt + "}]}"; 
            }
            else if(contributionPercent > 0.0F && subscriptionPercent > 0.0F) {
                substr = String.format("%3.0f", subscriptionPercent*100.0F);
                tempstr = spc16 + "{c:[" + "{'v': '" + pi.alias + "'}," +
                        " {'v': '" + substr + "%'}, {'v': " + pi.exempt + "}]}"; 
            }
            else if(contributionPercent == 0.0F && suPercent > 0.0F) {
                tempstr = spc16 + "{c:[" + "{'v': '" + pi.alias + "'}," +
                        " {'v': '" + "Over" + "'}, {'v': " + pi.exempt+"}]}"; 
            }
            else if(contributionPercent > 0.0F && suPercent == 0.0F) {
                substr = pi.exempt ? "N/A" : "Under";
                tempstr = spc16 + "{c:[" + "{'v': '" + pi.alias + "'}," +
                        " {'v': '" + substr  + "'}, {'v': " +
                        pi.exempt + "}]}";
            }
            else if(contributionPercent == 0.0F && suPercent == 0.0F) {
                tempstr = spc16 + "{c:[" + "{'v': '" + pi.alias + "'}," +
                        " {'v': '" + "N/A"  + "'}, {'v': " +
                        pi.exempt + "}]}";
            }
            else {
                System.err.printf("Error: Erroneous subscription calculation for PI: "
                        +"%18s contributePercent: %8.2fi% suPercent: %8.2f%\n",
                        pi.userID, contributionPercent*100.0F,suPercent*100.0F);
            }
        }else{
            System.out.printf("%18s  %8s\n", pi.alias, "No SU records meet criteria");
        }
        report.add(tempstr + ","); 
    }

    protected void createLineChartDataTable(UtilizationArray ua, Boolean pp) {
        DecimalFormat df = new DecimalFormat("#.#");
        String spc4 = "    ";
        String spc6 = "      ";
        String spc8 = "        ";
        String spc10 = "          ";
        String spc14 = "              ";
        String spc16 = "                ";

        // Begin report section
        if(pp){
            report.add(spc6+"// Create the data table for previous period, service units line chart.");
            report.add(spc6+"var data6 = new google.visualization.DataTable(");
        }
        else{
            report.add(spc6+"// Create the data table for interval service units line chart.");
            report.add(spc6+"var data5 = new google.visualization.DataTable(");
        }
        report.add(spc6+"{");
        report.add(spc8+"cols: [");
        report.add(spc16+"{id: 'sclusterutil', label: 'SCLUSTER %', type: 'number'},");
        report.add(spc16+"{id: 'katanautil', label: 'PCLUSTER %', type: 'number'}");
        report.add(spc14+"],");
        report.add(spc8+"rows: ["); 
        Integer ub = ua.sclusterLineGraphValues.length;
        for(int i=0;i<ub;i++) {
            report.add(spc16 + "{c:["+"{'v': "+df.format(ua.sclusterLineGraphValues[i]) + "}, " +
                    "{'v': " + df.format(ua.katanaLineGraphValues[i]) +  "}]},"); 
        }
				removeLastRowComma(report);
        report.add(spc14+"]"); 
        report.add(spc8+"}"); 
        report.add(spc6+")"); 
        report.add("");
		if(pp){
			report.add(spc6+"var dataView6 = new google.visualization.DataView(data6);");
			report.add(spc6+"dataView6.setColumns([{calc: function(data6, row) { return ''; }, type:'string'}, 0, 1]);");
        }
        else{
			report.add(spc6+"var dataView5 = new google.visualization.DataView(data5);");
			report.add(spc6+"dataView5.setColumns([{calc: function(data5, row) { return ''; }, type:'string'}, 0, 1]);");
        }
        report.add("");
    } 

    protected void createPieChartOptions(Integer recno, String byIdentifier) {
        String spc6 = "      ";
        String spc8 = "        ";
        String spc14 = "              ";

        // Begin report section
		report.add(spc6+"// Set options for the service units pie chart.");
		report.add(spc6+"var options1 = {'title':'Service Units by " +
				byIdentifier + "',");
		report.add("'chartArea':{left:20,top:40,width:\"85%\",height:\"100%\"},");
		report.add(spc14+spc8+"'is3D': true,");
        report.add(spc14+spc8+"'width':400,");
		Integer height = Math.max(recno * 5, 300);
        report.add(spc14+spc8+"'height':" + height.toString() + "};");
        report.add("");
    } 
    
     protected void createTableChartOptions() {
        String spc6 = "      ";
        String spc8 = "        ";
        String spc14 = "              ";

        // Begin report section
		report.add(spc6+"// Set options for the service units pie chart.");
		report.add(spc6+"var options2 = {'showRowNumber':false,");
        report.add(spc14+spc8+"'showRowNumber':false};");
        report.add("");
    } 

    protected void createLineChartOptions() {
        String spc6 = "      ";
        String spc8 = "        ";
        String spc14 = "              ";

        // Begin report section
		report.add(spc6+"// Set options for the utilitzation line chart interval.");
		report.add(spc6+"var options3 = {title:'Cluster Utilization',");
		report.add(spc14+spc8+"'chartArea':{left:40,top:40,width:\"70%\",height:\"80%\"},");
        report.add(spc14+spc8+"'width':450,");
        report.add(spc14+spc8+"'height':300};");
        report.add("");
    } 

    protected void createPieChartDraw(Boolean pp) {
        String spc6 = "      ";

        // Begin report section
        if(pp){
            report.add(spc6+"// Instantiate and draw pie chart previous period, passing in some options.");
            report.add(spc6+"var chart2 = new google.visualization.PieChart"+
                    "(document.getElementById('cell1b'));");
            report.add(spc6+"chart2.draw(data2, options1);");
        }
        else{
            report.add(spc6+"// Instantiate and draw pie chart interval, passing in some options.");
            report.add(spc6+"var chart1 = new google.visualization.PieChart"+
                    "(document.getElementById('cell1a'));");
            report.add(spc6+"chart1.draw(data1, options1);");
        }
        report.add("");
    } 

    protected void createTableChartDraw(Boolean pp) {
        String spc6 = "      ";
        String spc8 = "        ";
        String spc14 = "              ";

        // Begin report section
        if(pp){
            report.add(spc6+"// Instantiate and draw table chart, passing in some options.");
            report.add(spc6+"var table2 = new google.visualization.Table"+
                    "(document.getElementById('cell2b'));");
            report.add(spc6+"table2.draw(data4, options2);");
        }
        else{
            report.add(spc6+"// Instantiate and draw table chart previous period,, passing in some options.");
            report.add(spc6+"var table1 = new google.visualization.Table"+
                    "(document.getElementById('cell2a'));");
            report.add(spc6+"table1.draw(data3, options2);");
        }
        report.add("");
    } 

    protected void createLineChartDraw(Boolean pp) {
        String spc6 = "      ";

        // Begin report section
        if(pp){
            report.add(spc6+"// Instantiate and draw utilization line chart previous period, passing in some options.");
            report.add(spc6+"var chart4 = new google.visualization.AreaChart"+
                    "(document.getElementById('cell3b'));");
            report.add(spc6+"chart4.draw(dataView6, options3);");
        }
        else{
            report.add(spc6+"// Instantiate and draw line chart interval, passing in some options.");
            report.add(spc6+"var chart3 = new google.visualization.AreaChart"+
                    "(document.getElementById('cell3a'));");
            report.add(spc6+"chart3.draw(dataView5, options3);");
        }
        report.add("");
    } 

    protected void createChartHeading()
    {
        Calendar intervalStart = Calendar.getInstance();
        Calendar intervalEnd = Calendar.getInstance();
        Calendar ppStart = Calendar.getInstance();
        Calendar ppEnd = Calendar.getInstance();
        String intervalStartStr = ""; 
        String intervalEndStr = "";
        String ppStartStr = "";
        String ppEndStr = "";
        String preface = "";
        String spc8 = "        ";
        try {
            intervalStart = DateConvert.epochToCal(Global.epochStartDate);
            intervalEnd = DateConvert.epochToCal(Global.epochEndDate);
            ppStart = DateConvert.epochToCal(Global.epochStartDatePP);
            ppEnd = DateConvert.epochToCal(Global.epochEndDate);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        try {
            intervalStartStr = DateConvert.epochToStr(Global.epochStartDate);
            intervalEndStr = DateConvert.epochToStr(Global.epochEndDate);
            ppStartStr = DateConvert.epochToStr(Global.epochStartDatePP);
            if(ppStartStr.contains("1/1/"))
                preface = "Year-To-Date: ";
			else
				preface = "Period: ";
            ppEndStr = DateConvert.epochToStr(Global.epochEndDate);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        report.add(spc8+"<th>"+"Period: "+intervalStartStr+" to "+intervalEndStr+"</th>");
        report.add(spc8+"<th>"+preface+ppStartStr+" to "+ppEndStr+"</th>");
    }

    protected void printReport(Database db) {
        String spc6 = "      ";

        readTemplate("template1.html");
		
		// Create pie, table, and line charts for current period
		if(Global.accountingLog)
			db.sumSUChargeAccountingLog(false);
		else
			db.sumSUChargeDatabase(false);
        createPieChartDataTable(db.piHashMap, false);
        createTableChartDataTable(db.piHashMap, false);
        createLineChartDataTable(db.utilizationArray, false);

		// Create pie, table and line charts for previous period
		if(Global.accountingLog)
			db.sumSUChargeAccountingLog(true);
		else
			db.sumSUChargeDatabase(true);
        createPieChartDataTable(db.piHashMap, true);
        createTableChartDataTable(db.piHashMap, true);
        createLineChartDataTable(db.utilizationArray, true);

        createPieChartOptions(db.piHashMap.size(),"PI");
        createTableChartOptions();
        createLineChartOptions();
        createPieChartDraw(false);
        createPieChartDraw(true);
        createTableChartDraw(false);
        createTableChartDraw(true);
        createLineChartDraw(false);
        createLineChartDraw(true);
        readTemplate("template2a.html");
        report.add(spc6+"<TR><TH CLASS=\"tl\">PI Usage, PI Subscription and Cluster "
                + "Utilization</TH></TR>");
		report.add(spc6+"<TR><TD CLASS=\"nv\" ALIGN=\"CENTER\">" + Global.printSubHeading()
				+ "</TD></TR>");
        readTemplate("template2b.html");
        createChartHeading();
        readTemplate("template3.html");
		createGeneratedLine();
        readTemplate("template4.html");
        Integer size = report.size();
		if(Global.debug > 2)
			for(int i=0;i<size;i++){
				System.out.printf("%s\n",report.get(i));
        }
    }

    protected void printReportByProject(Database db) {
        readTemplate("template1.html");
		
		// Create pie, table, and line charts for current period
		if(Global.accountingLog)
			db.sumSUChargeAccountingLog(false);
		else
			db.sumSUChargeDatabase(false);
        createPieChartDataTable(db.prjctHashMap, false);
        createLineChartDataTable(db.utilizationArray, false);

		// Create pie, table and line charts for previous period
		if(Global.accountingLog)
			db.sumSUChargeAccountingLog(true);
		else
			db.sumSUChargeDatabase(true);
        createPieChartDataTable(db.prjctHashMap, true);
        createLineChartDataTable(db.utilizationArray, true);

        createPieChartOptions(db.prjctHashMap.size(),"Project");
        createLineChartOptions();
        createPieChartDraw(false);
        createPieChartDraw(true);
        createLineChartDraw(false);
        createLineChartDraw(true);
        readTemplate("template2a.html");
        report.add("<TR><TH CLASS=\"tl\">Project Usage and Cluster Utilization</TH></TR>");
		report.add("<TR><TD CLASS=\"nv\" ALIGN=\"CENTER\">" + Global.printSubHeading()
				+ "</TD></TR>");
        readTemplate("template2b.html");
        createChartHeading();
        readTemplate("template3NoContrib.html");
		createGeneratedLine();
        readTemplate("template4.html");
        Integer size = report.size();
		if(Global.debug > 2)
			for(int i=0;i<size;i++){
				System.out.printf("%s\n",report.get(i));
        }
    }

    protected void printReportByOwner(Database db) {
        readTemplate("template1.html");
		
		// Create pie, table, and line charts for current period
		if(Global.accountingLog)
			db.sumSUChargeAccountingLog(false);
		else
			db.sumSUChargeDatabase(false);
        createPieChartDataTable(db.ownerHashMap, false);
        createLineChartDataTable(db.utilizationArray, false);

		// Create pie, table and line charts for previous period
		if(Global.accountingLog)
			db.sumSUChargeAccountingLog(true);
		else
			db.sumSUChargeDatabase(true);
        createPieChartDataTable(db.ownerHashMap, true);
        createLineChartDataTable(db.utilizationArray, true);

        createPieChartOptions(db.ownerHashMap.size(),"Client");
        createLineChartOptions();
        createPieChartDraw(false);
        createPieChartDraw(true);
        createLineChartDraw(false);
        createLineChartDraw(true);
        readTemplate("template2a.html");
        report.add("<TR><TH CLASS=\"tl\">Client Usage and Cluster Utilization</TH></TR>");
		report.add("<TR><TD CLASS=\"nv\" ALIGN=\"CENTER\">" + Global.printSubHeading()
				+ "</TD></TR>");
        readTemplate("template2b.html");
        createChartHeading();
        readTemplate("template3NoContrib.html");
		createGeneratedLine();
        readTemplate("template4.html");
        Integer size = report.size();
		if(Global.debug > 2)
			for(int i=0;i<size;i++){
				System.out.printf("%s\n",report.get(i));
        }
    }

	private void createGeneratedLine() {
        String spc4 = "    ";
		Calendar now = Calendar.getInstance();
		String temp = "";
		try {
			temp = DateConvert.calToStrLong(now);
		} catch (Exception e) {
			System.out.println("Error: "+e.getMessage());
		}
		report.add(spc4+"<tr><td class=\"nv\">Generated: "+temp+"</td></tr>");
	}
	
    protected void writeReport(String filename) {
        FileWriter outFile=null;
        try {
            outFile = new FileWriter(filename);
        }
        catch(Exception e)
        {
            System.err.println("Error: "+e.getMessage());
        }
        PrintWriter out = new PrintWriter(outFile);
        Integer size = report.size();
        String tempstr;
        for(int i=0;i<size;i++){
            out.printf("%s\n",report.get(i));
        }
        out.close();
    }

}
