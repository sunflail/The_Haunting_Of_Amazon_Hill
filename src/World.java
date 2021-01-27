import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class World {
    Ghost currentGhost;

    List<Room> gameMap = new ArrayList<>();

    Room lobby = new Room("Lobby");
    Room dungeon = new Room("Dungeon");
    Room diningRoom = new Room("Dining Room");
    Room balcony = new Room("Balcony");
    Room furnaceRoom = new Room("Furnace Room");

    Room currentRoom = lobby;

    public World() {


        lobby.roomExits.put("east", dungeon);
        lobby.roomExits.put("west", diningRoom);
        lobby.roomExits.put("north", balcony);
        lobby.roomExits.put("south", furnaceRoom);
        lobby.setRoomItems(Ghost.SAMCA.getEvidence()[0]);

        gameMap.add(lobby);


        //  dungeon.roomExits.put("east", dungeon);
        dungeon.roomExits.put("west", lobby);
        //    dungeon.roomExits.put("north", balcony);
        //    dungeon.roomExits.put("south", furnaceRoom);
        dungeon.roomItems = Ghost.SAMCA.getEvidence()[1];

        gameMap.add(dungeon);


        //  diningRoom.roomExits.put("east", dungeon);
        diningRoom.roomExits.put("east", lobby);
        //   diningRoom.roomExits.put("north", balcony);
        //  diningRoom.roomExits.put("south", furnaceRoom);
        diningRoom.setRoomItems("");

        gameMap.add(diningRoom);


        //  balcony.roomExits.put("east", dungeon);
        //   balcony.roomExits.put("west", diningRoom);
        //   balcony.roomExits.put("north", balcony);
        balcony.roomExits.put("south", lobby);
        balcony.setRoomItems("");

        gameMap.add(balcony);


        // furnaceRoom.roomExits.put("east", dungeon);
        //    furnaceRoom.roomExits.put("west", diningRoom);
        furnaceRoom.roomExits.put("north", lobby);
        // furnaceRoom.roomExits.put("south", furnaceRoom);
        furnaceRoom.setRoomItems("");

        gameMap.add(furnaceRoom);

    }




}