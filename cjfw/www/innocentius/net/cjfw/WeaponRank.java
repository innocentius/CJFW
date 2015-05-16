package innocentius.net.cjfw;

import net.minecraft.server.v1_8_R2.EntitySkeleton;
import net.minecraft.server.v1_8_R2.EntityZombie;

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
	public void setWeapon(int a, EntityZombie e)
	{
	}
//	weapon rank 0: none -- ATK+0
//	weapon rank 1: bow -- ATK+ -1~3 -- this is the normal level
//	weapon rank 2: bow -- power 1 -- ATK+0~5
//	weapon rank 3: bow -- power 2 -- ATK+0~6
//	weapon rank 4: wooden sword -- ATK+4
//	weapon rank 5: bow -- power 3 -- ATK+0~7
//	weapon rank 6: stone sowrd -- ATK+5
//	weapon rank 7: bow -- power 5 -- ATK+1~9.5
//	weapon rank 8: diamond sword -- ATK+7
//	weapon rank 9: bow -- power 5 flame 1 --ATK+1~9.5 + Fire
//	weapon rank10: bow -- unbreak10 power 5 flame 1 punch 2 -- ATK+1~9,5+Fire+knockback 
//	weapon rank11: diamond sword -- enchant sharp 1 -- ATK+8~8.5
//	weapon rank12: diamond sword -- enchant sharp 3 -- ATK+10~11.5
//	weapon rank13: diamond sword -- enchant sharp 5 -- ATK+12~14.5
//	weapon rank14: diamond sword -- enchant unbreak10 sharp 5 fire 2 knock 2 -- ATK+12~14.5 + Fire -- BOSS only
//	PS: weapon rank 10+ automaticly set mob type to ender skeleton
	/**
	 * set the rank of weapon to an entityskeleton accordingly to the integer given
	 * @param a
	 * @param e
	 */
	public void setWeapon(int a, EntitySkeleton e)
	{
		
	}
}
