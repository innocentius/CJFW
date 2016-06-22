package innocentius.net.cjfw;

import net.minecraft.server.v1_8_R1.Enchantment;
import net.minecraft.server.v1_8_R1.EntityGiantZombie;
import net.minecraft.server.v1_8_R1.EntityPigZombie;
import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.EntityZombie;
import net.minecraft.server.v1_8_R1.Item;
import net.minecraft.server.v1_8_R1.ItemStack;

public class WeaponRank {
//	weapon rank 0: none
//	weapon rank 1: wooden shovel -- ATK+1
//	weapon rank 2: wooden axe -- ATK+3
//	weapon rank 3: wooden sword -- ATK+4
//	weapon rank 4: stone sword -- ATK+5
//	weapon rank 5: iron sword -- ATK+6
//	weapon rank 6: diamond axe -- enchant sharp 1 --ATK+7~7.5
//	weapon rank 7: diamond sword -- enchant sharp 1 -- ATK+8~8.5
//	weapon rank 8: diamond sword -- enchant sharp 2 -- ATK+9~10
//	weapon rank 9: diamond sword -- enchant sharp 3 fire 1 -- ATK+10~11.5 + Fire
//	weapon rank10: diamond sword -- enchant sharp 4 fire 2 -- ATK+11~13 + Fire
//	weapon rank11: diamond sword -- enchant sharp 5 fire 2 knock 1 -- ATK+12~14.5 + Fire
//	weapon rank12: diamond sword -- enchant unbreak 10 sharp 5 fire 2 knock 2 -- ATK+12~14.5 + Fire -- BOSS only
	/**
	 * set the rank of weapon to an entityzombie accordingly to the integer given
	 * the rank of zombie is different than the rank of a skeletons
	 * @param a
	 * @param e
	 */
	public static void setWeapon(int a, EntityZombie e)
	{
		ItemStack weapon;
		switch(a)
		{
		case 0:
			return;
		case 1:
			e.setEquipment(0, new ItemStack(Item.getById(269)));
			return;
		case 2:
			e.setEquipment(0, new ItemStack(Item.getById(271)));
			return;
		case 3:
			e.setEquipment(0, new ItemStack(Item.getById(268)));
			return;
		case 4:
			e.setEquipment(0, new ItemStack(Item.getById(272)));
			return;
		case 5:
			e.setEquipment(0, new ItemStack(Item.getById(267)));
			return;
		case 6:
			weapon = new ItemStack(Item.getById(279));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			e.setEquipment(0, weapon);
			return;
		case 7:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			e.setEquipment(0, weapon);
			return;
		case 8:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			e.setEquipment(0, weapon);
			return;
		case 9:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 3);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 1);
			e.setEquipment(0, weapon);
			return;
		case 10:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 4);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 2);
			e.setEquipment(0, weapon);
			return;
		case 11:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 5);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 2);
			weapon.addEnchantment(Enchantment.KNOCKBACK, 1);
			e.setEquipment(0, weapon);
			return;
		case 12:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 5);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 2);
			weapon.addEnchantment(Enchantment.KNOCKBACK, 2);
			weapon.addEnchantment(Enchantment.DURABILITY, 10);
			e.setEquipment(0, weapon);
			return;
		default:
			return;
		}
	}
	public static void setWeapon(int a, EntityPigZombie e)
	{
		ItemStack weapon;
		switch(a)
		{
		case 0:
			return;
		case 1:
			e.setEquipment(0, new ItemStack(Item.getById(269)));
			return;
		case 2:
			e.setEquipment(0, new ItemStack(Item.getById(271)));
			return;
		case 3:
			e.setEquipment(0, new ItemStack(Item.getById(268)));
			return;
		case 4:
			e.setEquipment(0, new ItemStack(Item.getById(272)));
			return;
		case 5:
			e.setEquipment(0, new ItemStack(Item.getById(267)));
			return;
		case 6:
			weapon = new ItemStack(Item.getById(279));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			e.setEquipment(0, weapon);
			return;
		case 7:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			e.setEquipment(0, weapon);
			return;
		case 8:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			e.setEquipment(0, weapon);
			return;
		case 9:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 3);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 1);
			e.setEquipment(0, weapon);
			return;
		case 10:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 4);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 2);
			e.setEquipment(0, weapon);
			return;
		case 11:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 5);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 2);
			weapon.addEnchantment(Enchantment.KNOCKBACK, 1);
			e.setEquipment(0, weapon);
			return;
		case 12:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 5);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 2);
			weapon.addEnchantment(Enchantment.KNOCKBACK, 2);
			weapon.addEnchantment(Enchantment.DURABILITY, 10);
			e.setEquipment(0, weapon);
			return;
		default:
			return;
		}
	}
	public static void setWeapon(int a, EntityGiantZombie e)
	{
		ItemStack weapon;
		switch(a)
		{
		case 0:
			return;
		case 1:
			e.setEquipment(0, new ItemStack(Item.getById(269)));
			return;
		case 2:
			e.setEquipment(0, new ItemStack(Item.getById(271)));
			return;
		case 3:
			e.setEquipment(0, new ItemStack(Item.getById(268)));
			return;
		case 4:
			e.setEquipment(0, new ItemStack(Item.getById(272)));
			return;
		case 5:
			e.setEquipment(0, new ItemStack(Item.getById(267)));
			return;
		case 6:
			weapon = new ItemStack(Item.getById(279));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			e.setEquipment(0, weapon);
			return;
		case 7:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
			e.setEquipment(0, weapon);
			return;
		case 8:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 2);
			e.setEquipment(0, weapon);
			return;
		case 9:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 3);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 1);
			e.setEquipment(0, weapon);
			return;
		case 10:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 4);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 2);
			e.setEquipment(0, weapon);
			return;
		case 11:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 5);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 2);
			weapon.addEnchantment(Enchantment.KNOCKBACK, 1);
			e.setEquipment(0, weapon);
			return;
		case 12:
			weapon = new ItemStack(Item.getById(276));
			weapon.addEnchantment(Enchantment.DAMAGE_ALL, 5);
			weapon.addEnchantment(Enchantment.FIRE_ASPECT, 2);
			weapon.addEnchantment(Enchantment.KNOCKBACK, 2);
			weapon.addEnchantment(Enchantment.DURABILITY, 10);
			e.setEquipment(0, weapon);
			return;
		default:
			return;
		}
	}
//	weapon rank 0: none -- ATK+0
//	weapon rank 1: bow -- ATK+ -1~3 -- this is the normal level
//	weapon rank 2: bow -- power 1 -- ATK+0~5
//	weapon rank 3: bow -- power 2 -- ATK+0~6
//	weapon rank 4: wooden sword -- ATK+4
//	weapon rank 5: bow -- power 3 -- ATK+0~7
//	weapon rank 6: stone sword -- ATK+5
//	weapon rank 7: bow -- power 5 -- ATK+1~9.5
//	weapon rank 8: diamond sword -- ATK+7
//	weapon rank 9: bow -- power 5 flame 1 --ATK+1~9.5 + Fire
//	weapon rank10: bow -- power 5 flame 1 punch 2 -- ATK+1~9,5+Fire+knockback 
//	weapon rank11: diamond sword -- enchant sharp 1 -- ATK+8~8.5
//	weapon rank12: diamond sword -- enchant sharp 3 -- ATK+10~11.5
//	weapon rank13: diamond sword -- enchant sharp 5 -- ATK+12~14.5
//	weapon rank14: diamond sword -- enchant unbreak10 sharp 5 fire 2 knock 2 -- ATK+12~14.5 + Fire -- BOSS only
//	PS: weapon rank 10+ automatically set mob type to ender skeleton
	/**
	 * set the rank of weapon to an entityskeleton accordingly to the integer given
	 * @param a
	 * @param e
	 */
	public static void setWeapon(int a, EntitySkeleton e)
	{
		ItemStack weapon;
		switch(a)
		{
			case 0:
				return;
			case 1:
				e.setEquipment(0, new ItemStack(Item.getById(261)));
				return;
			case 2:
				weapon = new ItemStack(Item.getById(261));
				weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
				e.setEquipment(0, weapon);
				return;
			case 3:
				weapon = new ItemStack(Item.getById(261));
				weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 2);
				e.setEquipment(0, weapon);
				return;
			case 4:
				e.setEquipment(0, new ItemStack(Item.getById(268)));
				return;
			case 5:
				weapon = new ItemStack(Item.getById(261));
				weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 3);
				e.setEquipment(0, weapon);
				return;
			case 6:
				e.setEquipment(0, new ItemStack(Item.getById(272)));
				return;
			case 7:
				weapon = new ItemStack(Item.getById(261));
				weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
				e.setEquipment(0, weapon);
				return;
			case 8:
				e.setEquipment(0, new ItemStack(Item.getById(276)));
				return;
			case 9:
				weapon = new ItemStack(Item.getById(261));
				weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
				weapon.addEnchantment(Enchantment.ARROW_FIRE, 1);
				e.setEquipment(0, weapon);
				return;
			case 10:
				weapon = new ItemStack(Item.getById(261));
				weapon.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
				weapon.addEnchantment(Enchantment.ARROW_FIRE, 1);
				weapon.addEnchantment(Enchantment.ARROW_KNOCKBACK, 2);
				e.setEquipment(0, weapon);
				return;
			case 11:
				e.setSkeletonType(1);
				weapon = new ItemStack(Item.getById(276));
				weapon.addEnchantment(Enchantment.DAMAGE_ALL, 1);
				e.setEquipment(0, weapon);
				return;
			case 12:
				e.setSkeletonType(1);
				weapon = new ItemStack(Item.getById(276));
				weapon.addEnchantment(Enchantment.DAMAGE_ALL, 3);
				e.setEquipment(0, weapon);
				return;
			case 13:
				e.setSkeletonType(1);
				weapon = new ItemStack(Item.getById(276));
				weapon.addEnchantment(Enchantment.DAMAGE_ALL, 5);
				e.setEquipment(0, weapon);
				return;
			case 14:
				e.setSkeletonType(1);
				weapon = new ItemStack(Item.getById(276));
				weapon.addEnchantment(Enchantment.DURABILITY, 10);
				weapon.addEnchantment(Enchantment.DAMAGE_ALL, 5);
				weapon.addEnchantment(Enchantment.FIRE_ASPECT, 2);
				weapon.addEnchantment(Enchantment.KNOCKBACK, 2);
				e.setEquipment(0, weapon);
				return;
			default:
				return;
				
		}
	}
}
