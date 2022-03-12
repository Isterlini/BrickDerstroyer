import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.MathUtils;


public class Explosion extends BaseActor
{
    public Explosion(float x, float y, Stage s)
    {
        super(x, y, s);
        this.setAnimator( new Animator("images/explosion.png",6,6,1/20f,false) );
        this.setBoundaryPolygon(8);
    }

    public void act(float deltaTime)
    {
        super.act(deltaTime);
         //when the animation is finished remove from the game
        if(this.animator.isAnimationFinished())
        this.remove();
    }
}