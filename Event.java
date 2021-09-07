//abstract class stating we shhould have a process event function, and 
// tells java how it should compare event objects with eachother
public abstract class Event implements Comparable<Event> 
{
	public final int time;
	public Event (int t) 
	{
		time = t;
	}
	abstract void processEvent ();
	public int compareTo (Event e) 
	{
		Event right = e;
		if (time < right.time) return -1;
		if (time == right.time) return 0;
		return 1;
	}
}

