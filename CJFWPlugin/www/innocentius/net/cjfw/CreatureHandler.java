package innocentius.net.cjfw;

import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.server.v1_8_R1.AttributeInstance;
import net.minecraft.server.v1_8_R1.AttributeModifier;
import net.minecraft.server.v1_8_R1.EntityInsentient;
import net.minecraft.server.v1_8_R1.GenericAttributes;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftLivingEntity;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;

/**
 * summarize all method for change mob status.
 * @author Administrator
 *
 */
@SuppressWarnings("deprecation")
public class CreatureHandler {
	static Random random;
	static UUID x;
	public CreatureHandler()
	{
		random = new Random();
	}
	/**
	 * summon a creature
	 * Automatically set remove when far off
	 * automatically change range to 60
	 * @param <T>
	 * @param baselist
	 * @param base_name
	 * @param x
	 * @param name
	 * @param spawnarea
	 * @param namevisible
	 * @return 
	 * @return
	 */
	public <T extends LivingEntity> LivingEntity summonCreatures(Map<String, Location> baselist, String base_name, Class<T> creatureclass, String name, Location spawnarea, boolean namevisible)
	{
			LivingEntity temp_eni;
			temp_eni = baselist.get(base_name).getWorld().spawn(spawnarea, creatureclass);
			temp_eni.setRemoveWhenFarAway(false);
			temp_eni.setCustomName(name);
			temp_eni.setCustomNameVisible(namevisible);
			changerange(temp_eni);
			return temp_eni;
	}
	public void changerange(LivingEntity temp_eni) 
	{
		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) temp_eni).getHandle();
		AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.b);
		x = UUID.randomUUID();
		attributes.a(new AttributeModifier(x, "" + random.nextInt(), 60D, 0));
	}
	public void changespeed(LivingEntity temp_eni)
	{
		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) temp_eni).getHandle();
		AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.d);
		x = UUID.randomUUID();
		attributes.a(new AttributeModifier(x, "" + random.nextInt(), 0.2, 1));
	}
	public void changeattack(LivingEntity temp_eni, double i)
	{
		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) temp_eni).getHandle();
		AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.e);
		x = UUID.randomUUID();
		attributes.a(new AttributeModifier(x, "" + random.nextInt(), i, 1));
	}
	public void sethealth(Damageable a, double b)
	{
		//max health < 2048
		if(b < 2048 && b > 0)
		{
			a.setMaxHealth(b);
			a.setHealth(b);
		}
	}
	public boolean checkentitydata(Map<String, Location> baselist) 
	{
		Entity[] temp_dat;
		temp_dat = getNearbyEntities(baselist.get("BLUE"), 70);
		for(Entity ent:temp_dat)
		{
			if(ent instanceof Monster || ent instanceof EnderDragon)
			{
				return false;
			}
		}
        temp_dat = getNearbyEntities(baselist.get("AQUA"), 70);
        for(Entity ent:temp_dat)
        {
        	if(ent instanceof Monster || ent instanceof EnderDragon)
        	{
        		return false;
        	}
        	
        }
        temp_dat = getNearbyEntities(baselist.get("PURPLE"), 70);
        for(Entity ent:temp_dat)
        {
        	if(ent instanceof Monster || ent instanceof EnderDragon)
        	{
        		return false;
        	}
        }
        temp_dat = getNearbyEntities(baselist.get("GREEN"), 70);
        for(Entity ent:temp_dat)
        {
        	if(ent instanceof Monster || ent instanceof EnderDragon)
        	{
        		return false;
        	}
        }
        return true;
	}
	/**
	 * The method check the area around the center location &l with a radius of &radius
	 * @param l - the center location
	 * @param radius - the radius of detect area
	 * @return the list of entity without sort
	 * @author At least not me
	 */
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
