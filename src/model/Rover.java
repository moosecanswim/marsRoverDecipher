package model;
import java.util.ArrayList;


/*
This packages the orientation (x y position and the direction it is facing)
and the instructions into an easy to use rover object.  This object also holds the
methods that interperet the instructions and re-orient the rover.
Each Rover knows where it is and where it is going.

I used switch case conditionnals instead of if/else due to the fact that i
know exactly what the inputs will be and have already controled for extraneous
inputs in the roverInstructions object.  could have used enum instead too

 Each rover has a complete list of its path

 */


public class Rover {
    private long id;
    private RoverOrientation roverOrientation;
    private RoverInstructions roverInstructions;

    //to print out directions taken for mod2
    private ArrayList<RoverOrientation> roverPath;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RoverOrientation getRoverOrientation() {
        return roverOrientation;
    }

    public void setRoverOrientation(RoverOrientation roverOrientation) {
        this.roverOrientation = roverOrientation;
    }

    public RoverInstructions getRoverInstructions() {
        return roverInstructions;
    }

    public void setRoverInstructions(RoverInstructions roverInstructions) {
        this.roverInstructions = roverInstructions;
    }

    public ArrayList<RoverOrientation> getRoverPath() {
        return roverPath;
    }

    public void setRoverPath(ArrayList<RoverOrientation> roverPath) {
        this.roverPath = roverPath;
    }

    public void addRoverPath(RoverOrientation aRoverOrientation){
        this.getRoverPath().add(aRoverOrientation);
    }

    @Override
    public String toString() {
        return "Rover: " + id +
                "| " + roverOrientation ;
    }


    /////////////helper methods///////////
    //this method takes in an orientation and will move that direction one
    //could move this outside for better encapsulation
    public static void moveM(RoverOrientation roverOrientation){
        int inX=roverOrientation.getxPosition();
        int inY=roverOrientation.getyPosition();
        String dir = roverOrientation.getDirection();
        int outX=inX;
        int outY=inY;

        switch (dir){
            case "N":
                outY = inY + 1;
                roverOrientation.setyPosition(outY);
                break;
            case "S":
                outY=inY-1;
                roverOrientation.setyPosition(outY);
                break;
            case "E":
                outX=inX+1;
                roverOrientation.setxPosition(outX);
                break;
            case "W":
                outX=inX-1;
                roverOrientation.setxPosition(outX);
                break;

        }


    }


    //This method preforms the rotate opperation by taking the rover orientation and a direction to move
    public static void rotate(RoverOrientation roverOrientation, String rotate){
        //System.out.println("before rotatin orientation is" + roverOrientation.toString());
        switch (rotate){
            case "R":
                //do right- clockwise
                roverOrientation.setDirection(goClockWise(roverOrientation.getDirection()));
                break;
            case "L":
                //do left- counterClockwise
                roverOrientation.setDirection(goCounterClockWise(roverOrientation.getDirection()));
                break;

        }

    }


    //help switch statment in rotate method
    public static String goClockWise(String in){
        String output=in;
        switch(in){
            case "N":
                output="E";
                break;
            case "E":
                output="S";
                break;
            case "S":
                output="W";
                break;
            case "W":
                output="N";
                break;
        }
        return output;
    }
    public static String goCounterClockWise(String in){
        String output=in;
        switch(in){
            case "N":
                output="W";
                break;
            case "E":
                output="N";
                break;
            case "S":
                output="E";
                break;
            case "W":
                output="S";
                break;
        }
        return output;
    }



}
