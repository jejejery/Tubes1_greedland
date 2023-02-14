package Enums;

public enum Effects {
    NONE(0),
    AFTERBURNER(1),
    ASTEROIDFIELD(2),
    GASCLOUD(4),
    SUPERFOOD(8),
    SHIELD(16);

    public final Integer value;

    Effects(Integer value){
        this.value = value;
    }
    
    public static Effects val(Integer valuee) {
        for (Effects eff : Effects.values()) {
          if (eff.value == valuee) return eff;
        }

        throw new IllegalArgumentException("Value not found");
    }

    public Integer getHashCode(){
        return this.value;
    }
}
