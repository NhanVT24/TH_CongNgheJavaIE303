import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class angryBird extends JPanel implements ActionListener, KeyListener, MouseListener {

    // =========================
    // Kích thước cửa sổ
    // =========================
    int boardWidth = 360;
    int boardHeight = 640;

    // =========================
    // Ảnh
    // =========================
    Image backgroundImg;
    Image birdImg;
    Image topPipeImg;
    Image bottomPipeImg;

    // =========================
    // Bird
    // =========================
    int birdX = boardWidth / 8;
    int birdY = boardHeight / 2;
    int birdWidth = 34;
    int birdHeight = 24;

    int velocityY = 0;
    int gravity = 1;
    int jumpStrength = -10;

    // =========================
    // Trạng thái game
    // =========================
    boolean gameStarted = false;
    boolean gameOver = false;

    // =========================
    // Nút Start / Retry
    // =========================
    Rectangle startButton = new Rectangle(110, 300, 140, 50);
    Rectangle retryButton = new Rectangle(110, 360, 140, 50);

    // =========================
    // Điểm số
    // =========================
    int score = 0;
    int bestScore = 0;   // thành tích cao nhất
    int lastScore = 0;   // điểm lần gần nhất

    // =========================
    // Pipe
    // =========================
    class Pipe {
        int x, y, width, height;
        Image img;
        boolean passed = false;

        Pipe(Image img, int x, int y, int width, int height) {
            this.img = img;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    ArrayList<Pipe> pipes;
    int pipeWidth = 64;
    int pipeHeight = 512;
    int pipeVelocityX = -4;
    int pipeGap = 150;

    Random random = new Random();

    // =========================
    // Timer
    // =========================
    Timer gameLoop;
    Timer pipeTimer;

    public angryBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setFocusable(true);
        addKeyListener(this);
        addMouseListener(this);

        // Load ảnh
        backgroundImg = new ImageIcon("Lab02/flappybirdbg.png").getImage();
        birdImg = new ImageIcon("Lab02/flappybird.png").getImage();
        bottomPipeImg = new ImageIcon("Lab02/bottompipe.png").getImage();
        topPipeImg = new ImageIcon("Lab02/toppipe.png").getImage();

        pipes = new ArrayList<>();

        // Mỗi 1.5 giây tạo pipe
        pipeTimer = new Timer(1500, e -> placePipes());

        // Game loop 60 FPS
        gameLoop = new Timer(1000 / 60, this);
        gameLoop.start();
    }

    // =========================
    // Tạo pipe
    // =========================
    void placePipes() {
        if (!gameStarted || gameOver) return;

        int randomPipeY = -pipeHeight / 4 - random.nextInt(pipeHeight / 2);

        Pipe topPipe = new Pipe(topPipeImg, boardWidth, randomPipeY, pipeWidth, pipeHeight);
        Pipe bottomPipe = new Pipe(
                bottomPipeImg,
                boardWidth,
                randomPipeY + pipeHeight + pipeGap,
                pipeWidth,
                pipeHeight
        );

        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }

    // =========================
    // Bắt đầu game
    // =========================
    void startGame() {
        birdY = boardHeight / 2;
        velocityY = 0;
        pipes.clear();
        score = 0;
        gameOver = false;
        gameStarted = true;
        pipeTimer.start();
        requestFocusInWindow();
    }

    // =========================
    // Kết thúc game
    // =========================
    void endGame() {
        gameOver = true;
        pipeTimer.stop();

        lastScore = score;
        if (score > bestScore) {
            bestScore = score;
        }
    }

    // =========================
    // Cập nhật game
    // =========================
    void move() {
        if (!gameStarted || gameOver) return;

        velocityY += gravity;
        birdY += velocityY;

        // Không cho bird bay khỏi mép trên
        if (birdY < 0) {
            birdY = 0;
            velocityY = 0;
        }

        // Pipe chạy sang trái
        for (Pipe pipe : pipes) {
            pipe.x += pipeVelocityX;

            // Cộng điểm khi chim qua ống trên
            if (!pipe.passed && pipe.img == topPipeImg && birdX > pipe.x + pipe.width) {
                pipe.passed = true;
                score++;
            }
        }

        // Xóa pipe đã ra khỏi màn hình
        pipes.removeIf(pipe -> pipe.x + pipe.width < 0);

        // Chạm đất => game over
        if (birdY + birdHeight >= boardHeight) {
            birdY = boardHeight - birdHeight;
            endGame();
        }

        // Va chạm pipe => game over
        Rectangle birdRect = new Rectangle(birdX, birdY, birdWidth, birdHeight);
        for (Pipe pipe : pipes) {
            Rectangle pipeRect = new Rectangle(pipe.x, pipe.y, pipe.width, pipe.height);
            if (birdRect.intersects(pipeRect)) {
                endGame();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    // =========================
    // Vẽ giao diện
    // =========================
    void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // Vẽ background
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null);

        // Vẽ pipe
        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        // Vẽ bird
        g.drawImage(birdImg, birdX, birdY, birdWidth, birdHeight, null);

        // =========================
        // Thanh điểm phía trên
        // Score bên trái, Best bên phải
        // =========================
        g2d.setColor(new Color(0, 0, 0, 120));
        g2d.fillRoundRect(10, 10, 120, 40, 15, 15);
        g2d.fillRoundRect(220, 10, 130, 40, 15, 15);

        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 20));
        g2d.drawString("Score: " + score, 20, 37);
        g2d.drawString("Best: " + bestScore, 230, 37);

        // =========================
        // Màn hình bắt đầu
        // =========================
        if (!gameStarted) {
            g2d.setColor(new Color(0, 0, 0, 130));
            g2d.fillRect(0, 0, boardWidth, boardHeight);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 34));
            g2d.drawString("Flappy Bird", 82, 180);

            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            g2d.drawString("Nhan chuot vao nut de bat dau", 52, 225);
            g2d.drawString("SPACE / ENTER de bay len", 70, 255);

            // Nút Start
            g2d.setColor(new Color(255, 204, 0));
            g2d.fillRoundRect(startButton.x, startButton.y, startButton.width, startButton.height, 20, 20);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("START", startButton.x + 28, startButton.y + 33);

            return;
        }

        // =========================
        // Màn hình Game Over
        // =========================
        if (gameOver) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, boardWidth, boardHeight);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 34));
            g2d.drawString("Game Over", 85, 210);

            g2d.setFont(new Font("Arial", Font.BOLD, 23));
            g2d.drawString("Last Score: " + lastScore, 95, 260);
            g2d.drawString("Best Score: " + bestScore, 92, 295);

            g2d.setFont(new Font("Arial", Font.PLAIN, 17));
            g2d.drawString("Bam nut Retry de choi lai", 90, 330);

            // Nút Retry
            g2d.setColor(new Color(255, 204, 0));
            g2d.fillRoundRect(retryButton.x, retryButton.y, retryButton.width, retryButton.height, 20, 20);

            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 24));
            g2d.drawString("RETRY", retryButton.x + 28, retryButton.y + 33);
        }
    }

    // =========================
    // Bấm phím để bird bay
    // Chỉ bay khi đang chơi
    // Không restart bằng space nữa
    // =========================
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameStarted && !gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER) {
                velocityY = jumpStrength;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    // =========================
    // Bấm chuột vào Start / Retry
    // =========================
    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();

        if (!gameStarted && startButton.contains(p)) {
            startGame();
        }

        if (gameOver && retryButton.contains(p)) {
            startGame();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    // =========================
    // Main
    // =========================
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        angryBird gamePanel = new angryBird();

        frame.add(gamePanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}