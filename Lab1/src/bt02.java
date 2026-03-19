import java.util.Random;
import java.util.Scanner;

public class bt02 {
    public static Double tinhpi(int N){
        Random rd = new Random();
        int inside = 0;

        for(int i = 0; i < N; i++){
            double x = -1 + 2*rd.nextDouble();
            double y = -1 + 2*rd.nextDouble();

            if(x*x + y*y <= 1) inside++;
        }

        return 4.0*inside/N;
    }

    public static void main(String[] args) {
        int N = 1000000;
        System.out.println(tinhpi(N));
    }
}
