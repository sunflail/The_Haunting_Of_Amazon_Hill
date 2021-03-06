package com.intelligents.haunting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class World implements java.io.Serializable {
    List<Room> gameMap = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();
    private Room currentRoom;

    private Room startingRoom;

    Room getCurrentRoom() {
        return currentRoom;
    }

    void setCurrentRoom(Room currentRoom) {
        this.currentRoom = currentRoom;
    }

    World(ClassLoader classLoader, String resourcePath) {
        //read all room objects in
        populateRoomList(classLoader,resourcePath);
        currentRoom = rooms.get(0);

        HashMap<String, Room> map = new HashMap<>();
        // create map between room name and room object
        for (Room room : rooms) {
            // Save starting room for a game reset
            if (room.getRoomTitle().equals("Lobby")) setStartingRoom(room);
            map.put(room.getRoomTitle(), room);
            gameMap.add(room);
        }
        // get the directions from each room and map a direction to a room object using previous map
        for (Room room : rooms) {
            for (String key : room.directionList.keySet()) {
                String roomName = room.directionList.get(key);
                room.roomExits.put(key, map.get(roomName));
            }
        }

    }

    void populateRoomList(ClassLoader cl, String resourcePath) {
        this.setRooms(XMLParser.populateRooms(XMLParser.readXML(resourcePath + "Rooms",cl)));
    }

    void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    public Room getStartingRoom() {
        return startingRoom;
    }

    public void setStartingRoom(Room startingRoom) {
        this.startingRoom = startingRoom;
    }

}