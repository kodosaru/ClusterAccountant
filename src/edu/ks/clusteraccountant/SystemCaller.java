/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ks.clusteraccountant;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 *
 * @author donj
 */
public class SystemCaller
{
	public static String runCmd(String[] args, Boolean tokenized) {
		String cmd = "" ;
		String output = "";
		Integer len = args.length;
        for(int i=0;i<len;i++) {
            if(args[i] == null) {
                len = i;
                break;
            }
        }
		for(int i = 0;i<len;i++) {
			cmd += args[i];
			if(i < (len - 1))
				cmd += " ";
		}
		Runtime run = Runtime.getRuntime();
		Process pr = null;
		String line;
		try {
			// This option was necessary because the exec command doesn't handle
			// double quotes in normal string properly
			if(tokenized) { 
				switch(len) {
					case 1:	pr = run.exec(new String[] {args[0]});
						break;
					case 2:	pr = run.exec(new String[] {args[0], args[1]});
						break;
					case 3:	pr = run.exec(new String[] {args[0], args[1], args[2]});
						break;
					case 4:	pr = run.exec(new String[] {args[0], args[1], args[2],
						args[3]});
						break;
					case 5:	pr = run.exec(new String[] {args[0], args[1], args[2],
						args[3], args[4]});
						break;
					case 6:	pr = run.exec(new String[] {args[0], args[1], args[2],
						args[3], args[4], args[5]});
						break;
					case 7:	pr = run.exec(new String[] {args[0], args[1], args[2],
						args[3], args[4], args[5], args[6]});
				}
			}
			else
				pr = run.exec(cmd) ;
			pr.waitFor();
			InputStreamReader isro, isre;
			isro = new InputStreamReader(pr.getInputStream());
			isre = new InputStreamReader(pr.getErrorStream());
			BufferedReader buf = new BufferedReader(isro);
			while ( (line = buf.readLine() ) != null ) {
				output += line;
			}
			buf = new BufferedReader(isre);
			while ( (line = buf.readLine() ) != null ) {
				output += line;
			}
}
		catch (Exception e){
			System.err.println(e.getMessage());
		}
		return(output);
	}
}	
