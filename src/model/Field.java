package model;

import java.util.Arrays;

public class Field {
    private int topX;
    private int topY;
    private long[][] tracker;
    private Boolean searched;
    public int area;

    public Field(int inX,int inY){
        this.topX=inX;
        this.topY=inY;
        startTracker();
        this.searched = false;
        setArea();
    }

    public int getTopX() {
        return topX;
    }

    public void setTopX(int topX) {
        this.topX = topX;
    }

    public int getTopY() {
        return topY;
    }

    public void setTopY(int topY) {
        this.topY = topY;
    }

    public Boolean getSearched() {
        return searched;
    }

    public void setSearched(Boolean searched) {
        this.searched = searched;
    }

    public void setArea() {
        this.area = topX*topY;
    }

    public void startTracker() {
        if (topX!=0 && topY!=0){
            long[][] t = new long[this.topY][this.topX];
            this.tracker = t;
        }else{
            System.out.println("Could not start tracker because field has not been (or area is 0)");
        }
    }
    public void updateTracker(Rover aRover){
        //set each spot on the tracker (grid) with the id of the rover that has crossed it
        RoverOrientation temp = aRover.getRoverOrientation();
        int x = temp.getxPosition();
        int y = temp.getyPosition();
        tracker[x][y]=aRover.getId();
        //check to see if everything space has been touched
        setSearched(isSearchComplete());
    }

    //this is to test the field or zero it out
    public void setAllTracker(){
      System.out.println("the field is " + tracker.length + " by " + tracker[0].length);
        for (int i = 0; i < tracker.length; i++){
            for (int j = 0; j < tracker[i].length ; j++){
                tracker[i][j]=0;
            }
        }
    }
    public String queryTracker(Rover r, int x, int y){
        //given a rover check to see if spot has been landed on before respond "YOU", "OTHER", "NONE"
        long roverID = r.getId();
        long currentValue = tracker[y][x];
        String output = null;
        if (roverID == currentValue){
            output=  "YOU";
        }
        else if( currentValue == 0){
            output= "NONE";
        }else{
            output= "OTHER";
        }
        return output;
    }
    public boolean isSearchComplete(){
        String temp = Arrays.deepToString(this.tracker);
        boolean output = true;
        outerfor:
        for (int i = 0; i < temp.length(); i++){
            if(temp.charAt(i)=='0'){
                output = false;
                break outerfor;
            }
        }
        return output;
    }



    @Override
    public String toString(){
        return "topX: "+ topX + " topY: "+topY;
    }
    public void printTracker(){
        System.out.print(Arrays.deepToString(this.tracker));
    }
}
