package com.rs.core.cache.loaders;

import com.rs.core.cache.Cache;
import com.rs.server.net.io.InputStream;

import java.util.concurrent.ConcurrentHashMap;

public final class VarBitDefinitions {

    private static final ConcurrentHashMap<Integer, VarBitDefinitions> varpbitDefs = new ConcurrentHashMap<Integer, VarBitDefinitions>();

    public int id;
    public int baseVar;
    public int startBit;
    public int endBit;

    public static VarBitDefinitions getClientVarpBitDefinitions(int id) {
        VarBitDefinitions script = varpbitDefs.get(id);
        if (script != null)// open new txt document
            return script;
        byte[] data = Cache.STORE.getIndexes()[22].getFile(id >>> 10, id & 0x3ff); //1416501898
        script = new VarBitDefinitions();
        script.id = id;
        if (data != null)
            script.readValueLoop(new InputStream(data));
        varpbitDefs.put(id, script);
        return script;

    }

    private void readValueLoop(InputStream stream) {
        int opcode;
        while ((opcode = stream.readUnsignedByte()) != 0) {
            readValues(stream, opcode);
        }
    }

    private void readValues(InputStream stream, int opcode) {
        if (opcode == 1) {
            baseVar = stream.readUnsignedShort();
            startBit = stream.readUnsignedByte();
            endBit = stream.readUnsignedByte();
        }
    }

    private VarBitDefinitions() {

    }
}
