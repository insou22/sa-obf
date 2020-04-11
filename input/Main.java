import java.util.Scanner;

public class Main {

    private static String message = "The third side of the triangle is %f.";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        double firstSide = scanner.nextDouble();
        double secondSide = scanner.nextDouble();

        double thirdSide = pythagoras(firstSide, secondSide);

        System.out.println(String.format(message, thirdSide));
    }

    private static double pythagoras(double x, double y) {
        double x_squared = x * x;
        double y_squared = y * y;

        return Math.sqrt(x_squared + y_squared);
    }

}
