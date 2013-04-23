import com.sun.j3d.utils.pickfast.PickCanvas;
import com.sun.j3d.utils.picking.PickResult;
import com.sun.j3d.utils.picking.PickTool;

import javax.media.j3d.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import javax.vecmath.Vector3d;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

/**
 * A Behavior that works in conjunction with InsertBehavior and FlyCam.
 *
 * This Behavior's job is to listen for object selections through the left mouse click and
 * forward those to InsertBehavior to "grab" the object for repositioning.
 */
public class CamGrabBehavior extends Behavior {

    private PickCanvas pickCanvas;
    private WakeupOr wakeupEvents;
    private InsertBehavior insertBehavior;
    private BranchGroup root;

    public CamGrabBehavior(Canvas3D canvas3D, BranchGroup branchGroup, InsertBehavior insertBehavior) {
        this.insertBehavior = insertBehavior;
        this.root = branchGroup;
        pickCanvas = new PickCanvas(canvas3D, branchGroup);
        pickCanvas.setMode(PickInfo.PICK_GEOMETRY);
        pickCanvas.setFlags(PickInfo.SCENEGRAPHPATH);

        WakeupCriterion[] eventsNormal = {
                new WakeupOnAWTEvent(MouseEvent.MOUSE_CLICKED)};
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

        // Check all wakeup criteria...
        while(criteria.hasMoreElements()) {
            wakeup = (WakeupCriterion)criteria.nextElement();

            //should be the only event
            if (wakeup instanceof WakeupOnAWTEvent) {
                events = ((WakeupOnAWTEvent)wakeup).getAWTEvent();
                for(AWTEvent e : events) {
                    if(e instanceof MouseEvent) {
                        handleMouse((MouseEvent)e);
                    } else {
                        //eh?
                    }
                }
            }
        }
        wakeupOn(wakeupEvents);
    }

    /**
     * Handle the mouse input. Interprets a left click to mean "grab object"
     * @param e
     */
    private void handleMouse(MouseEvent e) {
        //only respond to middle click
        if(e.getButton() != MouseEvent.BUTTON1) return;

        //check and bail out if needed
        pickCanvas.setShapeLocation(e);
        PickInfo pi = pickCanvas.pickClosest();
        if(pi == null) {
            System.out.println("no pick");
            return;
        }
        SceneGraphPath sgp = pi.getSceneGraphPath();
        if(sgp == null) {
            System.out.println("no path");
            return;
        }
        System.out.println(sgp);


        //process the point for selection
        Transform3D t3d = sgp.getTransform(); //save the transform for passing to selectObject

        Node node = sgp.getObject();
        if(node instanceof BranchGroup) {
            BranchGroup parent_group = (BranchGroup)node;
            parent_group.detach();
        }

        for(int i = sgp.nodeCount()-1; i >= 0; i--) {
            node = sgp.getNode(i);
            if(node instanceof BranchGroup) {
                BranchGroup parent_group = (BranchGroup)node;
                try {
                    parent_group.detach();
                    insertBehavior.selectObject(node,t3d);
                    return;
                } catch(Exception ex) {
                    //this branch group was not detachable
                }
            }
        }
        //all detachable objects will have a pickable BranchGroup in their SceneGraphPath
    }
}
