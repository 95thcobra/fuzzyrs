import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rs.Launcher;
import com.rs.core.file.DataFile;
import com.rs.core.file.JsonFileManager;
import com.rs.player.Player;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Fuzzy 1/25/2018
 */
public class JsonPlayerSaveTest {

    @Test
    public void run() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        Launcher.main();
        String accountPath = "./data/playersaves/characters/fuzzyavacado.p";
        Player player = new DataFile<Player>(new File(accountPath)).fromSerialUnchecked();
        String jsonAccountPath = "./data/players/fuzzyavacado.json";
        JsonFileManager jsonFileManager = JsonFileManager.create();
        jsonFileManager.save(jsonAccountPath, player);
        player = jsonFileManager.load(jsonAccountPath, new TypeToken<Player>() {
        }.getType());
        System.out.println(player);
    }
}
