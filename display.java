
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.MathUtils;
public class display extends BaseActor
{
    public String imageName;
    public display(float x,float y, Stage s)
    {
        super(x, y, s);
        String[] imageNames = {"ball-speed-down", "ball-speed-up",
                "ball-spawn", "ball-large", "ball-small","paddle-shrink"
            ,"paddle-expand","bonus-points","ball-fire"
            ,"ball-extra","paddle-destroy"};
       int randomIndex = MathUtils.random(0, imageNames.length - 1);
        this.imageName = imageNames[ randomIndex ];
       String fileName = "images/items/" + imageName + ".png";
        this.setAnimator( new Animator(fileName) );

        this.setSize(50,50);
        
        this.setScale(0.01f);
        this.addAction(
            Actions.scaleTo(1,1, 0.5f)
        );
    }
}