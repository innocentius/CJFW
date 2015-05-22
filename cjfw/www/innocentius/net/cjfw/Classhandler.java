package innocentius.net.cjfw;

import java.util.LinkedHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class Classhandler 
{
	LinkedHashMap<String, ItemStack[]> first;
	LinkedHashMap<String, ItemStack[]> betw;
	public Classhandler()
	{
		first = new LinkedHashMap<String, ItemStack[]>();
		betw = new LinkedHashMap<String, ItemStack[]>();
	}
	
	public void setfirst(Player a, String name)
	{
		PlayerInventory s = a.getInventory();
		ItemStack[] y = s.getContents().clone();
		first.put(name, y);
	}
	public void setbetween(Player a, String name)
	{
		PlayerInventory s = a.getInventory();
		ItemStack[] y = s.getContents().clone();
		betw.put(name, y);
	}
	public void retrievefirst(String args, String name)
	{
		Player a = Bukkit.getServer().getPlayer(args);
		if(a != null)
		{
		  if(first.containsKey(name))
		  {
		  	//a.getInventory().clear();
			a.getInventory().setContents(first.get(name));
		  }
		  else
		  {
			a.sendMessage(ChatColor.RED + "The Class " + name + " does not exist!");
		  }
		}
	}
	public void retrievebetween(String args, String name)
	{
		Player a = Bukkit.getServer().getPlayer(args);
		if(a != null)
		{
		  if(betw.containsKey(name))
		  {
			a.getInventory().addItem(betw.get(name));
		  }
		  else
		  {
			a.sendMessage(ChatColor.RED + "The Class " + name + " does not exist!");
		  }
		}
	}
	
}
