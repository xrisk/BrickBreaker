import java.awt.Color;
import java.awt.event.MouseEvent;

import acm.graphics.*;
import acm.program.*;
import acm.util.RandomGenerator;
import static java.awt.Color.*;

public class Main extends GraphicsProgram
{
    private static final long      serialVersionUID = 1L;
    private static int             BRICK_WIDTH      = 40;
    private static int             BRICK_HEIGHT     = 20;
    private static int             WINDOW_WIDTH     = 800;
    private static int             WINDOW_HEIGHT    = 600;
    private static int             BALL_DIAMETER    = 10;
    private static int             PADDLE_HEIGHT    = 20;
    private static int             PADDLE_WIDTH     = 150;
    private static Color           BALL_COLOR       = Color.BLACK;
    private static int             ROW_GAP          = 0;
    private static boolean         CHEAT            = false;
    private static RandomGenerator rgen             = new RandomGenerator();
    private static boolean         gameActive       = false;
    private static int             paddleX;
    private GOval                  ball;
    private GRoundRect             paddle;
    private int                    bricksHit        = 0;
    private GLabel                 brickCount;

    public void run()
    {
        

        int halfHeight = (int) (WINDOW_HEIGHT * 0.5);
        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        // set the size of the window to predefined constants
        this.setBackground(rgen.nextColor());
        int bricksLeft = 0;
        GRect brick;
        for (int i = 0; i < halfHeight; i += (BRICK_HEIGHT + ROW_GAP))
        {
            Color rowColor = rgen.nextColor();
            for (int j = 0; j < WINDOW_WIDTH; j += BRICK_WIDTH)
            {
                brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
                brick.setFillColor(rowColor);
                brick.setFilled(true);
                add(brick, j, i);
                bricksLeft++;
            }
        }
        ball = new GOval(BALL_DIAMETER, BALL_DIAMETER);
        ball.setFillColor(BALL_COLOR);
        ball.setFilled(true);
        add(ball, WINDOW_WIDTH / 2 - BALL_DIAMETER / 2, WINDOW_HEIGHT - PADDLE_HEIGHT
                - BALL_DIAMETER);
        paddle = new GRoundRect(PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setFillColor(Color.ORANGE);
        paddle.setFilled(true);
        add(paddle, WINDOW_WIDTH / 2 - PADDLE_WIDTH / 2, WINDOW_HEIGHT - PADDLE_HEIGHT);
        brickCount = new GLabel("Your Score: " + bricksHit);
        brickCount.setFont("Segoe UI-15");
        brickCount.setColor(Color.RED);
        add(brickCount, 20, WINDOW_HEIGHT - 20);
        this.addMouseListeners();
        while (!(gameActive))
        {
            pause(100);
        }
        int ballDeltaX = rgen.nextInt(3, 5);
        int ballDeltaY = rgen.nextInt(-5, -7);
        if (rgen.nextBoolean())
        {
            ballDeltaX /= -1;
        }
        while (gameActive)
        {
            if (CHEAT)
            {
                paddle.setLocation(ball.getX() - paddle.getWidth() / 2, WINDOW_HEIGHT
                        - PADDLE_HEIGHT);
            }
            else
            {
                paddle.setLocation(paddleX, WINDOW_HEIGHT - PADDLE_HEIGHT);
            }
            ball.move(ballDeltaX, ballDeltaY);
            if (ball.getX() + BALL_DIAMETER > WINDOW_WIDTH || ball.getX() < 0)
            {
                ballDeltaX /= -1;
            }
            if (ball.getY() - 2 < 0)
            {
                ballDeltaY /= -1;
            }
            if (ball.getY() + BALL_DIAMETER > WINDOW_HEIGHT)
            {
                gameActive = false;
                GLabel label = new GLabel("You Lose!");
                label.setColor(Color.RED);
                label.setFont("Segoe UI-60");
                this.add(label, (WINDOW_WIDTH - label.getWidth()) / 2, WINDOW_HEIGHT / 2);
                break;
            }
            GObject temp = null;
            temp = getElementAt(ball.getX() + BALL_DIAMETER / 2, ball.getY() + BALL_DIAMETER);
            if (temp != null && !(temp instanceof GLabel) && !(temp instanceof GOval))
            {
                ballDeltaY /= -1;
            }
            if (temp instanceof GRect && !(temp instanceof GRoundRect) && !(temp instanceof GLabel))
            {
                bricksLeft--;
                remove(temp);
                if (bricksLeft == 0)
                {
                    gameActive = false;
                    GLabel label = new GLabel("You Win!");
                    label.setColor(Color.RED);
                    label.setFont("Segoe UI-60");
                    this.add(label, (WINDOW_WIDTH - label.getWidth()) / 2, WINDOW_HEIGHT / 2);
                    bricksHit++;
                    brickCount.setLabel("Your Score: " + bricksHit);
                    break;
                }
                bricksHit++;
                brickCount.setLabel("Your Score: " + bricksHit);
                if (bricksHit % 10 == 0 && bricksHit != 0)
                {
                    if (rgen.nextBoolean())
                    {
                        ballDeltaX += rgen.nextInt(2);
                    }
                    else
                    {
                        ballDeltaX -= rgen.nextInt(2);
                    }
                    ballDeltaY += rgen.nextInt(4);
                }
            }
            temp = getElementAt(ball.getX(), ball.getY());
            if (temp != null && !(temp instanceof GLabel) && !(temp instanceof GOval))
            {
                ballDeltaY /= -1;
            }
            if (temp instanceof GRect && !(temp instanceof GRoundRect))
            {
                bricksLeft--;
                remove(temp);
                if (bricksLeft == 0)
                {
                    gameActive = false;
                    GLabel label = new GLabel("You Win!");
                    label.setColor(Color.RED);
                    label.setFont("Segoe UI-60");
                    this.add(label, (WINDOW_WIDTH - label.getWidth()) / 2, WINDOW_HEIGHT / 2);
                    bricksHit++;
                    brickCount.setLabel("Your Score: " + bricksHit);
                    break;
                }
                bricksHit++;
                brickCount.setLabel("Your Score: " + bricksHit);
                if (bricksHit % 10 == 0 && bricksHit != 0)
                {
                    if (rgen.nextBoolean())
                    {
                        ballDeltaX += rgen.nextInt(2);
                    }
                    else
                    {
                        ballDeltaX -= rgen.nextInt(2);
                    }
                    ballDeltaY += rgen.nextInt(4);
                    if (ballDeltaY == 0)
                    {
                        ballDeltaY = 4;
                    }
                }
            }
            pause(10);
        }
    }

    public void mouseClicked(MouseEvent evt)
    {
        gameActive = true;
    }

    public void mouseMoved(MouseEvent evt)
    {
        if (gameActive)
        {
            paddleX = evt.getX();
        }
    }
}