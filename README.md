![image](http://kodosaru.net/images/ca-report.png)
The Cluster Accountant
========================================

**Introduction**  

The &ldquo;PI Usage, PI Subscription and Cluster Utilization&rdquo; [report](http://kodosaru.net/docs/oct_2012.html) is a product of the &ldquo;Cluster Accountant&rdquo; application. This application was written to help scientists manage a computer cluster and to avoid the depletion of a shared resource.  In support of those goals, this reporting tool attempts to answer two questions: &ldquo;When is time to buy new computer blades?&rdquo; and &ldquo;Who should contribute grant money toward the purchase?&rdquo;

**The Charts and Tables**  

There are two sets of graphs arranged into two columns representing time intervals. The left column is for the previous month and the right column represents the year-to-date period. Each column has three graphs: Usage pie chart, subscription table and utilization line graph.

A pie chart percentage equals each research group&#39;s service unit (SU) usage for the period divided by total SU usage for all scientists.

A subscription table value, &ldquo;a ratio of ratios,&rdquo; equals a research group&#39;s SU usage relative to the sum of all usage, divided by the research group&#39;s computer processing contribution relative to the sum of all research groups&#39; computer processing contribution. In other words, &ldquo;Subscription&rdquo; is a percentage equal to &ldquo;What a PI&#39;s research group is using relative to others, over what a PI contributed in equipment relative to others.&rdquo;

For example, if a research group used 50% of the total SUs consumed for a period and the PI of that group contributed 25% of the computer processing power, that PI/research group&#39;s subscription rate would be 50% / 25% = 200%. The group would be &ldquo;over&rdquo; subscribed. On the other hand, a group that used only 10% of total SUs consumed for a period, but had contributed 30% of the processing power of the cluster, would have a 10% / 30% = 33% subscription rate. The group would be &ldquo;under&rdquo; subscribed.

When a group had either usage, but no contribution or contribution, but no usage, a numerical percentage is not defined. Consequently, the table will only show the values &ldquo;Over&rdquo; or &ldquo;Under.&rdquo; &ldquo;Exempt&rdquo; means that the group listed will not be contributing any funds to the growth of the cluster and a subscription ratio for them cannot be calculated.

A utilization line graph value shows absolute SU usage for all clients of the PCLUSTER or SCLUSTER clusters over all SUs capable of being supplied by the hardware for an interval. The utilization rate includes usage by all researchers at BU including those outside of the earth system&#39;s departments. Since SCLUSTER nodes are a subset of PCLUSTER nodes, all PCLUSTER utilization includes SCLUSTER utilization.

**Summary**  

Providing this information is not meant to evoke the &ldquo;pathogenic effects of conscience&rdquo; [<a href="http://kodosaru.net/docs/tragedy_of_the_commons.pdf">Hardin, 2001</a>]. Instead, our hope is that it creates friendly "mutual coercion." Because of the better management and utilization provided by a computer cluster over standalone hosts, the CLUSTER project is a win-win endeavor where both big and small contributors benefit. CLUSTER was as much a social project as a technical project and this reporting tool is the last essential element. By using these reports to encourage contributions, we can assure that the growth of the cluster tracks the growth of our research activities.
 
**Technical Details**

One service unit (SU) equals one older processor working fully for one hour. The new blades are almost twice as powerful, so one SU equals about a 1/2 hour of full-time processing. Internally, the Linux operating system sums up arbitrary computational units called &ldquo;jiffies,&rdquo; the &ldquo;atom&rdquo; of computation. On the Intel machines running the Linux v2.6 kernel, there are 100 jiffies in a second. If a program used 100 jiffies = 1 second of computation, but 3 seconds had passed on the wall clock and the processing had taken place on four processors, the program would still have been only charged for one second of computing time. This one second would then have been multiplied by a factor that depends on how powerful the machine is to compute the SU charge. The program is charged for jiffies it uses and for jiffies used by operating system calls, such as disk access, that support the program. A program is not charged when it is waiting in line for the next slice of processor time.

With respect to the &ldquo;contribution&rdquo; calculation, one processor (older processors) equals 100 contribution units, newer processors with a lot of memory equal 190 contribution units or more. For example, a PI contributing a 12 processor blade, assigned a machine dependent factor of 1.9 by SCV based on its processing power, will receive 12 x 1.9 x 100 = 2280 contribution units. The intermediate value was multiplied by one hundred to simplify contribution calculations when PIs contribute a fraction of a computer blade.

The utilization graph credits the full usage for a job to the period when the job ended. The monthly usage is summed by day and a graph valued generated. The year-to-date values are aggregated into either fifty intervals or into a number of intervals equal to the number of days between the start and end dates of the report, whichever is smaller. Because of the way utilization is calculated, the graph represents more of a moving average than instantaneous use. So a the cluster may have hit 100% utilization during a day, but not have had that event reflected in its utilization graph if all researchers piled their jobs onto the cluster in the afternoon, instead of submitting them throughout the day.

The information for this report comes from the Oracle Grid Engine Manager&#39;s accounting records and from a standalone MySQL database managed by the College of Arts and Sciences. The application is written in Oracle JDK v7.1 Java. The graphs and tables are created utilizing Google's Chart Tools service. The report will be generated automatically monthly and sent to all principal investigators. 

**Examples of how to run the Cluster Accountant from the command line:**

java -jar cluster_accountant.jar main  
java -jar cluster_accountant.jar main -s 12/01/2011 -e 12/31/2011 -S 01/01/2011 -E 12/31/2011 -t html  
java -jar cluster_accountant.jar main -s 12/01/2011 -e 12/31/2011 -S 01/01/2011 -E 12/31/2011 -t html_by_client -n donj -p landsat  
java -jar cluster_accountant.jar main -s 12/01/2011 -e 12/31/2011 -S 01/01/2011 -E 12/31/2011 -t html -n index  
java -jar cluster_accountant.jar main -s 3/01/2012 -e 3/31/2012 -S 01/01/2012 -E 12/31/2012 -t html -n index
 
**How to import accounting records:**

java -jar cluster_accountant.jar main -x sge_root/default/common/accounting.2011  
java -jar cluster_accountant.jar main -x sge_root/default/common/accounting.2012  
java -jar cluster_accountant.jar main -x sge_root/default/common/accounting  


**Happy Coding!**
