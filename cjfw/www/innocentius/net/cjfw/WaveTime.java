package innocentius.net.cjfw;

public class WaveTime 
{
	public int wave;
	public int time;
	public WaveTime(int wave, int time)
	{
		this.wave = wave;
		this.time = time;		
	}
	public String toString()
	{
		return "Wave: " + wave + " Time: " + time;
	}
}
