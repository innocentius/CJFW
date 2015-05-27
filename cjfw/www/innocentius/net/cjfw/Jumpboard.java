package innocentius.net.cjfw;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Jumpboard {

	public static void Jump(Player a, Vector z)
	{
		a.setVelocity(z);
		//TODO find a way to give player 5 second of invulnerable time, or just ignore it.
	}
}
