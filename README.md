# fuzzyrs
Runescape private server revision 718. 

# TODO

* Fix Nomad attack animation
* Clean up region after sailing
* Fix Nomad ending. Something happens to character not allowing them to move.
* Fix Overload timer in middle of screen

* Player dot on minimap doesn't show orange for clan members
* Implement an iron man mode
    * Iron man and regular player
    * Implement a PlayerType system to use this.

* Nex needs to be listed in boss tabs.
* Fix last note entry not being able to be deleted.
* Fine tune Wilderness Agility and Gnome Agility course
* Fix exit portals for runecrafting
* Add check action to Giant Pouch item id = 5514
* Fix obby maul in Cache
* Avatar doesn't spawn


# DONE

* [FIXED] Fix StackOverFlow thrown when Nomad is finished
    *
	    ```
    	java.lang.StackOverflowError
    	at com.rs.world.Entity.setForceMultiArea(Entity.java:1271)
    	at com.rs.player.controlers.NomadsRequiem.leave(NomadsRequiem.java:275)
    	at com.rs.player.controlers.NomadsRequiem.forceClose(NomadsRequiem.java:248)
    	at com.rs.player.ControllerManager.forceStop(ControllerManager.java:235)
    	at com.rs.player.ControllerManager.startController(ControllerManager.java:46)
    	at com.rs.player.controlers.NomadsRequiem.leave(NomadsRequiem.java:276)
    	at com.rs.player.controlers.NomadsRequiem.forceClose(NomadsRequiem.java:248)
    	at com.rs.player.ControllerManager.forceStop(ControllerManager.java:235)
    	at com.rs.player.ControllerManager.startController(ControllerManager.java:46)
    	```


* [FIXED] NPCs don't deal damage and attack processing is broken for NPCs
    * Specifically magic damage it seems
    * Bosses dealing reflective damage instead of actual damage.