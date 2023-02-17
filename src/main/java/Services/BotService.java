package Services;

import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;
import java.lang.Math;

public class BotService {
    private GameObject bot;
    private PlayerAction playerAction;
    private GameState gameState;
    private greedyByDeff greed1;
    private greedyByOff greed2;
    private greedyByEW greed3;

    public BotService() {
        this.playerAction = new PlayerAction();
        this.gameState = new GameState();
        this.greed1 = new greedyByDeff(null, playerAction, gameState);
        this.greed2 = new greedyByOff(null, playerAction, gameState);
        this.greed3 = new greedyByEW(null, playerAction, gameState);
    }


    public GameObject getBot() {
        return this.bot; 
    }

    public void setBot(GameObject bot) {
        this.bot = bot;
        this.greed3.setBot(bot);
        this.greed2.setBot(bot);
        this.greed1.setBot(bot);
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

    public void computeNextPlayerAction(PlayerAction pa) {
        /*Prioritas: 
        1. Defensif
        2. Ofensif
        3. Cari Makan by Size Growth Rate
        */

        // if(pa.getAction() == PlayerActions.STARTAFTERBURNER) System.out.println("hehe");
        pa.action =PlayerActions.FORWARD;
        pa.heading = new Random().nextInt(360);

        
        greed1.setGameState(this.gameState);
        greed2.setGameState(this.gameState);
        greed3.setGameState(this.gameState);

        greed1.setBot(this.bot);
        greed2.setBot(this.bot);
        greed3.setBot(this.bot);
    
        if(greed1.isDeffensive()){
            
            this.playerAction = greed1.updatePlayerAction(pa);
            if(this.playerAction.action == PlayerActions.STARTAFTERBURNER) this.playerAction.action = PlayerActions.STOPAFTERBURNER;
        } 
        else{
            if(greed2.isEnemyNear()) this.playerAction = greed2.updatePlayerAction(pa);
            else{
                 this.playerAction = greed3.updatePlayerAction(pa);
                 if(this.playerAction.action == PlayerActions.STARTAFTERBURNER) playerAction.action = PlayerActions.STOPAFTERBURNER;
            }
        }
        
        
    }
    //Polar coordinate
   

    public void debugAtributeBot(){
                System.out.println("===================================");
                System.out.println("Tic Now:" + getGameState().getWorld().getCurrentTick());
                System.out.println("current greedy strategy: " +(greed1.isDeffensive() ? "defensive" : (greed2.isEnemyNear() ? "offensive" : "greedy by growth rate")));
                System.out.println("===================================");
                if(greed1.isDeffensive())greed1.debug();
                else if(greed2.isEnemyNear())greed2.debug();
                else greed3.debug();
                System.out.println("===================================");
                this.bot.debug();
                System.out.println("===================================");
                System.out.println();

                
                
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

    public int getHeadingBetween(GameObject otherObject) {
        var direction = toDegrees(Math.atan2(otherObject.getPosition().y - bot.getPosition().y,
                otherObject.getPosition().x - bot.getPosition().x));
        return (direction + 360) % 360;
    }

    private int toDegrees(double v) {
        return (int) (v * (180 / Math.PI));
    }


}
