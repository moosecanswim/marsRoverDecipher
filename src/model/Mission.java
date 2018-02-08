package model;

import java.util.ArrayList;

/*
This method is not entirely necessary but i felt it was a cool way to manage the mission
if this app were to be scaled with multiple missions on multiple fields this could be utilized
to ensure there are no mismatches of rovers (with instructions and orientations) and their
assigned fields
 */

public class Mission {

    private MissionType missionType;
    private Field field;
    private ArrayList<Rover> apparatus;

    public Mission(){
        this.apparatus=new ArrayList<Rover>();
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public ArrayList<Rover> getApparatus() {
        return apparatus;
    }

    public void setApparatus(ArrayList<Rover> apparatus) {
        this.apparatus = apparatus;
    }
    public void addApparatus(Rover rover){
       apparatus.add(rover);
    }

    public MissionType getMissionType() {
        return missionType;
    }

    public void setMissionType(MissionType missionType) {
        this.missionType = missionType;
    }

    public void printField(){
        this.field.printTracker();
        System.out.println();
    }

    @Override
    public String toString(){
        return String.format("Mission: %d aparatus | field of opperations: %s ",apparatus.size(),field.toString());
    }
}
