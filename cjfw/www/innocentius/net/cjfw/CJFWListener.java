package innocentius.net.cjfw;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
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
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
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
	boolean on;
	boolean init;
	Random random;
	CreatureHandler ch;
	int wave;
	int player_count;
	int wave_time;
	int bonus_time;
	int back_timer;
	Villager bluedef;
	Villager aquadef;
	Villager purpdef;
	Villager greedef;
	boolean finish;
	boolean wave9_switch;
	boolean bonus_switch;
	boolean bonus_finish_switch;
	boolean last_boss_phase1;
	boolean last_boss_phase2;
	boolean last_boss_phase3;
	double difficulty;
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
		back_timer = 0;
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
			return true;
			}
		}
	}
	private boolean calcborder(Location bord12, Location bord22) {
		if(bord12.getWorld().getName().equals(bord22.getWorld().getName()))
		{
			if(bord12.getY() == bord22.getY())
			{
				if(bord12.getX() < bord22.getX() && bord12.getZ() < bord22.getZ())
				{
					return true;
				}
				else if(bord12.getX() > bord22.getX() && bord12.getZ() < bord22.getZ())
				{
					double x = bord12.getX();
					bord12.setX(bord22.getX());
					bord22.setX(x);
					return true;
				}
				else if(bord12.getX() < bord22.getX() && bord12.getZ() > bord22.getZ())
				{
					double z = bord12.getZ();
					bord12.setZ(bord22.getZ());
					bord22.setZ(z);
					return true;
				}
				else if(bord12.getX() > bord22.getX() && bord12.getZ() > bord22.getZ())
				{
					double x = bord12.getX();
					bord12.setX(bord22.getX());
					bord22.setX(x);
					double z = bord12.getZ();
					bord12.setZ(bord22.getZ());
					bord22.setZ(z);
					return true;
				}
				else
				{
					//if x = x or z = z
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
				double a = 0.05 * counthp(player_count);
				Double doubleobj = new Double(a);
				HPModify(string, doubleobj.intValue());
				Player_use_heal.put(name, 1);
				return true;
			}
			else if(Player_use_heal.get(name) < 2)
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
		if(wave_time == 0)
		{
			wave++;
			//only when 120 -> 0 will trigger.
			switch(wave)
			{
			case 0:
				wave_time = -30;
				//teleport
				break;
			case 1:
				wave_time = -35;
				playbgm("wave_1");
				//wave 2 BGM is 162s containing 32s rest time, with 30s there is a 5s lap over
				break;
			case 2:
				wave_time = -35;
				playbgm("wave_2");
				//wave 3 BGM is 226s containing 31s rest time
				break;
			case 3:
				wave_time = -35;
				playbgm("wave_3");
				//wave 4 BGM is 226s containing 31s rest time
				break;
			case 4:
				wave_time = -35;
				playbgm("wave_4");
				//wave 5 BGM is 218s containing 28s rest time
				break;
			case 5:
				wave_time = -40;
				playbgm("wave_5");
				//wave 6 BGM is 279s containing 29s rest time
				break;
			case 6:
				wave_time = -40;
				playbgm("wave_6");
				//wave 7 BGM is 282s containing 32s rest time
				break;
			case 7:
				wave_time = -35;
				playbgm("wave_7");
				//wave 8 BGM is 215s containing 30s rest time
				break;
			case 8:
				wave_time = -40;
				playbgm("wave_8");
				//wave 9 BGM is 250s containing 30s rest time
				break;
			case 9:
				wave_time = -40;
				playbgm("wave_9");
				//wave 10 BGM is 689s containing 29s rest time
				break;
			case 10:
				wave_time = -40;
				playbgm("wave_10_1");
				break;
			default:
				System.out.println("PANIC! wave = " + wave);
				break;
			}
			wave_time --;
		}
		if(wave_time == -1)
		{
			switch(wave)
			{
			case 1:
				wave_time = 125;
				//the BGM is 152s containing 32s rest time
				break;
			case 2:
				wave_time = 135;
				//wave 2 BGM is 162s containing 32s rest time
				break;
			case 3:
				wave_time = 200;
				//wave 3 BGM is 226s containing 31s rest time
				break;
			case 4:
				wave_time = 200;
				//wave 4 BGM is 226s containing 31s rest time
				break;
			case 5:
				wave_time = 190;
				//wave 5 BGM is 218s containing 28s rest time
				break;
			case 6:
				wave_time = 250;
				//wave 6 BGM is 279s containing 29s rest time
				break;
			case 7:
				wave_time = 250;
				//wave 7 BGM is 282s containing 32s rest time
				break;
			case 8:
				wave_time = 185;
				//wave 8 BGM is 215s containing 30s rest time
				break;
			case 9:
				wave_time = 250;
				//wave 9 BGM is 281s containing 30s rest time
				break;
			case 10:
				wave_time = 670;
				//wave 10 BGM is 344+345s containing 29s rest time
				break;
			}
			wave_time ++;
		}
		if(wave == 10 && wave_time == 355)
		{
			playbgm("wave_10_2");
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
		if(Base_HP.get("BLUE") == 0 && Base_HP.get("AQUA") == 0 && Base_HP.get("PURPLE") == 0 && Base_HP.get("GREEN") == 0)
		{
			finish = true;
		}
		WaveTime ti = new WaveTime(wave, wave_time);
		try
		{
		if(gameevent.serversayevent.containsKey(ti))
		{
			Bukkit.broadcastMessage(gameevent.serversayevent.get(ti));
		}
		if(gameevent.spawnmobevent.containsKey(ti))
		{
			
		}
		if(gameevent.showrankeventlist.contains(ti))
		{
			gettop();
//			Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard objectives setdisplay sidebar cjfw");
//		    sb.getObjective("contri_point").setDisplaySlot(DisplaySlot.BELOW_NAME);
		}
		}
		catch(Exception e)
		{
			System.out.println("PANIC:" + e.getMessage());
		}
		//check event
		
//		//the update of wave should be the first step
//		Location temp;
//		LivingEntity e;
//		if(!finish && !wave9_switch && !bonus_switch)
//		{
//			if(wave_time == 0)
//			{
//				wave++;
//			}
//			//wave 9 methods
//			if(wave == 9 && wave_time == 0)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GREEN+"WAVE 8 FINISH!");
//				}
//				wave_time = -45;
//				baselist.get("BLUE").getWorld().setStorm(false);
//				baselist.get("BLUE").getWorld().setThundering(false);
//				baselist.get("BLUE").getWorld().setWeatherDuration(1000000);
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "revclass supply confirm");
//			}
//			if(wave == 9 && wave_time == -40)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.YELLOW+"在最後一波結束后將不能使用塔防支援！請各位尚餘回血的玩家儘快使用，不要浪費！");
//				}
//			}
//			if(wave == 9 && wave_time == -1)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GOLD+"FINAL WAVE START!");
//				}
//				
//				wave_time = 300;
//				baselist.get("BLUE").getWorld().setTime(6000);
//			}
//			if(wave == 9 && wave_time == 295)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,10,-45);
//				Entity temp_eni;
//				temp_eni = baselist.get("BLUE").getWorld().spawnEntity(temp, EntityType.WITHER);
//				LivingEntity a = (LivingEntity)temp_eni;
//				a.setRemoveWhenFarAway(false);
//				a.setCustomName(ChatColor.RED+"第二型巴特勒");
//				a.setCustomNameVisible(true);
//				ch.changerange(a); 
//				a.setMaxHealth(200 * player_count * difficulty);
//				a.setHealth(200 * player_count * difficulty);
//				final_boss = (Wither)a;
//				
//			}
//			if(wave == 9 && wave_time > 0 && wave_time < 295 && wave_time % 15 == 0)
//			{
//				if(final_boss != null)
//				{
//					if(!final_boss.isDead())
//					{
//						temp = baselist.get("AQUA").clone();
//						temp.add(0,0,-50);
//						for(int i = 0; i < 0.5 * player_count * difficulty; i++)
//						{
//							e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團近衛", temp, false);
//							Skeleton skel = (Skeleton)e;
//							skel.setSkeletonType(SkeletonType.WITHER);
//							CraftSkeleton skelc = (CraftSkeleton)skel;
//					        EntitySkeleton skelMC = skelc.getHandle();
//							ItemStack item = new ItemStack(Item.getById(261));
//							item.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
//					        skelMC.setEquipment(0, item);
//					        skelMC.setEquipment(1, new ItemStack(Item.getById(301)));
//					        skelMC.setEquipment(2, new ItemStack(Item.getById(300)));
//					        skelMC.setEquipment(3, new ItemStack(Item.getById(299)));
//					        skelMC.setEquipment(4, new ItemStack(Item.getById(298)));
//						}
//						temp = baselist.get("PURPLE").clone();
//						temp.add(0,0,-50);
//						for(int i = 0; i < 0.5 * player_count * difficulty; i++)
//						{
//							e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團本部", temp, false);
//							Skeleton skel = (Skeleton)e;
//							skel.setSkeletonType(SkeletonType.WITHER);
//							CraftSkeleton skelc = (CraftSkeleton)skel;
//					        EntitySkeleton skelMC = skelc.getHandle();
//							ItemStack item = new ItemStack(Item.getById(261));
//							item.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
//					        skelMC.setEquipment(0, item);
//					        skelMC.setEquipment(1, new ItemStack(Item.getById(301)));
//					        skelMC.setEquipment(2, new ItemStack(Item.getById(300)));
//					        skelMC.setEquipment(3, new ItemStack(Item.getById(299)));
//					        skelMC.setEquipment(4, new ItemStack(Item.getById(298)));
//						}
//					}
//				}
//			}
//			if(wave == 9 && wave_time > 0 && wave_time < 200 && wave_time % 5 == 0)
//			{
//				if(checkentitydata())
//				{
//					bonus_time = bonus_time + wave_time;
//					wave_time = 0;
//					wave9_switch = true;
//				}
//			}
//			if(wave == 9 && wave_time == 1)
//			{
//				wavefinalremove();
//				wave9_switch = true;
//			}
//			//wave 8 methods
//			if(wave == 8 && wave_time == 0)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GREEN+"WAVE 7 FINISH!");
//				}
//				wave_time = -30;
//				baselist.get("BLUE").getWorld().setStorm(false);
//				baselist.get("BLUE").getWorld().setThundering(false);
//				baselist.get("BLUE").getWorld().setWeatherDuration(1000000);
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "revclass supply confirm");
//			}
//			if(wave == 8 && wave_time == -1)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GOLD+"WAVE 8 START!");
//				}
//				
//				wave_time = 270;
//				baselist.get("BLUE").getWorld().setTime(6000);
//			}
//			if(wave == 8 && wave_time == 265)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.DARK_RED+"凋零軍團本隊來襲！");
//				}
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,10,-45);
//				e = ch.summonCreatures(baselist, "BLUE", CreatureType.ENDER_DRAGON, ChatColor.GOLD+"凋零軍團副官", temp, true);
//				e.setMaxHealth(2000 * player_count * difficulty);
//				e.setHealth(2000 * player_count * difficulty);
//				temp = baselist.get("AQUA").clone();
//				temp.add(-20,10,-45);
//				e = ch.summonCreatures(baselist, "AQUA", CreatureType.ENDER_DRAGON, ChatColor.GOLD+"凋零軍團副官", temp, true);
//				e.setMaxHealth(2000 * player_count * difficulty);
//				e.setHealth(2000 * player_count * difficulty);
//				temp = baselist.get("PURPLE").clone();
//				temp.add(20,10,-45);
//				e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ENDER_DRAGON, ChatColor.GOLD+"凋零軍團副官", temp, true);
//				e.setMaxHealth(2000 * player_count * difficulty);
//				e.setHealth(200 * player_count * difficulty);
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < 0.5 * player_count * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團本部", temp, false);
//					e.setMaxHealth(80D);
//					e.setHealth(80D);
//					Skeleton skel = (Skeleton)e;
//					skel.setSkeletonType(SkeletonType.WITHER);
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//					ItemStack item = new ItemStack(Item.getById(261));
//					item.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(301)));
//			        skelMC.setEquipment(2, new ItemStack(Item.getById(300)));
//			        skelMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(298)));
//				}
//				for(int i = 0; i < 1.2 * player_count * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團本部", temp, false);
//					e.setMaxHealth(100D);
//					e.setHealth(100D);
//					Skeleton skel = (Skeleton)e;
//					skel.setSkeletonType(SkeletonType.WITHER);
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(301)));
//			        skelMC.setEquipment(2, new ItemStack(Item.getById(300)));
//			        skelMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(298)));
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(-20,0,-30);
//				for(int i = 0; i < 1.2 * player_count * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團本部", temp, false);
//					e.setMaxHealth(80D);
//					e.setHealth(80D);
//					Skeleton skel = (Skeleton)e;
//					skel.setSkeletonType(SkeletonType.WITHER);
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//					ItemStack item = new ItemStack(Item.getById(261));
//					item.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(301)));
//			        skelMC.setEquipment(2, new ItemStack(Item.getById(300)));
//			        skelMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(298)));
//				}
//				for(int i = 0; i < 1.2 * player_count * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團本部", temp, false);
//					e.setMaxHealth(100D);
//					e.setHealth(100D);
//					Skeleton skel = (Skeleton)e;
//					skel.setSkeletonType(SkeletonType.WITHER);
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(301)));
//			        skelMC.setEquipment(2, new ItemStack(Item.getById(300)));
//			        skelMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(298)));
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(20,0,-30);
//				for(int i = 0; i < 1.2 * player_count * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團本部", temp, false);
//					e.setMaxHealth(80D);
//					e.setHealth(80D);
//					Skeleton skel = (Skeleton)e;
//					skel.setSkeletonType(SkeletonType.WITHER);
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//					ItemStack item = new ItemStack(Item.getById(261));
//					item.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(301)));
//			        skelMC.setEquipment(2, new ItemStack(Item.getById(300)));
//			        skelMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(298)));
//				}
//				for(int i = 0; i < 1.0 * player_count * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團本部", temp, false);
//					e.setMaxHealth(100D);
//					e.setHealth(100D);
//					Skeleton skel = (Skeleton)e;
//					skel.setSkeletonType(SkeletonType.WITHER);
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(301)));
//			        skelMC.setEquipment(2, new ItemStack(Item.getById(300)));
//			        skelMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(298)));
//				}
//			}
//			if(wave == 8 && wave_time > 0 && wave_time < 200 && wave_time % 5 == 0)
//			{
//				if(checkentitydata())
//				{
//					bonus_time = bonus_time + wave_time;
//					wave_time = 0;
//					
//				}
//			}
//			if(wave == 8 && wave_time == 1)
//			{
//				wavefinalremove();
//			}
//			//wave 7 methods
//			if(wave == 7 && wave_time == 0)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GREEN+"WAVE 6 FINISH!");
//				}
//				baselist.get("BLUE").getWorld().setStorm(false);
//				baselist.get("BLUE").getWorld().setThundering(false);
//				baselist.get("BLUE").getWorld().setWeatherDuration(1000000);
//				wave_time = -30;
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "revclass supply confirm");
//			}
//			if(wave == 7 && wave_time == -25)
//			{
//				gettop();
//			}
//			if(wave == 7 && wave_time == -10)
//			{
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard objectives setdisplay sidebar cjfw");
//			}
//			if(wave == 7 && wave_time == -1)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GOLD+"WAVE 7 START!");
//				}
//				
//				wave_time = 240;
//				baselist.get("BLUE").getWorld().setTime(17000);
//			}
//			if(wave == 7 && wave_time == 240)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-45);
//				for(int i = 0; i < player_count * 1.0 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團前衛", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"凋零軍團先鋒", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(0,0,-45);
//				for(int i = 0; i < player_count * 0.75 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團前衛", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.4 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"凋零軍團先鋒", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0,0,-45);
//				for(int i = 0; i < player_count * 0.75 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團前衛", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.4 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"凋零軍團先鋒", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//			}
//			if(wave == 7 && wave_time == 200)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-45);
//				for(int i = 0; i < player_count * 0.75 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團前衛", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.4 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"凋零軍團先鋒", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					Entity temp_eni;
//					temp_eni = baselist.get("BLUE").getWorld().spawnEntity(temp, EntityType.WITCH);
//					LivingEntity a = (LivingEntity)temp_eni;
//					a.setRemoveWhenFarAway(false);
//					a.setCustomName("凋零軍團巫師");
//					a.setCustomNameVisible(true);
//					ch.changerange(a); 
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(0,0,-45);
//				for(int i = 0; i < player_count * 0.85 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團前衛", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"凋零軍團先鋒", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					Entity temp_eni;
//					temp_eni = baselist.get("AQUA").getWorld().spawnEntity(temp, EntityType.WITCH);
//					LivingEntity a = (LivingEntity)temp_eni;
//					a.setRemoveWhenFarAway(false);
//					a.setCustomName(ChatColor.GREEN+"凋零軍團巫師");
//					a.setCustomNameVisible(true);
//					ch.changerange(a); 
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0,0,-45);
//				for(int i = 0; i < player_count * 0.85 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團前衛", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"凋零軍團先鋒", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.25 * difficulty; i++)
//				{
//					Entity temp_eni;
//					temp_eni = baselist.get("PURPLE").getWorld().spawnEntity(temp, EntityType.WITCH);
//					LivingEntity a = (LivingEntity)temp_eni;
//					a.setRemoveWhenFarAway(false);
//					a.setCustomName(ChatColor.GREEN+"凋零軍團巫師");
//					a.setCustomNameVisible(true);
//					ch.changerange(a); 
//				}
//			}
//			if(wave == 7 && wave_time == 185)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.RED+"中路有強力的敵人出現！");
//				}
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-40);
//				e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.BLACK+"凋零型巴特勒", temp, true);
//				e.setMaxHealth(250 * player_count * difficulty);
//				e.setHealth(250 * player_count * difficulty);
//				Skeleton skel = (Skeleton)e;
//				skel.setSkeletonType(SkeletonType.WITHER);
//				CraftSkeleton skelc = (CraftSkeleton)skel;
//				EntitySkeleton skelMC = skelc.getHandle();
//				ItemStack item = new ItemStack(Item.getById(276));
//				item.addEnchantment(Enchantment.KNOCKBACK, 2);
//				item.addEnchantment(Enchantment.FIRE_ASPECT, 1);
//				skelMC.setEquipment(0, item);
//				skelMC.setEquipment(1, new ItemStack(Item.getById(309)));
//				skelMC.setEquipment(2, new ItemStack(Item.getById(308)));
//				skelMC.setEquipment(4, new ItemStack(Item.getById(306)));
//				item = new ItemStack(Item.getById(307));
//				item.addEnchantment(Enchantment.THORNS, 2);
//				skelMC.setEquipment(3, item);
//			}
//			if(wave == 7 && wave_time == 140)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-45);
//				for(int i = 0; i < player_count * 0.5 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團前衛", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"凋零軍團先鋒", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.1 * difficulty; i++)
//				{
//					Entity temp_eni;
//					temp_eni = baselist.get("BLUE").getWorld().spawnEntity(temp, EntityType.WITCH);
//					LivingEntity a = (LivingEntity)temp_eni;
//					a.setRemoveWhenFarAway(false);
//					a.setCustomName("凋零軍團巫師");
//					a.setCustomNameVisible(true);
//					ch.changerange(a); 
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(0,0,-45);
//				for(int i = 0; i < player_count * 0.6 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團前衛", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.3 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"凋零軍團先鋒", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.1 * difficulty; i++)
//				{
//					Entity temp_eni;
//					temp_eni = baselist.get("AQUA").getWorld().spawnEntity(temp, EntityType.WITCH);
//					LivingEntity a = (LivingEntity)temp_eni;
//					a.setRemoveWhenFarAway(false);
//					a.setCustomName(ChatColor.GREEN+"凋零軍團巫師");
//					a.setCustomNameVisible(true);
//					ch.changerange(a); 
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0,0,-45);
//				for(int i = 0; i < player_count * 0.6 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團前衛", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.3 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"凋零軍團先鋒", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				for(int i = 0; i < player_count * 0.1 * difficulty; i++)
//				{
//					Entity temp_eni;
//					temp_eni = baselist.get("PURPLE").getWorld().spawnEntity(temp, EntityType.WITCH);
//					LivingEntity a = (LivingEntity)temp_eni;
//					a.setRemoveWhenFarAway(false);
//					a.setCustomName(ChatColor.GREEN+"凋零軍團巫師");
//					a.setCustomNameVisible(true);
//					ch.changerange(a); 
//				}
//			}
//			if(wave == 7 && wave_time == 120)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.RED+"偵測到隱形的自爆部隊！");
//				}
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-32);
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.CREEPER, ChatColor.GREEN+"凋零軍團自爆連", temp, true);
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(0,0,-32);
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.CREEPER, ChatColor.GREEN+"凋零軍團自爆連", temp, true);
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0,0,-32);
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.CREEPER, ChatColor.GREEN+"凋零軍團自爆連", temp, true);
//				}
//			}
//			if(wave == 7 && wave_time == 110)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 0.6 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團近衛", temp, false);
//					e.setMaxHealth(38D);
//					e.setHealth(38D);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 0.6 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團近衛", temp, false);
//					e.setMaxHealth(38D);
//					e.setHealth(38D);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 0.6 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"凋零軍團近衛", temp, false);
//					e.setMaxHealth(38D);
//					e.setHealth(38D);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				}
//			}
//			if(wave == 7 && wave_time > 0 && wave_time < 70 && wave_time % 5 == 0)
//			{
//				if(checkentitydata())
//				{
//					bonus_time = bonus_time + wave_time;
//					wave_time = 0;
//				}
//			}
//			if(wave == 7 && wave_time == 1)
//			{
//				wavefinalremove();
//			}
//			//wave 6 methods
//			if(wave == 6 && wave_time == 0)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GREEN+"WAVE 5 FINISH!");
//				}
//				baselist.get("BLUE").getWorld().setStorm(false);
//				baselist.get("BLUE").getWorld().setThundering(false);
//				baselist.get("BLUE").getWorld().setWeatherDuration(1000000);
//				wave_time = -30;
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "revclass supply confirm");
//			}
//			if(wave == 6 && wave_time == -1)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GOLD+"WAVE 6 START!");
//				}
//				
//				wave_time = 240;
//				baselist.get("BLUE").getWorld().setTime(5000);
//			}
//			if(wave == 6 && wave_time == 240)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-45);
//				for(int i = 0; i < player_count * 0.3 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.GRAY+"天災遠程兵", temp, false);
//					ch.changeattack(e);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.9 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//			}
//			if(wave == 6 && wave_time == 235)
//			{
//				temp = baselist.get("AQUA").clone();
//				temp.add(-20,0,-35);
//				for(int i = 0; i < player_count * 0.3 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.SKELETON, ChatColor.GRAY+"天災遠程兵", temp, false);
//					ch.changeattack(e);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.9 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(20,0,-35);
//				for(int i = 0; i < player_count * 0.3 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.SKELETON, ChatColor.GRAY+"天災遠程兵", temp, false);
//					ch.changeattack(e);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.9 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//			}
//			if(wave == 6 && wave_time == 215)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-50);
//				for(int i = 0; i < player_count * 0.3 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"天災遠程小兵", temp, false);
//					ch.changeattack(e);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.9 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(-20,0,-35);
//				for(int i = 0; i < player_count * 0.2 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.SKELETON, ChatColor.WHITE+"天災遠程兵", temp, false);
//					ch.changeattack(e);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.6 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(20,0,-35);
//				for(int i = 0; i < player_count * 0.2 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.SKELETON, ChatColor.WHITE+"天災遠程兵", temp, false);
//					ch.changeattack(e);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.6 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//			}
//			if(wave == 6 && wave_time == 180)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-50);
//				for(int i = 0; i < player_count * 0.9 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"天災遠程兵", temp, false);
//					ch.changeattack(e);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 1);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 1.9 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.4 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.CREEPER, ChatColor.GREEN+"地精工程師", temp, true);
//				}
//			}
//			if(wave == 6 && wave_time == 160)
//			{
//				temp = baselist.get("AQUA").clone();
//				temp.add(0,0,-50);
//				e = ch.summonCreatures(baselist, "AQUA", CreatureType.GIANT, ChatColor.DARK_PURPLE+"腐肉山型巴特勒", temp, true);
//				e.setMaxHealth(100 * player_count * difficulty);
//				e.setHealth(100 * player_count * difficulty);
//				ch.changeattack(e);
//				Giant gian = (Giant)e;
//				CraftGiant skelc = (CraftGiant)gian;
//		        EntityGiantZombie skelMC = skelc.getHandle();
//		        ItemStack item = new ItemStack(Item.getById(261));
//		        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 10);
//		        skelMC.setEquipment(0, item);
//		        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//		        skelMC.setEquipment(2, new ItemStack(Item.getById(316)));
//		        skelMC.setEquipment(3, new ItemStack(Item.getById(315)));
//		        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//		        temp = baselist.get("PURPLE").clone();
//				temp.add(0,0,-50);
//				e = ch.summonCreatures(baselist, "PURPLE", CreatureType.GIANT, ChatColor.DARK_PURPLE+"腐肉山型巴特勒", temp, true);
//				e.setMaxHealth(100 * player_count * difficulty);
//				e.setHealth(100 * player_count * difficulty);
//				ch.changeattack(e);
//				gian = (Giant)e;
//				skelc = (CraftGiant)gian;
//		        skelMC = skelc.getHandle();
//		        item = new ItemStack(Item.getById(261));
//		        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 10);
//		        skelMC.setEquipment(0, item);
//		        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//		        skelMC.setEquipment(2, new ItemStack(Item.getById(316)));
//		        skelMC.setEquipment(3, new ItemStack(Item.getById(315)));
//		        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			}
//			if(wave == 6 && wave_time == 120)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-50);
//				for(int i = 0; i < player_count * 0.8 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"天災遠程兵", temp, false);
//					ch.changeattack(e);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 1.6 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 2);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.3 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.CREEPER, ChatColor.GREEN+"地精工程師", temp, true);
//				}
//			}
//			if(wave == 6 && wave_time == 80)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-50);
//				for(int i = 0; i < player_count * 0.4 * difficulty; i ++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.WHITE+"天災遠程兵", temp, false);
//					ch.changeattack(e);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(261));
//			        item.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
//			        skelMC.setEquipment(0, item);
//			        skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.8 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack item = new ItemStack(Item.getById(272));
//			        item.addEnchantment(Enchantment.KNOCKBACK, 2);
//			        zombMC.setEquipment(0, item);
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(317)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//			}
//			if(wave == 6 && wave_time > 0 && wave_time < 60 && wave_time % 5 == 0)
//			{
//				if(checkentitydata())
//				{
//					bonus_time = bonus_time + wave_time;
//					wave_time = 0;
//				}
//			}
//			if(wave == 6 && wave_time == 1)
//			{
//				wavefinalremove();
//			}
//			//wave 5 methods
//			if(wave == 5 && wave_time == 0)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GREEN+"WAVE 4 FINISH!");
//				}
//				baselist.get("BLUE").getWorld().setStorm(false);
//				baselist.get("BLUE").getWorld().setThundering(false);
//				baselist.get("BLUE").getWorld().setWeatherDuration(1000000);
//				wave_time = -60;
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "revclass supply confirm");
//			}
//			if(wave == 5 && wave_time == -55)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GREEN+"中場休息60秒！");
//				}
//			}
//			if(wave == 5 && wave_time == -45)
//			{
//				gettop();
//			}
//			if(wave == 5 && wave_time == -15)
//			{
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard objectives setdisplay sidebar cjfw");
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.RED+"下一波敵人還有15秒到達戰場！");
//				}
//			}
//			if(wave == 5 && wave_time == -1)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GOLD+"WAVE 5 START!");
//				}
//				
//				wave_time = 210;
//				baselist.get("BLUE").getWorld().setTime(16000);
//			}
//			if(wave == 5 && wave_time == 208)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.RED+"清冥之加護：敵方行進速度增加20%！");
//				}
//			}
//			if(wave == 5 && wave_time == 206)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.RED+"左塔出現了強大的敵人！");
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(0,0,-40);
//				temp.getWorld().strikeLightningEffect(temp);
//				e = ch.summonCreatures(baselist, "AQUA" , CreatureType.ZOMBIE, ChatColor.RED+"Yan's WIFI", temp, true);
//				
//				e.setMaxHealth(200 * player_count * difficulty);
//				e.setHealth(200 * player_count * difficulty);
//				ch.changespeed(e);
//				Zombie zomb = (Zombie)e;
//				CraftZombie zombc = (CraftZombie)zomb;
//		        EntityZombie zombMC = zombc.getHandle();
//				ItemStack item = new ItemStack(Item.getById(267));
//				item.addEnchantment(Enchantment.DAMAGE_ALL, 1);
//				item.addEnchantment(Enchantment.KNOCKBACK, 1);
//				zombMC.setEquipment(0, item);
//				zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//				zombMC.setEquipment(2, new ItemStack(Item.getById(308)));
//				zombMC.setEquipment(3, new ItemStack(Item.getById(307)));
//				zombMC.setEquipment(4, new ItemStack(Item.getById(306)));
//			}
//			if(wave == 5 && wave_time == 205)
//			{
//				temp = baselist.get("AQUA").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 1 * difficulty; i ++)
//				{
//				e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//				ch.changespeed(e);
//				Zombie zomb = (Zombie)e;
//				CraftZombie zombc = (CraftZombie)zomb;
//		        EntityZombie zombMC = zombc.getHandle();
//		        zombMC.setEquipment(0, new ItemStack(Item.getById(269)));
//		        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//		        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.3 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.WHITE+"幽靈", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(261)));
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//			        zombMC.setEquipment(2, new ItemStack(Item.getById(313)));
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(312)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-45);
//				for(int i = 0; i < player_count * 1.5 * difficulty; i ++)
//				{
//				e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//				ch.changespeed(e);
//				Zombie zomb = (Zombie)e;
//				CraftZombie zombc = (CraftZombie)zomb;
//		        EntityZombie zombMC = zombc.getHandle();
//		        zombMC.setEquipment(0, new ItemStack(Item.getById(269)));
//		        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//		        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"幽靈", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(261)));
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//			        zombMC.setEquipment(2, new ItemStack(Item.getById(313)));
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(312)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 1.5 * difficulty; i ++)
//				{
//				e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.WHITE+"鬼魂", temp, false);
//				ch.changespeed(e);
//				Zombie zomb = (Zombie)e;
//				CraftZombie zombc = (CraftZombie)zomb;
//		        EntityZombie zombMC = zombc.getHandle();
//		        zombMC.setEquipment(0, new ItemStack(Item.getById(269)));
//		        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//		        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.3 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.WHITE+"幽靈", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(261)));
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//			        zombMC.setEquipment(2, new ItemStack(Item.getById(313)));
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(312)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//			}
//			if(wave == 5 && wave_time == 160)
//			{
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 1 * difficulty; i ++)
//				{
//				e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.WHITE+"僵尸(真的)", temp, false);
//				ch.changespeed(e);
//				Zombie zomb = (Zombie)e;
//				CraftZombie zombc = (CraftZombie)zomb;
//		        EntityZombie zombMC = zombc.getHandle();
//		        zombMC.setEquipment(0, new ItemStack(Item.getById(267)));
//		        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//		        zombMC.setEquipment(2, new ItemStack(Item.getById(313)));
//		        zombMC.setEquipment(3, new ItemStack(Item.getById(312)));
//		        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 1 * difficulty; i ++)
//				{
//				e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.WHITE+"僵尸(真的)", temp, false);
//				ch.changespeed(e);
//				Zombie zomb = (Zombie)e;
//				CraftZombie zombc = (CraftZombie)zomb;
//		        EntityZombie zombMC = zombc.getHandle();
//		        zombMC.setEquipment(0, new ItemStack(Item.getById(267)));
//		        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//		        zombMC.setEquipment(2, new ItemStack(Item.getById(313)));
//		        zombMC.setEquipment(3, new ItemStack(Item.getById(312)));
//		        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//			}
//			if(wave == 5 && wave_time == 110)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 0.6 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.WHITE+"僵尸(真的)", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(267)));
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//			        zombMC.setEquipment(2, new ItemStack(Item.getById(313)));
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(312)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.6 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"幽靈", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(261)));
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//			        zombMC.setEquipment(2, new ItemStack(Item.getById(313)));
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(312)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//			}
//			if(wave == 5 & wave_time == 80)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 0.4 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.WHITE+"僵尸(真的)", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(267)));
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//			        zombMC.setEquipment(2, new ItemStack(Item.getById(313)));
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(312)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.4 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.WHITE+"幽靈", temp, false);
//					ch.changespeed(e);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(261)));
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(309)));
//			        zombMC.setEquipment(2, new ItemStack(Item.getById(313)));
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(312)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//			}
//			if(wave == 5 && wave_time > 0 && wave_time < 60 && wave_time % 5 == 0)
//			{
//				if(checkentitydata())
//				{
//					bonus_time = bonus_time + wave_time;
//					wave_time = 0;
//				}
//			}
//			if(wave == 5 && wave_time == 1)
//			{
//				wavefinalremove();
//			}
//			//wave 4 methods
//			if(wave == 4 && wave_time == 0)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GREEN+"WAVE 3 FINISH!");
//				}
//				baselist.get("BLUE").getWorld().setStorm(false);
//				baselist.get("BLUE").getWorld().setThundering(false);
//				baselist.get("BLUE").getWorld().setWeatherDuration(1000000);
//				wave_time = -30;
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "revclass supply confirm");
//			}
//			if(wave == 4 && wave_time == -25)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage("防衛結束后會根據基地剩餘血量決定獎勵數量！");
//				}
//			}
//			if(wave == 4 && wave_time == -15)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage("如果防衛失敗的話只會拿到少量參與獎，請小心應對！");
//				}
//			}
//			if(wave == 4 && wave_time == -1)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GOLD+"WAVE 4 START!");
//				}
//				
//				wave_time = 150;
//				baselist.get("BLUE").getWorld().setTime(4000);
//			}
//			if(wave == 4 && wave_time == 147)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					
//					Bukkit.broadcastMessage(ChatColor.YELLOW+"左塔附近出現了大量的怪物！");
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 1.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.GRAY+"动员兵", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(270)));
//				}
//				temp.add(0,0,-5);
//				for(int i = 0; i < player_count * 1.0 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.SKELETON, ChatColor.GRAY+"防空(假的)步兵", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        skelMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        skelMC.setEquipment(0, new ItemStack(Item.getById(261)));
//				}
//				temp.add(0,0,5);
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.GRAY+"恐怖分子", temp, false);
//					Zombie zomb = (Zombie)e;
//					zomb.setBaby(true);
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(272)));
//				}
//			}
//			if(wave == 4 && wave_time == 140)
//			{
//				temp = baselist.get("AQUA").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.GRAY+"恐怖分子", temp, false);
//					Zombie zomb = (Zombie)e;
//					zomb.setBaby(true);
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(272)));
//				}
//				for(int i = 0; i < player_count * 1.0 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.GRAY+"动员兵", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(270)));
//				}
//			}
//			if(wave == 4 && wave_time == 110)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					
//					Bukkit.broadcastMessage(ChatColor.YELLOW+"中塔附近出現了大量的怪物！");
//				}
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 1.0 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.GRAY+"重装大兵(没穿鞋的)", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(270)));
//				}
//				temp.add(0,0,-5);
//				for(int i = 0; i < player_count * 1.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.GRAY+"美国大(新)兵", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        skelMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        skelMC.setEquipment(0, new ItemStack(Item.getById(261)));
//				}
//				temp.add(0,0,5);
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.GRAY+"自爆机器(划掉)人", temp, false);
//					Zombie zomb = (Zombie)e;
//					zomb.setBaby(true);
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(272)));
//				}
//			}
//			if(wave == 4 && wave_time == 100)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.GRAY+"自爆机器(划掉)人", temp, false);
//					Zombie zomb = (Zombie)e;
//					zomb.setBaby(true);
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(272)));
//				}
//				for(int i = 0; i < player_count * 1.0 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.GRAY+"美国大(新)兵", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(270)));
//				}
//			}
//			if(wave == 4 && wave_time == 70)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					
//					Bukkit.broadcastMessage(ChatColor.YELLOW+"右塔附近出現了大量的怪物！");
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 1.0 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.GRAY+"尤裡(被)控制人", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(270)));
//				}
//				temp.add(0,0,-5);
//				for(int i = 0; i < player_count * 1.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.SKELETON, ChatColor.GRAY+"尤裡新兵", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        skelMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        skelMC.setEquipment(0, new ItemStack(Item.getById(261)));
//				}
//				temp.add(0,0,5);
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.GRAY+"（回收用）市民", temp, false);
//					Zombie zomb = (Zombie)e;
//					zomb.setBaby(true);
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(272)));
//				}
//			}
//			if(wave == 4 && wave_time == 60)
//			{
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.GRAY+"尤裡敢死隊", temp, false);
//					Zombie zomb = (Zombie)e;
//					zomb.setBaby(true);
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(272)));
//				}
//				for(int i = 0; i < player_count * 1.0 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.GRAY+"尤裡老(真的很老)兵", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(270)));
//				}
//			}
//			if(wave == 4 && wave_time > 0 && wave_time < 50 && wave_time % 5 == 0)
//			{
//				if(checkentitydata())
//				{
//					bonus_time = bonus_time + wave_time;
//					wave_time = 0;
//				}
//			}
//			if(wave == 4 && wave_time == 1)
//			{
//				wavefinalremove();
//			}
//			//wave 3 methods
//			if(wave == 3 && wave_time == 0)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GREEN+"WAVE 2 FINISH!");
//				}
//				baselist.get("BLUE").getWorld().setStorm(false);
//				baselist.get("BLUE").getWorld().setThundering(false);
//				baselist.get("BLUE").getWorld().setWeatherDuration(1000000);
//				wave_time = -30;
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "revclass supply confirm");
//			}
//			if(wave == 3 && wave_time == -25)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage("總計九輪的防禦過後會根據人頭數排名頒發榮譽獎！");
//				}
//				gettop();
//			}
//			if(wave == 3 && wave_time == -20)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage("榮譽獎不會過高，請各位專心于防衛！");
//				}
//			}
//			if(wave == 3 && wave_time == -15)
//			{
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard objectives setdisplay sidebar cjfw");
//				sb.getObjective("contri_point").setDisplaySlot(DisplaySlot.BELOW_NAME);
//			}
//			if(wave == 3 && wave_time == -1)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GOLD+"WAVE 3 START!");
//				}
//				
//				wave_time = 180;
//				baselist.get("BLUE").getWorld().setTime(14000);
//			}
//			if(wave == 3 && wave_time == 180)
//			{
//				temp = baselist.get("AQUA").clone();
//				temp.add(0, 0, -30);
//				for(int i = 0; i < player_count * 1.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒工兵+", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//				}
//				temp.add(0,0,-5);
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.GREEN+"先鋒精英工兵", temp, true);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(268)));
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(301)));
//			        zombMC.setEquipment(2, new ItemStack(Item.getById(300)));
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(298)));
//				} 
//				temp.add(0,0,-5);
//				for(int i = 0; i < player_count * 0.1 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.CREEPER, "先鋒自爆連", temp, true);
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0,0,-30);
//				for(int i = 0; i < player_count * 1.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒敢死隊", temp, false);
//					Zombie zomb = (Zombie)e;
//					zomb.setBaby(true);
//				}
//				temp.add(0,0,-5);
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.GREEN+"先鋒精英工兵", temp, true);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(0, new ItemStack(Item.getById(283)));
//			        zombMC.setEquipment(1, new ItemStack(Item.getById(301)));
//			        zombMC.setEquipment(2, new ItemStack(Item.getById(300)));
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(298)));
//				}
//				temp.add(0,0,-5);
//				for(int i = 0; i < player_count * 0.1 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.CREEPER, "先鋒自爆連", temp, true);
//				}
//			}
//			if(wave == 3 && wave_time == 160)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0, 0, -40);
//				for(int i = 0; i < player_count * 1.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒工兵+", temp, false);
//				}
//				temp.add(0,0,-5);
//				for(int i = 0; i < player_count * 1.0 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.GRAY+"先鋒弓箭手", temp, false);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        skelMC.setEquipment(0, new ItemStack(Item.getById(261)));
//				}
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.CREEPER, "先鋒自爆連", temp, true);
//				}
//			}
//			if(wave == 3 && wave_time == 150)
//			{
//				temp = baselist.get("AQUA").clone();
//				temp.add(-20,0,-30);
//				for(int i = 0; i < player_count * 1.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒工兵+", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//				}
//				temp.add(20,0,-15);
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.CREEPER, "先鋒自爆連", temp, true);
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(20,0,-30);
//				for(int i = 0; i < player_count * 1.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒工兵+", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(3, new ItemStack(Item.getById(299)));
//				}
//				temp.add(-20,0,-15);
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.CREEPER, "先鋒自爆連", temp, true);
//				}
//			}
//			if(wave == 3 && wave_time == 124)
//			{
//				Bukkit.broadcastMessage(ChatColor.RED+"变异型巴特勒：【就算没有魔王的指示，我也能一个人把你们全部打烂！】");
//			}
//			if(wave == 3 && wave_time == 120)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.YELLOW+"中塔附近出現了強大的敵人！");
//				}
//				baselist.get("BLUE").getWorld().setStorm(true);
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-40);
//				e = ch.summonCreatures(baselist, "BLUE" , CreatureType.SKELETON, ChatColor.RED+"变异型巴特勒", temp, true);
//				e.setMaxHealth(150 * player_count * difficulty);
//				e.setHealth(150 * player_count * difficulty);
//				Skeleton skel = (Skeleton)e;
//				CraftSkeleton skelc = (CraftSkeleton)skel;
//				EntitySkeleton skelMC = skelc.getHandle();
//				ItemStack item = new ItemStack(Item.getById(283));
//				item.addEnchantment(Enchantment.DAMAGE_ALL, 3);
//				skelMC.setEquipment(0, item);
//				skelMC.setEquipment(1, new ItemStack(Item.getById(317)));
//				skelMC.setEquipment(2, new ItemStack(Item.getById(316)));
//				skelMC.setEquipment(3, new ItemStack(Item.getById(315)));
//				skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				 
//				for(int i = 0; i < player_count * 1.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒工兵+", temp, false);
//				}
//				temp.add(0,0,-5);
//				for(int i = 0; i < player_count * 1.0 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.GRAY+"先鋒弓箭手", temp, false);
//					skel = (Skeleton)e;
//					skelc = (CraftSkeleton)skel;
//					skelMC = skelc.getHandle();
//					skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//					skelMC.setEquipment(0, new ItemStack(Item.getById(261)));
//				}
//				for(int i = 0; i < player_count * 0.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.CREEPER, "先鋒自爆連", temp, true);
//				}
//			        
//			}
//			if(wave == 3 && wave_time == 70)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.YELLOW+"中塔附近出現了大量的怪物！");
//				}
//				temp = baselist.get("BLUE").clone();
//				temp.add(0,0,-40);
//				for(int i = 0; i < player_count * 3.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.SKELETON, ChatColor.GRAY+"先鋒弓箭手", temp, true);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        skelMC.setEquipment(0, new ItemStack(Item.getById(261)));
//				}
//			}
//			if(wave == 3 && wave_time == 40)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.YELLOW+"偷襲部隊時刻有可能出現，請小心應對！");
//				}
//				
//			}
//			if(wave == 3 && wave_time > 0 && wave_time < 61 && wave_time % 5 == 0)
//			{
//				if(checkentitydata())
//				{
//					bonus_time = bonus_time + wave_time;
//					wave_time = 0;
//				}
//			}
//			if(wave == 3 && wave_time == 1)
//			{
//				wavefinalremove();
//				Bukkit.broadcastMessage(ChatColor.RED+"变异型巴特勒：可恶啊！！要是再多给我一点时间的话...");
//			}
//			//wave 2 methods
//			if(wave == 2 && wave_time == 0)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage("每波之间有30秒钟休息时间！");
//				}
//				baselist.get("BLUE").getWorld().setStorm(false);
//				baselist.get("BLUE").getWorld().setThundering(false);
//				baselist.get("BLUE").getWorld().setWeatherDuration(1000000);
//				wave_time = -30;
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "revclass supply confirm");
//			}
//			if(wave == 2 && wave_time == -25)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GREEN+"WAVE 1 FINISH!");
//				}
//			}
//			if(wave == 2 && wave_time == -20)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GREEN+"每波結束各職業都會有不同的補給品發出!");
//				}
//			}
//			if(wave == 2 && wave_time == -15)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GREEN+"請善加利用！");
//				}
//			}
//			if(wave == 2 && wave_time == -1)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GOLD+"WAVE 2 START!");
//				}
//				wave_time = 150;
//			}
//			if(wave == 2 && wave_time == 150)
//			{
//				baselist.get("BLUE").getWorld().setTime(0);
//				temp = baselist.get("BLUE").clone();
//				temp.add(0, 0, -40);
//				for(int i = 0; i < player_count * 1.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒工兵", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(0, 0, -40);
//				for(int i = 0; i < player_count * 1.2 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒工兵", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0, 0, -40);
//				for(int i = 0; i < player_count * 1.2 * difficulty; i++)
//				{
//					
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒工兵", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//			}
//			if(wave == 2 && wave_time == 120)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0, 0, -40);
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒工兵", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.7 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.GRAY+"先鋒工兵", temp, true);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        skelMC.setEquipment(0, new ItemStack(Item.getById(261)));
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(-20, 0, -30);
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒工兵", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.7 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.SKELETON, ChatColor.GRAY+"先鋒弓箭手", temp, true);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        skelMC.setEquipment(0, new ItemStack(Item.getById(261)));
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(20, 0, -30);
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒工兵", temp, false);
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				for(int i = 0; i < player_count * 0.7 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.SKELETON, ChatColor.GRAY+"先鋒弓箭手", temp, true);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        skelMC.setEquipment(0, new ItemStack(Item.getById(261)));
//				}
//			}
//			if(wave == 2 && wave_time == 90)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0, 0, -40);
//				for(int i = 0; i < player_count * 1.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒敢死隊", temp, false);
//					Zombie zomb = (Zombie)e;
//					zomb.setBaby(true);
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(0, 0, -40);
//				for(int i = 0; i < player_count * 1.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒敢死隊", temp, false);
//					Zombie zomb = (Zombie)e;
//					zomb.setBaby(true);
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0, 0, -40);
//				for(int i = 0; i < player_count * 1.5 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.GRAY+"先鋒敢死隊", temp, false);
//					Zombie zomb = (Zombie)e;
//					zomb.setBaby(true);
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.setEquipment(4, new ItemStack(Item.getById(314)));
//				}
//			}
//			if(wave == 2 && wave_time == 60)
//			{
//				temp = baselist.get("PURPLE").clone();
//				temp.add(20, 0, -30);
//				for(int i = 0; i < player_count * 0.7 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "PURPLE", CreatureType.SKELETON, "拿錯裝備的", temp, true);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        skelMC.setEquipment(0, new ItemStack(Item.getById(268)));
//				}
//				temp = baselist.get("BLUE").clone();
//				temp.add(0, 0, -40);
//				for(int i = 0; i < player_count * 0.7 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, "拿錯裝備的", temp, true);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        skelMC.setEquipment(0, new ItemStack(Item.getById(268)));
//				}
//				temp = baselist.get("AQUA").clone();
//				temp.add(-20, 0, -30);
//				for(int i = 0; i < player_count * 0.7 * difficulty; i++)
//				{
//					e = ch.summonCreatures(baselist, "AQUA", CreatureType.SKELETON, "拿錯裝備的", temp, true);
//					Skeleton skel = (Skeleton)e;
//					CraftSkeleton skelc = (CraftSkeleton)skel;
//			        EntitySkeleton skelMC = skelc.getHandle();
//			        skelMC.setEquipment(4, new ItemStack(Item.getById(314)));
//			        skelMC.setEquipment(0, new ItemStack(Item.getById(268)));
//				}
//			}
//			if(wave == 2 && wave_time > 0 && wave_time < 55 && wave_time % 5 == 0)
//			{
//				if(checkentitydata())
//				{
//					bonus_time = bonus_time + wave_time;
//					wave_time = 0;
//				}
//			}
//			if(wave == 2 && wave_time == 1)
//			{
//				wavefinalremove();
//			}
//			//wave 1 methods
//			if(wave == 1 && wave_time == 0)
//			{
//				for(int i = 0; i < 8; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GOLD+"WAVE 1 START!");
//				}
//				
//				wave_time = 120;
//				baselist.get("BLUE").getWorld().setTime(14000);
//				
//					temp = baselist.get("BLUE").clone();
//					temp.add(0, 0, -40);
//					for(int i = 0; i < player_count * 1.5 * difficulty; i++)
//					{
//						ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.GRAY+"渣渣", temp, false);
//					}
//			}
//			if(wave == 1 && wave_time == 110)
//			{
//				temp = baselist.get("AQUA").clone();
//				temp.add(0, 0, -40);
//				for(int i = 0; i < player_count * 1 * difficulty; i++)
//				{
//					ch.summonCreatures(baselist, "AQUA", CreatureType.SKELETON, ChatColor.GRAY+"炮灰", temp, false);
//				}
//			}
//			if(wave == 1 && wave_time == 100)
//			{
//				temp = baselist.get("PURPLE").clone();
//				temp.add(0, 0, -35);
//				for(int i = 0; i < player_count * 1 * difficulty; i++)
//				{
//					ch.summonCreatures(baselist, "PURPLE", CreatureType.SKELETON, ChatColor.GRAY+"菜鳥", temp, false);
//				}
//				for(int i = 0; i < player_count * 0.25 * difficulty; i++)
//				{
//					ch.summonCreatures(baselist, "PURPLE", CreatureType.ZOMBIE, ChatColor.GRAY+"難民", temp, false);
//				}
//			}
//			if(wave == 1 && wave_time == 90)
//			{
//				temp = baselist.get("AQUA").clone();
//				temp.add(-20, 0 ,-30);
//				for(int i = 0; i < player_count * 0.75 * difficulty; i++)
//				{
//					ch.summonCreatures(baselist, "AQUA", CreatureType.SKELETON, ChatColor.GRAY+"雜魚", temp, false);
//				}	
//			}
//			if(wave == 1 && wave_time == 80)
//			{
//				temp = baselist.get("BLUE").clone();
//				temp.add(0, 0, -40);
//				for(int i = 0; i < player_count * 0.5 * difficulty; i++)
//				{
//					ch.summonCreatures(baselist, "BLUE", CreatureType.SKELETON, ChatColor.GRAY+"兵痞", temp, false);
//				}
//			}
//			if(wave == 1 && wave_time == 60)
//			{
//				temp = baselist.get("PURPLE").clone();
//				temp.add(20, 0 , -30);
//				for(int i = 0; i < player_count * 1 * difficulty; i++)
//				{
//					ch.summonCreatures(baselist, "PURPLE", CreatureType.SKELETON, ChatColor.GRAY+"民兵", temp, false);
//				}
//			}
//			if(wave == 1 && wave_time > 0 && wave_time < 60 && wave_time % 5 == 0)
//			{
//				if(checkentitydata())
//				{
//					bonus_time = bonus_time + wave_time;
//					wave_time = 0;
//				}
//			}
//			if(wave == 1 && wave_time == 1)
//			{
//				wavefinalremove();
//			}
//			//wave 0 methods
//			if(wave == 0 && wave_time == -1)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage("欢迎各位参加防卫战的勇士们！");
//				}
//				wave_time = 90;
//			}
//			if(wave == 0 && wave_time == 75)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage("敌人即将入侵采掘基地，请各位利用手中的武器保护基地！");
//				}
//				//temp = baselist.get("PURPLE").clone();
//				//temp.add(20, 0 , -20);
//				//tp = ch.summonCreatures(baselist, "PURPLE", CreatureType.SKELETON, ChatColor.GRAY+"民兵", temp, false);
//			}
//			if(wave == 0 && wave_time == 60)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage("请避免让近程怪物靠近红圈，远程怪物靠近黄圈！进入该范围的怪物将会使基地损伤！");
//				}
//				//if(tp.isDead())
//				//{
//				//	Bukkit.broadcastMessage("you got it.");
//				//}
//			}
//			if(wave == 0 && wave_time == 50)
//			{
//				for(int i = 0; i < 2; i++)
//				{
//					Bukkit.broadcastMessage("撑过一波的方法有两种！");
//					Bukkit.broadcastMessage(ChatColor.RED+"其一是wave时间耗尽！");
//					Bukkit.broadcastMessage(ChatColor.GREEN+"其二是将所有怪物歼灭！");
//				}
//			}
//			if(wave == 0 && wave_time == 45)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.RED+"敌军还有45秒到达战场！");
//				}
//			}
//			if(wave == 0 && wave_time == 30)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage("自爆类的怪物在绿圈内自爆会对基地造成大损伤！请一定注意！");
//					}
//				}
//				if(wave == 0 && wave_time == 20)
//				{
//					for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage("每位玩家均有兩次开启某一塔塔防和恢复塔受损耐久值的机会！可以在塔的后方开启，请好好利用！");
//				}
//			}
//			if(wave == 0 && wave_time == 15)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage("每波会有额外的道具提供！请法师记得清理物品栏！");
//				}
//			}
//			if(wave == 0 && wave_time == 10)
//			{
//				for(int i = 0; i < 4; i++)
//				{
//					Bukkit.broadcastMessage(ChatColor.GOLD+"介绍就到这里，以上！祝各位武运昌隆！");
//				}
//			}
//			//the update of wave_time should be the last step before refresh scoreboard
//			if(wave_time < 0)
//			{
//				wave_time ++;
//			}
//			if(wave_time > 0)
//			{
//				wave_time --;
//			}
//			
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set Wave cjfw " + wave);
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set Time cjfw " + wave_time);
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set BLUE cjfw " + Base_HP.get("BLUE"));
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set AQUA cjfw " + Base_HP.get("AQUA"));
//				Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "scoreboard players set PURPLE cjfw " + Base_HP.get("PURPLE"));
//				if(Base_HP.get("BLUE") == 0 && Base_HP.get("AQUA") == 0 && Base_HP.get("PURPLE") == 0)
//				{
//					finish = true;
//				}
//		}
//		if(!finish && wave9_switch && !bonus_switch)
//		{
//			String rank = calculate_rank();
//			Bukkit.broadcastMessage(ChatColor.GREEN + "防衛成功！");
//			Bukkit.broadcastMessage(ChatColor.BLUE + "本次防衛的評價為：");
//			Bukkit.broadcastMessage(ChatColor.GOLD + rank + "!");
//			if(bonus_time > 180)
//			{
//				bonus_switch = true;
//				back_timer = -10;
//				for(int i = 0; i < 3; i++)
//				{
//					baselist.get("BLUE").getWorld().setTime(0000);
//					Bukkit.broadcastMessage("一分鐘后傳送主城！");
//				}
//			}
//			else
//			{
//				gettop();
//				for(int i = 0; i < 3; i++)
//				{
//					Bukkit.broadcastMessage("活動結束！請等待傳送和獎勵發放！");
//				}
//				finish = true;
//			}
//		}
//		//HIDDEN BOSS
//		if(!finish && wave9_switch && bonus_switch && !bonus_finish_switch)
//		{
//			if(back_timer < 0 )
//			{
//				back_timer ++;
//			}
//			else
//			{
//				if(back_timer == 0 && !last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 4; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.YELLOW+"【傳送主城...？有意思。】");
//					}
//				}
//				if(back_timer == 5 && !last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 4; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.RED + "【費盡千辛萬苦才等到這麼一個好機會...】");
//					}
//				}
//				if(back_timer == 15 && !last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 4; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "吾烈焰魔王【伊諾增忒斯】會讓你們這麼容易逃走嗎？！");
//					}
//				}
//				if(back_timer == 20 && !last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 4; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "爲了讓你們輕敵，吾可是故意偽裝成深淵魔王進攻啊...");
//					}
//				}
//				if(back_timer == 25 && !last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 4; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "沒想到你們這麼容易就上當了...真是可笑啊...");
//					}
//				}
//				if(back_timer == 30 && !last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 4; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "哼...吾可是會出全力的，看你們能撐到什麼時候！");
//					}
//				}
//				if(back_timer == 32 && !last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					temp = baselist.get("BLUE").clone();
//					temp.add(0, 0, -40);
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.DARK_RED+"烈焰魔王Innocentius", temp, true);
//					e.setMaxHealth(300 * player_count * difficulty);
//					e.setHealth(300 * player_count * difficulty);
//					ch.changespeed(e);
//					ch.changeattack(e);
//					last_boss = e;
//					Zombie zomb = (Zombie)e;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        ItemStack i = new ItemStack(Item.getById(276));
//			        i.addEnchantment(Enchantment.DAMAGE_ALL, 5);
//			        i.addEnchantment(Enchantment.FIRE_ASPECT, 2);
//			        i.addEnchantment(Enchantment.KNOCKBACK, 4);
//			        zombMC.setEquipment(0, i);
//			        i = new ItemStack(Item.getById(305));
//			        i.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//			        zombMC.setEquipment(1, i);
//			        i = new ItemStack(Item.getById(304));
//			        i.addEnchantment(Enchantment.PROTECTION_FIRE, 1);
//			        zombMC.setEquipment(2, i);
//			        i = new ItemStack(Item.getById(303));
//			        i.addEnchantment(Enchantment.THORNS, 2);
//			        zombMC.setEquipment(3, i);
//			        i = new ItemStack(Item.getById(302));
//			        i.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
//			        zombMC.setEquipment(4, i);
//				}
//				if(back_timer == 37 && !last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 4; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.YELLOW + "最終boss:Innocentius出現了！");
//					}
//				}
//				if(back_timer == 40 && !last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 4; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.YELLOW + "本回合不計算時間，不計算總評價，不計算對塔的傷害。");
//					}
//				}
//				if(last_boss != null && !last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					Damageable a = (Damageable)last_boss;
//					if((a.getHealth() / a.getMaxHealth() < 0.7) && !last_boss_phase1 && !last_boss_phase3)
//					{
//						last_boss_phase1 = true;
//						back_timer = 0;
//					}
//				}
//				if(back_timer == 0 && last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 哦？頗有點意思呢。");
//					}
//				}
//				if(back_timer == 3 && last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 出來吧！吾之守護！");
//					}
//				}
//				if(back_timer == 5 && last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					last_boss.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 1000000, 5), true);
//					e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.AQUA+"烈風", last_boss.getLocation(), true);
//					e.setMaxHealth(40 * player_count * difficulty);
//					e.setHealth(40 * player_count * difficulty);
//					Zombie zomb = (Zombie)e;
//					last_boss1 = zomb;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//					ItemStack i = new ItemStack(Item.getById(276));
//			        i.addEnchantment(Enchantment.DAMAGE_ALL, 1);
//			        i.addEnchantment(Enchantment.KNOCKBACK, 3);
//			        zombMC.setEquipment(0, i);
//			        i = new ItemStack(Item.getById(305));
//			        zombMC.setEquipment(1, i);
//			        i = new ItemStack(Item.getById(304));
//			        zombMC.setEquipment(2, i);
//			        i = new ItemStack(Item.getById(303));
//			        zombMC.setEquipment(3, i);
//			        i = new ItemStack(Item.getById(302));
//			        zombMC.setEquipment(4, i);
//			        e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.DARK_GREEN+"森木", last_boss.getLocation(), true);
//					e.setMaxHealth(40 * player_count * difficulty);
//					e.setHealth(40 * player_count * difficulty);
//					zomb = (Zombie)e;
//					last_boss2 = zomb;
//					zombc = (CraftZombie)zomb;
//			        zombMC = zombc.getHandle();
//					i = new ItemStack(Item.getById(276));
//			        i.addEnchantment(Enchantment.DAMAGE_ALL, 3);
//			        i.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        zombMC.setEquipment(0, i);
//			        i = new ItemStack(Item.getById(305));
//			        zombMC.setEquipment(1, i);
//			        i = new ItemStack(Item.getById(304));
//			        zombMC.setEquipment(2, i);
//			        i = new ItemStack(Item.getById(303));
//			        zombMC.setEquipment(3, i);
//			        i = new ItemStack(Item.getById(302));
//			        zombMC.setEquipment(4, i);
//			        e = ch.summonCreatures(baselist, "BLUE", CreatureType.ZOMBIE, ChatColor.RED+"焱魔", last_boss.getLocation(), true);
//					e.setMaxHealth(40 * player_count * difficulty);
//					e.setHealth(40 * player_count * difficulty);
//					zomb = (Zombie)e;
//					last_boss3 = zomb;
//					zombc = (CraftZombie)zomb;
//			        zombMC = zombc.getHandle();
//					i = new ItemStack(Item.getById(276));
//			        i.addEnchantment(Enchantment.DAMAGE_ALL, 1);
//			        i.addEnchantment(Enchantment.KNOCKBACK, 1);
//			        i.addEnchantment(Enchantment.FIRE_ASPECT, 2);
//			        zombMC.setEquipment(0, i);
//			        i = new ItemStack(Item.getById(305));
//			        zombMC.setEquipment(1, i);
//			        i = new ItemStack(Item.getById(304));
//			        zombMC.setEquipment(2, i);
//			        i = new ItemStack(Item.getById(303));
//			        zombMC.setEquipment(3, i);
//			        i = new ItemStack(Item.getById(302));
//			        zombMC.setEquipment(4, i);
//			        
//				}
//				if(back_timer == 8 && last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 只要吾之三大守護尚在，吾就不會受到分毫傷害！");
//					}
//				}
//				if(last_boss_phase1 && !last_boss_phase2 && !last_boss_phase3 && last_boss1 != null && last_boss2 != null && last_boss3 != null)
//				{
//					if(last_boss1.isDead() && last_boss2.isDead() && last_boss3.isDead())
//					{
//						last_boss_phase2 = true;
//						back_timer = 0;
//					}
//				}
//				if(back_timer == 0 && last_boss_phase1 && last_boss_phase2 && !last_boss_phase3)
//				{
//					Zombie zomb = (Zombie)last_boss;
//					CraftZombie zombc = (CraftZombie)zomb;
//			        EntityZombie zombMC = zombc.getHandle();
//			        zombMC.removeAllEffects();
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 唔唔唔...可惡的臭蟲們！");
//					}
//				}
//				if(back_timer == 3 && last_boss_phase1 && last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 但是你們以為吾是單槍匹馬到來的嗎...嘿嘿嘿...");
//					}
//				}
//				if(back_timer == 6 && last_boss_phase1 && last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 顯現吧！吾之燃燒軍團！！");
//					}
//				}
//				if(back_timer == 8 && last_boss_phase1 && last_boss_phase2 && !last_boss_phase3)
//				{
//					last_boss.getWorld().setStorm(true);
//					last_boss.getWorld().setWeatherDuration(100000);
//				}
//				if(back_timer == 14 && last_boss_phase1 && last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: ...什麽？！");
//					}
//				}
//				if(back_timer == 17 && last_boss_phase1 && last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 下雨了？！你【嗶---】在逗吾烈焰魔王！");
//					}
//				}
//				if(back_timer == 20 && last_boss_phase1 && last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 明明是沙漠，為什麼會下雨啊啊啊啊啊啊啊啊！！！");
//					}
//				}
//				if(back_timer == 22 && last_boss_phase1 && last_boss_phase2 && !last_boss_phase3)
//				{
//					last_boss.getWorld().strikeLightning(last_boss.getLocation());
//				}
//				if(back_timer == 25 && last_boss_phase1 && last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 啊啊啊啊啊啊啊！可惡啊啊啊啊啊！");
//					}
//					last_boss.setHealth(120 * player_count * difficulty);
//					last_boss.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 1000000, 3));
//					last_boss.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 1000000, 1));
//				}
//				if(back_timer == 30 && last_boss_phase1 && last_boss_phase2 && !last_boss_phase3)
//				{
//					for(int i = 0; i < 5; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.WHITE + "現在正是好時機！乘勝追擊消滅烈焰魔王吧！");
//					}
//				}
//				if(last_boss_phase1 && last_boss_phase2 && !last_boss_phase3 && last_boss != null)
//				{
//					Damageable a = (Damageable)last_boss;
//					if(a.getHealth() / a.getMaxHealth() < 0.3)
//					{
//						last_boss_phase3 = true;
//						back_timer = 0;
//					}
//						
//				}
//				if(back_timer == 0 && last_boss_phase1 && last_boss_phase2 && last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 可惡啊啊啊啊啊！為何你們不會被燃盡！！");
//					}
//				}
//				if(back_timer == 3 && last_boss_phase1 && last_boss_phase2 && last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: ？？！！這是什麽情況！！");
//					}
//				}
//				if(back_timer == 5 && last_boss_phase1 && last_boss_phase2 && last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius:你們的鎧甲...為何被附魔了最高級的火焰防護！");
//					}
//				}
//				if(back_timer == 8 && last_boss_phase1 && last_boss_phase2 && last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 怎麼會...吾居然被人算計了。");
//					}
//				}
//				if(back_timer == 11 && last_boss_phase1 && last_boss_phase2 && last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 是誰敢算計本大爺？！光明神嗎？水神嗎？可惡啊啊啊啊啊啊啊啊！");
//					}
//				}
//				if(back_timer == 15 && last_boss_phase1 && last_boss_phase2 && last_boss_phase3)
//				{
//					for(int i = 0; i < 2; i++)
//					{
//						Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 我死不瞑目啊啊啊啊啊啊!");
//					}
//				}
//				if(last_boss_phase1 && last_boss_phase2 && last_boss_phase3 && last_boss != null)
//				{
//					if(last_boss.isDead())
//					{
//						for(int i = 0; i < 2; i++)
//						{
//							Bukkit.broadcastMessage(ChatColor.DARK_RED + "Innocentius: 我恨啊啊啊啊啊啊啊啊啊啊啊啊啊！");
//							bonus_finish_switch = true;
//						}
//					}
//				}
//				if(back_timer < 60)
//				{
//					back_timer ++;
//				}
//				
//			}
//		}
//		if(!finish && wave9_switch && bonus_switch && bonus_finish_switch)
//		{
//			for(int i = 0; i < 2; i++)
//			{
//				Bukkit.broadcastMessage(ChatColor.GOLD + "恭喜通過隱藏關卡！最終評價上升一級！");
//			}
//			gettop();
//			for(int i = 0; i < 3; i++)
//			{
//				Bukkit.broadcastMessage("活動結束！請等待傳送和獎勵發放！");
//			}
//			finish = true;
//		}
		
	}
	private void playbgm(String string) {
		for(Player a : baselist.get("BLUE").getWorld().getPlayers())
		{
			a.playSound(a.getLocation(), string, 1000, 1);
		}
		
	}
	private String calculate_rank() {
		int full = 3 *counthp(player_count);
		int fin = Base_HP.get("BLUE") + Base_HP.get("AQUA") + Base_HP.get("PURPLE");
		int brok = 0;
		String rank = "";
		if(Base_HP.get("BLUE") == 0)
		{
			brok++;
		}
		if(Base_HP.get("AQUA") == 0)
		{
			brok++;
		}
		if(Base_HP.get("PURPLE") == 0)
		{
			brok++;
		}
		if(fin > 0.97 * full)
		{
			rank = "SS";
		}
		if(fin > 0.80 * full && fin < 0.97 * full)
		{
			rank = "S";
		}
		if(fin > 0.60 * full && fin < 0.8 * full)
		{
			rank = "A";
		}
		if((fin > 0.4 * full && fin < 0.6 * full) || brok == 1)
		{
			rank = "B";
		}
		if((fin < 0.4 * full) || brok == 2)
		{
			rank = "C";
		}
		return rank;
		
	}
	private void wavefinalremove()
	{
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
				Base_damageable.put(string, false);
				Player_use_fort.put(name, 1);
				return true;
			}
			else if(Player_use_fort.get(name) < 2)
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
			wave = Integer.parseInt(string);
		}
		
	}
	public void settime(String string) {
		if(init)
		{
			wave_time = Integer.parseInt(string);
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
}