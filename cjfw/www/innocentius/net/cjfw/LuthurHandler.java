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
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.scheduler.BukkitScheduler;

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
	@SuppressWarnings("deprecation")
	public LuthurHandler(Location a)
	{
		LivingEntity temp_eni;
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
		ArmorRank.setArmor(12, entityluthur);
		entityluthur.getAttributeInstance(GenericAttributes.c).setValue(1);
		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) temp_eni).getHandle();
		AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.b);
		x = UUID.randomUUID();
		attributes.a(new AttributeModifier(x, "" + stdran.nextInt(), 60D, 0));
		Luthur.setMaxHealth(calchealth(a));
		Luthur.setHealth(calchealth(a));
		incd = false;
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
