package innocentius.net.cjfw;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;
/**
 * This is the main class of Plugin<CJFW>
 * @author Shellingford
 *
 */
public final class CJFWmain extends JavaPlugin 
{
	public static int hinaruscheduler;
	public static int mainscheduler;
	public static int luthurscheduler;
	public final CJFWListener game = new CJFWListener();
	public static Classhandler innoclass;
	public BukkitScheduler scheduler;
	public void onEnable() 
	{
		innoclass = new Classhandler();
		PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this.game, this);
	}
	
	public void onDisable() 
	{
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, final String[] args)
	{
		//class command
		//There is one main command: cjfw
		if(cmd.getName().equals("cjfw"))
		{
			if(args.length == 0)
				// If no argument, display a welcome signal
				// also, return false to get usage
			{
				sender.sendMessage(ChatColor.AQUA+"This is the plugin for a game Desert Base Defense!");
				return false;
			}
			else if(args.length == 1 && !args[0].equalsIgnoreCase("innoclass"))
			{
				if(args[0].equalsIgnoreCase("test"))
				{
					Player a = sender.getServer().getPlayer(sender.getName());
					//org.bukkit.inventory.ItemStack weapon = new org.bukkit.inventory.ItemStack(Material.DIAMOND_SWORD,1);
					//weapon.addUnsafeEnchantment(org.bukkit.enchantments.Enchantment.DURABILITY, 10);
					//a.getInventory().addItem(weapon);
					a.playSound(a.getLocation(), "emergency_test", 1000 , 1);
					return true;
				}
				
				if(args[0].equalsIgnoreCase("init"))
					// call listener to initialize the game
					// The initialize will 100% succeed
				{
					scheduler = Bukkit.getServer().getScheduler();
					scheduler.cancelTasks(this);
					sender.sendMessage(ChatColor.GREEN+"Initializing Desert Base Defend... Standby.");
					game.init();
					sender.sendMessage(ChatColor.GOLD+"Successfully initialized!");
					sender.sendMessage("Total Player : " + Bukkit.getOnlinePlayers().size());
					sender.sendMessage("Base_HP = " + game.counthp(Bukkit.getOnlinePlayers().size()));
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
				        scheduler.scheduleAsyncRepeatingTask(this, new Runnable() {
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
					game.getbaseloc("GREEN", sender);
					return true;
				}
				else
				{
					return false;
				}
			}
			else if(args.length == 2 && !args[0].equalsIgnoreCase("innoclass"))
			{
				if(args[0].equalsIgnoreCase("addmis"))
				{
					scheduler = Bukkit.getServer().getScheduler();
					scheduler.scheduleAsyncDelayedTask(this, new Runnable() {
			            public void run() {
			                innoclass.returnmis(Bukkit.getServer().getPlayer(args[1]));
			                //TODO add check if in same world
			            }
			        }, 2400L);
			   
				}
				if(args[0].equalsIgnoreCase("setborder"))
				{
				  try
				  {
					if(args[1].equalsIgnoreCase("1"))
					{
						game.setborder(sender.getServer().getPlayer(sender.getName()).getLocation(), 1);
					}
					else if(args[2].equalsIgnoreCase("2"))
					{
						game.setborder(sender.getServer().getPlayer(sender.getName()).getLocation(), 2);
					}
					else
					{
						sender.sendMessage(ChatColor.RED+"We only need two points for a border... right?");
					}
				  }
				  catch (Exception e)
				  {
					  sender.sendMessage(ChatColor.RED+"This command is player only.");
				  }
				}
				else if(args[0].equalsIgnoreCase("testmusic"))
				{
					if(args[1] != null)
					{
						Player a = sender.getServer().getPlayer(sender.getName());
						if(args[1].equalsIgnoreCase("1"))
						{
							a.playSound(a.getLocation(), "wave_10_1", 1000 , 1);
						}
						else
						{
							a.playSound(a.getLocation(), "wave_10_2", 1000 , 1);
						}
					}
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
						//v2.0 note: change tower total to four
						if(Integer.parseInt(args[1]) > 4 || Integer.parseInt(args[1]) <= 0)
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
			else if (args.length == 3 && !args[0].equalsIgnoreCase("innoclass"))
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
		}
		if(args.length > 0 && args[0].equalsIgnoreCase("innoclass"))
		{
			if(args.length == 1)
			{
				//cjfw innoclass
				//display help message
				sender.sendMessage("/cjfw innoclass for this help page.");
				sender.sendMessage("/cjfw innoclass setfirst <classname>");
				sender.sendMessage("/cjfw innoclass setbetw <classname>");
				sender.sendMessage("/cjfw innoclass givefirst [classname] [playername]");
				sender.sendMessage("/cjfw innoclass givebetw [classname]  [playername]");
			}
			else if(args.length == 3)
			{
				if(args[1].equalsIgnoreCase("setfirst"))
				{
					innoclass.setfirst(sender.getServer().getPlayer(sender.getName()), args[2]);
				}
				else if(args[1].equalsIgnoreCase("setbetw"))
				{
					innoclass.setbetween(sender.getServer().getPlayer(sender.getName()), args[2]);
				}
			}
			else if(args.length == 4)
			{
				if(args[1].equalsIgnoreCase("givefirst"))
				{
					innoclass.retrievefirst(args[3], args[2]);
				}
				else if(args[1].equalsIgnoreCase("givebetw"))
				{
					innoclass.retrievebetween(args[3], args[2]);
				}
			}
			return true;
		}
		if(args.length > 0)
		{
		if(args.length == 5 && args[0].equalsIgnoreCase("jump"))
		{
			//cjfw jump <playername> <x amount> <y amount> <z amount>
			//use only in command block!
			Vector a = new Vector();
			try
			{
			a.setX(Double.parseDouble(args[2]));
			a.setY(Double.parseDouble(args[3]));
			a.setZ(Double.parseDouble(args[4]));
			}
			catch(Exception e)
			{
				return false;
			}
			Jumpboard.Jump(Bukkit.getServer().getPlayer(args[1]), a);
			//then give player 100? tick of invulnerable time.
			return true;
		}
		if(args.length == 5 && args[0].equalsIgnoreCase("spawnboss"))
		{
			//cjfw spawnboss <name> <x amount> <y amount> <z amount>
			if(args[1].equalsIgnoreCase("hinaru"))
			{
			Location a = new Location(sender.getServer().getPlayer(sender.getName()).getWorld(),Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
			final HinaruHandler hh = new HinaruHandler(a);
			scheduler = Bukkit.getServer().getScheduler();
			hinaruscheduler = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
	            public void run() {
	                hh.update();
	            }
	        }, 0L, 20L);
			return true;
			}
			if(args[1].equalsIgnoreCase("luthur"))
			{
				Location a = new Location(sender.getServer().getPlayer(sender.getName()).getWorld(),Double.parseDouble(args[2]), Double.parseDouble(args[3]), Double.parseDouble(args[4]));
				final LuthurHandler lh = new LuthurHandler(a);
				scheduler = Bukkit.getServer().getScheduler();
				luthurscheduler = scheduler.scheduleSyncRepeatingTask(this, new Runnable() {
		            public void run() {
		                lh.update();
		            }
		        }, 0L, 20L);
				return true;
			}
		}
		if(args.length == 6 && args[0].equalsIgnoreCase("spawnbossconsole"))
		{
			//cjfw spawnbossconsole <world> <bossname> <x amount> <y amount> <z amount>
			Location a = new Location(sender.getServer().getWorld(args[1]), Double.parseDouble(args[3]), Double.parseDouble(args[4]), Double.parseDouble(args[5]));
		}
		}
			return false;		
	}
}