import javax.xml.transform.Source;
import java.util.Random;
import java.util.Scanner;

public class bt01 {
    public static double dientich(double r, int N) {
        Random rd = new Random();
        int inside = 0;

        for (int i = 0; i < N; i++) {
            double x = -r + 2 * r * rd.nextDouble();
            double y = -r + 2 * r * rd.nextDouble();

            if (x * x + y * y <= r * r) {
                inside++;
            }
        }

        return 4.0 * inside / N;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Vui Long Nhap ban kinh : ");
        double r = sc.nextDouble();
        int N = 1000000;
        System.out.println(dientich(r, N));
    }
}