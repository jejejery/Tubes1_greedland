package Services;
import Enums.*;
import Models.*;

import java.util.*;
import java.util.stream.*;

public class greedyByDeff extends greedy{
    public final int objDistance = 50;
    public final int enemyDistanceWarning = 75;
    public final int enemyDistanceDanger = 30;

    private boolean isTeleport; //Apakah Teleport sudah ditembakkan
    private int TeleportTick;
    private boolean isSupernovaFired;
    private int SupernovaTick;
    private boolean isTorpedoFired;
    private int TorpedoTick;
    
    private GameObject hazardousObject;
    private GameObject enemyBot;
    //KETERANGAN:
    //Teleportasi akan dilakukan setelah 5 tick
    //Supernova akan diledakan setelah 3 tick
    
    


    public greedyByDeff(GameObject bot, PlayerAction playerAction, GameState gameState){
        super(bot,playerAction,gameState);
        isTeleport = false;
        isSupernovaFired = false;
        isTorpedoFired = false;
        TeleportTick = 0;
        SupernovaTick = 0;
        TorpedoTick = 0;
        hazardousObject = null;
        enemyBot = null;
    }

    public boolean ifNearObj(GameObject nearestGC){
        return getEdgeDistanceBetween(getBot(),nearestGC) < objDistance; 
    }

    public boolean isNearEdge(){
        return getGameState().getWorld().getRadius() - getEuclidianDistance(getBot().getPosition().getX(), getBot().getPosition().getY(), 0, 0) - getBot().getSize()/2  < 50;  
    }

    public boolean ifNearEnemy(GameObject enemyBot){
        return getEdgeDistanceBetween(getBot(),enemyBot) < enemyDistanceWarning && getBot().getSize() < enemyBot.getSize(); 
    }

    public boolean ifVeryNearEnemy(GameObject enemyBot){
        return getEdgeDistanceBetween(getBot(),enemyBot) < enemyDistanceDanger && getBot().getSize() < enemyBot.getSize(); 
    }

    public boolean isDeffensive(){
        var BotList = getGameState().getPlayerGameObjects()
        .stream().filter(enemy -> enemy.getId() != getBot().getId()).collect(Collectors.toList());
        var objList = getGameState().getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.GASCLOUD || item.getGameObjectType() == ObjectTypes.ASTEROIDFIELD)
                    .collect(Collectors.toList());

        for(int i = 0; i < BotList.size(); i++){
            if(getEdgeDistanceBetween(getBot(), BotList.get(i)) <= enemyDistanceWarning && getBot().getSize() < BotList.get(i).getSize()) return true;
        }
        for(int i = 0; i < objList.size(); i++){
            if(getEdgeDistanceBetween(getBot(), objList.get(i)) <= objDistance) return true;
        }
        if(getGameState().getWorld().getCurrentTick() != null) return isNearEdge();
        return false;
    }

    public boolean isTorpedoDangerous(GameObject torpedo){    
        if(getEdgeDistanceBetween(torpedo,getBot()) < 15)return true;
        else return false;
    }

    public PlayerAction updatePlayerAction(PlayerAction playerAction){
        if(playerAction.action == PlayerActions.STARTAFTERBURNER) playerAction.action = PlayerActions.STOPAFTERBURNER; //Saat defensif, harus mematikan
        else playerAction.action = PlayerActions.FORWARD; //Default

        var objList = getGameState().getGameObjects()
                    .stream().filter(item -> item.getGameObjectType() == ObjectTypes.GASCLOUD || item.getGameObjectType() == ObjectTypes.ASTEROIDFIELD)
                    .sorted(Comparator
                            .comparing(item -> getEdgeDistanceBetween(getBot(), item)))
                    .collect(Collectors.toList());

                    
            
        var BotList = getGameState().getPlayerGameObjects()
                    .stream().filter(enemy -> enemy.getId() != getBot().getId())
                    .sorted(Comparator
                    .comparing(item -> getEdgeDistanceBetween(getBot(), item)))
                    .collect(Collectors.toList());

        var torpedoList = getGameState().getGameObjects()
            .stream().filter(item -> item.getGameObjectType() == ObjectTypes.TORPEDOSALVO)
            .sorted(Comparator
                    .comparing(item -> getEdgeDistanceBetween(getBot(), item)))
            .collect(Collectors.toList());
        if (!BotList.isEmpty() && !objList.isEmpty()) {;
            

            /*
                     Prioritas kondisi greedy: (Prioritas dari pertama)
                     1. Activate shield kalau ada torpedo, tanpa mempertimbangkan keadaan sebelum/sesudah
                     2. Menghindar dari lawan:
                        2a. Tembak torpedo/supernova kalau punya
                        2b. Greedy by Food tapi dengan constrain enemy, default
                        2c. Jika sangat dekat, langsung putar balik 180. Kalau ada teleporter, bagus.
                     3. Menghindar dari hazard objects; Greedy by food
                     4. Menjauhi map yang mengecil!

                     */
            if(!torpedoList.isEmpty()){
                if(isTorpedoDangerous(torpedoList.get(0)) && getBot().getShieldCount() > 0 && !isTorpedoFired){ //isTorpedoFired berguna untuk mendeteksi apakah objek torpedo yang ditembakkan adalah punya bot kita atau bukan
                    playerAction.action = PlayerActions.ACTIVATESHIELD;
                }
            }
            else if(ifNearEnemy(BotList.get(0))){ 
                //Dekat dengan lawan, default
                //Implementasi dari 2b. Greedy by Food, tapi dengan constrain enemy, default

                //==================================//
                this.enemyBot = BotList.get(0);
                double enemyCenterDistance = getDistanceBetween(getBot(), this.enemyBot);
                
                
                int enemyCenterHeading = getHeadingBetween(objList.get(0));
                int enemySize = objList.get(0).getSize();
                playerAction.heading = getHeadingFoodWithConstraint(enemyCenterDistance, enemyCenterHeading, enemySize);
                 //==================================//

                if(this.TorpedoTick - getGameState().getWorld().getCurrentTick() > 3 && this.isTorpedoFired){
                    this.isTorpedoFired = false;
                }
                else if(getBot().getTorpedoSalvoCount() > 0){
                    playerAction.heading = enemyCenterHeading;
                    playerAction.action = PlayerActions.FIRETORPEDOES;
                    this.TorpedoTick = getGameState().getWorld().getCurrentTick();
                    this.isTorpedoFired = true;
                }
                else if(getBot().getSupernovaAvailable() > 0 && !isSupernovaFired){
                    playerAction.heading = enemyCenterHeading;
                    playerAction.action = PlayerActions.FIRESUPERNOVA;
                    this.SupernovaTick = getGameState().getWorld().getCurrentTick();
                    this.isSupernovaFired = true;
                }
                else if(this.isSupernovaFired && getGameState().getWorld().getCurrentTick() > this.SupernovaTick + 3){
                    playerAction.action = PlayerActions.DETONATESUPERNOVA;
                }
                // Apabila dekat sekali, gunakan enemydistancedanger. Lari ke arah yang berlawanan
                else if(ifVeryNearEnemy(objList.get(0))){
                    playerAction.action = PlayerActions.STARTAFTERBURNER;
                    playerAction.heading = (enemyCenterHeading + 180) % 360;

                    // Lakukan teleportasi apabila sempat
                    if(getBot().TeleporterCount > 0 && !this.isTeleport && getBot().getSize() > 50){
                        playerAction.action = PlayerActions.FIRETELEPORT;
                        this.TeleportTick = getGameState().getWorld().getCurrentTick();
                        this.isTeleport = true;
                    }
                    
                    if(this.isTeleport && getGameState().getWorld().getCurrentTick()-TeleportTick > 5){
                        playerAction.action = PlayerActions.TELEPORT;
                        this.isTeleport = false;
                    }
                    
                    
                } 
                
            } 
            else if(ifNearObj(objList.get(0))){ //Dekat dengan hazardous object
                this.hazardousObject = objList.get(0);

                double objCenterDistance = getDistanceBetween(getBot(), this.hazardousObject);
            
                int objCenterHeading = getHeadingBetween(this.hazardousObject);
              
                int objSize = objList.get(0).getSize();
                
                playerAction.heading = getHeadingFoodWithConstraint(objCenterDistance, objCenterHeading, objSize);
                
            }
            else if(isNearEdge()){
                double x = getBot().getPosition().getX();
                double y = getBot().getPosition().getY(); 
                int angles = (toDegrees(Math.atan2(y,x)) + 180) % 180;
                playerAction.heading = angles;
            }

            
        
        }
        return playerAction;
    }

    private int getHeadingFoodWithConstraint(double centerDistanceCons, int headingCons, int sizeCons){
        int anglesTreshold = toDegrees(Math.atan(2*sizeCons/centerDistanceCons));
        var foodList = getGameState().getGameObjects()
                    .stream().filter(item -> (item.getGameObjectType() == ObjectTypes.FOOD || item.getGameObjectType() == ObjectTypes.SUPERFOOD) && !isInRangeHeading(getHeadingBetween(item), (headingCons-anglesTreshold) % 360, (headingCons+anglesTreshold)%360))
                    .sorted(Comparator
                            .comparing(item -> item.getGameObjectType() == ObjectTypes.FOOD ? getDistanceBetween(getBot(), item) : getDistanceBetween(getBot(), item)/2))
                    .collect(Collectors.toList());
        return getHeadingBetween(foodList.get(0));
    }

    public void debug(){
        if(this.hazardousObject != null){
            if(ifNearObj(this.hazardousObject)){
                System.out.println("Terdapat hazardous object dengan tipe: " + this.hazardousObject.getGameObjectType());
                System.out.println("Jarak antara objek dengan bot: " + getEdgeDistanceBetween(this.hazardousObject, getBot()));
            }
            
        }
        else if(this.enemyBot != null){
            if(ifNearEnemy(this.enemyBot)){
                System.out.println("Jarak antara musuh dengan bot: " + getEdgeDistanceBetween(this.enemyBot, getBot()));
            }
        }
        else if(isNearEdge()){
            System.out.println("Terlalu dekat dengan pinggiran map! ");
        }
        
    }
}
