import java.util.LinkedList;

/*
	File        : HeapTree.java
	Author      : Eric Sia
	Design Date : 08 Nov 2022
	Test Date   : 11 Nov 2022
	Purpose     : this HeapEntry will purely use Self-referential Object
	              to achieve heap effect with O(1) complexity when finding new tail during adding and removing
*/
public class HeapTree
{
	private class HeapEntry
	{
		private int       priority;
		private Object    value;
		private HeapEntry parent; 	   // to prevent the need to recursive (bad time complexity) from head to search
		private HeapEntry left, right; // branch

		public HeapEntry(HeapEntry inParent, int inPriority, Object inValue)
		{
			parent   = inParent;
			priority = inPriority;
			value    = inValue;
			left     = null;
			right    = null;
		}

		public int getPriority()
		{
			return priority;
		}

		public Object getValue()
		{
			return value;
		}

		public void setChild(HeapEntry newChild) // when adding
		{
			if (left == null)
			{
				left = newChild;
			}
			else // we are inserting the right branch
			{
				right = newChild;
				tail  = tailQueue.removeFirst(); // take out one tail candidate
			}
		}

		public void swapContentWithChild(HeapEntry childNode) // for swap when trickleUp and trickeDown
		{
			int    childPriority = childNode.priority;
			Object childValue    = childNode.value;

			childNode.priority = priority; // perform swapping
			childNode.value    = value;
			priority           = childPriority;
			value              = childValue;
		}

		public void askParentSetThisNodeNull() // when remove(), remove last node
		{
			if (parent.right == this) // if this node is right node
			{
				parent.right = null;
			}
			else
			{
				parent.left = null;
			}
		}

		public void updateTail() // for during remove()
		{
			if (parent.right == this)   // if this node is right branch, add the old tail to tailQueue to be candidate
			{
				tailQueue.add(0, tail); // the tail candidate for next time add()
			}

			tail = parent;			    // update this class new tail for next time add()
		}
	}

	private HeapEntry head;  // the root as an indicator during remove()
	private HeapEntry tail;  // we always add new node to tail
	private int       count; // the total node in this class
	private int       size;  // the maximum size limited by this heap

	private LinkedList<HeapEntry> tailQueue = new LinkedList<>(); // hold all the available tail candidate after add

	public HeapTree(int inSize)
	{
		head  = tail = null;
		count = 0;

		if (inSize < 1)
		{
			throw new IllegalArgumentException("the size limit of this heap cannot be less than 1");
		}
		size = inSize; // if there is IllegalArgumentException, it won't reach here
	}

	public void add(int priority, Object value)
	{
		if (count == 0) // if our entire Heap tree is empty
		{
			HeapEntry newChild = new HeapEntry(null, priority, value);
			head  = tail = newChild;
			count = 1;
		}
		else if (count < size)
		{
			// first step is add newChild to the tail (parent) if it's not empty
			HeapEntry newChild = new HeapEntry(tail, priority, value);
			tail.setChild(newChild);
			tailQueue.add(newChild); // the future tail candidate for add() next time
			trickleUp(newChild);
			count++;
		}
	}

	private void trickleUp(HeapEntry currentNode) // recursive
	{
		HeapEntry parentNode = currentNode.parent;

		if (currentNode != head) // if not at the root yet
		{
			if (currentNode.getPriority() > parentNode.getPriority())
			{
				// child swap position with parent
				parentNode.swapContentWithChild(currentNode);
				trickleUp(parentNode); // call recursively
			}
		}
	}

	public Object remove() // when we remove we return the removed value
	{
		Object retValue = null;

		if (count == 1)
		{
			retValue = head.getValue();
			head     = tail = null;
			count    = 0;
		}
		else if (count > 1)
		{
			retValue = head.getValue();
			HeapEntry lastNode = tailQueue.removeLast();
			head.swapContentWithChild(lastNode); // this is how heap work, last element swap with head
			lastNode.updateTail();               // we need to add tail to tailQueue for next time add() if this is right branch
			lastNode.askParentSetThisNodeNull(); // we must ask the parent set this child(self) reference to null for next time add()
			count--;
			trickleDown(head); // perform a trickle down
		}

		return retValue;
	}

	private void trickleDown(HeapEntry currentNode) // recursive
	{
		HeapEntry leftChild  = currentNode.left;
		HeapEntry rightChild = currentNode.right;
		HeapEntry largerChild;

		if (leftChild != null) // if currentNode have child
		{
			largerChild = leftChild; // initialise
			if (rightChild != null)  // if we got right child
			{
				// see if the right child is larger and update largerChild
				if (leftChild.getPriority() < rightChild.getPriority())
				{
					largerChild = rightChild;
				}
			}

			if (largerChild.getPriority() > currentNode.getPriority())
			{
				// swap largerChild with currentNode
				currentNode.swapContentWithChild(largerChild);
				trickleDown(largerChild);
			}
		}
	}

	@Override
	public String toString()
	{
		String msg = "Current Tail Queue for debug: ";
		for (HeapEntry tail : tailQueue)
		{
			msg += tail.value + " ";
		}

		return msg;
	}
}