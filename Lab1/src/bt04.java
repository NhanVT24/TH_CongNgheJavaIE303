import java.util.*;

public class bt04 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();
        int k = sc.nextInt();

        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }

        // dp[s] = độ dài lớn nhất của dãy con có tổng s
        int[] dp = new int[k + 1];
        Arrays.fill(dp, -1);
        dp[0] = 0;

        // dùng để truy vết
        int[] prevSum = new int[k + 1];
        int[] chosenIndex = new int[k + 1];
        Arrays.fill(prevSum, -1);
        Arrays.fill(chosenIndex, -1);

        // xét từng phần tử
        for (int i = 0; i < n; i++) {
            // duyệt ngược để mỗi phần tử chỉ dùng 1 lần
            for (int s = k; s >= a[i]; s--) {
                if (dp[s - a[i]] != -1 && dp[s - a[i]] + 1 > dp[s]) {
                    dp[s] = dp[s - a[i]] + 1;
                    prevSum[s] = s - a[i];
                    chosenIndex[s] = i;
                }
            }
        }

        // không có dãy con nào tổng bằng k
        if (dp[k] == -1) {
            System.out.println("Khong ton tai");
            return;
        }

        // truy vết đáp án
        List<Integer> ans = new ArrayList<>();
        int cur = k;

        while (cur > 0) {
            int idx = chosenIndex[cur];
            ans.add(a[idx]);
            cur = prevSum[cur];
        }

        Collections.reverse(ans);

        for (int i = 0; i < ans.size(); i++) {
            if (i > 0) System.out.print(" ");
            System.out.print(ans.get(i));
        }
    }
}