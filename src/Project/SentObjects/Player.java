package Project.SentObjects;

import java.io.Serializable;

public class Player implements Serializable {

    private String name;
    private final String address;
    private boolean ready;
    private int positionOnMap;


    public Player(String name, String address, boolean ready, int positionOnMap){
        this.name = name;
        this.address = address;
        this.ready = ready;
        this.positionOnMap = positionOnMap;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getPositionOnMap() {
        return positionOnMap;
    }

    public void setPositionOnMap(int positionOnMap) {
        this.positionOnMap = positionOnMap;
    }

    public void setName(String name) {
        this.name = name;
    }
}
