package EncoderMapper;

public class EncoderMapper
{
    private double kEncoder2Robot_ ;
    private double rmax_ ;
    private double rmin_ ;
    private double rc_ ;    
    private double emax_ ;
    private double emin_ ;
    private double ec_ ;

    /// \brief create a new encoder mapper objects
    /// \param rmax the maximum range of the robot physical characteristic
    /// \param rmin the minimum range of the robot physical characteristic
    /// \param emax the maximum range of the electrical value returned from the encoder
    /// \param emin the minimum range of the electrical value returned from the encoder
    public EncoderMapper(double rmax, double rmin, double emax, double emin) {
        rmax_ = rmax ;
        rmin_ = rmin ;
        emax_ = emax ;
        emin_ = emin ;
        kEncoder2Robot_ = (rmax - rmin) / (emax - emin) ;   
    }

    /// \brief calibrate the encoder mapper.
    /// This establishes the wrap point of the encoder versus the physical system being measured.
    /// \param robot a reading from the robot
    /// \param encoder a reading from the encoder
    public void calibrate(double robot, double encoder) {
        ec_ = encoder ;
        rc_ = robot ;
    }

    /// \brief convert an encoder value to a physical robot value
    /// \param encoder the encoder value
    /// \returns the robot value
    public double toRobot(double encoder) {
        double ret ;
        double offset; 

        encoder = clamp(encoder, emax_, emin_) ;
        offset = normalize(ec_ - (rc_ - rmin_) / kEncoder2Robot_, emax_, emin_) ;
        ret = normalize((encoder - offset) * kEncoder2Robot_ + rmin_, rmax_, rmin_) ;
        
        return ret ;
    }

    /// \brief convert an robot value to a encoder value
    /// \param robot the robot value
    /// \returns the encoder value
    public double toEncoder(double robot) {
        double ret ;
        double offset ;

        robot = clamp(robot, rmax_, rmin_) ;
        offset = normalize(ec_ - (rc_ - rmin_) / kEncoder2Robot_,  emax_, emin_) ;
        ret = normalize(offset + (robot - rmin_) / kEncoder2Robot_,  emax_, emin_) ;
        
        return ret ;
    }    

    private double normalize(double value, double vmax, double vmin) {
        if (vmax < vmin)
        {
            double temp = vmax ;
            vmax = vmin ;
            vmin = temp ;
        }

        while (value < vmin)
        {
            value += (vmax - vmin) ;
        }

        while (value > vmax)
        {
            value -= (vmax - vmin) ;
        }

        return value ;
    }

    private double clamp(double value, double vmax, double vmin) {
        if (vmax < vmin)
        {
            double temp = vmax ;
            vmax = vmin ;
            vmin = temp ;
        }

        if (value > vmax)
            value = vmax ;
        else if (value < vmin)
            value = vmin ;

        return value ;
    }    
} ;