/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;

/**
 *
 * @author donj
 */
public class UtilizationArray {
    protected Float[] sclusterLineGraphValues;
    protected Float[] katanaLineGraphValues;
    protected Float[] sclusterMaxComputationUnitsPerInterval;
    protected Float[] katanaMaxComputationUnitsPerInterval;
    protected Long periodDays;
    protected Long graphIntervals;
    protected Long millisecondsPerInterval;
    protected Float sclusterServiceUnits;
    protected Float katanaServiceUnits;
    private Long esd;
    private Long eed;

    public UtilizationArray(Long esd, Long eed){
        this.esd = esd;
        this.eed = eed;
		sclusterServiceUnits = 0.0F;
		katanaServiceUnits = 0.0F;
        Long millisecondsPerDay = 86400000L; 
        // Even when the start and end dates are the same, since start begins
        // a midnight and end date finishes at 12:59:59, the difference is still
        // one second less than a day
        Long periodMilliseconds = eed - esd + 1000L;
        periodDays = periodMilliseconds / millisecondsPerDay;
        // Divide periodDays by 2 to allow some averaging because SU are all
        // accumulated into the bucket where the job terminates instead of
        // being distributed across the period
        graphIntervals = Math.min(periodDays/2, Global.maxGraphIntervals); 
        millisecondsPerInterval = periodMilliseconds / graphIntervals;
		Integer nlgv =Global.safeLongToInt(graphIntervals);
        sclusterLineGraphValues = new Float[nlgv];
        katanaLineGraphValues = new Float[nlgv];
        sclusterMaxComputationUnitsPerInterval = new Float[nlgv]; 
        katanaMaxComputationUnitsPerInterval = new Float[nlgv]; 
		for(int i=0;i<nlgv;i++){
			sclusterLineGraphValues[i] = 0.0F;
			katanaLineGraphValues[i] = 0.0F;
            sclusterMaxComputationUnitsPerInterval[i] = 0.0F;
            katanaMaxComputationUnitsPerInterval[i] = 0.0F;
		}
 }

    protected Integer calcIntervalArrayIndex(Long epochDate){
        Long offsetMilliseconds = 0L;
        Integer index;
        offsetMilliseconds = epochDate - esd;
        if(offsetMilliseconds < 0) {
            offsetMilliseconds = 0L;
        }
        index = Global.safeLongToInt(offsetMilliseconds /millisecondsPerInterval);   
        return(index);
    }

    protected DateRange calcIntervalDateRange(Integer index){
        DateRange dr = new DateRange();
        dr.fromDate = esd + index * millisecondsPerInterval;
        dr.toDate = esd + (index+1) * millisecondsPerInterval;
        if(Global.debug > 2){
            System.out.println("dr.fromDate = " + dr.fromDate);
            System.out.println("dr.toDate = " + dr.toDate);
        }
        return(dr);
    }

    // Computation unit = machine_dependent_factor (SU value) * cpu seconds;
    // Computation unit/3600 = Service Unit;
    protected void calcMaxComputationUnitsPerInterval(ProcessorTypeArrayList ptal) {
        Integer len = sclusterMaxComputationUnitsPerInterval.length;
        Float sclusterMaxSU = 0.0F;
        Float katanaMaxSU = 0.0F;
        DateRange dr = new DateRange();
        Integer i = 0;

        // Interval caculation
        for(i=0;i<len;i++) {
            dr = calcIntervalDateRange(i);
            sclusterMaxSU =
                 ptal.clusterMaxSU(ProcessorTypeArrayList.Cluster.SCLUSTER, dr.toDate);
            sclusterMaxComputationUnitsPerInterval[i] = sclusterMaxSU * millisecondsPerInterval / 1000L; 
            katanaMaxSU =
                 ptal.clusterMaxSU(ProcessorTypeArrayList.Cluster.PCLUSTER, dr.toDate);
            katanaMaxComputationUnitsPerInterval[i] = katanaMaxSU * millisecondsPerInterval / 1000L; 
        }
    }

    protected void convertComputationUnitsToSU() {
        Float secsPerHour = 3600.0F; 
        Integer len = sclusterLineGraphValues.length;
        for(int i=0;i<len;i++) {
            sclusterLineGraphValues[i] /= secsPerHour;
            katanaLineGraphValues[i] /= secsPerHour;
        }
    }

    protected void divideByMaxComputationUnitsPerInterval() {
        Integer len = sclusterLineGraphValues.length;
        for(int i=0;i<len;i++) {
            if(sclusterMaxComputationUnitsPerInterval[i] > 0.0F) {
                sclusterLineGraphValues[i] *= 100.0F;
                sclusterLineGraphValues[i] /= sclusterMaxComputationUnitsPerInterval[i];
            }
            if(katanaMaxComputationUnitsPerInterval[i] > 0.0F) {
                katanaLineGraphValues[i] *= 100.F;
                katanaLineGraphValues[i] /= katanaMaxComputationUnitsPerInterval[i];
            }
        }
    } 
    
	protected void printReport() {
        Long secPerHour = 3600L;
		Integer len = sclusterLineGraphValues.length;
        System.out.printf("Utilization Percentage on SCLUSTER\n");
		for(int i=0;i<len;i++)
           System.out.printf("\tIndex: %d Value: %8.2f\n", i, sclusterLineGraphValues[i]);
        if(Global.earthSystemsOnly)
            System.out.printf("Utilization ES Only SCLUSTER SU Total: %.2f\n", sclusterServiceUnits/secPerHour);
        else
            System.out.printf("Utilization All Groups SCLUSTER SU Total: %.2f\n", sclusterServiceUnits/secPerHour);
		System.out.println("");
        System.out.printf("Utilization Percentage on PCLUSTER\n");
		for(int i=0;i<len;i++)
           System.out.printf("\tIndex: %d Val: %8.2f\n", i, katanaLineGraphValues[i]);
        if(Global.earthSystemsOnly)
            System.out.printf("Utilization ES Only PCLUSTER SU Total: %.2f\n", katanaServiceUnits/secPerHour);
        else
            System.out.printf("Utilization All Groups PCLUSTER SU Total: %.2f\n", katanaServiceUnits/secPerHour);
    } 

   	protected void printDebug(ProcessorTypeArrayList.Cluster cluster) {
		Integer len = sclusterLineGraphValues.length;
        if(cluster == ProcessorTypeArrayList.Cluster.SCLUSTER) {
            for(int i=0;i<len;i++)
               System.out.printf("\tSCLUSTER Int Index: %d Graph: %8.2f Max CU %8.2f\n", i,
                       sclusterLineGraphValues[i], sclusterMaxComputationUnitsPerInterval[i]);
        }
        if(cluster == ProcessorTypeArrayList.Cluster.PCLUSTER) {
            for(int i=0;i<len;i++)
               System.out.printf("\tPCLUSTER Int Index: %d Grap: %8.2f Max CU %8.2f\n",
                       i, katanaLineGraphValues[i], katanaMaxComputationUnitsPerInterval[i]);
        }
    }

}
