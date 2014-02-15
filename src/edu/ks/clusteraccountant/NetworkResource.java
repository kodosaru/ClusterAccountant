/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;

import java.util.regex.Pattern;

/**
 *
 * @author donj
 */
public class NetworkResource {
	private String protocol = "";
    private String hostname = "";
    private Integer port = 0;
	private String directory = "";
    private String userID = "";
    private String password = "";
	private String pathToPrivateKey = "";

	NetworkResource(String protocol, String hostname, Integer port, String directory,
			String userID, String password, String pathToPrivateKey) {
		this.protocol = protocol;
	    Pattern period = Pattern.compile("\\.");
		String splitarray[] = period.split(hostname);
		if (splitarray.length != 3) {
			System.err.println("Error: Malformed hostname " + hostname);
			System.exit(1);
		}
		this.hostname = hostname;
		if(port < 0 || port > 65535) {
			System.err.println("Error: Invalid port number " + port);
			System.exit(1);
		}
		this.port = port;
		if(directory.charAt(0) != '/') {
			System.err.println("Error: Malformed directory " + directory);
			System.exit(1);
		}
		this.directory = directory;
		this.userID = userID;
		this.password = password; 
		this.pathToPrivateKey = pathToPrivateKey;
	}

	protected void setHostName(String hostname) {
	    Pattern period = Pattern.compile("\\.");
		String splitarray[] = period.split(hostname);
		if (splitarray.length != 3) {
			System.err.println("Error: Malformed hostname " + hostname);
			System.exit(1);
		}
		this.hostname = hostname;
	}

	protected String getHostName(Boolean shortName) {
	    Pattern period = Pattern.compile("\\.");
		String splitarray[] = period.split(hostname);
		if(shortName)
			return(splitarray[0]);
		else
			return(hostname);
	}

	protected String getHostName() {
		return(getHostName(false));
	}

	public String getURL (Boolean addPort) {
		String url = "";
		if(protocol.equalsIgnoreCase("mysql")) {
			protocol = "jdbc:" + protocol;
			if(addPort)
				url = protocol+"://"+hostname+":"+port+directory;
			else
				url = protocol+"://"+hostname+directory;
		}
		else if(protocol.equalsIgnoreCase("http")) {
			if(addPort)
				url = protocol+"://"+hostname+":"+port+directory;
			else
				url = protocol+"://"+hostname+directory;
		}
		else if (protocol.equalsIgnoreCase("rsync")) {
			url = userID + "@" + hostname + ":" + directory;
		}

		return(url);
	}

	public String getURL () {
		return(getURL(true));
	}

	public void setUserID(String userId) {
		this.userID = userId;
	}

	public String getUserID() {
		return(userID);
	}

	protected void setPassword(String password) {
		this.password = password;
	}

	protected String getPassword() {
		return(password);
	}

	protected void setPathToPrivateKey(String pathToPrivateKey) {
		this.pathToPrivateKey = pathToPrivateKey;
	}

	protected String getPathToPrivateKey() {
		return(this.pathToPrivateKey);
	}
}
