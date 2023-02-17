package Models;

import Enums.*;
import java.util.*;

public class GameObject {
  public UUID id;
  public Integer size;
  public Integer speed;
  public Integer currentHeading;
  public Position position;
  public ObjectTypes gameObjectType;
  public Integer theEffects;
  public Integer TorpedoSalvoCount;
  public Integer SupernovaAvailable;
  public Integer TeleporterCount;
  public Integer ShieldCount;

  public GameObject(UUID id, Integer size, Integer speed, Integer currentHeading, ObjectTypes gameObjectType, Position position, Effects eff, Integer TorpedoSalvoCount, Integer SupernovaAvailable, Integer TeleporterCount, Integer ShieldCount) {
    this.id = id;
    this.size = size;
    this.speed = speed;
    this.currentHeading = currentHeading;
    this.position = position;
    this.gameObjectType = gameObjectType;
    this.theEffects = eff.getHashCode();
    this.TorpedoSalvoCount = TorpedoSalvoCount;
    this.SupernovaAvailable = SupernovaAvailable;
    this.TeleporterCount = TeleporterCount;
    this.ShieldCount = ShieldCount;
  }

  //getter setter
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public int getSize() {
    return size;
  }

  public void setSize(int size) {
    this.size = size;
  }

  public int getSpeed() {
    return speed;
  }

  public void setSpeed(int speed) {
    this.speed = speed;
  }

  public Position getPosition() {
    return position;
  }

  public int getTheEffects(){
    return this.theEffects;
  }

  public void setPosition(Position position) {
    this.position = position;
  }

  public ObjectTypes getGameObjectType() {
    return gameObjectType;
  }

  public void setGameObjectType(ObjectTypes gameObjectType) {
    this.gameObjectType = gameObjectType;
  }

  public Integer getEffects() {
    return this.theEffects;
  }

  public void setEffect(Integer eff) {
    this.theEffects = eff;
  }

  public Integer getTorpedoSalvoCount() {
    return this.TorpedoSalvoCount;
  }

  public void setTorpedoSalvoCount(Integer t) {
    this.TorpedoSalvoCount = t;
  }

  public Integer getSupernovaAvailable() {
    return this.SupernovaAvailable;
  }

  public void setSupernovaAvailable(Integer t) {
    this.SupernovaAvailable = t;
  }

  public Integer getTeleporterCount() {
    return this.TeleporterCount;
  }

  public void setTeleporterCount(Integer tc) {
    this.TeleporterCount = tc;
  }

  public Integer getShieldCount() {
    return this.ShieldCount;
  }

  public void setShieldCount(Integer sc) {
    this.ShieldCount = sc;
  }

  public static GameObject FromStateList(UUID id, List<Integer> stateList)
  {
    Position position = new Position(stateList.get(4), stateList.get(5));
    if(ObjectTypes.valueOf(stateList.get(3)) == ObjectTypes.PLAYER){
    return new GameObject(id, stateList.get(0), stateList.get(1), stateList.get(2), ObjectTypes.valueOf(stateList.get(3)), position, Effects.val(stateList.get(6)) ,stateList.get(7),stateList.get(8),stateList.get(9),stateList.get(10));

    }
    else{
    return new GameObject(id, stateList.get(0), stateList.get(1), stateList.get(2), ObjectTypes.valueOf(stateList.get(3)), position, Effects.val(0) ,0,0,0,0);

    }
  }

  public void debug(){
    System.out.println("Size: " + this.size);
    System.out.println("Speed: " + this.speed);
    System.out.println("CurrentHeading: " + this.currentHeading);
    System.out.println("Position.x: " + this.position.x);
    System.out.println("Position.y: " + this.position.y);
    System.out.println("Effects: " + this.theEffects);
    System.out.println("TorpedoSalvoCount: " + this.TorpedoSalvoCount);
    System.out.println("SupernovaAvailable: " + this.SupernovaAvailable);
    System.out.println("TeleporterCount: " + this.TeleporterCount);
    System.out.println("ShieldCount: " + this.ShieldCount);
  }
}
