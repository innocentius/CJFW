package innocentius.net.cjfw;

import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import net.minecraft.server.v1_8_R1.AttributeInstance;
import net.minecraft.server.v1_8_R1.AttributeModifier;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.GenericAttributes;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftZombie;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;

@SuppressWarnings("deprecation")
public class HinaruHandler {
	Zombie hinaru;
	CraftZombie crafthinaru;
	EntityZombie entityhinaru;
	CreatureHandler ch;
	UUID x;
	Random random = new Random();
	Location a;
	boolean incd;
	boolean thresh80;
	boolean thresh66;
	boolean thresh66_1;
	boolean thresh66_2;
	boolean thresh66_3;
	boolean thresh66_4;
	boolean thresh40;
	boolean thresh33;
	boolean thresh33_1;
	boolean thresh33_2;
	boolean thresh33_3;
	boolean thresh33_4;
	boolean thresh20;
	boolean chain1_1;
	boolean chain1_2;
	boolean chain1_3;
	int cdt;
	public HinaruHandler(Location a)
	{
		LivingEntity temp_eni;
		temp_eni = a.getWorld().spawnCreature(a, CreatureType.ZOMBIE);
		temp_eni.setRemoveWhenFarAway(false);
		temp_eni.setCustomName("ファルス・ヒューナル");
		temp_eni.setCustomNameVisible(true);
		hinaru = (Zombie)temp_eni;
		crafthinaru = (CraftZombie)hinaru;
		entityhinaru = crafthinaru.getHandle();
		WeaponRank.setWeapon(1, entityhinaru);
		ArmorRank.setArmor(14, entityhinaru);
		entityhinaru.getAttributeInstance(GenericAttributes.c).setValue(0.9);
		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) temp_eni).getHandle();
		AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.b);
		x = UUID.randomUUID();
		attributes.a(new AttributeModifier(x, "" + random.nextInt(), 60D, 0));
		hinaru.setMaxHealth(calchealth(a));
		hinaru.setHealth(calchealth(a));
		incd = false;
		for(Player p: a.getWorld().getPlayers())
		{
			p.playSound(hinaru.getLocation(), "hinaru_start" , 3, 1);
		}
		incd = true;
		cdt = 4;
		thresh80 = true;
		thresh66 = true;
		thresh66_1 = true;
		thresh66_2 = true;
		thresh66_3 = true;
		thresh66_4 = true;
		thresh40 = true;
		thresh33 = true;
		thresh33_1 = true;
		thresh33_2 = true;
		thresh33_3 = true;
		thresh33_4 = true;
		thresh20 = true;
		chain1_1 = false;
		chain1_2 = false;
		chain1_3 = false;
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
				return 200;
			}
			else if(n < 20)
			{
				return 1000 + (n-10) * 100;
			}
			else if(n < 40)
			{
				return 2000 + (n - 20) * 80;
			}
			else
			{
				return 3600 + (n - 40) * 60;
			}
		}
		return 20;
	}
	public void update() {
		if(hinaru.isDead())
		{
			for(Player p : hinaru.getWorld().getPlayers())
			{
				p.playSound(hinaru.getLocation(), "hinaru_death", 3, 1);
			}
			BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
			scheduler.cancelTask(CJFWmain.hinaruscheduler);
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
			//PART A: Check health threshold, if good, do new action.
			if(thresh80)
			{
				if(hinaru.getHealth() < hinaru.getMaxHealth() * 0.8)
				{
					thresh80 = false;
					for(Player p : hinaru.getWorld().getPlayers())
					{
						p.playSound(hinaru.getLocation(), "hinaru_laugh", 3, 1);
					}
					hinaru.setFireTicks(0);
					incd = true;
					cdt = 4;
					return;
				}
			}
			else if(thresh66)
			{
				if(hinaru.getHealth() < hinaru.getMaxHealth() * 0.66)
				{
					if(thresh66_1)
					{
						thresh66_1 = false;
						for(Player p: hinaru.getWorld().getPlayers())
						{
							p.playSound(hinaru.getLocation(), "hinaru_laugh(short)", 3, 1);
						}
						incd = true;
						cdt = 3;
						return;
					}
					else if(thresh66_2)
					{
						thresh66_2 = false;
						for(Player p: hinaru.getWorld().getPlayers())
						{
							p.playSound(hinaru.getLocation(), "hinaru_critical", 3, 1);
						}
						for(Entity e: getNearbyEntities(hinaru.getLocation(), 5))
						{
							if(e instanceof Player)
							{
								PotionEffect temp = new PotionEffect(PotionEffectType.SLOW, 200, 4);
								((Player)e).addPotionEffect(temp);
							}
						}
						incd = true;
						cdt = 4;
						return;
					}
					else if(thresh66_3)
					{
						thresh66_3 = false;
						for(Player p: hinaru.getWorld().getPlayers())
						{
							p.playSound(hinaru.getLocation(), "hinaru_shinen", 3, 1);
						}
						incd = true;
						cdt = 3;
						return;
					}
					else if(thresh66_4)
					{
						thresh66_4 = false;
						for(int i = 0; i < 72; i++)
						{
							Arrow a = hinaru.launchProjectile(Arrow.class);
						}
						incd = true;
						cdt = 2;
						return;
					}
					else
					{
						thresh66 = false;
						for(Player p: hinaru.getWorld().getPlayers())
						{
							p.playSound(hinaru.getLocation(), "hinaru_asobi", 3, 1);
						}
						WeaponRank.setWeapon(5, entityhinaru);
						incd = true;
						cdt = 5;
						return;
					}
				}
			}
			else if(thresh40)
			{
				if(hinaru.getHealth() < hinaru.getMaxHealth() * 0.4)
				{
					thresh40 = false;
					for(Player p : hinaru.getWorld().getPlayers())
					{
						p.playSound(hinaru.getLocation(), "hinaru_laugh", 3, 1);
					}
					hinaru.setFireTicks(0);
					incd = true;
					cdt = 4;
					return;
				}
			}
			else if(thresh33)
			{
				if(hinaru.getHealth() < hinaru.getMaxHealth() * 0.33)
				{
					if(thresh33_1)
					{
						thresh33_1 = false;
						for(Player p: hinaru.getWorld().getPlayers())
						{
							p.playSound(hinaru.getLocation(), "hinaru_laugh(short)", 3, 1);
						}
						incd = true;
						cdt = 3;
						return;
					}
					else if(thresh33_2)
					{
						thresh33_2 = false;
						for(Player p: hinaru.getWorld().getPlayers())
						{
							p.playSound(hinaru.getLocation(), "hinaru_critical", 3, 1);
						}
						for(Entity e: getNearbyEntities(hinaru.getLocation(), 5))
						{
							if(e instanceof Player)
							{
								PotionEffect temp = new PotionEffect(PotionEffectType.SLOW, 200, 4);
								((Player)e).addPotionEffect(temp);
							}
						}
						incd = true;
						cdt = 4;
						return;
					}
					else if(thresh33_3)
					{
						thresh33_3 = false;
						for(Player p: hinaru.getWorld().getPlayers())
						{
							p.playSound(hinaru.getLocation(), "hinaru_shinen", 3, 1);
						}
						incd = true;
						cdt = 3;
						return;
					}
					else if(thresh33_4)
					{
						thresh33_4 = false;
						for(int i = 0; i < 72; i++)
						{
							Arrow a = hinaru.launchProjectile(Arrow.class);
						}
						incd = true;
						cdt = 2;
						return;
					}
					else
					{
						thresh33 = false;
						for(Player p: hinaru.getWorld().getPlayers())
						{
							p.playSound(hinaru.getLocation(), "hinaru_asobi", 3, 1);
						}
						WeaponRank.setWeapon(12, entityhinaru);
						incd = true;
						cdt = 5;
						return;
					}
				}
			}
			else if(thresh20)
			{
				if(hinaru.getHealth() < hinaru.getMaxHealth() * 0.2)
				{
					thresh20 = false;
					for(Player p : hinaru.getWorld().getPlayers())
					{
						p.playSound(hinaru.getLocation(), "hinaru_laugh", 3, 1);
					}
					hinaru.setFireTicks(0);
					incd = true;
					cdt = 4;
					return;
				}
			}
			//PART B:
			int rand = random.nextInt(100);
			if(rand < 5)
			{
				hinaru.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 200, 1));
				hinaru.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 200, 1));
				for(Player p : hinaru.getWorld().getPlayers())
				{
					p.playSound(hinaru.getLocation(), "hinaru_waredawaarumai", 3, 1);
				}
				incd = true;
				cdt = 4;
				return;
			}
			else if(rand >= 5 && rand < 10)
			{
				for(Entity e: getNearbyEntities(hinaru.getLocation(), 5))
				{
					if(e instanceof Player)
					{
						PotionEffect temp = new PotionEffect(PotionEffectType.CONFUSION, 200, 3);
						((Player)e).addPotionEffect(temp);
					}
				}
				for(Player p : hinaru.getWorld().getPlayers())
				{
					p.playSound(hinaru.getLocation(), "hinaru_wakimayo", 3, 1);
				}
				incd = true;
				cdt = 4;
				return;
			}
			else if(rand >= 10 && rand < 20)
			{
				if(hinaru.getTarget() instanceof Player)
				{
					entityhinaru.locX = hinaru.getTarget().getLocation().getX();
					entityhinaru.locY = hinaru.getTarget().getLocation().getY();
					entityhinaru.locZ = hinaru.getTarget().getLocation().getZ();
				for(Player p : hinaru.getWorld().getPlayers())
				{
					p.playSound(hinaru.getLocation(), "hinaru_munon", 3, 1);
				}
				chain1_1 = true;
				incd = true;
				cdt = 4;
				return;
				}
				else
				{
					for(Player p : hinaru.getWorld().getPlayers())
					{
						p.playSound(hinaru.getLocation(), "hinaru_munon", 3, 1);
					}
					chain1_1 = true;
					incd = true;
					cdt = 4;
					return;
				}
			}
			else if(rand >= 30 && chain1_1)
			{
				chain1_1 = false;
				for(Entity e: getNearbyEntities(hinaru.getLocation(), 3))
				{
					if(e instanceof Player)
					{
						PotionEffect temp = new PotionEffect(PotionEffectType.SLOW, 100, 3);
						((Player)e).addPotionEffect(temp);
					}
				}
				for(Player p : hinaru.getWorld().getPlayers())
				{
					p.playSound(hinaru.getLocation(), "hinaru_senbaku", 3, 1);
				}		
				chain1_2 = true;
				incd = true;
				cdt = 4;
				return;
			}
			else if(rand >= 40 && chain1_2)
			{
				chain1_2 = false;
				for(Player p : hinaru.getWorld().getPlayers())
				{
					p.playSound(hinaru.getLocation(), "hinaru_zejaku", 3, 1);
				}
				hinaru.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 100, 1));
				chain1_3 = true;
				incd = true;
				cdt = 4;
				return;
			}
			else if(rand >= 50 && chain1_3)
			{
				chain1_3 = false;
				for(Player p : hinaru.getWorld().getPlayers())
				{
					p.playSound(hinaru.getLocation(), "hinaru_uei", 3, 1);
				}
				for(Entity e: getNearbyEntities(hinaru.getLocation(), 3))
				{
					if(e instanceof Player)
					{
						((Player) e).damage(100, hinaru);
					}
				}
				incd = true;
				cdt = 4;
				return;
			}
			else
			{
				chain1_1 = false;
				chain1_2 = false;
				chain1_3 = false;
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
