public class Test {

	public static void main(String[] args)
	{
		new Test(3, 4);
	}

	private int multiplier = 2;
	private int adder = 5;
	private int times = 3;

	public Test(int x, int y)
	{
		int multiplied = this.multiplier * x * y;
		int added = this.adder + multiplied;

		printTimes(added, this.times);
	}

	private void printTimes(int number, int times)
	{
		int i = 0;

		while (i < times)
		{
			System.out.println(number);

			i++;
		}
	}

}



