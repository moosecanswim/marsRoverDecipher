package marsRover;

import model.*;


import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.List;

/*
The main class first builds a a mission around an input from a file that you can choose.
each line of the text file is validated to be a field or an orientation.  if it is not
a field nor an orientation then it is assumed to be a set of instructions.
A field and rovers are created, each rover is assigned a paired set of an orientation and
a an instruction set.  These objects have their own validations and will ignore eronious inputs.
 */

               /* The way this is currently set up the algorithm would have them all start lined up on the bottom left vertically
                move right until there was a diagonal (45 degrees so each could pivot 90 degrees)
                move around the field like that 3 times.at this point they would pivot around the the most interior
                (point closet to the center of the field) and resume the search for three more iterations
                until the entire field has been swept.

                This algo does not currently take into account interactions with other rovers and would
                re-search area that was already searched by another rover.


                 In hind sight i would have had each rover rotate around their point of origin in ever growing circles
                 untill they hit another rovers path.  Back traced a step then start their search in the opposite direction.
                 This would provide a cleaner coverage and allow the rover to have more avenues to hit skipped blocks at the end.
                 In the current setup i can see similar issues to the premis of the game "snake" from the old nokia phones.
                 */

public class Main {
    public static void main(String[] args){

        File newFile = new File("test1Input.txt");
        Scanner scan = null;

        try {
            scan = new Scanner(newFile);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("issue scanning input file");
        }
        // create a mission and add field and apparatus to it
        Mission aMission = new Mission();
        ArrayList<RoverInstructions> allRoverInstructions=new ArrayList<RoverInstructions>();
        ArrayList<RoverOrientation> allRoverOrientations=new ArrayList<RoverOrientation>();


        Boolean run=true;
        //will ignore any instructions that do not have a matching "orientations" followed by "instructions"
        mainloop:
        while(scan.hasNext()){

            String line = scan.next();

            String[] f = line.split("");
            if (isField(f)){
                try {
                    int fieldInputWidth =  Integer.valueOf(f[0]);
                    int fieldInputLength = Integer.valueOf(f[1]);

                    //create a field
                    Field aField = new Field(fieldInputWidth,fieldInputLength);

                    //set mission field
                    aMission.setField(aField);
                }
                catch(Exception e){
                    System.out.println("Field inputs are invalid (must be 2 integers)");
                    break mainloop;
                }
            }
            else{
                Rover aRover= new Rover();
                if(isOrientation(f)){
                    try {
                        int roverStartX = Integer.valueOf(f[0]);
                        int roverStartY = Integer.valueOf(f[1]);
                        String roverDirection = f[2];

                        //create a new roverOrientation
                        RoverOrientation roverOrientation = new RoverOrientation();
                        roverOrientation.setxPosition(roverStartX);
                        roverOrientation.setyPosition(roverStartY);
                        roverOrientation.setDirection(roverDirection);

                        allRoverOrientations.add(roverOrientation);
                    }catch(Exception e){
                        System.out.println("Rover Orientation is invalid (must be 2 integers followed by a char");

                    }
                }
                else{
                    RoverInstructions roverInstructions=new RoverInstructions();
                    //print the directions
                    for (int i =0;i <f.length; i++){
                        roverInstructions.addInstruction(f[i]);
                    }
                    allRoverInstructions.add(roverInstructions);
                }

            }

        }//end of while- the mission has been created now


        //This puts together the starting orientations and instructions and assigns them to a mission that has a field size
        //this is the where i could map it for more clarity
        for(int i = 0; i < allRoverOrientations.size(); i++){
            Rover aRover = new Rover();
            aRover.setId(i+1);
            aRover.setRoverOrientation(allRoverOrientations.get(i));
            aRover.setRoverInstructions(allRoverInstructions.get(i));
            aMission.addApparatus(aRover);
            aMission.getField().updateTracker(aRover);

            if(allRoverOrientations.size() == allRoverInstructions.size()){
                //then it is a mission mod1
                aMission.setMissionType(MissionType.MOD1);
            }else if(allRoverOrientations.size()>1 && allRoverInstructions.size()==0){
                //then it is a mission mod2
                aMission.setMissionType(MissionType.MOD2);
            }else{
                System.out.println("issue with parsing input text.  I cannot determine what module to use.  Peace and blessings.  haveaa nice day or try a different input");
            }
        }

        //run the mission but try to catch errors
        try{
            runMission(aMission);
        }catch(Exception e){
            System.out.println(e);
        }




    }//end of main

    public static void runMission(Mission aMission){
        try {
            if (aMission.getMissionType().equals(MissionType.MOD1)) {
                //run module 1
                //************ Here i will itterate through the instructions and modify the roverOrientation*****************
                System.out.println("-----------Start Mission---------------");
                for (Rover r : aMission.getApparatus()) {
                    System.out.println(r.toString() + "|   Instructions: " + r.getRoverInstructions());

                    for (int i = 0; i < r.getRoverInstructions().getInstructions().size(); i++) {
                        String tempInst = r.getRoverInstructions().getInstructions().get(i);
                        switch (tempInst) {
                            case "M":
                                //use moveM method
                                RoverOrientation rO = r.getRoverOrientation();
                                if (validateMove(rO, aMission.getField()).equals(true)) {
                                    Rover.moveM(rO);
                                } else {
                                    System.out.println("Move ignored as rover will be out of field");
                                }
                                break;
                            case "R":
                                //use roatate method with "R" perameter
                                r.rotate(r.getRoverOrientation(), "R");
                                break;
                            case "L":
                                //use rotate method with "L" perameter
                                r.rotate(r.getRoverOrientation(), "L");
                                break;
                        }

                    }
                }
                System.out.println("-----------Mission Complete.  End Rover Orientations---------------");
                System.out.println();
                System.out.println("Results:");
                System.out.println(aMission.toString());
                for (Rover rover : aMission.getApparatus()) {
                    System.out.println(rover.toString());
                }
            } else if (aMission.getMissionType().equals(MissionType.MOD2)) {
                //run module 2

                //move rovers to 0,0 (and stack vertically [0,0] [1,0] [2,0]... how ever many
                //map each rover and their distance to 0;
                Map<Double , Rover> roverDistances = new TreeMap<Double, Rover>();
                for( Rover r: aMission.getApparatus()){
                    Point a = new Point(r.getRoverOrientation().getxPosition(),r.getRoverOrientation().getyPosition());
                    Point b = new Point(0,0);
                    double dist = distance(a,b);
                    roverDistances.put(dist,r);
                }
                Set<Double> roverDistanceKeys = roverDistances.keySet();
                List listRoverDistanceKeys = new ArrayList(roverDistanceKeys);
                Collections.sort(listRoverDistanceKeys);

                //now i have a list that i will use to get these rovers to 0,0 and stack up
                //if i rearanged this i could do a distance check to the current target point (more calls but if it is a large rover set then the cost in re sorting may be eased by the time saved in movement (assuming actual robots)
                //currently does not check path for validity (not crossing paths)
                int count = 0;
                for(Double d : roverDistanceKeys){
                    //move each rover to the as close to the origin
                    Rover currentRover = roverDistances.get(d);
                    Point target = new Point(0,count);
                    while (!currentRover.getRoverOrientation().getPointLocation().equals(target)){
                        String moveDirection = directionToPoint(currentRover.getRoverOrientation().getPointLocation(),target);
                        Rover.moveM(currentRover.getRoverOrientation());
                        currentRover.addRoverPath(currentRover.getRoverOrientation());
                        aMission.getField().updateTracker(currentRover);
                    }

                }

                do{
                    //search around field
                /* The way this is currently set up the algorithm would have them all start lined up on the bottom left vertically
                move right until there was a diagonal (45 degrees so each could pivot 90 degrees)
                move around the field like that 3 times.at this point they would pivot around the the most interior
                (point closet to the center of the field) and resume the search for three more iterations
                until the entire field has been swept.

                This algo does not currently take into account interactions with other rovers and would
                re-search area that was already searched by another rover.


                 In hind sight i would have had each rover rotate around their point of origin in ever growing circles
                 untill they hit another rovers path.  Back traced a step then start their search in the opposite direction.
                 This would provide a cleaner coverage and allow the rover to have more avenues to hit skipped blocks at the end.
                 In the current setup i can see similar issues to the premis of the game "snake" from the old nokia phones.
                 */







                }while (aMission.getField().getSearched() == false);


            }
        }
        catch (NullPointerException e){
            throw new RuntimeException("Error Running mission run mission.  NO MISSION TYPE ASSIGNED");
        }



    }



    //this method checks the input consists of 2 numbers to see if it is a field
    public static Boolean isField(String[] input){
        Boolean output=true;
        if(input.length!=2){
            output=false;
        }else{
            try {
                int temp1=Integer.valueOf(input[0]);
                int temp2=Integer.valueOf(input[1]);
                output=true;
            }catch(Exception e){
                output=false;
            }
        }
        return output;
    }
    //this method checks the input to see if it is a orientation
    public static Boolean isOrientation(String[] input){
        Boolean output=true;
        if(input.length!=3){
            output=false;
        }else{
            try {
                int temp1=Integer.valueOf(input[0]);
                int temp2=Integer.valueOf(input[1]);
                String tempS=input[2];
                if(tempS =="N" || tempS =="S" || tempS =="E" || tempS =="W"){
                    output=true;
                }
            }catch(Exception e){
                output=false;
            }
        }
        return output;
    }


    //this method validates a move before it is preformed.
    // if the move will cause the rover to fall off the field it is ignored
    public static Boolean validateMove(RoverOrientation roverOrientation, Field field){
        Boolean output=true;
        int inX=roverOrientation.getxPosition();
        int inY=roverOrientation.getyPosition();
        switch (roverOrientation.getDirection()){
            case "N":
                if(inY+1 > field.getTopY()){
                    output=false;
                }
                break;
            case "S":
                if(inY-1<0){
                    output=false;
                }
                break;
            case "E":
                if(inX+1>field.getTopX()){
                    output=false;
                }
                break;
            case "W":
                if(inX-1<0){
                    output=false;
                }
                break;
        }
        return output;
    }

    public static double distance(Point a, Point b){
        return Math.sqrt(Math.abs(Math.pow(a.getX()-b.getX(),2) + Math.pow(a.getY()-b.getY(),2)));
    }

    //the way this is written it gives a bias towards the North East (the farthest point away from 0,0 which is the default target for the primary moveset
    public static String directionToPoint(Point current, Point destination){
        String output = null;
        if (distance(current,destination) == 0){
            throw new RuntimeException("These Points are on top of each other already");
        }
        double dX = current.getX()-destination.getX();
        double dY = current.getY()-destination.getY();
        double temp = Math.max(dX,dY);
        if (temp == dX){
            //East West Change
            if(dX>0){
                return "W";
            }else{
                return "E";
            }
        }else if(temp == dY){
            //North South Change
            if(dY>0){
                return "S";
            }else{
                return "N";
            }
        }

        return "ERROR";
    }
}


