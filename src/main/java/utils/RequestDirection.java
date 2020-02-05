package utils;

/**
 * Enum indicating request's direction (up/down).
 */
public enum RequestDirection {
    UPWARDS("upwards"),
    DOWNWARDS("downwards");

    String directionName;
    RequestDirection(String directionName){
        this.directionName=directionName;
    }

    public String toSring(){
        return this.directionName;
    }

}
