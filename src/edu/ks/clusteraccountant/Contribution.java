/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;

/**
 *
 * @author donj
 */
public class Contribution {
	Integer id;
	Long epochDateContributed;
	String contributorUserID;
	String creditedToUserID;
	String hostName;
	String processorType;
	Integer noOfProcessors;
	Integer splitPercentage;
	Float suCharge;
	
	Contribution(Integer id, Long epocheDateContributed, String contributorUserID,
            String creditedToUserID, String hostName, String processorType,
            Integer noOfProcessors, Integer splitPercentage, Float suCharge) {
        this.id=id;
        this.epochDateContributed = epocheDateContributed; 
		this.contributorUserID = contributorUserID;
		this.creditedToUserID = creditedToUserID;
		this.hostName = hostName;
		this.processorType = processorType;
		this.noOfProcessors = noOfProcessors;
		this.splitPercentage = splitPercentage;
		this.suCharge = suCharge;
    }

	protected void print() {
		String date = "";
        try {
            date = DateConvert.epochToStr(epochDateContributed);
        } catch (Exception e) {
            System.err.println("Error: "+e.getMessage());
        }
		System.out.println("Date Contributed: "+date);
		System.out.println("Contributor UserID: "+contributorUserID);
		System.out.println("Credited To UserID: "+creditedToUserID);
		System.out.println("Host Name: "+hostName);
		System.out.println("Processor Type: "+processorType);
		System.out.println("No. of Processors: "+noOfProcessors);
		System.out.println("Split Percentage: "+splitPercentage);
		System.out.println("SU Charge: "+suCharge);
	}
      
    protected Float sumContribution() {
        return(noOfProcessors * splitPercentage * suCharge);
    }
}
