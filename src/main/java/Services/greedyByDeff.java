package Services;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class greedyByDeff extends greedy{
    public final int objDistance = 100;
    public final int enemyDistance = 40;
    
    


    public greedyByDeff(GameObject bot, PlayerAction playerAction, GameState gameState){
        super(bot,playerAction,gameState);
    }

    public boolean ifNearObj(GameObject nearestGC){
        return getEdgeDistanceBetween(getBot(),nearestGC) < objDistance; 
    }

    public boolean ifNearEnemy(GameObject enemyBot){
        return getEdgeDistanceBetween(getBot(),enemyBot) < enemyDistance && getBot().getSize() < enemyBot.getSize(); 
    }

    public boolean isDeffensive(){
        var BotList = getGameState().getPlayerGameObjects()
        .stream().filter(enemy -> enemy.getId() != getBot().getId()).collect(Collectors.toList());
        var objList = getGameState().getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.GASCLOUD || item.getGameObjectType() == ObjectTypes.ASTEROIDFIELD)
                    .collect(Collectors.toList());

        for(int i = 0; i < BotList.size(); i++){
            if(getDistanceBetween(getBot(), BotList.get(i)) <= enemyDistance && getBot().getSize() < BotList.get(i).getSize()) return true;
        }
        for(int i = 0; i < objList.size(); i++){
            if(getDistanceBetween(getBot(), objList.get(i)) <= objDistance) return true;
        }
        return false;
    }

    public PlayerAction updatePlayerAction(PlayerAction playerAction){
        playerAction.action = PlayerActions.FORWARD;
        if (!getGameState().getGameObjects().isEmpty()) {
            
            var objList = getGameState().getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.GASCLOUD || item.getGameObjectType() == ObjectTypes.ASTEROIDFIELD)
                    .sorted(Comparator
                            .comparing(item -> getDistanceBetween(getBot(), item)))
                    .collect(Collectors.toList());


            
            var BotList = getGameState().getPlayerGameObjects()
                    .stream().filter(enemy -> enemy.getId() != getBot().getId())
                    .sorted(Comparator
                    .comparing(item -> getDistanceBetween(getBot(), item)))
                    .collect(Collectors.toList());
            

           

            if(ifNearEnemy(BotList.get(0))){
                double enemyCenterDistance = getDistanceBetween(getBot(), BotList.get(0));
                int enemyCenterHeading = getHeadingBetween(objList.get(0));
                int enemySize = objList.get(0).getSize();
                playerAction.heading = getHeadingFoodWithConstraint(enemyCenterDistance, enemyCenterHeading, enemySize);
            } 
            else if(ifNearObj(objList.get(0))){
                double enemyCenterDistance = getDistanceBetween(getBot(), objList.get(0));
                int enemyCenterHeading = getHeadingBetween(objList.get(0));
                int enemySize = objList.get(0).getSize();
                playerAction.heading = getHeadingFoodWithConstraint(enemyCenterDistance, enemyCenterHeading, enemySize);
            }
            


        
            // playerAction.heading = getHeadingBetween(gasList.get(0));
        }
        return playerAction;
    }

    private int getHeadingFoodWithConstraint(double centerDistanceCons, int headingCons, int sizeCons){
        int anglesTreshold = toDegrees(Math.atan(2*sizeCons/centerDistanceCons));
        var foodList = getGameState().getGameObjects()
                    .stream().filter(item -> (item.getGameObjectType() == ObjectTypes.FOOD || item.getGameObjectType() == ObjectTypes.SUPERFOOD) && isInRangeHeading(headingCons, (headingCons-anglesTreshold) % 360, (headingCons+anglesTreshold)%360))
                    .sorted(Comparator
                            .comparing(item -> item.getGameObjectType() == ObjectTypes.FOOD ? getDistanceBetween(getBot(), item) : getDistanceBetween(getBot(), item)/2))
                    .collect(Collectors.toList());
        return getHeadingBetween(foodList.get(0));
    }
}
