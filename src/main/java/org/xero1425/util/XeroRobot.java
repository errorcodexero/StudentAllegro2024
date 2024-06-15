package org.xero1425.util;

import org.littletonrobotics.junction.LoggedRobot;

import edu.wpi.first.util.WPIUtilJNI;

public class XeroRobot extends LoggedRobot{
    public XeroRobot(){

    }

    public double getTime(){
        return WPIUtilJNI.now() * 1.0e-6;
    }
}
