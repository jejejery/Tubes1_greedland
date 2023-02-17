package Services;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;
// import java.lang.Math;

public class greedyByEW extends greedy{
    private ObjectTypes typeFood;
    private double nearestFood;
    private UUID theID;
    // private boolean lockState;

    public greedyByEW(GameObject bot, PlayerAction playerAction, GameState gameState){
        super(bot,playerAction,gameState);
        // this.lockState = false;
    }

    public PlayerAction updatePlayerAction(PlayerAction playerAction){
        if(playerAction.action == PlayerActions.STARTAFTERBURNER) playerAction.action = PlayerActions.STOPAFTERBURNER; //Saat defensif, harus mematikan
        else playerAction.action = PlayerActions.FORWARD; //Default
        if (!getGameState().getGameObjects().isEmpty()) {
            var foodList = getGameState().getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.FOOD || item.getGameObjectType() == ObjectTypes.SUPERFOOD)
                    .sorted(Comparator
                            .comparing(item -> item.getGameObjectType() == ObjectTypes.FOOD ? getDistanceBetween(getBot(), item) : getDistanceBetween(getBot(), item)/2))
                    .collect(Collectors.toList());

        
            playerAction.heading = getHeadingBetween(foodList.get(0));
            this.nearestFood = getDistanceBetween(getBot(), foodList.get(0));
            this.typeFood = foodList.get(0).getGameObjectType();
            this.theID = foodList.get(0).getId();
        }
        return playerAction;
    }

    public void debug(){
        System.out.println("Jarak Makanan Terdekat: " + this.nearestFood);
        System.out.println("Jenis Makanan Terdekat: " + this.typeFood);
        System.out.println("ID Makanan: " + this.theID);

    }
    
}
