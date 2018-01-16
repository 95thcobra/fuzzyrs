package com.rs.world.task.worldtask;

public abstract class WorldTask implements Runnable {

    protected boolean needRemove;

    public final void stop() {
        needRemove = true;
    }
}
