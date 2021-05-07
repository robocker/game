package pl.pastmo.robocker.engine.model;

import java.util.ArrayList;
import java.util.List;

public class Game {

    List<Player> players = new ArrayList<Player>();

    public List<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player){
        players.add(player);
    }




}
