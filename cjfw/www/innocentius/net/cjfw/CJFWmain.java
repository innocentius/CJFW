package www.innocentius.net.cjfw;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
/**
 * This is the main class of Plugin<CJFW>
 * @author Shellingford
 *
 */
public final class CJFWmain extends JavaPlugin 
{
	
	public final CJFWListener game = new CJFWListener();
	public BukkitScheduler scheduler;
	public void onEnable() 
	{
		PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this.game, this);
	}
	
	public void onDisable() 
	{
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, final String[] args)
	{
		//There is only one main command: cjfw
		if(cmd.getName().equals("cjfw"))
		{
			if(args.length == 0)
				// If no argument, display a welcome signal
				// also, return false to get usage
			{
				sender.sendMessage(ChatColor.AQUA+"This is the plugin for a game Desert Base Defence!");
				return false;
			}
			else if(args.length == 1)
			{
				if(args[0].equalsIgnoreCase("init"))
					// call listener to initialize the game
					// The initialize will 100% succeed
				{
					scheduler = Bukkit.getServer().getScheduler();
					scheduler.cancelTasks(this);
					sender.sendMessage(ChatColor.GREEN+"Initializing Desert Base Defend... Standby.");
					game.init();
					sender.sendMessage(ChatColor.GOLD+"Successfully initialized!");
					sender.sendMessage("Total Player : " + Bukkit.getOnlinePlayers().length);
					sender.sendMessage("Base_HP = " + game.counthp(Bukkit.getOnlinePlayers().length));
					return true;
				}
				else if(args[0].equalsIgnoreCase("showhp"))
					// call listener to show the current hitpoint of bases
					// if game not begin, or not initialized
					// the sender will get a error message
				{
					game.showhp(sender);
					return true;
				}
				else if(args[0].equalsIgnoreCase("rank"))
					// show the current score rank of players
					// the top 6 will be shown
					// if game not begin, or not initialized
					// the sender will get a error message
				{
					game.getrank();
					return true;
				}
				else if(args[0].equalsIgnoreCase("reset"))
					// call listener to reset the game
					// The reset will 100% succeed
					// The boolean is also change to false for re-initialize
					// reset can be done while running
					// after reset, the status of game will return to stop
				{
					game.reset();
					scheduler = Bukkit.getServer().getScheduler();
					scheduler.cancelTasks(this);
					sender.sendMessage(ChatColor.YELLOW+"Reset complete! Please re-initialize.");
					return true;
				}
				else if(args[0].equalsIgnoreCase("start"))
				{
					// call listener to start the game
					// If the game is not initialized, or is already started
					// The start action will fail and send an error message to sender
					if(game.start())
					{
						scheduler = Bukkit.getServer().getScheduler();
				        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
				            public void run() {
				                game.checkdamage();
				                game.update(1);
				            }
				        }, 0L, 20L);
						sender.sendMessage(ChatColor.GOLD+"Desert Base Defence is started!");
						sender.sendMessage(ChatColor.GOLD+"Timer is started.");
					}
					else
					{
						sender.sendMessage(ChatColor.BLUE+"The game is already "
								+ "running");
						sender.sendMessage(ChatColor.RED+"Or it has not been properly initialized!");
					}
					return true;
				}
				else if(args[0].equalsIgnoreCase("stop"))
				{
					// call listener to pause the game
					// The game will remain its critical parameters
					// If the game is not running
					// The stop action will fail and send an error message to sender
					if(game.stop())
					{
						scheduler = Bukkit.getServer().getScheduler();
						scheduler.cancelTasks(this);
						sender.sendMessage(ChatColor.YELLOW+"Desert Base Defence is paused");
						sender.sendMessage(ChatColor.GRAY+"All parameters are keeped.");
					}
					else
					{
						sender.sendMessage(ChatColor.RED+"The game is not "
								+ "running!");
					}
					return true;
				}
				else if(args[0].equalsIgnoreCase("showloc"))
					//call listener to show the location of towers
					//don't show if three towers are not initialized.
				{
					game.getbaseloc("BLUE", sender);
					game.getbaseloc("AQUA", sender);
					game.getbaseloc("PURPLE", sender);
					return true;
				}
				else
				{
					return false;
				}
			}
			else if(args.length == 2)
			{
				if(args[0].equalsIgnoreCase("choose"))
					// Is this necessary?
					// Let players choose from 4 careers
					// If the players already chose a career
					// The action will fail and send an error message to sender
				{
					
					return true;
				}
				else if(args[0].equalsIgnoreCase("setwave"))
				{
					game.setwave(args[1]);
					return true;
				}
				else if(args[0].equalsIgnoreCase("settime"))
				{
					game.settime(args[1]);
					return true;
				}
				else if(args[0].equalsIgnoreCase("setdiff"))
				{
					try
					{
					double i = Double.parseDouble(args[1]);
					game.setdifficulty(i);
					return true;
					}
					catch(Exception x)
					{
						return false;
					}
				}
				else if(args[0].equalsIgnoreCase("setbase"))
					// Call Listener to set the base location for the game
					// The location can be reset
					// The action will cause exceptions
					// So it is surrounded with try catch
				{
					try
					{
						if(Integer.parseInt(args[1]) > 3 || Integer.parseInt(args[1]) <= 0)
						{
							sender.sendMessage("Please enter the correct parameter!");
							return false;
						}
						else
						{
							game.setbaselocation(Integer.parseInt(args[1]), game.getloc(sender.getName()));
							sender.sendMessage("Set Location for tower" + Integer.parseInt(args[1]));
						}
					}
					catch(NumberFormatException nfe)
					{
						sender.sendMessage("Please enter an integer for tower number!");
						return false;
					}
					return true;
				}
				
				else
				{
					return false;
				}
			}
			else if (args.length == 3)
			{
				 if(args[0].equalsIgnoreCase("heal"))
					//Call listener to heal the hit point of one of the bases for a portion (5%)
					//args[1] is the number of base
					//This method is called by players hitting heal button
					//This method won't fail unless the game is not started or initialized
					//The final hit point of base will not exceed its original one
					//If player is out of usage of this method, it will give back an error message
				{
					if(game.heal(args[1], args[2]))
					{
						Bukkit.broadcastMessage(ChatColor.GREEN + args[2] + " 治疗了 " + args[1] + " !");
					}
					else
					{
						Bukkit.getPlayer(args[2]).sendMessage(ChatColor.DARK_RED+"你已经没有使用次数或者游戏尚未开始！");
					}
					return true;
				}
				else if(args[0].equalsIgnoreCase("fortify"))
					//Call listener to fortify the base for a period of time(60 ticks now)
					//args[1] is the number of the base
					//This method is called by players hitting the fortify button
					//This method won't fail unless the game is not started or initialized
					//If player is out of usage of this method, it will give back an error message
				{
					if(game.fortify(args[1], args[2]))
					{
				    Bukkit.broadcastMessage(args[2] + " 开启了 " + args[1] + " 的防护！");
					scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleSyncDelayedTask(this, new Runnable() 
					{
						public void run() 
						{
							game.defortify(args[1]);
							
						}
					}, 60L);
					}
					else
					{
						Bukkit.getPlayer(args[2]).sendMessage(ChatColor.DARK_RED+"你已经没有使用次数或者游戏尚未开始！");
					}
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
		
	}
}