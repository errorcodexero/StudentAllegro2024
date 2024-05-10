package AKInput;

import java.util.ArrayList;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.LogTable.LogValue;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggableInputs;

final class AddInputIOInputs implements LoggableInputs{
    private static ArrayList<String> names = new ArrayList<String>();
    private static ArrayList<Object> values = new ArrayList<Object>();

    public AddInputIOInputs(){}

    public static int add(String name, Object value){
        names.add(name);
        values.add(value);
        return names.size() - 1;
    }

    public static void update(int idx, Object value){
        values.set(idx, value);
    }

    public static void update(String name, Object value){
        values.set(names.indexOf(name), value);
    }

    @Override
    public void toLog(LogTable table) {
        for(int i = 0; i < names.size(); i ++){
            table.put(names.get(i), new LogValue(values.get(i).toString(), null));
        }
    }

    @Override
    public void fromLog(LogTable table) {
        for(int i = 0; i < names.size(); i ++){
            values.set(i, table.get(names.get(i)));
        }
    }
}


public class AKInput{

    private String sub_name;

    public AKInput(){
        sub_name = "Non Motor Inputs";
    }

    public AKInput(String sub_name){
        this.sub_name = sub_name;
    }

    public void add(String name, Object value){
        AddInputIOInputs.add(name, value);
    }

    public void update(int idx, Object value){
        AddInputIOInputs.update(idx, value);
    }

    public void update(String name, Object value){
        AddInputIOInputs.update(name, value);
    }

    public void periodic(String sub_name_){
        Logger.processInputs(sub_name_, new AddInputIOInputs());
    }

    public void periodic(){
        periodic(sub_name);
    }
}
