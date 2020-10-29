package org.firstinspires.ftc.teamcode.depricated;

import com.qualcomm.robotcore.hardware.HardwareDevice;

import java.util.HashMap;
import java.util.function.BiConsumer;
// lets turn this into something a little more intuitive

/* What do we want:
- An object that constructs easy-to-use move functions
- The objects which are being acted upon, the method used, and the parameters of that method should all be customizable

IDEAS:
- Constructor takes motor objs and lambda, function assigns objs to params
- Abstract class Moveable and sub-class DriveTrain


Q. What does the move function actually do?
A. Takes necessary params, those params are transformed according to passed stuff then put in lambda

At the end of the day, I do think just making a regular move method would just be better
*/

@Deprecated
public class Moveable<K extends HardwareDevice, V> {
    private HashMap map;
    private BiConsumer function;
    Moveable(HashMap<K, V> paramMap, BiConsumer<K, V> motorFunction) {
        map = paramMap; // note: use this.
        function = motorFunction;
    }
    public void run() {
            map.forEach(function);
    } // update might be a more descriptive word
}
