package utils;

/**
 * Enum indicating cart's current direction (up/down/none).
 */
public enum CartDirection {

    UPWARDS("upwards"),
    DOWNWARDS("downwards"),
    NONE("none");

    String directionName;
     CartDirection(String directionName){
        this.directionName=directionName;
    }

    public String toSring(){
        return this.directionName;
    }


}
