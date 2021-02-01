package com.intelligents.haunting;

import com.intelligents.client.Main;
import com.intelligents.haunting.Game;
//import com.intelligents.haunting.Player;
//import com.intelligents.haunting.Room;
//import com.intelligents.haunting.World;  ?? Done: ask team about package private World, Player and Room

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class saveGame {
    private Game game;

    public saveGame() {
    }

    public void save() {
     // NOTE: classes you want to be able to save must implement java.io.Serializable. Without implementing, you will be
        // hit with exception type NotSerializableException.



        // using stream to Transfer bytes. From memory to disk for saving, from disk to memory for loading.
        try {
            //passing a file to save to FileOutputStream
            FileOutputStream fos = new FileOutputStream("usr.save");
            // passing those bytes to ObjectOutputStream for the ability to write, for data to be represented as objects
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            //writing top-level objects
            oos.writeObject(game.getWorld());
            oos.writeObject(game.getCurrentGhost());
            oos.writeObject(game.getGhosts());
            oos.writeObject(game.getPlayer());
            // make sure any buffered data is written before i close the stream
            oos.flush();
            oos.close();
            System.out.println("Game saved\n");
        }catch (Exception e) {
            //System.out.println("Serialization Error! Can not save data.\n" + e.getClass() + ": " + e.getMessage() + "\n");
            System.out.println("Sorry currently cannot save game. **coming Soon!**");
            e.printStackTrace();
        }



    }

    @SuppressWarnings("unchecked")  //i wrote this code, so i can guarantee this is an array of objects
    public void loadGame() {
        try {
            //pulling data from file with FileInputStream
            FileInputStream fis = new FileInputStream("usr.save");
            //passing bytes to ObjectInputStream for the ability to read those bytes as data representing objects
            ObjectInputStream ois = new ObjectInputStream(fis);

            World world = (World) ois.readObject();
            Ghost ghost = (Ghost) ois.readObject();
            ArrayList<Ghost> ghosts = (ArrayList<Ghost>) ois.readObject();
            Player player = (Player) ois.readObject();
            game.setPlayer(player);
            game.setWorld(world);
            game.setCurrentGhost(ghost);
            game.setGhosts(ghosts);
            ois.close();
        }catch (Exception e) {
            System.out.println("Could not load saved game");
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }










}
