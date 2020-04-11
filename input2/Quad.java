import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

class Quad {

    private boolean debug;
    private boolean simplify;

    public static void main(String[] args)
    {
        new Quad();
    }

    Quad() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Debug? ");
        debug = scanner.nextBoolean();

        print("Equation: Ax^2 + Bx + C (= 0)");

        System.out.print("A: ");
        int a = scanner.nextInt();

        System.out.print("B: ");
        int b = scanner.nextInt();

        System.out.print("C: ");
        int c = scanner.nextInt();

        print("Would you like to simplify or solve? (Simplify: true, Solve: false): ");
        simplify = scanner.nextBoolean();

        if (simplify) {
            simplify(a, b, c);
        } else {
            solve(a, b, c);
        }
    }

    private void simplify(int a, int b, int c) {
        int b1 = 0;
        int b2 = 0;
        if (a == 1) {
            debug("Equation is monic");
        } else {
            debug("Equation is non-monic");
        }
        debug("About to look for factors of " + (a * c) + " (a * c)");
        for (int b1num : factors(a * c)) {
            debug("Trying factor " + b1num);
            int b2num = b - b1num;
            debug("Matching number of factor: " + b2num);
            if (b1num * b2num == a * c) {
                debug("Found two numbers: " + b1num + " and " + b2num);
                b1 = b1num;
                b2 = b2num;
                break;
            } else {
                debug("Two numbers do not work");
            }
        }
        if (b1 == 0) {
            print("No two numbers were found.");
            return;
        }
        debug("b1: " + b1 + ", b2: " + b2 + ", a: " + a + ", c: " + c);
        debug(a + " (a) & " + b1 + " (b1) has HCF: " + hasHcf(a, b1));
        debug(b2 + " (b2) & " + c + " (c) has HCF: " + hasHcf(b2, c));

        int x1 = gcd(b1, a);
        int x2 = a / x1;
        int x3 = b1 / x1;
        int x4 = b2 / x2;

        print(x1 + "x(" + x2 + "x + " + x3 + ") + " + x4 + "(" + x2 + "x + " + x3 + ")");

        String x1s = x1 + "x";
        if (x1 == 1 || x1 == -1) {
            x1s = (x1 > 0 ? "x" : "-x");
        }
        String x4s = (x4 > 0 ? "+ " + x4 : "- " + (x4 * -1));
        String x2s = x2 + "x";
        if (x2 == 1 || x2 == -1) {
            x2s = (x2 > 0 ? "x" : "-x");
        }
        String x3s = (x3 > 0 ? "+ " + x3 : "- " + x3 * -1);
        String set1 = "(" + x1s + " ";
        String set2 = x4s + ") ";
        String set3 = "(" + x2s + " ";
        String set4 = x3s + ")";
        print("Solution: ");
        print(set1 + set2 + set3 + set4);

        if (simplify) {
//            System.out.print("Would you also like to solve? ");
//            if (new Scanner(System.in).nextBoolean()) {
//                solve(a, b, c);
//            }
        }
    }

    private void solve(int a, int b, int c) {
        int dis = (b * b) - (4 * a * c);
        debug("Discriminant is " + (b * b) + " - " + (4 * a * c));
        debug("Discriminant has been calculated to " + (dis));
        if (dis < 0) {
            print("There are no solutions!");
        } else if (dis == 0) {
            print("Solution: ");
            print("x = " + (b * -1) + " / " + (2 * a));
            System.out.print("Approximate? ");
            if (new Scanner(System.in).nextBoolean()) {
                print("x = " + ((b * -1) / (2 * a)));
            }
        } else {
            print("Solutions: ");
            print("x = (" + (b * -1) + " + sqrt(" + (dis) + ")) / " + (2 * a));
            print("OR");
            print("x = (" + (b * -1) + " - sqrt(" + (dis) + ")) / " + (2 * a));
            System.out.print("Approximate? ");
            if (new Scanner(System.in).nextBoolean()) {
                debug("Approximating - " + (b * -1) + " + " + (Math.sqrt(dis)) + " / " + (2 * a));
                print("x = " + (((b * -1) + Math.sqrt(dis)) / (2 * a)));
                print("OR");
                print("x = " + (((b * -1) - Math.sqrt(dis)) / (2 * a)));
            }
        }
        if (!simplify) {
            System.out.print("Would you also like to simplify? ");
            if (new Scanner(System.in).nextBoolean()) {
                simplify(a, b, c);
            }
        }
    }

    private List<Integer> factors(int n) {
        if (n < 0) {
            n *= -1;
        }
        int upperlimit = (int) (Math.sqrt(n));
        List<Integer> factors = new ArrayList<>();
        for (int i = 1; i <= upperlimit; i += 1) {
            if (n % i == 0) {
                factors.add(i);
                factors.add(i * -1);
                if(i != n / i){
                    factors.add(n / i);
                    factors.add((n / i) * -1);
                }
            }
        }
        Collections.sort(factors);
        return factors;
    }

    private boolean hasHcf(int x, int y) {
        return gcd(x, y) != 0;
    }

    private int gcd(int x, int y) {
        BigInteger b1 = BigInteger.valueOf(x);
        BigInteger b2 = BigInteger.valueOf(y);
        BigInteger gcd = b1.gcd(b2);
        return gcd.intValue();
    }

    private void debug(String text) {
        if (this.debug) {
            print(">> " + text);
        }
    }

    private void print(String text) {
        System.out.println(text);
    }

}