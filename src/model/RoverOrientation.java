package model;
/*
The RoverOrientation object nicely packages the rover x and y positions
as well as the direction it is facing for easy manipulaiton.
 */


import java.awt.*;

public class RoverOrientation {

    private int xPosition;
    private int yPosition;
    private String direction;

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Point getPointLocation(){
        return new Point(getxPosition(),getyPosition());
    }

    @Override
    public String toString() {
        return "RoverOrientation{" +
                "xPos=" + xPosition +
                ", yPos=" + yPosition +
                ", Facing='" + direction + '\'' +
                '}';
    }
}
