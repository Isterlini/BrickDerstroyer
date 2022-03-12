// import Gdx class to get user input
import com.badlogic.gdx.Gdx;
// import constants that represent each key
import com.badlogic.gdx.Input.Keys;

// working with text
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

// value-based animations
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

// music and sounds
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import com.badlogic.gdx.math.Intersector.MinimumTranslationVector;
import com.badlogic.gdx.math.Vector2;

public class LevelScreen extends BaseScreen
{
    Paddle paddle;

    int score;
    int reserveBalls;
    Label scoreLabel;
    Label reserveBallLabel;

    Label messageLabel;

    boolean over;
    
    private float audioVolume;
    private Sound explosion; 
    private Sound wallbump;
    private Sound paddleBump;
    private Sound brickBump;
    private Sound pwp;    
    private Sound paddlePwp;
    public void initialize()
    {
        Background background = new Background(0,0, mainStage);

        Wall wallLeft = new Wall(0,0, mainStage);
        wallLeft.setSize(40,600);

        Wall wallRight = new Wall(760,0, mainStage);
        wallRight.setSize(40,600);

        Wall wallTop = new Wall(0,500, mainStage);
        wallTop.setSize(800,100);

        for (BaseActor wall : BaseActor.getList(mainStage, "Wall"))
            wall.setColor( Color.PURPLE );

        int brickRows = 6;
        int brickColumns = 8;
        Brick tempBrick = new Brick(0,0,mainStage);
        int brickWidth = (int)tempBrick.getWidth();
        int brickHeight = (int)tempBrick.getHeight();
        tempBrick.remove();
        int horizontalMargin = (800 - brickWidth * brickColumns) / 2;
        int verticalMargin = (600 - brickHeight * brickRows) / 2;

        // assign colors to bricks based on row number
        Color[] brickColors = {Color.RED, Color.ORANGE, Color.YELLOW,
                Color.GREEN, Color.BLUE, Color.VIOLET};

        for (int rowNumber = 0; rowNumber < brickRows; rowNumber++)
        {
            for (int columnNumber = 0; columnNumber < brickColumns; columnNumber++)
            {
                Brick brick = new Brick(0,0, mainStage);
                int x = columnNumber * brickWidth + horizontalMargin;
                int y = rowNumber * brickHeight + verticalMargin;
                brick.setPosition(x,y);
                brick.setColor( brickColors[rowNumber] );
            }
        }

        new Ball(400, 80, mainStage);

        paddle = new Paddle(400, 50, mainStage);

        score = 0;
        reserveBalls = 3;

        scoreLabel = new Label("Score: 0", BaseGame.labelStyle);
        reserveBallLabel = new Label("Balls: 3", BaseGame.labelStyle);

        mainStage.addActor(scoreLabel);
        scoreLabel.setPosition( 20, 520 );

        mainStage.addActor(reserveBallLabel);
        reserveBallLabel.setPosition( 580, 520 );

        messageLabel = new Label("Press Space to Start", BaseGame.labelStyle);
        mainStage.addActor( messageLabel );
        messageLabel.setPosition( 150, 120 );

        over=false;

        explosion = Gdx.audio.newSound( Gdx.files.internal("audio/explosion.ogg") );
        wallbump = Gdx.audio.newSound( Gdx.files.internal("audio/boing.wav") );
        paddleBump = Gdx.audio.newSound( Gdx.files.internal("audio/bump.wav") );
        pwp = Gdx.audio.newSound( Gdx.files.internal("audio/pop.wav") );
        brickBump = Gdx.audio.newSound( Gdx.files.internal("audio/bump-low.wav") );
        paddlePwp = Gdx.audio.newSound( Gdx.files.internal("audio/sparkle.ogg")        );
        audioVolume = 1.00f;
    }

    public void update(float deltaTime)
    {
        if ( Gdx.input.isKeyPressed( Keys.LEFT ) )
            paddle.physics.accelerateAtAngle(180);
        if ( Gdx.input.isKeyPressed( Keys.RIGHT ) )
            paddle.physics.accelerateAtAngle(0);

        // if ball is not moving, then position it on center top of paddle
        for (BaseActor ball : BaseActor.getList(mainStage, "Ball"))
        {
            if ( ball.physics.getSpeed() < 0.001 )
            {
                ball.centerAtActor(paddle);
                ball.moveBy(0,28);
            }
        }
        // press space to start the ball moving
        if ( Gdx.input.isKeyJustPressed( Keys.SPACE ) )
        {
            messageLabel.setVisible(false);

            for (BaseActor ball : BaseActor.getList(mainStage, "Ball"))
            {
                if ( ball.physics.getSpeed() < 0.001 )
                {
                    ball.physics.setSpeed(400);
                    ball.physics.setMotionAngle(90);
                }
            }
        }

        for (BaseActor wall : BaseActor.getList(mainStage, "Wall"))
        {
            if ( paddle.overlaps(wall) )
            {
                paddle.preventOverlap(wall);
                paddle.physics.velocity.set(0,0);
                paddle.physics.acceleration.set(0,0);
            }
        }

        // check for ball-object collision
        for (BaseActor ball : BaseActor.getList(mainStage, "Ball"))
        {
            // ball-wall collisions
            for (BaseActor wall : BaseActor.getList(mainStage, "Wall"))
            {
                if (ball.overlaps(wall))
                {
                    Vector2 mtv = ball.preventOverlap(wall);
                    if (mtv != null)
                    {
                        float surfaceAngle = mtv.angle() + 90;
                        ball.physics.bounceAgainst(surfaceAngle);
                    }
                    wallbump.play(audioVolume);
                }
            }

            // ball-brick collisions
            for (BaseActor brick : BaseActor.getList(mainStage, "Brick"))
            {
                if (ball.overlaps(brick)&&!over)
                {
                    Vector2 mtv = ball.preventOverlap(brick);
                    if (mtv != null)
                    {
                        float surfaceAngle = mtv.angle() + 90;
                        ball.physics.bounceAgainst(surfaceAngle);
                    }
                    brickBump.play(audioVolume);
                    brick.remove();
                    if(ball.getColor().equals(Color.RED))
                    {
                        Explosion explode = new Explosion(0,0,mainStage);
                        explode.centerAtActor(brick);
                        explode.setScale(2);
                        explosion.play(audioVolume);
                        if(explode.overlaps(brick) )
                        {
                            brick.remove();
                            score+=100;
                        }
                        explode.toFront();

                    }
                    score += 100;
                    scoreLabel.setText("Score: " + score);

                    double powerupProbability = 0.50;
                    if (Math.random() < powerupProbability&&!over)
                    {
                        Powerup powerup = new Powerup(0,0,mainStage);
                        powerup.centerAtActor(brick);
                        pwp.play(audioVolume);
                    }
                }
            }

            // ball-paddle collisions
            if (ball.overlaps(paddle))
            {
                ball.preventOverlap(paddle);
                float min = paddle.getX();
                float max = paddle.getX() + paddle.getWidth();
                float x = ball.getX() + ball.getWidth()/2;
                float percent = percent(min, max, x);
                float bounceAngle = lerp(135, 45, percent);
                ball.physics.setMotionAngle(bounceAngle);
                paddleBump.play(audioVolume);
            }
        }

        // when ball goes off screen, spawn a new ball
        if ( BaseActor.getList(mainStage, "Ball").size() == 0 )
        {
            // destroy all on-screen powerups
            for (BaseActor powerup : BaseActor.getList(mainStage, "Powerup"))
                powerup.remove();

            if ( reserveBalls > 0 && !over )
            {
                new Ball(0,0, mainStage);
                messageLabel.setVisible(true);
                reserveBalls--;
                reserveBallLabel.setText("Balls: " + reserveBalls);
            }
            else // reserveBalls == 0
            {
                messageLabel.setText("Game Over");
                messageLabel.setVisible(true);
                messageLabel.setPosition(250, 150);
                messageLabel.setColor(Color.RED);
                over = true;
            }
        }

        //win condition 
        if(BaseActor.getList(mainStage,"Brick").size() ==0)
        {
            over=true;
            messageLabel.setText("You Win!");
            messageLabel.setVisible(true);
            messageLabel.setPosition(250,150);
            messageLabel.setColor(Color.GREEN);
        }

        // paddle overlaps powerups
        for (BaseActor powerup : BaseActor.getList(mainStage, "Powerup"))
        {
            if (paddle.overlaps(powerup))
            {
                powerup.remove();
                paddlePwp.play(audioVolume);
                String type = ((Powerup)powerup).imageName;
                if ( type.equals("ball-speed-down") )
                {
                    for (BaseActor ball : BaseActor.getList(mainStage, "Ball"))
                    {
                        ball.physics.setSpeed(200);
                    }
                }
                else if ( type.equals("ball-speed-up") )
                {
                    for (BaseActor ball : BaseActor.getList(mainStage, "Ball"))
                    {
                        ball.physics.setSpeed(600);
                    }
                }
                else if ( type.equals("ball-spawn") )
                {
                    Ball ball = new Ball(0,0, mainStage);
                    ball.centerAtActor(paddle);
                    ball.moveBy(0,28);
                    ball.physics.setSpeed(400);
                    ball.physics.setMotionAngle(90);
                }
                else if ( type.equals("ball-small") )
                {
                    for (BaseActor ball : BaseActor.getList(mainStage, "Ball"))
                    {
                        ball.addAction(
                            Actions.scaleTo(0.5f, 0.5f, 0.5f)
                        );
                    }
                }
                else if ( type.equals("ball-large") )
                {
                    for (BaseActor ball : BaseActor.getList(mainStage, "Ball"))
                    {
                        ball.addAction(
                            Actions.scaleTo(2, 2, 0.5f)
                        );
                    }
                }
                else if (type.equals("paddle-expand") )
                {
                    paddle.addAction(
                        Actions.scaleTo(2,2,0.5f)
                    );
                }
                else if (type.equals("paddle-shrink") )
                {
                    paddle.addAction(
                        Actions.scaleTo(0.5f,0.5f,0.5f)
                    );
                }
                else if (type.equals("bonus-points") )
                {
                    score += 1000;
                }
                else if(type.equals("ball-extra") )
                {
                    reserveBalls++;
                    reserveBallLabel.setText("Balls: " + reserveBalls);
                }
                else if (type.equals("ball-fire") )
                {
                    for (BaseActor ball : BaseActor.getList(mainStage, "Ball"))
                    {
                        for (BaseActor brick : BaseActor.getList(mainStage, "Brick"))
                        {
                            ball.setColor(Color.RED);
                        }
                    }
                }
                else if (type.equals("paddle-destroy"))
                {
                    paddle.remove();
                    messageLabel.setText("Game Over");
                    messageLabel.setVisible(true);
                    messageLabel.setPosition(250, 150);
                    messageLabel.setColor(Color.RED);
                    over = true;
                }
            }
        }
    }

    // helper methods for calculating bounce angle for ball off paddle
    //   example: min = 2, max = 10
    //            percent = 0.50   ->   lerp = 6
    //            percent = 0.25   ->   lerp = 4

    public float lerp(float min, float max, float percent)
    {
        return (min + percent * (max - min));
    }

    public float percent(float min, float max, float x)
    {
        return ( (x - min) / (max - min) );
    }

}