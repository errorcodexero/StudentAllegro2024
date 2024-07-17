package org.xero1425.AKAbstraction;

import java.util.ArrayList;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.LogTable.LogValue;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggableInputs;

final class AddInputIOInputs implements LoggableInputs{
    private static ArrayList<String> names = new ArrayList<String>();
    private static ArrayList<Object> values = new ArrayList<Object>();

    public AddInputIOInputs(){}

    public int add(String name, Object value){
        names.add(name);
        values.add(value);
        return names.size() - 1;
    }

    public boolean update(int idx, Object value){
        if(idx >= values.size()){
            return false;
        }
        values.set(idx, value);
        return true;
    }

    public boolean update(String name, Object value){
        if(names.indexOf(name) == -1){
            return false;
        }
        values.set(names.indexOf(name), value);
        return true;
    }
    
    private void putTable(LogTable table, String name, Object value){
        table.put(name, (LogValue) value.getClass().cast(value));
    }

    @Override
    public void toLog(LogTable table) {
        for(int i = 0; i < names.size(); i ++){
            putTable(table, names.get(i), values.get(i));
        }
    }

    @Override
    public void fromLog(LogTable table) {
        for(int i = 0; i < names.size(); i ++){
            values.set(i, table.get(names.get(i)));
        }
    }
}


public class XeroAKInput{

    private static ArrayList<String> sub_names = new ArrayList<String>();
    private static ArrayList<AddInputIOInputs> sub_inputs = new ArrayList<AddInputIOInputs>();

    public static int[] add(String sub_name, String name, Object value){
        int idx1 = sub_names.indexOf(sub_name);
        int idx2 = -1;
        if(idx1 == -1){
            sub_names.add(sub_name);
            sub_inputs.add(new AddInputIOInputs());
            idx2 = sub_inputs.get(sub_inputs.size()- 1).add(name, value);
            int[] returned = {sub_inputs.size() - 1, idx2};
            return returned;
        }
        idx2 = sub_inputs.get(idx1).add(name, value);
        int[] returned = {idx1, idx2};

        return returned;
    }

    public static boolean update(int idx1, int idx2, Object value){
        if(idx1 >= sub_inputs.size()){
            return false;
        }
        return sub_inputs.get(idx1).update(idx2, value);
    }

    public static boolean update(int[] idxs, Object value){
        return update(idxs[0], idxs[1], value);
    }

    public static boolean update(String sub_name, String name, Object value){
        int idx1 = sub_names.indexOf(sub_name);
        if(idx1 == -1){
            return false;
        }
        return sub_inputs.get(idx1).update(name, value);
    }

    public static void periodic(){
        int counter = 0;
        for(String name : sub_names){
            Logger.processInputs(name, sub_inputs.get(counter));
        }
    }
}
