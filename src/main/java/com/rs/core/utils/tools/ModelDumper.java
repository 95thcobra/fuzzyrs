package com.rs.core.utils.tools;

import com.alex.store.Index;
import com.alex.store.Store;
import com.rs.core.cache.Cache;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ModelDumper {

    public static void main(final String[] args) throws IOException {
        Cache.STORE = new Store(
                "C:/Users/Alexandre/Desktop/Java/projectos/FileStore2/cache562/",
                false);
        final Index index = Cache.STORE.getIndexes()[7];
        System.out.println(index.getLastArchiveId());
        for (int i = 0; i < index.getLastArchiveId(); i++) {
            final byte[] data = index.getFile(i);
            if (data == null) {
                continue;
            }
            // if(!(data[data.length + -1] == -1 && data[-2 + data.length] ==
            // -1))
            // if((data[-1 + data.length] ^ 0xffffffff) != 0 || data[-2 +
            // data.length] != -1)
            // System.out.println(i);
            writeFile(data, "C:/Users/Alexandre/Downloads/RSMV 2/Models3/" + i
                    + ".dat");
        }

    }

    public static void writeFile(final byte[] data, final String fileName)
            throws IOException {
        final OutputStream out = new FileOutputStream(fileName);
        out.write(data);
        out.close();
    }

}
