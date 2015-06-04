package innocentius.net.cjfw;

import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import net.minecraft.server.v1_8_R1.AttributeInstance;
import net.minecraft.server.v1_8_R1.AttributeModifier;
import net.minecraft.server.v1_8_R1.Enchantment;
import net.minecraft.server.v1_8_R1.EntityGiantZombie;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.GenericAttributes;
import net.minecraft.server.v1_8_R1.Item;
import net.minecraft.server.v1_8_R1.ItemStack;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftGiant;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftZombie;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Giant;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.util.Vector;

public class LuthurHandler {
	Giant Luthur;
	CraftGiant craftluthur;
	EntityGiantZombie entityluthur;
	CreatureHandler ch;
	UUID x;
	Random stdran;
	Location a;
	int cdt;
	boolean incd;
	boolean thresh80;
	boolean thresh60;
	boolean thresh50;
	boolean thresh50_1;
	boolean thresh50_2;
	boolean thresh50_3;
	boolean thresh40;
	boolean thresh30;
	boolean thresh30_1;
	boolean thresh30_2;
	boolean thresh20;
	boolean gomi_on;
	int timeslap;
	@SuppressWarnings("deprecation")
	public LuthurHandler(Location a)
	{
		LivingEntity temp_eni;
		stdran = new Random();
		temp_eni = a.getWorld().spawnCreature(a, CreatureType.GIANT);
		temp_eni.setRemoveWhenFarAway(false);
		temp_eni.setCustomName("ダークファルス・ルーサー");
		temp_eni.setCustomNameVisible(true);
		Luthur = (Giant)temp_eni;
		craftluthur = (CraftGiant)Luthur;
		entityluthur = craftluthur.getHandle();
		ItemStack weapon = new ItemStack(Item.getById(276));
		weapon.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		weapon.addEnchantment(Enchantment.FIRE_ASPECT, 2);
		weapon.addEnchantment(Enchantment.KNOCKBACK, 2);
		weapon.addEnchantment(Enchantment.DURABILITY, 10);
		entityluthur.setEquipment(0, weapon);
		ArmorRank.setArmor(14, entityluthur);
		entityluthur.getAttributeInstance(GenericAttributes.c).setValue(1);
		entityluthur.getAttributeInstance(GenericAttributes.b).setValue(60);
		Luthur.setMaxHealth(calchealth(a));
		Luthur.setHealth(calchealth(a));
		incd = true;
		cdt = 2;
		thresh80 = true;
		thresh60 = true;
		thresh50 = true;
		thresh50_1 = true;
		thresh50_2 = true;
		thresh40 = true;
		thresh30 = true;
		thresh30_1 = true;
		thresh30_2 = true;
		thresh20 = true;
		gomi_on = false;
		timeslap = 2;
		for(Player p: a.getWorld().getPlayers())
		{
			p.playSound(Luthur.getLocation(), "luthur_hun" , 3, 1);
		}
	}
	private int calchealth(Location a)
	{
		if(a != null)
		{
			int n = a.getWorld().getPlayers().size();
			if(n == 1)
			{
				return 100;
			}
			else if(n < 10)
			{
				return 1000;
			}
			else if(n < 20)
			{
				return 1000 + (n-10) * 150;
			}
			else if(n < 40)
			{
				return 2500 + (n - 20) * 100;
			}
			else
			{
				return 4500 + (n - 40) * 80;
			}
		}
		return 20;
	}
	@SuppressWarnings("deprecation")
	public void update()
	{
		if(Luthur.isDead())
		{
			for(Player p : Luthur.getWorld().getPlayers())
			{
				p.playSound(Luthur.getLocation(), "luthur_machigaiada", 3, 1);
			}
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.cancelTask(CJFWmain.luthurscheduler);
		}
		if(incd)
		{
			cdt --;
			if(cdt <= 0)
			{
				incd = false;
				return;
			}
		}
		else
		{
			//check health threshold
			//80 60 50 40 30 20
			if(thresh80)
			{
				if(Luthur.getHealth() < Luthur.getMaxHealth() *0.8)
				{
					thresh80 = false;
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_michinojisho", 3, 1);
					}
					Luthur.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 200));
					cdt = 5;
					incd = true;
					return;
				}
			}
			else if(thresh60)
			{
				if(Luthur.getHealth() < Luthur.getMaxHealth() * 0.6)
				{
					thresh60 = false;
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_michinojisho", 3, 1);
					}
					Luthur.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 5, 200));
					cdt = 5;
					incd = true;
					return;
				}
			}
			else if(thresh50)
			{
				if(Luthur.getHealth() < Luthur.getMaxHealth() * 0.5)
				{
					if(thresh50_1)
					{
						thresh50_1 = false;
						for(Player p : Luthur.getWorld().getPlayers())
						{
							p.playSound(Luthur.getLocation(), "luthur_itarumichi", 3, 1);
						}
						cdt = 5;
						incd = true;
						Luthur.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 8, 200));
						return;
					}
					else if(thresh50_2)
					{
						thresh50_2 = false;
						for(Player p : Luthur.getWorld().getPlayers())
						{
							p.playSound(Luthur.getLocation(), "luthur_zenchi", 3, 1);
						}
						for(Entity e: getNearbyEntities(Luthur.getLocation(), 8))
						{
							if(e instanceof Player)
							{
								PotionEffect temp = new PotionEffect(PotionEffectType.SLOW, 300, 9);
								((Player)e).addPotionEffect(temp);
							}
						}
						cdt = 10;
						incd = true;
						return;
					}
					else
					{
						thresh50 = false;
						for(Entity e: getNearbyEntities(Luthur.getLocation(), 15))
						{
							if(e instanceof Player)
							{
								e.getWorld().strikeLightning(e.getLocation());
								((Player) e).damage(1000);
							}
						}
						cdt = 3;
						incd = true;
						return;
					}
				}
				
			}
			else if(thresh40)
			{
				
				if(Luthur.getHealth() < Luthur.getMaxHealth() * 0.4)
				{
					thresh40 = false;
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_hurt", 3, 1);
					}
				cdt = 3;
				incd = true;
				return;
				}
			}
			else if(thresh30)
			{
				if(Luthur.getHealth() < Luthur.getMaxHealth() * 0.3)
				{
				if(thresh30_1)
				{
					thresh30_1 = false;
					timeslap = 2;
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_machigaiwanai", 3, 1);
					}
					cdt = 5;
					incd = true;
					Luthur.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 8, 200));
					return;
				}
				else if(thresh30_2)
				{
					thresh30_2 = false;
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_zenchi", 3, 1);
					}
					for(Entity e: getNearbyEntities(Luthur.getLocation(), 8))
					{
						if(e instanceof Player)
						{
							PotionEffect temp = new PotionEffect(PotionEffectType.SLOW, 300, 9);
							((Player)e).addPotionEffect(temp);
						}
					}
					cdt = 6;
					incd = true;
					return;
				}
				else
				{
					thresh30 = false;
					for(Entity e: getNearbyEntities(Luthur.getLocation(), 15))
					{
						if(e instanceof Player)
						{
							e.getWorld().strikeLightning(e.getLocation());
							((Player) e).damage(1000);
						}
					}
					cdt = 3;
					incd = true;
					return;
				}
				}
			}
			else if(thresh20)
			{
				
				if(Luthur.getHealth() < Luthur.getMaxHealth() * 0.2)
				{
					thresh20 = false;
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_hurt", 3, 1);
					}
				cdt = 3;
				incd = true;
				return;
				}
			}
			//check timeslap 1 = slow 2 = normal 3 = fast
			int t = stdran.nextInt(100);
			if(timeslap == 2)
			{
				if(gomi_on)
				{
					gomi_on = false;
					for(Entity e: getNearbyEntities(Luthur.getLocation(), 7))
					{
						if(e instanceof Player)
						{
							e.getWorld().strikeLightning(e.getLocation());
							((Player) e).damage(10);
						}
					}
					cdt = 4;
					incd = true;
					return;
				}
				t = stdran.nextInt(100);
				if(t < 10)
				{
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_hun", 3, 1);
					}
					if(Luthur.getTarget() instanceof Player)
					{
						Luthur.getWorld().strikeLightning(Luthur.getTarget().getLocation());
						Luthur.getTarget().damage(5);
					}
					cdt = 3;
					incd = true;
					return;
				}
				else if(t >= 10 && t < 20)
				{
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_arksfuze", 3, 1);
					}
					for(Entity e: getNearbyEntities(Luthur.getLocation(), 5))
					{
						if(e instanceof Player)
						{
							PotionEffect temp = new PotionEffect(PotionEffectType.SLOW, 200, 1);
							((Player)e).addPotionEffect(temp);
						}
					}
					cdt = 4;
					incd = true;
					return;
				}
				else if(t >= 20 && t < 30)
				{
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_mikurushi", 3, 1);
					}
					for(Entity e: getNearbyEntities(Luthur.getLocation(), 5))
					{
						if(e instanceof Player)
						{
							((Player) e).damage(5);
						}
					}
					cdt = 3;
					incd = true;
					return;
				}
				else if(t == 30 || t == 31)
				{
					if(Luthur.getHealth() < Luthur.getMaxHealth() * 0.3)
					{
						return;
					}
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_wazerawa", 3, 1);
					}
					timeslap = 3;
					Luthur.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 800, 1));
					cdt = 5;
					incd = true;
					return;
				}
				else if(t == 32 || t == 33)
				{
					if(Luthur.getHealth() < Luthur.getMaxHealth() * 0.3)
					{return;}
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_katatsuke", 3, 1);
					}
					timeslap = 1;
					Luthur.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 800, 2));
					Luthur.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 800, 1));
					cdt = 5;
					incd = true;
					return;
				}
				else if(t > 33 && t <= 36)
				{
					if(Luthur.getHealth() < Luthur.getMaxHealth() * 0.3)
					{
						for(Player p : Luthur.getWorld().getPlayers())
						{
							p.playSound(Luthur.getLocation(), "luthur_gomi", 3, 1);
						}
					
					gomi_on = true;
					cdt = 2;
					incd = true;
					return;
					}
				}
				else if(t > 36 && t <= 41)
				{
					if(Luthur.getHealth() < Luthur.getMaxHealth() * 0.3)
					{
						for(Player p : Luthur.getWorld().getPlayers())
						{
							p.playSound(Luthur.getLocation(), "luthur_motomeru", 3, 1);
						}
						Luthur.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1));
					cdt = 5;
					incd = true;
					return;
					}
				}
				else
				{
					return;
				}
			}
			else if(timeslap == 3)
			{
				t = stdran.nextInt(100);
				if(t < 10)
				{
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_kaiwamuda", 3, 1);
					}
					for(Entity e: getNearbyEntities(Luthur.getLocation(), 5))
					{
						if(e instanceof Player)
						{
							e.setVelocity(new Vector(0, 3, 0));
						}
					}
					cdt = 5;
					incd = true;
					return;
				}
				else if(t >= 10 && t < 20)
				{
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_miesui", 3, 1);
					}
					for(Entity e: getNearbyEntities(Luthur.getLocation(), 5))
					{
						if(e instanceof Player)
						{
							((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 3));
						}
					}
					cdt = 5;
					incd = true;
					return;
				}
				else if(t >= 20 && t < 30)
				{
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_enzannashi", 3, 1);
					}
					for(Entity e: getNearbyEntities(Luthur.getLocation(), 5))
					{
						if(e instanceof Player)
						{
							((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 600, 1));
						}
					}
					cdt = 5;
					incd = true;
					return;
				}
				else if(t >= 30 && t < 40)
				{
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_omocha", 3, 1);
					}
					if(Luthur.getTarget() instanceof Player)
					{
						Luthur.getTarget().addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 140, 3));
					}
					cdt = 5;
					incd = true;
					return;
				}
				else if(t >= 40 && t < 42)
				{
					timeslap = 2;
				}
				else if(t >= 42 && t < 44)
				{
					if(Luthur.getHealth() < Luthur.getMaxHealth() * 0.3)
					{return;}
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_katatsuke", 3, 1);
					}
					timeslap = 1;
					Luthur.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 800, 2));
					Luthur.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 800, 1));
					cdt = 5;
					incd = true;
					return;
				}
				else
				{
					return;
				}
			}
			else if(timeslap == 1)
			{
				t = stdran.nextInt(100);
				if(t < 10)
				{
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_kaiwamuda", 3, 1);
					}
					for(Entity e: getNearbyEntities(Luthur.getLocation(), 5))
					{
						if(e instanceof Player)
						{
							((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 3));
						}
					}
					cdt = 5;
					incd = true;
					return;
				}
				else if(t >= 10 && t < 20)
				{
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_miesui", 3, 1);
					}
					for(Entity e: getNearbyEntities(Luthur.getLocation(), 5))
					{
						if(e instanceof Player)
						{
							((Player) e).addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 3));
						}
					}
					cdt = 5;
					incd = true;
					return;
				}
				else if(t >= 20 && t < 30)
				{
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_enzannashi", 3, 1);
					}
					for(Entity e: getNearbyEntities(Luthur.getLocation(), 5))
					{
						if(e instanceof Player)
						{
							((Player) e).damage(10);
						}
					}
					cdt = 5;
					incd = true;
					return;
				}
				else if(t >= 30 && t < 40)
				{
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_omocha", 3, 1);
					}
					if(Luthur.getTarget() instanceof Player)
					{
						Luthur.getTarget().damage(50);
					}
					cdt = 5;
					incd = true;
					return;
				}
				else if(t >= 40 && t < 42)
				{
					timeslap = 2;
				}
				else if(t >= 42 && t < 44)
				{
					if(Luthur.getHealth() < Luthur.getMaxHealth() * 0.3)
					{
						return;
					}
					for(Player p : Luthur.getWorld().getPlayers())
					{
						p.playSound(Luthur.getLocation(), "luthur_wazerawa", 3, 1);
					}
					timeslap = 3;
					Luthur.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 800, 1));
					cdt = 5;
					incd = true;
					return;
				}
				else
				{
					return;
				}
			}
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
	
}
