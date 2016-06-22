package innocentius.net.cjfw;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class Classhandler 
{
	LinkedHashMap<String, ArrayList<Map<String, Object>>> first;
	LinkedHashMap<String, ArrayList<Map<String, Object>>> betw;
	LinkedHashMap<String, ArrayList<Map<String, Object>>> misstore;
	LinkedHashMap<String, String> player_class;
	LinkedHashMap<String, String> player_class_beforemis;
	public Classhandler()
	{
		first = new LinkedHashMap<String, ArrayList<Map<String, Object>>>();
		betw = new LinkedHashMap<String, ArrayList<Map<String, Object>>>();
		misstore = new LinkedHashMap<String, ArrayList<Map<String, Object>>>();
		player_class = new LinkedHashMap<String, String>();
		player_class_beforemis = new LinkedHashMap<String, String>();
	}
	public void setmis(Player a)
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
		misstore.put(a.getName(), g);
		player_class_beforemis.put(a.getName(), player_class.get(a.getName()));
		retrievefirst(a.getName(), "MIS");
		a.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 2400, 10));
		a.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 2400, 2));
	}
	public void returnmis(Player a)
	{
		if(misstore.containsKey(a.getName()) && CJFWListener.gameworld != null)
		{
			if(CJFWListener.gameworld.equals(a.getWorld()))
			{
			a.getInventory().clear();
			ItemStack s;
			ArrayList<Map<String, Object>> g = misstore.get(a.getName());
			for(Map<String, Object> q: g)
			{
				if(q != null)
				{
					s = ItemStack.deserialize(q);
					a.getInventory().addItem(s);
				}
			}
			}
			misstore.remove(a.getName());
			player_class.put(a.getName(), player_class_beforemis.get(a.getName()));
			player_class_beforemis.remove(a.getName());
		}
		else
		{
			a.getInventory().clear();
		}
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
		a.sendMessage("The Equipment for " + name + "has been set.");
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
		a.sendMessage("The Between item for " + name + "has been set.");
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
		  		a.getInventory().setArmorContents(null);
		  		for(Map<String, Object> q: g)
		  		{
		  			if(q != null)
		  			{
		  			s = ItemStack.deserialize(q);
		  			a.getInventory().addItem(s);
		  			}
		  		}
		  		player_class.put(args, name);
		  	}
		}
	}
	public void retrievebetween(String args, String name)
	{
		Player a = Bukkit.getServer().getPlayer(args);
		if(a != null)
		{
		  	if(betw.containsKey(name) && CJFWListener.gameworld != null)
		  	{
		  		if(a.getWorld().equals(CJFWListener.gameworld))
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
	public void retrievebetweenall()
	{
		for(String name: player_class.keySet())
		{
			retrievebetween(name, player_class.get(name));
		}
	}
	
}
