/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;

/**
 *
 * @author donj
 */
public class PrincipalInvestigator {
	Integer uid;
	String userID;
	String firstName;
	String lastName;
	String alias;
	String emailAddress;
	Boolean exempt;
	Float suSum = 0.0F;
	Float contributionSum = 0.0F;

   PrincipalInvestigator(Integer uid, String userID, String firstName, String lastName,
           String alias, String emailAddress, Boolean exempt ) {
        this.uid = uid;
        this.userID=userID;
        this.firstName=firstName;
        this.lastName=lastName;
        this.alias=alias;
        this.emailAddress=emailAddress;
        this.exempt=exempt;
    }

   protected void print() {
    	System.out.println("uid: " + uid);
    	System.out.println("userID: " +  userID);
        System.out.println("firstName: " + firstName);
        System.out.println("lastName: " + lastName);
        System.out.println("alias: " + alias);
        System.out.println("emailAddress: " + emailAddress);
        System.out.println("exempt: " + exempt);
        System.out.println("suSum: " + suSum);
        System.out.println("contributionSum: " + contributionSum);
    }
}
