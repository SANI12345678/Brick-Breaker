import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class BrickBreaker extends JFrame implements KeyListener, ActionListener {
    private static final long serialVersionUID = 1L;

    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;
    private static final int BALL_SIZE = 20;
    private static final int BRICK_WIDTH = 40;
    private static final int BRICK_HEIGHT = 20;
    private static final int BRICK_ROWS = 5;
    private static final int BRICK_COLS = 10;
    private static final int BRICK_GAP = 5;

    private Timer timer;
    private int paddleX, ballX, ballY, ballXSpeed, ballYSpeed;
    private boolean left, right;
    private ArrayList<Rectangle> bricks;

    public BrickBreaker() {
        setTitle("Brick Breaker");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        addKeyListener(this);
        setFocusable(true);

        timer = new Timer(5, this);
        timer.start();

        initializeGame();
    }

    private void initializeGame() {
        paddleX = WIDTH / 2 - PADDLE_WIDTH / 2;
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT - PADDLE_HEIGHT - BALL_SIZE - 1;
        ballXSpeed = 2;
        ballYSpeed = -2;

        bricks = new ArrayList<>();
        for (int row = 0; row < BRICK_ROWS; row++) {
            for (int col = 0; col < BRICK_COLS; col++) {
                int brickX = col * (BRICK_WIDTH + BRICK_GAP);
                int brickY = row * (BRICK_HEIGHT + BRICK_GAP);
                Rectangle brick = new Rectangle(brickX, brickY, BRICK_WIDTH, BRICK_HEIGHT);
                bricks.add(brick);
            }
        }
    }

    private void movePaddle() {
        if (left && paddleX > 0) {
            paddleX -= 3;
        }
        if (right && paddleX < WIDTH - PADDLE_WIDTH) {
            paddleX += 3;
        }
    }

    private void moveBall() {
        ballX += ballXSpeed;
        ballY += ballYSpeed;

        // Ball and wall collisions
        if (ballX <= 0 || ballX >= WIDTH - BALL_SIZE) {
            ballXSpeed = -ballXSpeed;
        }
        if (ballY <= 0) {
            ballYSpeed = -ballYSpeed;
        }

        // Ball and paddle collision
        if (ballY + BALL_SIZE >= HEIGHT - PADDLE_HEIGHT && ballX + BALL_SIZE >= paddleX && ballX <= paddleX + PADDLE_WIDTH) {
            ballYSpeed = -ballYSpeed;
        }

        // Ball and brick collisions
        for (int i = 0; i < bricks.size(); i++) {
            if (bricks.get(i).intersects(new Rectangle(ballX, ballY, BALL_SIZE, BALL_SIZE))) {
                bricks.remove(i);
                ballYSpeed = -ballYSpeed;
                break;
            }
        }
    }

    private void checkGameOver() {
        if (ballY >= HEIGHT) {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over", "Game Over", JOptionPane.INFORMATION_MESSAGE);
            initializeGame();
            timer.start();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        movePaddle();
        moveBall();
        checkGameOver();
        repaint();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Draw paddle
        g.fillRect(paddleX, HEIGHT - PADDLE_HEIGHT, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw ball
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Draw bricks
        for (Rectangle brick : bricks) {
            g.fillRect(brick.x, brick.y, BRICK_WIDTH, BRICK_HEIGHT);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = true;
        }
        if (key == KeyEvent.VK_RIGHT) {
            right = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            right = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BrickBreaker brickBreaker = new BrickBreaker();
            brickBreaker.setVisible(true);
        });
    }
}
