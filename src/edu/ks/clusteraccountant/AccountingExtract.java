/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;
import java.sql.*;
import java.util.Map;
/**
 *
 * @author donj
 */
	
public class AccountingExtract {
    protected String hostname;
    protected String group;
    protected String owner;
    protected String job_number;
    protected long start_time;
    protected long end_time;
    protected String task_number;
    protected float cpu;
    protected String pe_taskid;

    AccountingExtract() {
        this.hostname = null; 
        this.group = null; 
        this.owner = null; 
        this.job_number = null; 
        this.start_time = 0L; 
        this.end_time = 0L; 
        this.task_number = null; 
        this.cpu = 0.0F; 
        this.pe_taskid = null; 
    }

    AccountingExtract(String hostname, String group, String owner,
            String job_number, Long start_time, Long end_time, String task_number,
                    Float cpu, String pe_taskid) {
        this.hostname = hostname; 
        this.group = group; 
        this.owner = owner; 
        this.job_number = job_number; 
        this.start_time = start_time; 
        this.end_time = end_time; 
        this.task_number = task_number; 
        this.cpu = cpu; 
        this.pe_taskid = pe_taskid; 
    }

    public void setAttributes (String hostname, String group, String owner,
            String job_number, Long start_time, Long end_time, String task_number,
                    Float cpu, String pe_taskid) {
        this.hostname = hostname; 
        this.group = group; 
        this.owner = owner; 
        this.job_number = job_number; 
        this.start_time = start_time; 
        this.end_time = end_time; 
        this.task_number = task_number; 
        this.cpu = cpu; 
        this.pe_taskid = pe_taskid; 
    }

    public String getHostname() {return(this.hostname);}
    public String getGroup() {return(this.group);}
    public String getOwner() {return(this.owner);}
    public String getJobNumber() {return(this.job_number);}
    public Long getStartTime() {return(this.start_time);}
    public Long getEndTime() {return(this.end_time);}
    public String getTaskNumber() {return(this.task_number);}
    public Float getCpu() {return(this.cpu);}
    public String getPeTaskid() {return(this.pe_taskid);}

    protected void printAttributes() {
        System.out.printf("hostname: %s\n", hostname);
        System.out.printf("group: %s\n", group);
        System.out.printf("owner: %s\n", owner);
        System.out.printf("job_number: %s\n", job_number);
		String date = "";
        try {
            date = DateConvert.epochToStr(start_time*1000); 
            System.out.printf("start_time: %s\n",date);
            date = DateConvert.epochToStr(end_time*1000); 
            System.out.printf("end_time: %s\n", date);
        } catch (Exception e) {
            System.err.println("Error: "+e.getMessage());
        }
        System.out.printf("task_number: %s\n", task_number);
        System.out.printf("cpu: %f\n", cpu);
        System.out.printf("pe_taskid: %s\n", pe_taskid);
    }
  
    protected void printShortAttributes(ProcessorTypeArrayList ptlist) {
		ProcessorType pt = ptlist.lookupProcessorType(hostname);
        System.out.printf("%22s\t%8s\t%8s\t%10d\t%10d\t%14f\t%8f\n",hostname, group,
                owner, start_time, end_time, cpu, pt.suCharge);
    }

    // Computation unit = machine_dependent_factor (SU value) * cpu seconds;
    // Computation units/3600 seconds per hour = Service Units;
    protected void sumSUCharge(ProcessorTypeArrayList ptlist, ProjectHashMap projecthm,
            PrincipalInvestigatorHashMap pihm, UtilizationArray ua,
			Map<String,Float> ghm, Map<String,Float> ohm) {
        Project p;
        PrincipalInvestigator pi;

        // Lookup host information 
		ProcessorType pt = ptlist.lookupProcessorType(hostname);
		Float suCharge = 0.0F;
		String cluster = "";
		if(pt == null) {
			suCharge = 1.0F;
			cluster = "katana";
            System.err.println("Error: SU charge arbitrarily set to 1.0 for host: " + hostname);
            System.err.println("Error: Cluster arbitrarily set to katana for host: " + hostname);
		}
		else {
			suCharge = pt.suCharge;
			cluster = pt.cluster; 
		}

		// Do per record service unit calculation
        Float computationUnitsPerAcctRecord =  (suCharge * cpu);

        // In earth-systems-only mode, only look at Earth Systems project groups
        // so that utilization totals can be compared to PI totals
        // apples to apples
        if (!Global.earthSystemsOnly || projecthm.containsKey(group)) {
            // Do per cluster utilitzation calculations 
            Integer index = ua.calcIntervalArrayIndex(end_time * 1000L); 
            if(cluster.equals("scluster")) {
                ua.sclusterLineGraphValues[index] +=  computationUnitsPerAcctRecord;
                ua.sclusterServiceUnits += computationUnitsPerAcctRecord;	
            }
            else {
                ua.katanaLineGraphValues[index] +=  computationUnitsPerAcctRecord;
                ua.katanaServiceUnits += computationUnitsPerAcctRecord;	
            }
        }

        if (projecthm.containsKey(group)) {

            // Sum service units per project
            p = projecthm.get(group);
            // In the error check, project = group and project list = project hashmap
            if(p == null){
                System.err.println("Error: Unable to find project "+ "\""+
                        group+"\" in project list");
                return;
            }
            p.suSum +=  computationUnitsPerAcctRecord;
            projecthm.totalServiceUnits += computationUnitsPerAcctRecord;

            // Sum service units per PI
            pi = pihm.get(p.piOwnerUserID);
            // In the error check, and PI list = PI hashmap 
            if(pi == null){ 
                System.err.println("Error: Unable to find PI "+ "\""+
                        p.piOwnerUserID+"\" in PI list");
                return;
            }
            pi.suSum += computationUnitsPerAcctRecord;
            pihm.totalServiceUnits += computationUnitsPerAcctRecord; 

			// Print messages
            if (Global.debug > 1) {
                System.out.printf(
                        "SCLUSTER group: %8s cpu: %8.2f sucharge: %8.2f gpsum: %f\n",
                        group, cpu, suCharge, projecthm.get(group).suSum);
            }
        } else if (Global.debug > 1) {
            System.out.printf("Other group: %8s cpu: %8.2f sucharge: %8.2f\n",
                    group, cpu, suCharge);
        }

		// Sum group, owner SU
		Float groupSUSum = 0.0F;
		if(ghm.containsKey(group)) {
			groupSUSum = ghm.get(group);
			ghm.remove(group);
		}
		ghm.put(group, computationUnitsPerAcctRecord + groupSUSum);

		Float ownerSUSum = 0.0F;
		if(ohm.containsKey(owner)) {
			ownerSUSum = ohm.get(owner);
			ohm.remove(owner);
		}
		ohm.put(owner, computationUnitsPerAcctRecord + ownerSUSum);
    }
 
    protected PreparedStatement testAttributes(PreparedStatement pst, String ssd, String sed,
            Long esd, Long eed) {
        esd /= 1000L;
        eed /= 1000L;

        // Four criteria: start_date, end_date, group, owner
        // There are 16 possibilities for when criteria exist or don't exist
        // We are using using the job end time for selection criteria
        try {
            
            // I. sd ed g o 
            if(ssd!=null && sed!=null &&
                    Global.group!=null && Global.owner!=null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time >= " + esd + " and " +
                        "end_time  <= " + eed + " and " +
                        "_group = '" + Global.group + "' and " +
                        "owner = '" + Global.owner + "'");
            }
            // II. sd ed g - 
            else if(ssd!=null && sed!=null &&
                    Global.group!=null && Global.owner==null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time >= " + esd + " and " +
                        "end_time <= " + eed + " and " +
                        "_group = '" + Global.group + "'");
            }
            // III. sd ed - o 
            else if(ssd!=null && sed!=null &&
                    Global.group==null && Global.owner!=null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time >= " + esd + " and " +
                        "end_time <= " + eed + " and " +
                        "owner = '" + Global.owner + "'");
            }
            // IV. sd ed - - 
            else if(ssd!=null && sed!=null &&
                    Global.group==null && Global.owner==null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time >= " + esd + " and " +
                        "end_time <= " + eed);
            }
            // V. sd - g o 
            else if(ssd!=null && sed==null &&
                    Global.group!=null && Global.owner!=null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time >= " + esd + " and " +
                        "_group = '" + Global.group + "' and " +
                        "owner = '" + Global.owner + "'");
            }
            // VI. sd - g - 
            else if(ssd!=null && sed==null &&
                    Global.group!=null && Global.owner==null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time >= " + esd + " and " +
                        "_group = '" + Global.group + "'");
            }
            // VII. sd - - o 
            else if(ssd!=null && sed==null &&
                    Global.group==null && Global.owner!=null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time >= " + esd + " and " +
                        "owner = '" + Global.owner + "'");
            }
            // VIII. sd - - - 
            else if(ssd!=null && sed==null &&
                    Global.group==null && Global.owner==null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time >= " + esd);
            }
            // IX. - ed g o 
            else if(ssd==null && sed!=null &&
                    Global.group!=null && Global.owner!=null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time <= " + eed + " and " +
                        "_group = '" + Global.group + "' and " +
                        "owner = '" + Global.owner + "'");
            }
            // X. - ed g - 
            else if(ssd==null && sed!=null &&
                    Global.group!=null && Global.owner==null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time <= " + eed + " and " +
                        "_group = '" + Global.group + "'");
            }
            // XI. - ed - o 
            else if(ssd==null && sed!=null &&
                    Global.group==null && Global.owner!=null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time <= " + eed + " and " +
                        "owner = '" + Global.owner + "'");
            }
            // XII. - ed - - 
            else if(ssd==null && sed!=null &&
                    Global.group==null && Global.owner==null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where end_time <= " + eed);
            }
            // XIII. - - g o 
            else if(ssd==null && sed==null &&
                    Global.group!=null && Global.owner!=null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where _group = '" + Global.group + "' and " +
                        "owner = '" + Global.owner + "'");
            }
            // XIV. - - g - 
            else if(ssd==null && sed==null &&
                    Global.group!=null && Global.owner==null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where _group = '" + Global.group + "'");
            }
            // XV. - - - o 
            else if(ssd==null && sed==null &&
                    Global.group==null && Global.owner!=null) {
                pst = Global.con.prepareStatement("select * from accounting " +
                        "where owner = '" + Global.owner + "'");
            }
            // XVI. - - - - 
            else if(ssd==null && sed==null &&
                    Global.group==null && Global.owner==null) {
                pst = Global.con.prepareStatement("select * from accounting");
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
		return(pst);
    }

}
