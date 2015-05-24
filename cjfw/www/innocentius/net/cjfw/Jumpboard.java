package innocentius.net.cjfw;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Jumpboard {

	public Jumpboard()
	{
		
	}
	public static void Jump(Player a, Vector z)
	{
		a.setVelocity(z);
	}
}
