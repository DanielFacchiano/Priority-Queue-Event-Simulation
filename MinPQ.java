//we can take out a key, and figure out what it is by looking it up in an array of objects...
//heap class, does heap things
public class MinPQ <Key extends Comparable<Key>>
{
	private Key data[];
	private int N = 0;
	
	@SuppressWarnings("unchecked")
	MinPQ()
	{
		data = (Key[]) new Comparable[2];	
	}
	// Ran after insertion
	public void siftUp(int index)
	{
		while (index > 1 && less(index, index/2))//while passed index is not the top and its parent is MORE than it
		{
			swap(index/2, index);//switch location
			index = index/2;//move index to where we swapped it too
		}
	}
	// Ran after deletion
	public void siftDown(int index)
	{
		while(2*index <= N)//while indexs child is less than the size of the list
		{
			int child = 2*index;
			if(child < N && less(child+1, child)) child++;//if child is MORE than its sibling, choose its sibling
			if(less(index,child))//if the parent is not less than its child, it is in the currect order and we may leave the loop
			{
				break;
			}
			swap(index, child);//swaps smaller parent with largest child.
			index=child;//move to index we swapped to
		}
	}
	public  void insert(Key new_dat)
	{
		if(isFull())
		{
			changeSize(data.length*2);
		}
		data[++N] = new_dat;
		siftUp(N);

	}
	//return lowest value without deleting
	public Key min()
	{
		return data[1];
	}
	//remove lowest value from top of heap
	public Key delMin()
	{
		if(isEmpty())
		{
			System.out.println("Cannot delete from empty Queue");
			return null;
		}
		Key temp = data[1];
		swap(1,N--);//swap index 1(top) with N then decrement N
		data[N+1] = null; // removes item from memory for garbage collection
		siftDown(1);
		if(N == data.length/4)
		{
			changeSize(data.length/2);
		}
		return temp;
	}
	public boolean isFull()
	{
		return N == data.length-1;
	}
	public boolean isEmpty()
	{
		return N == 0;
	}
	//number of keys in PQ
	public int size()
	{
		return N;
	}
	// if v is less than w, return true
	public boolean less(int i, int j)
	{
		return data[i].compareTo(data[j]) < 0;
	}
	public void swap(int i, int j)
	{
		Key temp = data[i];
		data[i] = data [j];
		data[j] = temp;
	}
	public void changeSize(int modi)
	{
		@SuppressWarnings("unchecked")
		Key temp[] = (Key[])new Comparable[modi];
		System.arraycopy(data, 1, temp, 1, N);
		data = temp;
	}
	public static void main(String args[])
	{
			MinPQ<Integer> pork = new MinPQ<>();
			int uArr[] = {3,10,4,23,12,5,3,22,5,2,27,11,3,14};
			for(int i = 0; i < uArr.length;i++)
			{
				pork.insert(uArr[i]);
			}
			System.out.println("printing array");
			for(int i = 0; i < uArr.length;i++)
			{
				System.out.println(pork.delMin());
			}
			for(int i = 0; i < uArr.length;i++)
			{
				pork.insert(uArr[i]);
			}
			System.out.println("printing array again");
			for(int i = 0; i < uArr.length;i++)
			{
				System.out.println(pork.delMin());
			}
	}
}

