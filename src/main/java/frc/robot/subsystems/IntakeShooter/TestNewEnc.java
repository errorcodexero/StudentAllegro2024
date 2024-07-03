package frc.robot.subsystems.IntakeShooter;

import org.xero1425.util.EncoderMapper;

public class TestNewEnc {
    public static void main(String[] args){
        EncoderMapper enc = new EncoderMapper(0, 0, 0, 0);

        for(int i = 0; i < 50; i ++){
            System.out.println(i);
            System.out.println(enc.normalize(i, i * -23, i * 345) - enc.oldNormalize(i, i * -23, i * 345));
            System.out.println(enc.normalize(i, i * -23, i * 345));
            System.out.println(enc.oldNormalize(i, i * -23, i * 345));
        }
    }
}
