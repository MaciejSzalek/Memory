package com.memory.memory;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Maciej Szalek on 2019-02-01.
 */

public class GlobalBus {
    private static EventBus sBus;
    public static EventBus getBus(){
        if(sBus == null){
            sBus = EventBus.getDefault();
        }
        return sBus;
    }
}
