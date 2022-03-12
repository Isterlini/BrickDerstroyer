import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.MathUtils;


public class title extends BaseActor
{
    public title(float x, float y, Stage s)
    {
        super(x, y, s);
        this.setAnimator( new Animator("images/title.png") );
    }

    public void act(float deltaTime)
    {
        super.act(deltaTime);

    }
}