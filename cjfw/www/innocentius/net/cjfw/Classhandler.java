package innocentius.net.cjfw;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class Classhandler 
{
	LinkedHashMap<String, ArrayList<Map<String, Object>>> first;
	LinkedHashMap<String, ArrayList<Map<String, Object>>> betw;
	public Classhandler()
	{
		first = new LinkedHashMap<String, ArrayList<Map<String, Object>>>();
		betw = new LinkedHashMap<String, ArrayList<Map<String, Object>>>();
	}
	
	public void setfirst(Player a, String name) 
	{
		PlayerInventory s = a.getInventory();
		ItemStack[] t = s.getContents();
		ArrayList<Map<String, Object>> g = new ArrayList<Map<String, Object>>();
		for(ItemStack q : t)
		{
			if(q != null)
			{
			g.add(q.serialize());
			}
		}
		first.put(name, g);
		
	}
	public void setbetween(Player a, String name)
	{
		PlayerInventory s = a.getInventory();
		ItemStack[] t = s.getContents();
		ArrayList<Map<String, Object>> g = new ArrayList<Map<String, Object>>();
		for(ItemStack q : t)
		{
			if(q != null)
			{
			g.add(q.serialize());
			}
		}
		betw.put(name, g);
	}
	public void retrievefirst(String args, String name)
	{
		Player a = Bukkit.getServer().getPlayer(args);
		if(a != null)
		{
		  	if(first.containsKey(name))
		  	{
		  		ItemStack s;
		  		ArrayList<Map<String, Object>> g = first.get(name);
		  		a.getInventory().clear();
		  		for(Map<String, Object> q: g)
		  		{
		  			if(q != null)
		  			{
		  			s = ItemStack.deserialize(q);
		  			a.getInventory().addItem(s);
		  			}
		  		}
		  	}
		}
	}
	public void retrievebetween(String args, String name)
	{
		Player a = Bukkit.getServer().getPlayer(args);
		if(a != null)
		{
		  	if(first.containsKey(name))
		  	{
		  		ItemStack s;
		  		ArrayList<Map<String, Object>> g = betw.get(name);
		  		for(Map<String, Object> q: g)
		  		{
		  			if(q != null)
		  			{
		  			s = ItemStack.deserialize(q);
		  			a.getInventory().addItem(s);
		  			}
		  		}
		  	}
		}
	}
	
}
