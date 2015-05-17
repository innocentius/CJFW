package innocentius.net.cjfw;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

import net.minecraft.server.v1_8_R2.AttributeInstance;
import net.minecraft.server.v1_8_R2.AttributeModifier;
import net.minecraft.server.v1_8_R2.EntityInsentient;
import net.minecraft.server.v1_8_R2.GenericAttributes;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R2.entity.CraftLivingEntity;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.LivingEntity;

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
	 * @param baselist
	 * @param base_name
	 * @param x
	 * @param name
	 * @param spawnarea
	 * @param namevisible
	 * @return
	 */
	public LivingEntity summonCreatures(Map<String, Location> baselist, String base_name, CreatureType x, String name, Location spawnarea, boolean namevisible)
	{
			LivingEntity temp_eni;
			temp_eni = baselist.get(base_name).getWorld().spawnCreature(spawnarea, x);
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
	public void changeattack(LivingEntity temp_eni)
	{
		EntityInsentient nmsEntity = (EntityInsentient) ((CraftLivingEntity) temp_eni).getHandle();
		AttributeInstance attributes = nmsEntity.getAttributeInstance(GenericAttributes.e);
		x = UUID.randomUUID();
		attributes.a(new AttributeModifier(x, "" + random.nextInt(), 0.3, 1));
	}
	public void sethealth(Damageable a, double b)
	{
		a.setMaxHealth(b);
		a.setHealth(b);
	}
}
