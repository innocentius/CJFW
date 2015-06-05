package innocentius.net.cjfw;

import net.minecraft.server.v1_8_R1.Enchantment;
import net.minecraft.server.v1_8_R1.EntityCreature;
import net.minecraft.server.v1_8_R1.EntitySkeleton;
import net.minecraft.server.v1_8_R1.Item;
import net.minecraft.server.v1_8_R1.ItemStack;

public class ArmorRank {
//	armor rank 0: none
//	armor rank 1: fur helmet --DEF+1 -- 96%
//	armor rank 2: gold helmet --DEF+2 -- 92%
//	armor rank 3: fur helmet + legging -- DEF+3 -- 88%
//	armor rank 4: fur helmet + chestplate + legging --DEF+6 -- 76%
//	armor rank 5: fur total -- DEF+7 -- 72%
//	armor rank 6: gold helmet + chestplate + legging -- DEF+10 -- 60%
//	armor rank 7: chain total -- DEF+12 -- 52%
//	armor rank 8: iron total -- DEF+15 -- 40%
//	armor rank 9: diamond total -- DEF+20 -- 20%
//	armor rank 10: diamond total -- Protection level 1 * 4 -- 16.8% ~ 18.4%
//	armor rank 11: diamond total -- Protection level 2 * 4 -- 13.6% ~ 16.8%
//	armor rank 12: diamond total -- Protection level 3 * 4 -- 10.4% ~ 15.2%
//	armor rank 13: diamond total -- Protection level 4 * 4 -- 4% ~ 12%
//  armor rank 14: armor rank 13 + unbreak*10
//	set equipment 4: head
//				  3: plate
//				  2: leg
//				  1: foot
//	id: 298: fur helmet
//		299: fur chestplate
//		300: fur legging
//		301: fur boot
//		302: chain helmet
//		303: chain chestplate
//		304: chain legging
//		305: chain boot
//		306: Iron helmet
//		307: Iron chestplate
//		308: Iron legging
//		309: Iron boot
//		310: Diamond helmet
//		311: Diamond chestplate
//		312: Diamond legging
//		313: Diamond boot
//		314: Gold helmet
//		315: Gold chestplate
//		316: Gold legging
//		317: Gold boot
	/**
	 * Add armor according to the integer given as rank of armor.
	 * rank 1: mob take 96% damage
	 * rank 13: mob take 4%~12% damage
	 * @param i
	 * @param e
	 */
	public static void setArmor(int i, EntityCreature e)
	{
		ItemStack item;
		switch(i)
		{
		case 0:
			return;
		case 1:
			e.setEquipment(4, new ItemStack(Item.getById(298)));
			return;
		case 2:
			e.setEquipment(4, new ItemStack(Item.getById(314)));
			return;
		case 3:
			e.setEquipment(4, new ItemStack(Item.getById(298)));
			e.setEquipment(2, new ItemStack(Item.getById(300)));
			return;
		case 4:
			e.setEquipment(4, new ItemStack(Item.getById(298)));
			e.setEquipment(3, new ItemStack(Item.getById(299)));
			e.setEquipment(2, new ItemStack(Item.getById(300)));
			return;
		case 5:
			e.setEquipment(4, new ItemStack(Item.getById(298)));
			e.setEquipment(3, new ItemStack(Item.getById(299)));
			e.setEquipment(2, new ItemStack(Item.getById(300)));
			e.setEquipment(1, new ItemStack(Item.getById(301)));
			return;
		case 6:
			e.setEquipment(4, new ItemStack(Item.getById(314)));
			e.setEquipment(3, new ItemStack(Item.getById(315)));
			e.setEquipment(2, new ItemStack(Item.getById(316)));
			return;
		case 7:
			e.setEquipment(4, new ItemStack(Item.getById(302)));
			e.setEquipment(3, new ItemStack(Item.getById(303)));
			e.setEquipment(2, new ItemStack(Item.getById(304)));
			e.setEquipment(1, new ItemStack(Item.getById(305)));
			return;
		case 8:
			e.setEquipment(4, new ItemStack(Item.getById(302)));
			e.setEquipment(3, new ItemStack(Item.getById(303)));
			e.setEquipment(2, new ItemStack(Item.getById(304)));
			e.setEquipment(1, new ItemStack(Item.getById(305)));
			return;
		case 9:
			e.setEquipment(4, new ItemStack(Item.getById(310)));
			e.setEquipment(3, new ItemStack(Item.getById(311)));
			e.setEquipment(2, new ItemStack(Item.getById(312)));
			e.setEquipment(1, new ItemStack(Item.getById(313)));
			return;
		case 10:
			item = new ItemStack(Item.getById(310));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			e.setEquipment(4, item);
			item = new ItemStack(Item.getById(311));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			e.setEquipment(3, item);
			item = new ItemStack(Item.getById(312));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			e.setEquipment(2, item);
			item = new ItemStack(Item.getById(313));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
			e.setEquipment(1, item);
			return;
		case 11:
			item = new ItemStack(Item.getById(310));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			e.setEquipment(4, item);
			item = new ItemStack(Item.getById(311));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			e.setEquipment(3, item);
			item = new ItemStack(Item.getById(312));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			e.setEquipment(2, item);
			item = new ItemStack(Item.getById(313));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
			e.setEquipment(1, item);
			return;
		case 12:
			item = new ItemStack(Item.getById(310));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			e.setEquipment(4, item);
			item = new ItemStack(Item.getById(311));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			e.setEquipment(3, item);
			item = new ItemStack(Item.getById(312));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			e.setEquipment(2, item);
			item = new ItemStack(Item.getById(313));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3);
			e.setEquipment(1, item);
			return;
		case 13:
			item = new ItemStack(Item.getById(310));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			e.setEquipment(4, item);
			item = new ItemStack(Item.getById(311));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			e.setEquipment(3, item);
			item = new ItemStack(Item.getById(312));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			e.setEquipment(2, item);
			item = new ItemStack(Item.getById(313));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			e.setEquipment(1, item);
			return;
		case 14:
			item = new ItemStack(Item.getById(310));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			item.addEnchantment(Enchantment.DURABILITY, 3);
			e.setEquipment(4, item);
			item = new ItemStack(Item.getById(311));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			item.addEnchantment(Enchantment.DURABILITY, 3);
			e.setEquipment(3, item);
			item = new ItemStack(Item.getById(312));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			item.addEnchantment(Enchantment.DURABILITY, 3);
			e.setEquipment(2, item);
			item = new ItemStack(Item.getById(313));
			item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4);
			item.addEnchantment(Enchantment.DURABILITY, 3);
			e.setEquipment(1, item);
			return;
		  default:
			return;
		}
	}

}
