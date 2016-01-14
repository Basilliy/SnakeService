package Project;

import java.io.Serializable;

public class Player implements Serializable {

    private final String name;
    private final String address;
    private boolean ready;

    public Player(String name, String address, boolean ready){
        this.name = name;
        this.address = address;
        this.ready = ready;
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
}
