import java.util.Vector;
/*
 * Contains a min heap of event objects
 * while heap is not empty, dequeue its objects and
 * run the processEvent function within them
 */
public class SimulationFramework 
{
	private int currentTime = 0;
	private MinPQ<Event> eventQueue = new MinPQ<Event>();
	
	public int time() {return currentTime;}
	public void scheduleEvent(Event newEvent)
	{
		eventQueue.insert(newEvent);
	}
	//takes a vector and returns the index for which the target fit into
	//we will use a value between 1 and 100 though and should work, 
	//Not sure why a vector was needed and why this is in simulation framework and not Bar
	public static int weightedProbability(Vector<Integer> vals, int target)
	{
		int sum = 0;
		for(int i = 0;i < vals.size();i++)
		{
			sum = sum + vals.get(i);
			if (target <= sum)
			{
				return i;
			}
		}
		return -1;
	}
	//Dequeues and runs event objects function
	public void run()
	{
		while(! eventQueue.isEmpty())
		{
			Event nextEvent = (Event) eventQueue.delMin();
			currentTime = nextEvent.time;
			nextEvent.processEvent();
		}
	}
}
