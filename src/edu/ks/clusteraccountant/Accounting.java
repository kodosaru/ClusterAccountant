package edu.ks.clusteraccountant;


/* From "man" page on "accounting":
 NAME
 accounting - Sun Grid Engine accounting file format

 DESCRIPTION
 An  accounting record is written to the Sun Grid Engine accounting
 file for each job having finished. The accounting file is processed
 by qacct(1) to derive  accounting  statistics.

 FORMAT
 Each  job  is  represented  by a line in the accounting file. Empty
 lines and lines which contain one character or less are ignored.
 Accounting record entries  are  separated  by colon (':') signs.
 The entries denote in their order of appearance:

 qname #1
 Name of the cluster queue in which the job has run.

 hostname #2
 Name of the execution host.

 group #3
 The effective group id of the job owner when executing the job.
 
 owner #4
 Owner of the Sun Grid Engine job.

 job_name #5
 Job name.

 job_number #6
 Job identifier - job number.

 account #7
 An account string as specified by the qsub(1) or qalter(1) -A option.
 account string as specified by the qsub(1) or qalter(1) -A option.

 priority #8
 Priority  value assigned to the job corresponding to the priority
 parameter in the queue configuration (see queue_conf(5)).

 submission_time #9
 Submission time (GMT unix time stamp).

 start_time #10
 Start time (GMT unix time stamp).

 end_time #11
 End time (GMT unix time stamp).

 failed #12
 Indicates the problem which occurred in case a job could not be
 started on the  execution host (e.g. because the owner of the job did
 not have a valid account on that machine). If Sun Grid Engine tries
 to start a job multiple times, this may lead to multiple entries in
 the accounting file corresponding to the same job ID.

 exit_status #13
 Exit status of the job script (or Sun Grid Engine specific status in
 case of certain error conditions).  The exit status is determined by
 following the normal shell  conventions.  If the command terminates
 normally the value of the command is its exit status. However, in the
 case that the command exits abnormally, a value of 0200 (octal), 128
 (decimal) is added to the value of the command to make up the exit
 status.

 For example: If a job dies through signal 9 (SIGKILL) then the exit
 status becomes 128 + 9 = 137.

 ru_wallclock #14
 Difference between end_time and start_time (see above).

 rusage #15 to #31
 The remainder of the accounting entries follows the contents of the
 standard UNIX  rusage structure  as described in getrusage(2). See
 also the class definitioin of Rusage in Rusage.java. Depending on the
 operating system where the job was executed some of the fields may
 be 0.

 project #32
 The project which was assigned to the job.

 department #33
 The department which was assigned to the job.

 granted_pe #34
 The parallel environment which was selected for that job.

 slots #35
 The number of slots which were dispatched to the job by the scheduler.

 task_number #36
 Array job task index number.

 cpu #37
 The cpu time usage in seconds.

 mem #38
 The integral memory usage in Gbytes cpu seconds.

 io #39
 The amount of data transferred in input/output operations.

 category #40
 A string specifying the job category.

 iow #41
 The io wait time in seconds.

 pe_taskid #42
 If this identifier is set the task was part of a parallel job and was
 passed to Sun  Grid Engine via the qrsh -inherit interface.

 maxvmem #43
 The maximum vmem size in bytes.

 arid
 Advance  reservation identifier. If the job used resources of an
 advance reservation then this field contains a positive integer
 identifier otherwise the value is "0" . (Not used at BU)

 ar_submission_time
 If the job used resources of an advance reservation then this field
 contains the  submission  time (GMT unix time stamp) of the advance
 reservation, otherwise the value is "0" . (Not used at BU)
 */

class Accounting extends AccountingExtract {
    private String qname;
    //private String hostname;
    //private String group;
    //private String owner;
    private String job_name;
    //private String job_number;
    private String account;
    private int priority;
    private long submission_time;
    //private long start_time;
    //private long end_time;
    private int failed;
    private int exit_status;
    private long ru_wallclock;
    private Rusage rusage = new Rusage();
    private String project; // SGE project, not SCV project = Unix group. Usually "NONE"
    private String department;
    private String granted_pe;
    private int slots;
    //private String task_number;
    //private float cpu;
    private String mem;
    private String io;
    private String category;
    private String iow;
    //private String pe_taskid;
    private String maxvmem;
    // arid;
    // ar_submission_time;

    protected void setAttributes(String sa[]) {
        rusage = new Rusage();
        qname = sa[0];
        hostname = sa[1];
        group = sa[2];
        owner = sa[3];
        job_name = sa[4];
        job_number = sa[5];
        account = sa[6];
        priority = Integer.parseInt(sa[7]);
        submission_time = Long.parseLong(sa[8]);
        start_time = Long.parseLong(sa[9]);
        end_time = Long.parseLong(sa[10]);
        failed = Integer.parseInt(sa[11]);
        exit_status = Integer.parseInt(sa[12]);
        ru_wallclock = Long.parseLong(sa[13]);
        rusage.setAttributes(
            Long.parseLong(sa[14]),
            Long.parseLong(sa[15]),
            sa[16],
            Long.parseLong(sa[17]),
            Long.parseLong(sa[18]),
            Long.parseLong(sa[19]),
            Long.parseLong(sa[20]),
            Long.parseLong(sa[21]),
            Long.parseLong(sa[22]),
            Long.parseLong(sa[23]),
            sa[24],
            Long.parseLong(sa[25]),
            Long.parseLong(sa[26]),
            Long.parseLong(sa[27]),
            Long.parseLong(sa[28]),
            Long.parseLong(sa[29]),
            Long.parseLong(sa[30])
            );
        project = sa[31];
        department = sa[32];
        granted_pe = sa[33];
        slots = Integer.parseInt(sa[34]);
        task_number = sa[35];
        cpu = Float.parseFloat(sa[36]);
        mem = sa[37];
        io = sa[38];
        category = sa[39];
        iow = sa[40];
        pe_taskid = sa[41];
        maxvmem = sa[42];
        // arid;
        // ar_submission_time;
    }

	@Override
    protected void printAttributes() {
		printAttributes();
        System.out.printf("qname: %s\n", qname);
        //System.out.printf("hostname: %s\n", hostname);
        //System.out.printf("group: %s\n", group);
        //System.out.printf("owner: %s\n", owner);
        System.out.printf("job_name: %s\n", job_name);
        //System.out.printf("job_number: %s\n", job_number);
        System.out.printf("account: %s\n", account);
        System.out.printf("priority: %d\n", priority);
        System.out.printf("submission_time: %d\n", submission_time);
		//String date = "";
        //try {
        //    date = DateConvert.epochToStr(start_time*1000); 
        //    System.out.printf("start_time: %s\n",date);
        //    date = DateConvert.epochToStr(end_time*1000); 
        //    System.out.printf("end_time: %s\n", date);
        //} catch (Exception e) {
        //    System.err.println("Error: "+e.getMessage());
        //}
        System.out.printf("failed: %d\n", failed);
        System.out.printf("exit_status: %d\n", exit_status);
        System.out.printf("ru_wallclock: %d\n", ru_wallclock);
        if(Global.debug > 2)
            rusage.printAttributes();
        System.out.printf("project: %s\n", project);
        System.out.printf("department: %s\n", department);
        System.out.printf("granted_pe: %s\n", granted_pe);
        System.out.printf("slots: %d\n", slots);
        //System.out.printf("task_number: %s\n", task_number);
        //System.out.printf("cpu: %f\n", cpu);
        System.out.printf("mem: %s\n", mem);
        System.out.printf("io: %s\n", io);
        System.out.printf("category: %s\n", category);
        System.out.printf("iow: %s\n", iow);
        //System.out.printf("pe_taskid: %s\n", pe_taskid);
        System.out.printf("maxvmem: %s\n\n", maxvmem);
    }
  
    protected boolean testAttributes(String ssd, String sed, Long esd, Long eed) {
        // Use Job end time for selection criteria
        if(!(ssd == null || (end_time * 1000 >= esd)))
            return(false);
        if(!(sed == null || (end_time * 1000 <= eed)))
            return(false);
        if(!(Global.group == null || Global.group.equalsIgnoreCase(group) ))
            return(false);
        if(!(Global.owner == null || Global.owner.equalsIgnoreCase(owner) ))
            return(false);

        // Accounting record meets all selection criteria
        return(true);
    }

}
