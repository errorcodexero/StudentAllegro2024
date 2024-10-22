package org.xero1425.util;

import edu.wpi.first.util.WPIUtilJNI;

public class XeroTimer {
    private static int LoggerID = -1 ;
    private static final String LoggerIDName = "XeroTimer" ;
    private boolean running_ ;
    private double duration_ ;
    private double endtime_ ;
    private double start_ ;
    private String name_ ;

    public XeroTimer(double duration) {
        duration_ = duration ;
        running_ = false ;
        endtime_ = 0.0 ;
    }

    public double getDuration() {
        return duration_ ;
    }

    public void setDuration(double dur) {
        duration_ = dur ;
    }

    public double elapsed() {
        return WPIUtilJNI.now() * 1.0e-6 - start_ ;
    }

    public void start() {
        running_ = true ;
        start_ = WPIUtilJNI.now() * 1.0e-6; ;
        endtime_ = start_ + duration_ ;
    }

    public boolean isRunning() {
        return running_ ;
    }

    public boolean isExpired() {
        boolean ret = false ;

        if (running_ == false)
            return true ;

        if (running_ && WPIUtilJNI.now() * 1.0e-6 > endtime_) {
            running_ = false ;
            ret = true ;
        }

        return ret ;
    }

    public String toString() {
        String ret = "XeroTimer";
        ret += " running " + (running_ ? "true" : "false");
        ret += " endtime " + endtime_ ;
        ret += " currenttime " + WPIUtilJNI.now() * 1.0e-6;
        return ret;
    }
}