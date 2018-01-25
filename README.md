# fuzzyrs
Runescape private server revision 718. 

TODO List

* Fix Nomad attack animation
* Clean up region after sailing
* Fix Nomad ending. Something happens to character not allowing them to move.
* Fix Overload timer in middle of screen
* Fix StackOverFlow thrown when Nomad is finished
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

* NPCs don't deal damage and attack processing is broken for NPCs
    * Specifically magic damage it seems
    * Bosses dealing reflective damage instead of actual damage.

* Avatar doesn't spawn
DONE List