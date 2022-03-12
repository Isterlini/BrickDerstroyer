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

public class ControlScreen extends BaseScreen
{

    public void initialize()
    {
        Label move = new Label("use the left and right arrow keys",BaseGame.labelStyle);
        move.setPosition(0,500);
        move.setScale(0.01f);
        mainStage.addActor(move);

        display fireball = new display(0,460,mainStage);
        fireball.setAnimator(new Animator("images/items/ball-fire.png"));
        fireball.setSize(50,50);
        Label control1 = new Label("Makes the ball red and causes explosion on impact",BaseGame.labelStyle);
        control1.setPosition(50,460);
        mainStage.addActor(control1);

        display extra = new display(0,400,mainStage);
        extra.setAnimator(new Animator("images/items/ball-extra.png"));
        extra.setSize(50,50);
        Label control2 = new Label("adds 1 to reserved balls",BaseGame.labelStyle); 
        control2.setPosition(50,400);
        mainStage.addActor(control2);

        display large = new display(0,360,mainStage);
        large.setAnimator(new Animator("images/items/ball-large.png"));
        large.setSize(50,50);
        Label control3 = new Label("enlarges the ball",BaseGame.labelStyle); 
        control3.setPosition(50,360);
        mainStage.addActor(control3);

        display small = new display(0,320,mainStage);
        small.setAnimator(new Animator("images/items/ball-small.png"));
        small.setSize(50,50);
        Label control4 = new Label("makes the ball smaller",BaseGame.labelStyle); 
        control4.setPosition(50,320);
        mainStage.addActor(control4);

        display speedup = new display(0,260,mainStage);
        speedup.setAnimator(new Animator("images/items/ball-speed-up.png"));
        speedup.setSize(50,50);
        Label control5 = new Label("makes the ball faster",BaseGame.labelStyle); 
        control5.setPosition(50,260);
        mainStage.addActor(control5);

        display speeddown = new display(0,220,mainStage);
        speeddown.setAnimator(new Animator("images/items/ball-speed-down.png"));
        speeddown.setSize(50,50);
        Label control6 = new Label("makes the ball slower",BaseGame.labelStyle); 
        control6.setPosition(50,220);
        mainStage.addActor(control6);

        display bonus = new display(0,160,mainStage);
        bonus.setAnimator(new Animator("images/items/bonus-points.png"));
        bonus.setSize(50,50);
        Label control7 = new Label("adds 1000 points to your score",BaseGame.labelStyle); 
        control7.setPosition(50,160);
        mainStage.addActor(control7);

        display shrink = new display(0,120,mainStage);
        shrink.setAnimator(new Animator("images/items/paddle-shrink.png"));
        shrink.setSize(50,50);
        Label control8 = new Label("makes the paddle smaller",BaseGame.labelStyle); 
        control8.setPosition(50,120);
        mainStage.addActor(control8);

        
        display destroy = new display(0,80,mainStage);
        destroy.setAnimator(new Animator("images/items/paddle-destroy.png"));
        destroy.setSize(50,50);
        Label control9 = new Label("destroys the paddle, makes it game over",BaseGame.labelStyle); 
        control9.setPosition(50,80);
        mainStage.addActor(control9);

        display expand = new display(0,45,mainStage);
        expand.setAnimator(new Animator("images/items/paddle-expand.png"));
        expand.setSize(50,50);
        Label control10 = new Label("extends the paddle",BaseGame.labelStyle); 
        control10.setPosition(50,45);
        mainStage.addActor(control10);

        display newball = new display(0,0,mainStage);
        newball.setAnimator(new Animator("images/items/ball-spawn.png"));
        newball.setSize(50,50);
        Label control11 = new Label("makes a new ball from the paddle",BaseGame.labelStyle); 
        control11.setPosition(50,0);
        mainStage.addActor(control11);
    }

    public void update(float deltaTime)
    {
        if ( Gdx.input.isKeyPressed( Keys.SPACE ) )
            BaseGame.setActiveScreen( new LevelScreen());
    }

}