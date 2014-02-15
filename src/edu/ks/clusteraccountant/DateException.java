/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;


/**
 *
 * @author donj
 */
// Custom exception class that descends from Java's Exception class.
class DateException extends Exception {
    String dateError;
    // ----------------------------------------------
    // Default constructor - initializes instance variable to unknown
    public DateException() {
        super(); // call superclass constructor
        dateError = "Unknown date error";
    }
  
    // -----------------------------------------------
    // Constructor receives some kind of message that is saved in an instance variable.
    public DateException(String err) {
        super(err); // call super class constructor
        dateError = err; // save message
    }
  
    // ------------------------------------------------  
    // public method, callable by exception catcher. It returns the error message.
    public String getError() {
        return dateError;
    }
}  
