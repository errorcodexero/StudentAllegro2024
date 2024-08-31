package org.xero1425.util;
/**
 * This class represents a Piecewise Linear (PWL) function.
 * It provides a method to perform linear interpolation between given points.
 */
public class PWLs {
    private double[] x;
    private double[] y;

    /**
     * Constructs a new PWL object with the given x and y arrays.
     * @param x The array of x-values for the PWL function.
     * @param y The array of y-values for the PWL function.
     */
    public PWLs(double[] x, double[] y){
        this.x = x;
        this.y = y;
    }

    /**
     * Performs linear interpolation between the given points to find the y-value for the given x-value.
     * @param val The x-value for which to find the interpolated y-value.
     * @return The interpolated y-value for the given x-value.
     *         Returns -1 if the x-value is outside the range of the given x-values.
     */
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