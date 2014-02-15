/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;

/**
 *
 * @author donj
 */
public class Project {
	Integer gid;
	String groupid;
	String piOwnerUserID;
	Float suSum=0.0F;
    
    Project(Integer gid, String groupID, String piOwnerUserID) {
		this.gid = gid;
        this.groupid = groupID;
        this.piOwnerUserID = piOwnerUserID;
    }

  	protected void print() {
    	System.out.println("gid: " + gid);
    	System.out.println("groupid: " + groupid);
        System.out.println("piOwnerUserID: " + piOwnerUserID);
        System.out.println("suSum: " + suSum);
    }
}
