import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

/*
 * simulates running a bar with X open seats for four hours
 * initially seeds the simulation with randomly generated events
 * then process these events, and prints the Profit result
 * Vectors to load weighted probability function with values
 */
public class Bar 
{
	public Vector<Integer> groupProbs;
	public Vector<Integer> drinkProbs;
	private SimulationFramework sim = new SimulationFramework();
	double profits = 0.0;
	int openSeats = 50;
	
	public static void main(String args[])
	{	
		@SuppressWarnings("unused")
		Bar danbar = new Bar();
	}
	public Bar()
	{
		//15 chance for local beer, 60 change for Imported Beer, 25 chance for special beer
		drinkProbs = new Vector<>(); drinkProbs.insertElementAt(15, 0);drinkProbs.insertElementAt(60, 1);drinkProbs.insertElementAt(25, 2);
		//20 chance for 1 person, 25 chance for 2 people, 25 chance for 3 people, 20 chance for 4 people, 10 chance for 5 people
		groupProbs = new Vector<>(); groupProbs.insertElementAt(20, 0);groupProbs.insertElementAt(25, 1);groupProbs.insertElementAt(25, 2);
		groupProbs.insertElementAt(20, 3);groupProbs.insertElementAt(10, 4);	
		
		Scanner keys= new Scanner(System.in);
		int time = 0;
		System.out.println("Welcome to the Bar Simulation, how many seats would you like to simulate?");
		openSeats=keys.nextInt();
		keys.close();
		//while loop to "seed" our simulation with groups arriving
		while (time < 240)
		{
			time += randBetween(2,5);//group arrive every 2-5 minutes
			int groupSize = 1 + SimulationFramework.weightedProbability(groupProbs, randBetween(1,100));
			sim.scheduleEvent(new ArriveEvent(time, groupSize));//pass event at current time and group between 1,5
		}
		sim.run();
		System.out.println("Total Profits: $" + profits);
	}
	/*
	 * determines if there are enough open seats available to accep a group, if so removes this amount from the 
	 * total amount of seats and returns true
	 */
	public boolean canSeat(int groupSize)
	{
		System.out.println("Group of: " +groupSize+" customers arrive at time "+ sim.time());
		if(groupSize <= openSeats)
		{
			openSeats -= groupSize;
			System.out.println("Group is seated with "+ openSeats+" open seats remaining");
			return true;
		}
		else
		{
			System.out.println("Group leaves, not enough seats.");
			return false;
		}
	}
	//accepts a beer type and adds it's integer value to the running
	// profits
	public void order(int beerType)
	{
		System.out.println("Serviced order for beer type " + beerType +
				" at time " + sim.time( ));
		profits = profits + (double)beerType;
	}
	//takes an int and subtracts it from group size
	public void leave(int groupSize)
	{
		openSeats = openSeats + groupSize;
	}
	//produce a random number between two integers seeded at the current time
	private static int randBetween(int min, int max) 
	{
		if (min >= max) 
		{
			throw new IllegalArgumentException("max must be greater than min");
		}
		Random r = new Random();
		r.setSeed((System.nanoTime()));
		return r.nextInt((max - min) + 1) + min;
	}
	//arrive event class takes group size and time and passes new event to sim heap
	//also incraments time 2-10 ints
	private class ArriveEvent extends Event
	{
		public ArriveEvent(int time, int gs) 
		{
			super(time);
			groupSize = gs;
		}
		public void processEvent()
		{
			if(canSeat(groupSize))
			{
				sim.scheduleEvent (new OrderEvent(time + randBetween(2,10),groupSize));
			}
		}
		private int groupSize;
	}
	//takes group size and runs order loop to simulate orders, then goes onto reorder check event
	private class OrderEvent extends Event
	{
		private int groupSize;
		public OrderEvent(int time, int gs) 
		{
			super(time);
			groupSize = gs;
		}
		public void processEvent() 
		{
			for (int i = 0; i < groupSize; i++)
			{
				int orderNum = 2 + SimulationFramework.weightedProbability(drinkProbs, randBetween(1,100));
				order(orderNum);
			}
			sim.scheduleEvent (new ReOrderEvent(time+randBetween(30,60),groupSize));
		// redirect orderEvent to reOrderEvent. here we will see if we re order or not, passing down a contiously shrinking function, could have 2 to make easy
		}
	}
	//if check is met, customers will order drinks again
	//overloaded constructor allows us determine wheather or not this is the first time running or a recursive run
	//recursive runs contiously decrease passed probability value
	private class ReOrderEvent extends Event
	{
		private int groupSize;
		private int probability;

		public ReOrderEvent(int t, int gs) 
		{
			super(t);		
			groupSize = gs;
			probability = 50;//default 50% chance to reorder, will pass this as itself divided by 2 every time
		}
		public ReOrderEvent(int t, int gs, int prob) 
		{
			super(t);		
			groupSize = gs;
			probability = prob;
		}
		void processEvent() 
		{
			if(randBetween(1, 100) < probability)
			{
				System.out.println("group of " +groupSize+" has decided to stay for another round at "+time);
				//we decide to order more drinks, advance time by 30-60 and check again with less probability
				for (int i = 0; i < groupSize; i++)
				{					
					int orderNum = 2 + SimulationFramework.weightedProbability(drinkProbs, randBetween(1,100));
					order(orderNum);
				}
				sim.scheduleEvent (new ReOrderEvent(time + randBetween(30,60),groupSize, probability/2 ));	
			}
			else
			{
				sim.scheduleEvent (new LeaveEvent(time, groupSize));
			}
		}
		
	}
	//removes event from queue and frees up open seats
	private class LeaveEvent extends Event
	{
		private int groupSize;
		public LeaveEvent(int time, int gs) 
		{
			super(time);
			groupSize = gs;
		}
		public void processEvent() 
		{
			leave(groupSize);
			System.out.println("Group of: " +groupSize+" customers leave at time "+ sim.time()+" "+ openSeats +" seats now open");
		}
	}
}

