package edu.ks.clusteraccountant;
import java.util.regex.*;

public class ProcessorType {
	Integer id;
	Long dateInstalled;
    String regex;
    String processorType;
    Integer processorQty;
    Integer nodeQty;
    Float suCharge;
    String cluster;
    Boolean disabled;
    Pattern p;

    ProcessorType(Integer id, Long dateInstalled, String regex,
            String processorType, Integer processorQty,
            Integer nodeQty, Float suCharge, String cluster, Boolean disabled) {
		this.id = id;
        this.dateInstalled = dateInstalled;
        this.regex = regex;
        this.processorType = processorType;
        this.processorQty = processorQty;
        this.nodeQty =  nodeQty;
        this.suCharge = suCharge;
        this.cluster = cluster;
        this.disabled = disabled; 
        p = Pattern.compile(regex);
    }

   	protected void print() {
    	System.out.println("id: " + id);
		String date = "";
        try {
            date = DateConvert.epochToStr(dateInstalled);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    	System.out.println("dateInstalled: " + date);
    	System.out.println("regex: " + regex);
        System.out.println("processorType: " + processorType);
        System.out.println("processorQty: " + processorQty);
        System.out.println("nodeQty: " + nodeQty);
        System.out.println("suCharge: " + suCharge);
        System.out.println("cluster: " + cluster);
        System.out.println("disabled: " + disabled);
    }
  
}
