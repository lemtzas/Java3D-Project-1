import com.sun.j3d.utils.geometry.Sphere;

import javax.media.j3d.*;
import javax.vecmath.Vector3d;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;

/**
 * A Behavior meant to work in concert with CamGrabBehavior and FlyCam.
 *
 * This behavior's job is to manage the selection and insertion of objects around the "cursor" position.
 */
public class InsertBehavior extends Behavior {

    /**What events are we listening to?**/
    private WakeupOr wakeupEvents;
    /**The transform to the cursor's space**/
    private TransformGroup curTransform;
    /**Where we add things**/
    private BranchGroup scene;
    /**The object to insert into the world when requested**/
    private Node curObject = new Sphere();




    /**scratch space variable**/
    Transform3D t3d = new Transform3D();
    /**scratch space variable**/
    Transform3D t3d2 = new Transform3D();
    /**scratch space variable**/
    Vector3d v3d = new Vector3d();
    /**scratch space variable**/
    Vector3d v3d2 = new Vector3d();



    /**
     * Sets up the Insert Behavior
     * @param scene where should we insert new elements?
     * @param curTransform Where is the cursor?
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

    /**Remove the objects currently selected**/
    private void removeObject() {
        System.out.println("remove object");

        //copy over appropriate children
        for(int i = curTransform.numChildren()-1; i >= 0 ; i--){
            Node child = curTransform.getChild(i);
            if(child instanceof BranchGroup) {
                curTransform.removeChild(i);
            }
        }
    }

    /**Place the currently selected objects into the world**/
    private void placeObject() {
        System.out.println("place objects");

        //copy the current transform
        Transform3D t3d = new Transform3D();
        curTransform.getTransform(t3d);
        TransformGroup tg = new TransformGroup(t3d);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        tg.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);

        BranchGroup bg = new BranchGroup();
        bg.addChild(tg);

        //copy over appropriate children
        for(int i = curTransform.numChildren()-1; i >= 0 ; i--){
            Node child = curTransform.getChild(i);
            if(child instanceof BranchGroup) {
                curTransform.removeChild(i);
                tg.addChild(child);
            }
        }

        //tg.addChild(curObject.cloneTree());
        scene.addChild(bg);
    }

    /**Insert a new object at the cursor. Starts "selected"**/
    private void insertObject() {
        System.out.println("insert object");

        //add branch group (for detach) and a transform (for manipulation) with the new object at the root
        BranchGroup bg = new BranchGroup();
        bg.setCapability(BranchGroup.ENABLE_PICK_REPORTING);
        bg.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        TransformGroup tg = new TransformGroup();
        bg.addChild(tg);
        bg.setCapability(BranchGroup.ALLOW_DETACH);
        tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.addChild(curObject.cloneTree());
        curTransform.addChild(bg);
    }

    /**Select an object from the world.
     * @param node The object to select. Should already be removed from previous parent.
     * @param local The transform to the selected object before it was removed.
     */
    public void selectObject(Node node, Transform3D local) {
        //node.getLocalToVworld(this.t3d);
        local.get(v3d);  //get object translation
        curTransform.getTransform(t3d2);
        t3d2.get(v3d2); //get translation from cur position
        v3d.sub(v3d2); //calculate offset

        BranchGroup bg;
        if(node instanceof BranchGroup) {
            bg = (BranchGroup)node;
        } else {
            //encapsulate the node in a detachable BranchGroup
            bg = new BranchGroup();
            bg.setCapability(BranchGroup.ENABLE_PICK_REPORTING);
            bg.setCapability(TransformGroup.ALLOW_CHILDREN_WRITE);
            bg.setCapability(BranchGroup.ALLOW_DETACH);
            TransformGroup tg = new TransformGroup();
            bg.addChild(node);
        }
//        bg.setCapability(BranchGroup.ALLOW_DETACH);
//        tg.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
//        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
//        tg.addChild(curObject.cloneTree());
        curTransform.addChild(bg);
    }

    /**Sets the object to insert**/
    public void setObject(Node node) {
        this.curObject = node;
    }
}
