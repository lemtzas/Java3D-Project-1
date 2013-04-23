import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

/**
 * Created with IntelliJ IDEA.
 * User: Lemtzas
 * Date: 4/22/13
 * Time: 7:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class InsertBehavior extends Behavior {

    private WakeupOr wakeupEvents;
    private TransformGroup curTransform;
    private BranchGroup scene;
    private Node curObject = new Sphere();

    /**
     *
     * @param scene where should we insert new elements?
     */
    public InsertBehavior(BranchGroup scene, TransformGroup curTransform) {
        this.scene = scene;
        this.curTransform = curTransform;


        WakeupCriterion[] eventsNormal = {
                new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED),
                new WakeupOnAWTEvent(KeyEvent.KEY_RELEASED),
                new WakeupOnAWTEvent(KeyEvent.KEY_TYPED)};
        wakeupEvents = new WakeupOr(eventsNormal);
    }

    @Override
    public void initialize() {
        wakeupOn(wakeupEvents);
    }

    @Override
    public void processStimulus(Enumeration criteria) {
        WakeupCriterion wakeup;
        AWTEvent[] events;

        wakeup = (WakeupCriterion)criteria.nextElement();

        //this should be the only type of event
        if (wakeup instanceof WakeupOnAWTEvent) {
            events = ((WakeupOnAWTEvent)wakeup).getAWTEvent();
            for(AWTEvent e : events) {
                if(e instanceof KeyEvent) {
                    KeyEvent ke = (KeyEvent)e;
                    if(ke.getID() == KeyEvent.KEY_TYPED) {
                        switch(ke.getKeyChar()) {
                            case '1' : //insert
                                insertObject();
                                break;
                            case '2' : //place
                                placeObject();
                                break;
                            case '3' : //remove
                                removeObject();
                                break;
                            default:
                                //eh?
                        }
                    }
                } else {
                    //eh?
                }
            }
        }

        wakeupOn(wakeupEvents);
    }

    private void removeObject() {
        System.out.println("remove object");
    }

    private void placeObject() {
        System.out.println("place object");
    }

    private void insertObject() {

        System.out.println("insert object");

        Transform3D t3d = new Transform3D();
        curTransform.getTransform(t3d);
        TransformGroup tg = new TransformGroup(t3d);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);

        BranchGroup bg = new BranchGroup();
        bg.addChild(tg);
        tg.addChild(curObject.cloneTree());
        scene.addChild(bg);
    }
}
