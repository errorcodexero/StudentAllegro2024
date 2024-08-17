package frc.robot.subsystems.IntakeShooter;

public class PWL {
    private double[] x;
    private double[] y;

    public PWL(double[] x, double[] y){
        this.x = x;
        this.y = y;
    }

    public double lerp(double val){
        for(int i = 0; i < x.length - 1; i ++){
            if(x[i] < val && x[i + 1] > val){
                double diff = x[i + 1] - x[i];
                double firstPercentage = (val - x[i]) / diff;
                double nextPercentage = (x[i + 1] - val) / diff;
                return nextPercentage * y[i] + firstPercentage * y[i + 1];
            }
        }
        return -1;
    }

}
