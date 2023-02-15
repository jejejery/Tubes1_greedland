package Services;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;


public class greedyByOff extends greedy{
    public final int offensiveDistance = 75;
    private double nearestEnemyDistance;
    public greedyByOff(GameObject bot, PlayerAction playerAction, GameState gameState){
        super(bot,playerAction,gameState);
    }

    public boolean isEnemyNear(){
        var BotList = getGameState().getPlayerGameObjects()
        .stream().filter(enemy -> enemy.getId() != getBot().getId()).collect(Collectors.toList());
        for(int i = 0; i < BotList.size(); i++){
            if(getDistanceBetween(getBot(), BotList.get(i)) <= offensiveDistance && getBot().getSize() > BotList.get(i).getSize()) return true;
        }
        return false;
    }

    public PlayerAction updatePlayerAction(PlayerAction playerAction){
        playerAction.action = PlayerActions.FORWARD;
        if (!getGameState().getGameObjects().isEmpty()) {
            var BotList = getGameState().getPlayerGameObjects()
            .stream().filter(enemy -> enemy.getId() != getBot().getId())
            .sorted(Comparator
            .comparing(item -> getDistanceBetween(getBot(), item)))
            .collect(Collectors.toList());

            this.nearestEnemyDistance = getDistanceBetween(getBot(), BotList.get(0));
            playerAction.heading = getHeadingBetween(BotList.get(0));
        }
        return playerAction;
    }

    public void debug(){
        if(isEnemyNear()) System.out.println("Musuh dekat denganmu dengan jarak " + this.nearestEnemyDistance);
        
    }
}
