package games;

import mainWindow.MainDatabase;

import java.util.List;

/**
 * Created by filip on 5/21/16.
 */
public interface Game {
    void play(int wordsQuantity, List<String> categories, MainDatabase db) throws Exception;
}
