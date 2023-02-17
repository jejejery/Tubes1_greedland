package Services;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;


public class greedyByOff extends greedy{
    public final int chaseOffensiveDistance = 100;
    

    private double nearestEnemyDistance;
    private boolean isTeleportFired;
    private int tickTeleportFired;
    private boolean bruteChaseMode;

    public greedyByOff(GameObject bot, PlayerAction playerAction, GameState gameState){
        super(bot,playerAction,gameState);
        this.isTeleportFired = false;
        this.tickTeleportFired = 0;
        this.bruteChaseMode = false;
    }

    public boolean isEnemyNear(){
        var BotList = getGameState().getPlayerGameObjects()
        .stream().filter(enemy -> enemy.getId() != getBot().getId()).collect(Collectors.toList());
        for(int i = 0; i < BotList.size(); i++){
            if(getEdgeDistanceBetween(getBot(), BotList.get(i)) <= chaseOffensiveDistance && getBot().getSize() > BotList.get(i).getSize()) return true;
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

            this.nearestEnemyDistance = getEdgeDistanceBetween(getBot(), BotList.get(0));
            int predictedSize = getBot().getSize() + (int) 0.5 * (getBot().getSpeed() - (int) Math.sqrt(getBot().getSpeed()*getBot().getSpeed() + 4*getEdgeDistanceBetween(getBot(), BotList.get(0))));
            int deltaSize = getBot().getSize() - BotList.get(0).getSize();
            int deltaPredictedSize = predictedSize - BotList.get(0).getSize();
            Boolean validTeleportSize = getBot().getSize() > 50;

            //Activate brutal chasing mode
            if(this.nearestEnemyDistance > 15 && this.nearestEnemyDistance < 25 && getBot().getTeleporterCount() > 0 && deltaSize > 30 && !this.isTeleportFired && validTeleportSize){
                playerAction.action = PlayerActions.FIRETELEPORT;
                this.isTeleportFired = true;
                this.tickTeleportFired = getGameState().getWorld().getCurrentTick();
            }
            else if(this.isTeleportFired && getGameState().getWorld().getCurrentTick() - this.tickTeleportFired > 5){
                this.isTeleportFired = false;
                playerAction.action = PlayerActions.TELEPORT;
            }
          
            else if(deltaPredictedSize > 3 ){ //Perhitungan matematis sudah dibuktikan di kertas
                    playerAction.action = PlayerActions.STARTAFTERBURNER;       
                    this.bruteChaseMode = true; 
                }
            else{
                this.bruteChaseMode = false;
            }
            
            playerAction.heading = getHeadingBetween(BotList.get(0));
        }
        return playerAction;
    }

    public void debug(){
        if(isEnemyNear()){
            System.out.println("Musuh terdekat denganmu dengan jarak " + this.nearestEnemyDistance);
        } 
        if(this.bruteChaseMode) System.out.println("Activate bruteChase mode");
    }
}
