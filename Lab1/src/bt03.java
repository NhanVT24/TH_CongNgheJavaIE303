import java.util.*;

public class bt03 {

    // B1: Tạo lớp Point để lưu tọa độ điểm
    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Biến toàn cục dùng làm điểm gốc
    static Point p0;

    // B2: Hàm tính tích có hướng của 3 điểm a, b, c
    // cross > 0 : rẽ trái
    // cross < 0 : rẽ phải
    // cross = 0 : thẳng hàng
    static int cross(Point a, Point b, Point c) {
        return (b.x - a.x) * (c.y - a.y)
                - (b.y - a.y) * (c.x - a.x);
    }

    // B3: Hàm tính bình phương khoảng cách từ a đến b
    // dùng khi 2 điểm có cùng góc với p0
    static int distSq(Point a, Point b) {
        return (a.x - b.x) * (a.x - b.x)
                + (a.y - b.y) * (a.y - b.y);
    }

    // Hàm chính tìm bao lồi
    static List<Point> grahamScan(Point[] points) {
        int n = points.length;

        // Nếu ít hơn 3 điểm thì không cần xử lý nhiều
        if (n < 3) {
            return Arrays.asList(points);
        }

        // -------------------------------------------------
        // Bước 1: Tìm điểm thấp nhất (y nhỏ nhất, nếu bằng thì x nhỏ nhất)
        // -------------------------------------------------
        int minIndex = 0;
        for (int i = 1; i < n; i++) {
            if (points[i].y < points[minIndex].y ||
                    (points[i].y == points[minIndex].y && points[i].x < points[minIndex].x)) {
                minIndex = i;
            }
        }

        // Đưa điểm thấp nhất về đầu mảng
        Point temp = points[0];
        points[0] = points[minIndex];
        points[minIndex] = temp;

        // Gán điểm gốc
        p0 = points[0];

        // -------------------------------------------------
        // Bước 2: Sắp xếp các điểm còn lại theo góc với p0
        // Nếu cùng góc thì điểm gần hơn đứng trước
        // -------------------------------------------------
        Arrays.sort(points, 1, n, new Comparator<Point>() {
            @Override
            public int compare(Point a, Point b) {
                int c = cross(p0, a, b);

                // Nếu cùng phương với p0
                if (c == 0) {
                    return distSq(p0, a) - distSq(p0, b);
                }

                // c > 0 nghĩa là a đứng trước b
                return -c;
            }
        });

        // -------------------------------------------------
        // Bước 3: Loại bớt các điểm trùng góc
        // Giữ lại điểm xa nhất nếu nhiều điểm cùng góc
        // -------------------------------------------------
        ArrayList<Point> filtered = new ArrayList<>();
        filtered.add(points[0]);

        for (int i = 1; i < n; i++) {
            // while điểm tiếp theo cùng góc với điểm hiện tại so với p0
            while (i < n - 1 && cross(p0, points[i], points[i + 1]) == 0) {
                i++;
            }
            filtered.add(points[i]);
        }

        // Nếu sau khi lọc mà còn < 3 điểm thì trả luôn
        if (filtered.size() < 3) {
            return filtered;
        }

        // -------------------------------------------------
        // Bước 4: Dùng Stack để xây bao lồi
        // -------------------------------------------------
        Stack<Point> stack = new Stack<>();

        // Đưa 3 điểm đầu vào stack
        stack.push(filtered.get(0));
        stack.push(filtered.get(1));
        stack.push(filtered.get(2));

        // -------------------------------------------------
        // Bước 5: Xét từng điểm tiếp theo
        // Nếu tạo rẽ phải thì pop
        // Nếu rẽ trái thì push
        // -------------------------------------------------
        for (int i = 3; i < filtered.size(); i++) {
            Point current = filtered.get(i);

            while (stack.size() >= 2) {
                Point top = stack.pop();        // lấy đỉnh stack
                Point nextToTop = stack.peek(); // phần tử dưới đỉnh

                // Nếu rẽ trái thì giữ lại top
                if (cross(nextToTop, top, current) > 0) {
                    stack.push(top);
                    break;
                }
                // Nếu không rẽ trái thì bỏ top
            }

            stack.push(current);
        }

        return new ArrayList<>(stack);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Nhập số lượng điểm
        int n = sc.nextInt();
        Point[] points = new Point[n];

        // Nhập tọa độ các điểm
        for (int i = 0; i < n; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            points[i] = new Point(x, y);
        }

        // Gọi hàm tìm bao lồi
        List<Point> hull = grahamScan(points);

        // In kết quả
        for (Point p : hull) {
            System.out.println(p.x + " " + p.y);
        }
    }
}