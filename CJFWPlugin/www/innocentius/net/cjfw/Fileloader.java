package innocentius.net.cjfw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Fileloader {
	/**
	 * only load one file into the directory.
	 */
	public LinkedHashMap<String, String> serversayevent;
	public LinkedHashMap<String, String> spawnmobevent;
	public ArrayList<String> showrankeventlist;
	/**
	 * Load file into the system, current file directory is "CJFWdata.txt";
	 */
	public Fileloader()
	{
      serversayevent = new LinkedHashMap<String, String>();
      spawnmobevent = new LinkedHashMap<String, String>();
	  showrankeventlist = new ArrayList<String>();
	  readfile("CJFWdata.txt");
	}
	/**
	 * 
	 * @param path
	 */
	private void readfile(String path) 
	{
		WaveTime temp;
		Boolean success = true;
		StringBuilder sb = new StringBuilder();
		 try 
		 {
			 BufferedReader br = new BufferedReader(new FileReader(path));
		    String line = br.readLine();

		      while (line != null) 
		      {
		         String[] sep =  line.split(" ");
		         //Remember the time of event
	             temp = new WaveTime(Integer.parseInt(sep[0]), Integer.parseInt(sep[1]));
		           switch(sep[2])
		           {
		                //server report
		   		        //usage: <wave number> <wave remaining time> say <report string(space recognize)>
		             case "say":
		            	 sb = new StringBuilder();
		            	 for(int i = 3; i < sep.length; i++)
		            	 {
		            		 sb.append(sep[i] + " ");//add space inorder to replace the parsed space
		            	 }
		            	 serversayevent.put(temp.toString(), sb.toString());
		            	 System.out.println(temp.toString());
		            	 System.out.println(sb.toString());
		            	 break;
		            	//spawn creature
		         		//usage: <wave number> <wave remaining time> spawn <creature type> <creature amount modifier> <creature weapon rank>
		            	//<creature armor rank> <creature name> 
		            	//eg: 8 30 Zombie 0.3 10 20 aaa
		            	//OR: <wave number> <wave remaining time> spawn BOSS <creature weapon rank> <creature armor rank> <creature name>
		             case "spawn":
		            	 sb = new StringBuilder();
		            	 for(int i = 3; i < sep.length; i++)
		            	 {
		            		 sb.append(sep[i] + " ");//add space inorder to replace the parsed space
		            	 }
		            	 System.out.println(temp.toString());
		            	 spawnmobevent.put(temp.toString(), sb.toString());
		            	 System.out.println(sb.toString());
		            	 break;
		            	//BGM play
		         		//<wave number> <wave remaining time> BGM <BGMname(with path, recognize space)>
		            	//push player rank
		         		//<wave number> <wave remaining time> rank
		             case "rank":
		            	 showrankeventlist.add(temp.toString());
		            	 break;
		           }
		         line = br.readLine();
		      }
		      br.close();
		 }
		 catch(Exception E)
		 {
			 System.out.println("PANIC: Input File Error, Abort Loading.");
			 File a = new File("CJFWdata.txt");
			 if(!a.exists())
			 {
				 try {
					a.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 success = false;
		 }
		
		//If success
		if(success)
		{
			System.out.println("File Read Success.");
		}		
	}
}
