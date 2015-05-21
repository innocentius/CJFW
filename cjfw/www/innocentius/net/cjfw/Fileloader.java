package innocentius.net.cjfw;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Fileloader {
	/**
	 * only load one file into the directory.
	 */
	public LinkedHashMap<WaveTime, String> serversayevent;
	public LinkedHashMap<WaveTime, String> spawnmobevent;
	public LinkedHashMap<WaveTime, String> backgroundmusicevent;
	public ArrayList<WaveTime> showrankeventlist;
	/**
	 * Load file into the system, current file directory is "CJFWdata.txt";
	 */
	public Fileloader()
	{
      serversayevent = new LinkedHashMap<WaveTime, String>();
      spawnmobevent = new LinkedHashMap<WaveTime, String>();
	  backgroundmusicevent = new LinkedHashMap<WaveTime, String>();
	  showrankeventlist = new ArrayList<WaveTime>();
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
		 try(BufferedReader br = new BufferedReader(new FileReader(path))) 
		 {
		    
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
		            	 serversayevent.put(temp, sb.toString());
		            	 return;
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
		            	 spawnmobevent.put(temp, sb.toString());
		            	 return;
		            	//BGM play
		         		//<wave number> <wave remaining time> BGM <BGMname(with path, recognize space)>
		             case "BGM":
		            	 sb = new StringBuilder();
		            	 for(int i = 3; i < sep.length; i++)
		            	 {
		            		 sb.append(sep[i] + " ");//add space inorder to replace the parsed space
		            	 }
		            	 backgroundmusicevent.put(temp, sb.toString());
		            	 return;
		            	//push player rank
		         		//<wave number> <wave remaining time> rank
		             case "rank":
		            	 showrankeventlist.add(temp);
		           }
		         line = br.readLine();
		      }
		      br.close();
		 }
		 catch(Exception E)
		 {
			 System.out.println("PANIC: Input File Error, Abort Loading.");
			 success = false;
		 }
		
		//If success
		if(success)
		{
			System.out.println("File Read Success.");
		}		
	}
}
