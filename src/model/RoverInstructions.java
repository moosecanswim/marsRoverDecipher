package model;

import java.util.ArrayList;

/*
I created an object for the rover object that is really just a reapackaged ArrayList of strings.
I did this to make sure that i dont take in any extraneous values that could cause errors later
This object also makes sure the values are a uniform UPPER case so there is no issue in the switch
case used to preform the instructions.
todo after i get mod 2 builld i can use a stingbuilder instead off this arraylist to manage the instructions.  itll be cleaner
 */
public class RoverInstructions {

    private ArrayList<String> instructions;


    public RoverInstructions(){
        this.instructions = new ArrayList<String>();
    }


    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<String> roverInstructions) {
        this.instructions = roverInstructions;
    }

    public void addInstruction(String in){
        if(in.equalsIgnoreCase("m") || in.equalsIgnoreCase("l") || in.equalsIgnoreCase("r")){
           in=in.toUpperCase();
           this.instructions.add(in);
        }
        else{
            System.out.println(String.format("The instruction you would like %s to add is an invalid (will be skipped) ",in));
        }
    }


    @Override
    public String toString() {
        return "RoverInstructions{" + instructions +
                '}';
    }

}
