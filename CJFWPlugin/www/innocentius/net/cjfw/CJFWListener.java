package innocentius.net.cjfw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Jukebox;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftZombie;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftGiant;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftSkeleton;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPigZombie;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Wither;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Scoreboard;

/**
 * The class is for ALL use of game<CJFW>
 * The class will handle everything happens in the game, include:
 *     The initialize of game
 *     The state of game
 *     INVALID_The calculation of player score during game
 *     The calculation of damage to base during game
 *     The calculation of player action to base
 *     
 * @author Michael_Innocentius_Shellingford
 *
 */

@SuppressWarnings("deprecation")
public class CJFWListener implements Listener
{
	public static World gameworld;
	boolean on;
	boolean init;
	Random random;
	CreatureHandler ch;
	int wave;
	int player_count;
	int wave_time;
	int bonus_time;
	int back_timer;
	public int lumen_count;

	boolean finish;
	boolean wave9_switch;
	boolean bonus_switch;
	boolean bonus_finish_switch;
	boolean last_boss_phase1;
	boolean last_boss_phase2;
	boolean last_boss_phase3;
	double difficulty;
	double attack_modifier;
	double hp_modifier;
	Fileloader gameevent;
	Wither final_boss;
	LivingEntity last_boss;
	Zombie last_boss1;
	Zombie last_boss2;
	Zombie last_boss3;
	//LivingEntity tp; //debug use
	Scoreboard sb;
	UUID x;
	Location bord1;
	Location bord2;
	Map<String, Location> baselist = new HashMap<String, Location>();
	Map<String, Integer> Base_HP = new HashMap<String, Integer>();
	Map<String, Boolean> Base_damageable = new HashMap<String, Boolean>();
	Map<String, Integer> Player_score = new HashMap<String, Integer>();
	Map<String, Integer> Player_use_heal = new HashMap<String, Integer>();
	Map<String, Integer> Player_use_fort = new HashMap<String, Integer>();
	Map<String, Villager> Base_defender = new HashMap<String, Villager>();
	Map<String, Location> spawn_loc = new HashMap<String, Location>();
	/**
	 * Create a new CJFWListener object.
	 * Contains:
	 *   on - the switch of game, false for default
	 *   random - the random seed for game use
	 *   wave - the stage of game
	 *   player - the total player of game
	 */
	public CJFWListener ()
	
	{
		 ch = new CreatureHandler();
		 on = false;
		 init = false;
		 random = new Random();
		 wave = -1;
		 player_count = 0;
		 wave_time = 0;
		 finish = false;
		 difficulty = 1;
	}
	/**
	 * reset the game.
	 */
	public void reset()
	{
		on = false;
		init = false;
		baselist.clear();
		Base_HP.clear();
		Base_damageable.clear();
		Player_score.clear();
		Player_use_heal.clear();
		Player_use_fort.clear();
		wave9_switch = false;
		bonus_switch = false;
		bonus_finish_switch = false;
		last_boss_phase1 = false;
		last_boss_phase2 = false;
		last_boss_phase3 = false;
		last_boss = null;
		last_boss1 = null;
		last_boss2 = null;
		last_boss3 = null;
		final_boss = null;
		bord2 = null;
		bord1 = null;
		back_timer = 0;
		player_count = 0;
		bonus_time = 0;
		finish = false;
		wave = -1;
		wave_time = 0;
		difficulty = 1;
		try
		{
			sb.getObjective("cjfw").unregister();
			sb.getObjective("contri_point").unregister();
		}
		catch(Exception x)
		{
			
		}
	}
	/**
	 * initialize the game.
	 * initialize also calls reset(), making sure it was properly reset
	 * @param back_timer 
	 */
	public void init() 
	{
		reset();
		
		wave = -1;
		wave_time = -1;
		finish = false;
		wave9_switch = false;
		bonus_switch = false;
		bonus_finish_switch = false;
		sb = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
		int i = Bukkit.getOnlinePlayers().size();
		player_count = i;
		last_boss_phase1 = false;
		last_boss_phase2 = false;
		last_boss_phase3 = false;
		difficulty = 1;
		bonus_time = 0;
		lumen_count = 0;
		back_timer = 0;
		hp_modifier = 1;
		attack_modifier = 1;
		int hpfull = counthp(player_count);
		Base_HP.put("BLUE", hpfull);
		Base_HP.put("AQUA", hpfull);
		Base_HP.put("PURPLE", hpfull);
		Base_HP.put("GREEN", hpfull);
		Base_damageable.put("BLUE", true);
		Base_damageable.put("AQUA", true);
		Base_damageable.put("PURPLE", true);
		Base_damageable.put("GREEN", true);
		gameevent = new Fileloader();
		try
		{
			sb.registerNewObjective("cjfw", "dummy");
			
			sb.registerNewObjective("contri_point", "totalKillCount");
		}
		catch(Exception x)
		{
			
		}
		sb.getObjective("cjfw").setDisplayName("GameStat");
		sb.getObjective("cjfw").setDisplaySlot(DisplaySlot.SIDEBAR);
		sb.getObjective("contri_point").setDisplayName("Kill");
		sb.getObjective("contri_point").setDisplaySlot(DisplaySlot.BELOW_NAME);
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set Wave cjfw " + wave);
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set Time cjfw " + wave_time);
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set BLUE cjfw " + Base_HP.get("BLUE"));
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set AQUA cjfw " + Base_HP.get("AQUA"));
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set PURPLE cjfw " + Base_HP.get("PURPLE"));
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set GREEN cjfw " + Base_HP.get("GREEN"));
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set Crystal cjfw " + lumen_count);
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard objectives setdisplay sidebar cjfw");
		init = true;
	}
	/**
	 * start the game.
	 * @return true if game is successfully started, false if not
	 */
	public boolean start()
	{
		if(on)
		{
			return false;
		}
		else
		{
			if(!init)
			{
				Bukkit.getConsoleSender().sendMessage("Game start without init, may flaw");
				return false;
			}
			if(baselist.get("BLUE") == null || baselist.get("AQUA") == null ||
						baselist.get("PURPLE") == null || baselist.get("GREEN") == null)
			{
				return false;
			}
			else
			{
				try
				{
					if(!calcborder(bord1, bord2))
					{
						return false;
					}
				}
				catch(Exception a)
				{
					return false;
				}
			on = true;
			baselist.get("BLUE").getWorld().setStorm(false);
			baselist.get("BLUE").getWorld().setThundering(false);
			baselist.get("BLUE").getWorld().setWeatherDuration(1000000);
			spawn_loc.put("1", new Location(baselist.get("BLUE").getWorld(), baselist.get("BLUE").getX() + 40, baselist.get("BLUE").getY(),
					baselist.get("BLUE").getZ()));
			spawn_loc.put("2", new Location(baselist.get("BLUE").getWorld(), baselist.get("BLUE").getX(), baselist.get("BLUE").getY(),
					baselist.get("BLUE").getZ() + 40));
			spawn_loc.put("3", new Location(baselist.get("AQUA").getWorld(), baselist.get("AQUA").getX() - 40, baselist.get("AQUA").getY(),
					baselist.get("AQUA").getZ()));
			spawn_loc.put("4", new Location(baselist.get("AQUA").getWorld(), baselist.get("AQUA").getX(), baselist.get("AQUA").getY(),
					baselist.get("AQUA").getZ() + 40));
			spawn_loc.put("5", new Location(baselist.get("PURPLE").getWorld(), baselist.get("PURPLE").getX() - 40, baselist.get("PURPLE").getY(),
					baselist.get("PURPLE").getZ()));
			spawn_loc.put("6", new Location(baselist.get("PURPLE").getWorld(), baselist.get("PURPLE").getX(), baselist.get("PURPLE").getY(),
					baselist.get("PURPLE").getZ() - 40));
			spawn_loc.put("7", new Location(baselist.get("GREEN").getWorld(), baselist.get("GREEN").getX() + 40, baselist.get("GREEN").getY(),
					baselist.get("GREEN").getZ()));
			spawn_loc.put("8", new Location(baselist.get("GREEN").getWorld(), baselist.get("GREEN").getX(), baselist.get("GREEN").getY(),
					baselist.get("GREEN").getZ() - 40));
			return true;
			}
		}
	}
	private boolean calcborder(Location bord12, Location bord22) {
		if(bord12.getWorld().getName().equals(bord22.getWorld().getName()))
		{
				if(bord12.getX() > bord22.getX())
				{
					double tX = bord12.getX();
					bord12.setX(bord22.getX());
					bord22.setX(tX);
				}
				if(bord12.getY() > bord22.getY())
				{
					double tY = bord12.getY();
					bord12.setY(bord22.getY());
					bord22.setY(tY);
				}
				if(bord12.getZ() > bord22.getZ())
				{
					double tZ = bord12.getZ();
					bord12.setZ(bord22.getZ());
					bord22.setZ(tZ);
				}
				bord1 = bord12;
				bord2 = bord22;
				return true;
				
		}
		else
		{
			return false;
		}
	}
	/**
	 * pause the game.
	 * @return true if successfully stopped, false if not
	 */
	public boolean stop()
	{
		if(!on)
		{
			return false;
		}
		else
		{
			on = false;
			return true;
		}
	}
	/**
	 * the method to modify the hit point of base
	 * @param basename - the name of base which will be modified
	 * @param amount - the amount which hitpoint will be modified, + or -
	 */
	private void HPModify(String basename, int amount)
	{
		if(Base_HP.containsKey(basename))
		{
			if(Base_HP.get(basename) != 0)
			{
				Base_HP.put(basename, Base_HP.get(basename) + amount);
				if(Base_HP.get(basename) < 0)
				{
					Base_HP.put(basename, 0);
				}
				else if(Base_HP.get(basename) > counthp(player_count))
				{
					Base_HP.put(basename, counthp(player_count));
				}
			}
		}
	}
	/**
	 * Count the base hp for bases based on the base player count and basic counting method.
	 * 
	 * @param i the players online
	 * @return the number of base hp of base
	 */
	public int counthp(int i) 
	{
		int hp = 0;
		if(i > 20)
		{
			hp = 4000 + (i - 20) * 150;
		}
		else if(i > 10 && i <= 20)
		{
			hp = 2000 + (i - 10) * 200;
		}
		else 
		{
			hp = 2000;
		}
		return hp;
	}
	/**
	 * get location of the player calling this method.
	 * WARNING!: THIS METHOD WILL NOT WORK ON CONSOLE!
	 * @param name the name of player called this method.
	 * @return the location of the player called this method.
	 */
	public Location getloc(String name) 
	{
		
		return Bukkit.getPlayer(name).getLocation();
	}
	/**
	 * set the location for three bases
	 * @param basenum - the number represent base
	 * @param i - location of base
	 */
	public void setbaselocation(int basenum, Location i) 
	{
		String basekey;
		LivingEntity e;
		if(!on)
			{
			if(basenum == 1)
			{
				gameworld = i.getWorld();
				basekey = "BLUE";
				baselist.put(basekey, i);
				e = ch.summonCreatures(baselist, "BLUE", CreatureType.VILLAGER, ChatColor.GOLD+"蓝塔守护者", i, false);
				ch.sethealth(e, 1000);
				e.setNoDamageTicks(999999999);
				if(!Base_defender.containsKey(basekey))
				{
					Base_defender.put(basekey, (Villager)e);
				}
				else
				{
					Base_defender.get(basekey).remove();
					Base_defender.remove(basekey);
					Base_defender.put(basekey, (Villager)e);
				}
			}
			else if(basenum == 2)
			{
				basekey = "AQUA";
				baselist.put(basekey, i);
				e = ch.summonCreatures(baselist, "AQUA", CreatureType.VILLAGER, ChatColor.GOLD+"青塔守护者", i, false);
				ch.sethealth(e, 1000);
				e.setNoDamageTicks(999999999);
				if(!Base_defender.containsKey(basekey))
				{
					Base_defender.put(basekey, (Villager)e);
				}
				else
				{
					Base_defender.get(basekey).remove();
					Base_defender.remove(basekey);
					Base_defender.put(basekey, (Villager)e);
				}
			}
			else if(basenum == 3)
			{
				basekey = "PURPLE";
				baselist.put(basekey, i);
				e = ch.summonCreatures(baselist, "PURPLE", CreatureType.VILLAGER, ChatColor.GOLD+"紫塔守护者", i, false);
				ch.sethealth(e, 1000);
				e.setNoDamageTicks(999999999);
				if(!Base_defender.containsKey(basekey))
				{
					Base_defender.put(basekey, (Villager)e);
				}
				else
				{
					Base_defender.get(basekey).remove();
					Base_defender.remove(basekey);
					Base_defender.put(basekey, (Villager)e);
				}
			}
			else if(basenum == 4)
			{
				basekey = "GREEN";
				baselist.put(basekey, i);
				e = ch.summonCreatures(baselist, "GREEN", CreatureType.VILLAGER, ChatColor.GOLD+"绿塔守护者", i, false);
				ch.sethealth(e, 1000);
				e.setNoDamageTicks(999999999);
				if(!Base_defender.containsKey(basekey))
				{
					Base_defender.put(basekey, (Villager)e);
				}
				else
				{
					Base_defender.get(basekey).remove();
					Base_defender.remove(basekey);
					Base_defender.put(basekey, (Villager)e);
				}
			}
			else
			{
				basekey = "FOO";
			}		
		}
	}
	/**
	 * Calculate Lumen Crystal amount, add some to random location in the area.
	 * 
	 */
	public void droplumencrystal()
	{
		if(on && !finish)
		{
			try
			{
				int count = 0;
				for(int i = bord1.getBlockX(); i <= bord2.getBlockX(); i= i + 16)
				{
					for(int j = bord1.getBlockY(); j <= bord2.getBlockY(); j = j + 16)
					{
						for(int z = bord1.getBlockZ(); z <= bord2.getBlockZ(); z = z + 16)
						{
							Location temp = new Location(bord1.getWorld(),i, j, z);
							for(Entity e :temp.getBlock().getChunk().getEntities())
							{
								if(e instanceof Item)
								{
									try
									{
									if(((Item)e).getItemStack().getItemMeta().getDisplayName().contains("Lumen Crystal"))
										{
											count = count + ((Item)e).getItemStack().getAmount();
										}
									}
									catch(Exception w)
									{
										
									}
								}
							
							}
						}
					}
				}
				//System.out.println(count);
				if(count < 100)
				{
					for(int i = count; i < 60; i++)
					{
						int tempx = random.nextInt(bord2.getBlockX() - bord1.getBlockX()) + bord1.getBlockX();
						int tempy = random.nextInt(bord2.getBlockY() - bord1.getBlockY()) + bord1.getBlockY();
						int tempz = random.nextInt(bord2.getBlockZ() - bord1.getBlockZ()) + bord1.getBlockZ();
						Location temploc = new Location(bord1.getWorld(), tempx, tempy, tempz);
						if(temploc.getBlock().isEmpty())
						{
							ItemStack crystal = new ItemStack(399);
							ItemMeta crysmeta = crystal.getItemMeta();
							crysmeta.setDisplayName("Lumen Crystal");
							crystal.setAmount(1);
							crystal.setItemMeta(crysmeta);
							temploc.getWorld().dropItem(temploc, crystal);
						}
					}
				}
			}
			catch(Exception E)
			{
				System.out.println("PANIC: "+ E.getMessage());
			}
		}
	}
	/**
	 * The sender will receive the current hp of bases.
	 * If not on or init, this method will send error messages.
	 * @param sender - the sender who called the method.
	 */
	public void showhp(CommandSender sender) 
	{
		if(!on)
		{
			sender.sendMessage(ChatColor.RED+"The game is not "
								+ "running!");
		}
		else if(!init)
		{
			sender.sendMessage(ChatColor.RED+"The game is not initialized!");
		}
		else
		{
			sender.sendMessage("BLUE: " + Base_HP.get("BLUE").toString());
			sender.sendMessage("AUQA: " + Base_HP.get("AQUA").toString());
			sender.sendMessage("PURPLE :" + Base_HP.get("PURPLE").toString());
			sender.sendMessage("GREEN" + Base_HP.get("GREEN").toString());
		}
	}
	/**
	 * Goes when a Ender Dragon dies
	 * To prevent the unknown bug caused by the death of dragon
	 * Remove it from game when detect its death.
	 * @param kill
	 */
	@EventHandler
	public void onDragonDeath(EntityDeathEvent kill)
	{
		if(on)
		{
			if(kill.getEntity() instanceof EnderDragon)
			{
				kill.getEntity().remove();
			}
		}
	}
	@EventHandler
	public void onPlayerEscape(PlayerChangedWorldEvent a)
	{
		if(on)
		{
			Player qu = a.getPlayer();
			if(CJFWmain.innoclass.misstore.containsKey(qu.getName()))
				{
					if(qu.getWorld().equals(gameworld))
					{
						qu.getInventory().clear();
						ItemStack s;
						ArrayList<Map<String, Object>> g = CJFWmain.innoclass.misstore.get(qu.getName());
						for(Map<String, Object> q: g)
						{
							if(q != null)
							{
								s = ItemStack.deserialize(q);
								qu.getInventory().addItem(s);
							}
						}
						CJFWmain.innoclass.misstore.remove(qu.getName());
						CJFWmain.innoclass.player_class.put(qu.getName(), CJFWmain.innoclass.player_class_beforemis.get(qu.getName()));
						CJFWmain.innoclass.player_class_beforemis.remove(qu.getName());
					}
				}
		}
	}
	@EventHandler
	public void onPlayerquit(PlayerQuitEvent a)
	{
		if(on)
		{
			Player qu = a.getPlayer();
			if(CJFWmain.innoclass.misstore.containsKey(qu.getName()))
				{
					if(qu.getWorld().equals(gameworld))
					{
						qu.getInventory().clear();
						ItemStack s;
						ArrayList<Map<String, Object>> g = CJFWmain.innoclass.misstore.get(qu.getName());
						for(Map<String, Object> q: g)
						{
							if(q != null)
							{
								s = ItemStack.deserialize(q);
								qu.getInventory().addItem(s);
							}
						}
						CJFWmain.innoclass.misstore.remove(qu.getName());
						CJFWmain.innoclass.player_class.put(qu.getName(), CJFWmain.innoclass.player_class_beforemis.get(qu.getName()));
						CJFWmain.innoclass.player_class_beforemis.remove(qu.getName());
					}
				}
		}
	}
	/**
	 * Goes of when explode.
	 * It checks if the explode is caused by creeper.
	 * It then checks the distance between the explode and tower
	 * if < 26 distance, the tower will be damaged
	 * @param boom - the event of boom.
	 */
	@EventHandler
	public void onCreeperBomb(EntityExplodeEvent boom)
	{
		if(on)
		{
			if(boom.getEntity() instanceof Creeper)
			{
				if(baselist.get("BLUE") != null && baselist.get("AQUA") != null &&
						baselist.get("PURPLE") != null && baselist.get("GREEN") != null)
				{
					Location a = boom.getLocation();
					if(boom.getLocation().getWorld() == baselist.get("BLUE").getWorld()
							&&boom.getLocation().getWorld() == baselist.get("AQUA").getWorld()
							&&boom.getLocation().getWorld() == baselist.get("PURPLE").getWorld()
							&&boom.getLocation().getWorld() == baselist.get("GREEN").getWorld())
					{
						if(Math.pow((a.getX() - baselist.get("AQUA").getX()), 2) + Math.pow((a.getZ() - baselist.get("AQUA").getZ()), 2) < 676 
								&& Base_damageable.get("AQUA"))
						{
							HPModify("AQUA", -100);
						}
						if(Math.pow((a.getX() - baselist.get("BLUE").getX()), 2) + Math.pow((a.getZ() - baselist.get("BLUE").getZ()), 2) < 676
								&& Base_damageable.get("BLUE"))
						{
							HPModify("BLUE", -100);
						}
						
						if(Math.pow((a.getX() - baselist.get("PURPLE").getX()), 2) + Math.pow((a.getZ() - baselist.get("PURPLE").getZ()), 2) < 676
								&& Base_damageable.get("PURPLE"))
						{
							HPModify("PURPLE", -100);
						}
						if(Math.pow((a.getX() - baselist.get("GREEN").getX()), 2) + Math.pow((a.getZ() - baselist.get("GREEN").getZ()), 2) < 676
								&& Base_damageable.get("GREEN"))
						{
							HPModify("GREEN", -100);
						}
					}
				}
			}
			
		
		}
	}
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent a)
	{
        if(on)
		{
			if(a.getDamager() instanceof Player)
			{
				Player dam = (Player)a.getDamager();
				try
				{
				if(dam.getItemInHand() != null)
				{
				if(dam.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.RED+"小型狂战斧"))
				{
					for(Entity e : getNearbyEntities(a.getEntity().getLocation(), 3))
					{
						if(e instanceof Monster)
						{
							((Monster) e).damage(3);
						}
					}
					return;
				}
				else if(dam.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.DARK_RED+"狂战斧"))
				{
					for(Entity e: getNearbyEntities(a.getEntity().getLocation(), 4))
					{
						if(e instanceof Monster)
						{
							((Monster) e).damage(15);
						}
					}
					return;
				}
				else if(dam.getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.BLUE+"激光炮"))
				{
					for(Entity e: getNearbyEntities(a.getEntity().getLocation(), 7))
					{
						if(e instanceof Monster)
						{
							((Monster) e).damage(100);
						}
					}
					dam.setItemInHand(null);
					return;
				}
				}
				}
				catch(Exception e)
			{
					
			}
		}
	}
}
	/**
	 * get location of set base, If base is not set, the location will not be 
	 * displayed
	 * @param string - base name
	 * @param sender - the sender param use to send back message
	 */
	public void getbaseloc(String string, CommandSender sender) {
		if(baselist.get(string) != null)
		{
		sender.sendMessage(string + " tower is at" + baselist.get(string).getX() 
				+ " " + baselist.get(string).getY() + " " 
				+ baselist.get(string).getZ()+ " " + baselist.get(string).getWorld().getName());
		}
	}
	/**
	 * The public method is called by main, then turn to private method
	 * to check damage to tower by mobs.
	 * TODO change to spawn point method
	 */
	public void checkdamage() {
		
		if(on && !finish && !wave9_switch && !bonus_switch)
		{
			dodamage("BLUE");
			dodamage("AQUA");
			dodamage("PURPLE");
			dodamage("GREEN");
//			if(Base_HP.get("BLUE") == 0)
//			{
//				baselist.put("BLUE", baselist.get("PURPLE").clone());
//			}
//			if(Base_HP.get("AQUA") == 0 && Base_HP.get("BLUE") != 0)
//			{
//				baselist.put("AQUA", baselist.get("BLUE").clone());
//			}
//			if(Base_HP.get("AQUA") == 0 && Base_HP.get("BLUE") == 0)
//			{
//				baselist.put("AQUA", baselist.get("PURPLE").clone());
//			}
//			if(Base_HP.get("PURPLE") == 0 && Base_HP.get("BLUE") != 0)
//			{
//				baselist.put("PURPLE", baselist.get("BLUE").clone());
//			}
//			if(Base_HP.get("PURPLE") == 0 && Base_HP.get("BLUE") == 0)
//			{
//				baselist.put("PURPLE", baselist.get("AQUA").clone());
//				baselist.put("BLUE", baselist.get("AQUA").clone());
//			}
		}
	}
	private void dodamage(String key)
	{
		if(Base_damageable.get(key))
		{
			Location loc;
			Entity[] temp = ch.getNearbyEntities(baselist.get(key), 21);
			for(Entity ent:temp)
			{
				if(ent instanceof PigZombie || ent instanceof Zombie|| ent instanceof Spider)
				{
					loc = ent.getLocation();
					if(loc.distance(baselist.get(key)) < 14)
					{
						HPModify(key, -15);
					}
				}
				if(ent instanceof Skeleton)
				{
					HPModify(key, -10);
				}
				if(ent instanceof Wither)
				{
					HPModify(key, -40);
				}
			}
		}
	}
	/**
	 * heal target base for 5% health
	 * @param string - the name of base
	 */
	public boolean heal(String string, String name) {
		if(on)
		{
			if(Player_use_heal.containsKey(name) == false)
			{
				if(calcheal() != 0)
				{
				double a = 0.05 * counthp(player_count);
				Double doubleobj = new Double(a);
				HPModify(string, doubleobj.intValue());
				Player_use_heal.put(name, 1);
				return true;
				}
				else
				{
					return false;
				}
			}
			else if(Player_use_heal.get(name) < calcheal())
			{
				double a = 0.05 * counthp(player_count);
				Double doubleobj = new Double(a);
				HPModify(string, doubleobj.intValue());
				Player_use_heal.put(name, Player_use_heal.get(name) + 1);
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
	
	private Integer calcheal() {
		if(lumen_count < 1000)	return 0;
		if(lumen_count < 3500) return 1;
		if(lumen_count < 10000) return 2;
		return 3;
	}
	private Integer calcfortify()
	{
		if(lumen_count < 500) return 0;
		if(lumen_count < 2000) return 1;
		if(lumen_count < 3000) return 2;
		if(lumen_count < 6000) return 3;
		return 4;
	}
	public Integer calcmis()
	{
		if(lumen_count < 200) return 0;
		if(lumen_count < 7000) return 1;
		if(lumen_count < 10000) return 2;
		return 3;
	} 
	/**
	 * --Update time
	 * --Check from event list
	 * --predefine wave time.
	 * --this will include special event
	 * --entity checker
	 * @param time
	 */
	public void update(int time) 
	{
		//check settime valid (do this at first)
		//wavetime idea:
		//Before wave X start: wave = X, time < 0 (amount equal to preparation
		//time left)
		//Each second time++ until time == -1
		//At time = -1 wave time change to designated time (e.g. 120)
		//During wave: wave = X, time > 0
		//Each second time-- until time == 0
		//At time = 0 WAVE++ and time change to designated preparation time
		if(finish)
		{
			return;
		}
		if(wave_time == 0)
		{
			wave++;
			wavefinalremove();
			//only when 120 -> 0 will trigger.
			switch(wave)
			{
			case 0:
				wave_time = -30;
				//teleport
				break;
			case 1:
				wave_time = -30;
				playbgmc("wave_1");
				//wave 2 BGM is 162s containing 32s rest time, with 30s there is a 5s lap over
				break;
			case 2:
				wave_time = -30;
				playbgmc("wave_2");
				//wave 3 BGM is 226s containing 31s rest time
				break;
			case 3:
				wave_time = -30;
				playbgmc("wave_3");
				//wave 4 BGM is 226s containing 31s rest time
				break;
			case 4:
				wave_time = -30;
				playbgmc("wave_4");
				//wave 5 BGM is 218s containing 28s rest time
				break;
			case 5:
				wave_time = -30;
				playbgmc("wave_5");
				//wave 6 BGM is 279s containing 29s rest time
				break;
			case 6:
				wave_time = -30;
				playbgmc("wave_6");
				//wave 7 BGM is 282s containing 32s rest time
				break;
			case 7:
				wave_time = -30;
				playbgmc("wave_7");
				//wave 8 BGM is 215s containing 30s rest time
				break;
			case 8:
				wave_time = -30;
				playbgmc("wave_8");
				//wave 9 BGM is 250s containing 30s rest time
				break;
			case 9:
				wave_time = -30;
				playbgmc("wave_9");
				//wave 10 BGM is 689s containing 29s rest time
				break;
			case 10:
				wave_time = -30;
				playbgmc("wave_10");
				break;
			default:
				System.out.println("PANIC! wave = " + wave);
				break;
			}
			wave_time --;
		}
		if(wave_time == -25)
		{
			switch(wave)
			{
			case 2:
				CJFWmain.innoclass.retrievebetweenall();
				break;
			case 3:
				CJFWmain.innoclass.retrievebetweenall();
				break;
			case 4:
				CJFWmain.innoclass.retrievebetweenall();
				break;
			case 5:
				CJFWmain.innoclass.retrievebetweenall();
				break;
			case 6:
				CJFWmain.innoclass.retrievebetweenall();
				break;
			case 7:
				CJFWmain.innoclass.retrievebetweenall();
				break;
			case 8:
				CJFWmain.innoclass.retrievebetweenall();
				break;
			case 9:
				CJFWmain.innoclass.retrievebetweenall();
				break;
			case 10:
				CJFWmain.innoclass.retrievebetweenall();
				break;
			}
		}
		if(wave_time == -1)
		{
			switch(wave)
			{
			case 1:
				wave_time = 122;
				//the BGM is 152s containing 32s rest time
				break;
			case 2:
				wave_time = 132;
				Bukkit.broadcastMessage(ChatColor.RED+"DANGER!怪物攻击力提升20%,血量提升20%!");
				hp_modifier = 1.2;
				attack_modifier = 1.2;
				//wave 2 BGM is 162s containing 32s rest time
				break;
			case 3:
				wave_time = 196;
				Bukkit.broadcastMessage(ChatColor.RED+"DANGER!怪物攻击力提升20%,血量提升40%!");
				hp_modifier = 1.4;
				attack_modifier = 1.2;
				//wave 3 BGM is 226s containing 31s rest time
				break;
			case 4:
				wave_time = 196;
				Bukkit.broadcastMessage(ChatColor.RED+"DANGER!怪物攻击力提升40%,血量提升40%!");
				hp_modifier = 1.4;
				attack_modifier = 1.4;
				//wave 4 BGM is 226s containing 31s rest time
				break;
			case 5:
				wave_time = 188;
				Bukkit.broadcastMessage(ChatColor.RED+"DANGER!怪物攻击力提升40%,血量提升60%!");
				hp_modifier = 1.6;
				attack_modifier = 1.4;
				//wave 5 BGM is 218s containing 28s rest time
				break;
			case 6:
				wave_time = 250;
				Bukkit.broadcastMessage(ChatColor.RED+"DANGER!怪物攻击力提升60%,血量提升60%!");
				hp_modifier = 1.6;
				attack_modifier = 1.6;
				//wave 6 BGM is 279s containing 29s rest time
				break;
			case 7:
				wave_time = 252;
				Bukkit.broadcastMessage(ChatColor.RED+"DANGER!怪物攻击力提升60%,血量提升60%!");
				hp_modifier = 1.6;
				attack_modifier = 1.6;
				//wave 7 BGM is 282s containing 32s rest time
				break;
			case 8:
				wave_time = 185;
				Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+"EXTREME DANGER!怪物攻击力提升80%,血量提升60%!");
				hp_modifier = 1.6;
				attack_modifier = 1.8;
				//wave 8 BGM is 215s containing 30s rest time
				break;
			case 9:
				wave_time = 250;
				Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+"EXTREME DANGER!怪物属性...计测不能。");
				hp_modifier = 1.8;
				attack_modifier = 1.8;
				//wave 9 BGM is 281s containing 30s rest time
				break;
			case 10:
				wave_time = 660;
				Bukkit.broadcastMessage(ChatColor.DARK_PURPLE+"EXTREME DANGER!");
				hp_modifier = 2.0;
				attack_modifier = 2.0;
				//wave 10 BGM is 344+345s containing 29s rest time
				break;
			}
			wave_time ++;//to eliminate the effect of removal at last.
		}
		//set time for new (do this at last)
		if(wave_time < 0)
		{
			wave_time ++;
		}
		if(wave_time > 0)
		{
			wave_time --;
		}
			
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set Wave cjfw " + wave);
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set Time cjfw " + wave_time);
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set BLUE cjfw " + Base_HP.get("BLUE"));
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set AQUA cjfw " + Base_HP.get("AQUA"));
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set PURPLE cjfw " + Base_HP.get("PURPLE"));
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set GREEN cjfw " + Base_HP.get("GREEN"));
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set Crystal cjfw " + lumen_count);
		//Time and Event based check
		if(Base_HP.get("BLUE") == 0 && Base_HP.get("AQUA") == 0 && Base_HP.get("PURPLE") == 0 && Base_HP.get("GREEN") == 0)
		{
			finish = true;
			Bukkit.broadcastMessage("防卫战结束！");
			Bukkit.broadcastMessage("最终防卫等级： F !");
		}
		if(wave == 11 && wave_time == 0)
		{
			finish = true;
			Bukkit.broadcastMessage("防卫战结束！");
			Bukkit.broadcastMessage("最终防卫等级： "+ calculate_rank() + " !");
		}
		WaveTime ti = new WaveTime(wave, wave_time);
		try
		{
		if(gameevent.serversayevent.containsKey(ti.toString()))
		{
			Bukkit.broadcastMessage(gameevent.serversayevent.get(ti.toString()));
			System.out.println(ti.toString());
		}
		if(gameevent.spawnmobevent.containsKey(ti.toString()))
		{
			System.out.println(ti.toString());
			//<Type> <amount_modifier> <weaponrank> <armorrank> <name>
			String a = gameevent.spawnmobevent.get(ti.toString());
			String[] args = a.split(" ");
			int sp = random.nextInt(8) + 1;
			LivingEntity spawned = null;
			int weaponrank = Integer.parseInt(args[2]);
			int armorrank = Integer.parseInt(args[3]);
			double modifier = Double.parseDouble(args[1]);
			for(int i = 0; i < modifier * player_count * difficulty; i ++)
			{
			switch(args[0])
			{
			case "zombie":
				spawned = ch.summonCreatures(baselist, "BLUE", Zombie.class, args[4], spawn_loc.get(Integer.toString(sp)), false);
	
				WeaponRank.setWeapon(weaponrank, spawned);
				ArmorRank.setArmor(armorrank, spawned);
				break;
			case "skeleton":
				spawned = ch.summonCreatures(baselist, "BLUE", Skeleton.class, args[4], spawn_loc.get(Integer.toString(sp)), false);
				WeaponRank.setWeapon(weaponrank, spawned);
				ArmorRank.setArmor(armorrank, spawned);
				break;
			case "giant":
				spawned = ch.summonCreatures(baselist, "BLUE", Giant.class, args[4], spawn_loc.get(Integer.toString(sp)), false);
				WeaponRank.setWeapon(weaponrank, spawned);
				ArmorRank.setArmor(armorrank, spawned);
				break;
			case "creeper":
				spawned = ch.summonCreatures(baselist, "BLUE", Creeper.class, args[4], spawn_loc.get(Integer.toString(sp)), false);
				break;
			case "spider":
				spawned = ch.summonCreatures(baselist, "BLUE", Spider.class, args[4], spawn_loc.get(Integer.toString(sp)), false);
				break;
			case "pigzombie":
				spawned = ch.summonCreatures(baselist, "BLUE", PigZombie.class, args[4], spawn_loc.get(Integer.toString(sp)), false);
				WeaponRank.setWeapon(weaponrank, spawned);
				ArmorRank.setArmor(armorrank, spawned);
			}
			if(spawned != null)
			{
				
				spawned.setMaxHealth(spawned.getMaxHealth() * hp_modifier);
				spawned.setHealth(spawned.getMaxHealth());
				ch.changeattack(spawned, attack_modifier - 1);
				switch(sp)
				{
				case 1:
					((Monster)spawned).setTarget(Base_defender.get("BLUE"));
					break;
				case 2:
					((Monster)spawned).setTarget(Base_defender.get("BLUE"));
					break;
				case 3:
					((Monster)spawned).setTarget(Base_defender.get("AQUA"));
					break;
				case 4:
					((Monster)spawned).setTarget(Base_defender.get("AQUA"));
					break;
				case 5:
					((Monster)spawned).setTarget(Base_defender.get("PURPLE"));
					break;
				case 6:
					((Monster)spawned).setTarget(Base_defender.get("PURPLE"));
					break;
				case 7:
					((Monster)spawned).setTarget(Base_defender.get("GREEN"));
					break;
				case 8:
					((Monster)spawned).setTarget(Base_defender.get("GREEN"));
					break;
				default:
					((Monster)spawned).setTarget(Base_defender.get("BLUE"));
					break;
				}
			}
			}
		}
		if(gameevent.showrankeventlist.contains(ti.toString()))
		{
			gettop();
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "cjfw showmission");
		}
		}
		catch(Exception e)
		{
			System.out.println("PANIC:" + e.getMessage()); 
		}
		if(wave == 5 && wave_time == 185)
		{
				Location temp = baselist.get("BLUE").clone();
				temp.add(0,10,40);
				Entity temp_eni;
				temp_eni = baselist.get("BLUE").getWorld().spawnEntity(temp, EntityType.WITHER);
				LivingEntity a = (LivingEntity)temp_eni;
				a.setRemoveWhenFarAway(false);
				a.setCustomName(ChatColor.RED+"第二型巴特勒");
				a.setCustomNameVisible(true);
				ch.changerange(a); 
				a.setMaxHealth(75 * player_count * difficulty);
	     		a.setHealth(75 * player_count * difficulty);		
		}
		if(wave == 6 && wave_time == 245)
		{
			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "cjfw spawnbossconsole "+baselist.get("BLUE").getWorld().getName()+" hinaru "+
		(baselist.get("GREEN").getBlockX()+40) + " " + baselist.get("GREEN").getBlockY() + " " + baselist.get("GREEN").getBlockZ());
		}
	}
	private void playbgmc(String args) 
	{
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "cjfw changemusic " + args);
	}
	public void playbgm(String args) 
	{
		for(int i = bord1.getBlockX(); i <= bord2.getBlockX(); i++)
		{
			for(int j = bord1.getBlockY(); j <= bord2.getBlockY(); j++)
			{
				for(int z = bord1.getBlockZ(); z <= bord2.getBlockZ(); z++)
				{
					Location temp = new Location(bord1.getWorld(),i, j, z);
					if(temp.getBlock().getState() != null)
					{
						BlockState e = temp.getBlock().getState();
						if(e instanceof Jukebox)
						{
							//RECORD11 - M1A
							switch(args)
							{
							case "wave_1":
								System.out.println("It should be playing.");
								((Jukebox) e).setPlaying(Material.RECORD_11);
								break;
							case "wave_2":
								((Jukebox) e).setPlaying(Material.GOLD_RECORD);
								break;
							case "wave_3":
								((Jukebox) e).setPlaying(Material.RECORD_3);
								break;
							case "wave_4":
								((Jukebox) e).setPlaying(Material.GREEN_RECORD);
								break;	
							case "wave_5":
								((Jukebox) e).setPlaying(Material.RECORD_5);
								break;
							case "wave_6":
								((Jukebox) e).setPlaying(Material.RECORD_6);
								break;
							case "wave_7":
								((Jukebox) e).setPlaying(Material.RECORD_7);
								break;
							case "wave_8":
								((Jukebox) e).setPlaying(Material.RECORD_4);
								break;
							case "wave_9":
								((Jukebox) e).setPlaying(Material.RECORD_8);
								break;
							case "wave_10":
								((Jukebox) e).setPlaying(Material.RECORD_9);
								break;
							//GOLD    -- M2A
							//RECORD3 -- M3A
							//GREEN   -- M4A
							//RECORD5 -- M5A
							//RECORD6 -- M6A
							//RECORD7 -- M7A
							//RECORD4 -- M4B
							//RECORD8 -- M7D
							//RECORD9 -- M8D
							}
						}
					}
				}
			}
		}
		
	}
	private String calculate_rank() {
		int full = 4 *counthp(player_count);
		int fin = Base_HP.get("BLUE") + Base_HP.get("AQUA") + Base_HP.get("PURPLE") + Base_HP.get("GREEN");
		int brok = 0;
		String rank = "";
		if(Base_HP.get("BLUE") <= 0)
		{
			brok++;
		}
		if(Base_HP.get("AQUA") <= 0)
		{
			brok++;
		}
		if(Base_HP.get("PURPLE") <= 0)
		{
			brok++;
		}
		if(Base_HP.get("GREEN") <= 0)
		{
			brok ++;
		}
		//calculation represent the lowest available situation
		if(fin > 0.97 * full) 
			//if total HP is >97%
		{
			rank = "SS";
		}
		if(fin > 0.80 * full && fin < 0.97 * full) 
			// if total HP is 80% ~ 97%
		{
			rank = "S";
		}
		if(fin > 0.60 * full && fin < 0.8 * full)
			// if total HP is 60% ~ 80%
		{
			rank = "A";
		}
		if((fin > 0.4 * full && fin < 0.6 * full) || brok == 2)
			//if total HP is 40% ~ 60%, or if 2 towers down
		{
			rank = "B";
		}
		if((fin < 0.4 * full) || brok == 3)
			//if total HP is < 40%, or if only 1 tower remains
		{
			rank = "C";
		}
		return rank;
		
	}
	private void wavefinalremove()
	{
		//called at wave finish. remove all nearby enemy in all towers.
		for(Entity ent:ch.getNearbyEntities(baselist.get("BLUE"), 70))
		{
			if(ent instanceof Monster || ent instanceof EnderDragon)
			{
				((Monster) ent).setHealth(0D);
			}
		}
		for(Entity ent:ch.getNearbyEntities(baselist.get("AQUA"), 70))
		{
			if(ent instanceof Monster || ent instanceof EnderDragon)
			{
				((Monster) ent).setHealth(0D);
			}
		}
		for(Entity ent:ch.getNearbyEntities(baselist.get("PURPLE"), 70))
		{
			if(ent instanceof Monster || ent instanceof EnderDragon)
			{
				((Monster) ent).setHealth(0D);
			}
		}
		for(Entity ent:ch.getNearbyEntities(baselist.get("GREEN"), 70))
		{
			if(ent instanceof Monster || ent instanceof EnderDragon)
			{
				((Monster) ent).setHealth(0D);
			}
		}
	}
	private void gettop()
	{
		try
		{
		sb.registerNewObjective("top", "dummy");
		sb.getObjective("top").setDisplayName("Kills");
		}
		catch (Exception e)
		{
			sb.getObjective("top").unregister();
			sb.registerNewObjective("top", "dummy");
			sb.getObjective("top").setDisplayName("Kills");
		}
		OfflinePlayer[] a = new OfflinePlayer[8]; 
		for(OfflinePlayer player:Bukkit.getOfflinePlayers())
		{
			try
			{
			for(int i = 0; i < 8; i++)
			{
				
					if(a[i] != null)
					{
						if (sb.getObjective("contri_point").getScore(player).getScore() > sb.getObjective("contri_point").getScore(a[i]).getScore())
						{
							int j = 0;
							for(j = 0; j < 8; j++)
							{
								if(a[j] == null)
								{
									break;
								}
							}
							if(j == 8)
							{
								j --;
							}
								for(int l = j; l >= i + 1; l --)
								{
									a[l] = a[l-1];
								}
								a[i] = player;
							break;
						}
					}
					else if(a[i] == null && i < 8)
					{
						a[i] = player;
						break;
					}
				}
				
			}
			catch (Exception e)
			{
				
			}
			
		}
		for(OfflinePlayer e:a)
		{
			if(e != null)
			{
				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set " + e.getName() + " top " + sb.getObjective("contri_point").getScore(e).getScore());
			}
		}
		Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard objectives setdisplay sidebar top");
	}
	
	/**
	 * The selected tower will go fortified
	 * @param string - the name of tower
	 */
	public boolean fortify(String string, String name) 
	{
		if(on)
		{
			if(Player_use_fort.containsKey(name) == false)
			{
				if(calcfortify() != 0)
				{
				Base_damageable.put(string, false);
				Player_use_fort.put(name, 1);
				return true;
				}
				else
				{
					return false;
				}
			}
			else if(Player_use_fort.get(name) < calcfortify())
			{
				Base_damageable.put(string, false);
				Player_use_fort.put(name, Player_use_fort.get(name) + 1);
				return true;
			}
			else
			{
				return false;
			}
		}
		return false;
	}
	/**
	 * The selected tower will be defortified
	 * @param string - the name of tower
	 */
	public void defortify(String string) 
	{
		Base_damageable.put(string, true);
		
	}
	
	
	/**
	 * get player killing rank at the end of game
	 * ONLY when on and finish
	 */
	public void getrank() 
	{
		if(on && finish)
		{
			gettop();
		}
		
	}
	/**
	 * set game difficulty, default is 1
	 * @param i
	 */
	public void setdifficulty(double i) 
	{
		difficulty = i;
		
	}
	public void setwave(String string) {
		if(init)
		{
			try
			{
				wave = Integer.parseInt(string);
			}
			catch(Exception e)
			{
				
			}
		}
		
	}
	public void settime(String string) {
		if(init)
		{
			try
			{
				wave_time = Integer.parseInt(string);
			}
			catch(Exception e)
			{
				
			}
		}
		
	}
	public void setborder(Location location, int i) {
		if(i == 1)
		{
			bord1 = location;
			
		}
		if(i == 2)
		{
			bord2 = location;
		}
	}
	public Entity[]  getNearbyEntities(Location l, int radius)
	{
		int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16))/16;
		HashSet<Entity> radiusEntities = new HashSet<Entity>();
		for (int chX = 0 -chunkRadius; chX <= chunkRadius; chX ++)
		{
			for (int chZ = 0 -chunkRadius; chZ <= chunkRadius; chZ++)
			{
				int x=(int) l.getX(),y=(int) l.getY(),z=(int) l.getZ();
				for (Entity e : new Location(l.getWorld(),x+(chX*16),y,z+(chZ*16)).getChunk().getEntities())
				{
					if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock()) radiusEntities.add(e);
				}
			}
		}
        return radiusEntities.toArray(new Entity[radiusEntities.size()]);
    }
	public void retrievecrystal(Player a) {
		for(Entity e : getNearbyEntities(a.getLocation(), 5))
		{
			if(e instanceof Player)
			{
				cargoscan((Player)e);
			}
		}
		
	}
	private void cargoscan(Player e) 
	{
		ItemStack[] temp = e.getInventory().getContents();
		ArrayList<Map<String, Object>> array = new ArrayList<Map<String, Object>>();
		for(ItemStack i : temp)
		{
			if(i != null)
			{
			if(i.hasItemMeta())
			{
				try
				{
				if(!i.getItemMeta().getDisplayName().contains("Lumen Crystal"))
				{
					array.add(i.serialize());
				}
				else
				{
					addlumen(i);
				}
				}
				catch(Exception ex)
				{
					array.add(i.serialize());
				}
			}
			else
			{
				array.add(i.serialize());
			}
			}
		}
		e.getInventory().clear();
		for(Map<String, Object> c : array)
		{
			e.getInventory().addItem(ItemStack.deserialize(c));
		}
	}
	private void addlumen(ItemStack i) 
	{
		if(on)
		{
			int k = i.getAmount();
			if(player_count < 10)
			{
				lumen_count = lumen_count + 10 * k;
			}
			else if(player_count < 20)
			{
				lumen_count = lumen_count + 7 * k;
			}
			else if(player_count < 40)
			{
				lumen_count = lumen_count + 5 * k;
			}
			else if(player_count < 80)
			{
				lumen_count = lumen_count + 3 * k;
			}
		}
		
	}
}