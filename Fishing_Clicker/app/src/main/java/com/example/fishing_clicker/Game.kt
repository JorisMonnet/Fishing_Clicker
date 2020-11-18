package com.example.fishing_clicker

import com.example.fishing_clicker.boat.Boat
import java.util.*

class Game() {
    private var boatList: MutableList<Boat> = LinkedList<Boat>();
    private var boatIndex = 1;
    private var baseEfficiency = 1.0;


    fun buyABoat() {
        boatList.add(Boat(baseEfficiency, boatIndex));
        baseEfficiency *= 1.5;
        boatIndex++;
    }


}