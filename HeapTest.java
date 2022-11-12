/*
	File        : HeapTest.java
	Author      : Eric Sia
	Design Date : 08 Nov 2022
	Test Date   : 11 Nov 2022
	Purpose     : a simple test to showcase HeapTree.java
*/

public class HeapTest
{
	private static int TOTAL_ITEM = 10;

	public static void main(String args[])
	{
		System.out.println("\n##### Testing Heaps #####\n");

		HeapTree testHeap = new HeapTree(TOTAL_ITEM);
		System.out.println("Adding items...");

		for (int i = 0; i < TOTAL_ITEM; i++)
		{
			testHeap.add(i, "value-" + Integer.toString(i));
			System.out.println("Added " + "value-" + Integer.toString(i) + " Priority: " + Integer.toString(i));
		}

		System.out.println(testHeap.toString());
		System.out.println();

		System.out.println("Removing items...");
		String temp;

		for (int i = 0; i < TOTAL_ITEM; i++)
		{
			temp = (String) testHeap.remove();
			System.out.println(temp);
		}
		System.out.println("\n##### Tests Complete #####\n");
	}
}