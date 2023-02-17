package Services;
import Enums.*;
import Models.*;
import io.reactivex.functions.Predicate;

import java.util.*;
import java.util.stream.*;

interface thePredicate {
    GameObject test(GameObject go);
 }
public class greedy {
    private GameObject bot;
    private PlayerAction playerAction;
    private GameState gameState;

    public greedy(GameObject bot, PlayerAction playerAction, GameState gameState){
        this.bot = bot;
        this.playerAction = playerAction;
        this.gameState = gameState;
    }

    //Setter Getter
    public GameObject getBot() {
        return this.bot; 
    }

    public void setBot(GameObject bot) {
        this.bot = bot;
    }

    public PlayerAction getPlayerAction() {
        return this.playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
        updateSelfState();
    }


    private void updateSelfState() {
        Optional<GameObject> optionalBot = gameState.getPlayerGameObjects().stream().filter(gameObject -> gameObject.id.equals(bot.id)).findAny();
        optionalBot.ifPresent(bot -> this.bot = bot);
    }

    public double getDistanceBetween(GameObject object1, GameObject object2) {
        var triangleX = Math.abs(object1.getPosition().x - object2.getPosition().x);
        var triangleY = Math.abs(object1.getPosition().y - object2.getPosition().y);
        return Math.sqrt(triangleX * triangleX + triangleY * triangleY);
    }

    public double getEuclidianDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
    }


    public double getEdgeDistanceBetween(GameObject object1, GameObject object2) { //jarak antar gameobject dari tepi ke tepi
        double centerDistance = getDistanceBetween(object1, object2);
        if(centerDistance - object1.getSize() - object2.getSize() < 0) return 0;
        else return centerDistance - object1.getSize() - object2.getSize();
    }

    public int getHeadingBetween(GameObject otherObject) {
        var direction = toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
                otherObject.getPosition().x - bot.getPosition().x));
        return (direction + 360) % 360;
    }

    public boolean isInRangeHeading(int theHeading, int constraint_lower, int constraint_upper){
        
        if(constraint_lower > constraint_upper){
            if(theHeading < 360) return constraint_lower <= theHeading && theHeading >= constraint_upper;
            else return constraint_lower >= theHeading && theHeading <= constraint_upper;
        }
        else return constraint_lower <= theHeading && theHeading <= constraint_upper;
        // return constraint_lower > constraint_upper ? (constraint_lower < 360 && constraint_upper >= 0 && theHeading > constraint_lower && theHeading > constraint_upper) : (theHeading > constraint_lower && theHeading < constraint_upper);
    }
    


    public int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }
}
