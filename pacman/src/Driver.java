package src;

import src.editor.Controller;
import src.utility.GameCallback;
import src.utility.PropertiesLoader;

import java.io.File;
import java.util.Optional;
import java.util.Properties;

public class Driver {
    public static final String DEFAULT_PROPERTIES_PATH = "properties/test1.properties";

    /**
     * Starting point
     * @param args the command line arguments
     */

    public static void main(String args[]) {
        String propertiesPath = DEFAULT_PROPERTIES_PATH;
        if (args.length > 0) {
            File chosenFile = new File(args[0]);
            if (chosenFile.isDirectory()) {
                // Start playing the game levels in that order
                /* TODO: develop the following function to take in the .xml file as
                         input and parse it accordingly */
                final Properties properties = PropertiesLoader.loadPropertiesFile(propertiesPath);
                GameCallback gameCallback = new GameCallback();
                File [] availableFiles = chosenFile.listFiles();
                Game game = Game.getInstance();
                for (File file : availableFiles) {
                    System.out.println("Now playing: " + file.toString());
                    game.setInitSettings(gameCallback, properties, Optional.of(file));
                    game.build();
                }
                /*
                Game game = Game.getInstance();
                game.setInitSettings(gameCallback, properties, Optional.empty());
                game.build();
                */
            } else if (chosenFile.isFile() && isXMLFile(chosenFile)) {
                // Start in Edit Mode on that map
            } else {
                throw new RuntimeException("Argument was successfully detected but not handled");
            }
            return;
        }
        // Start in edit mode with no current map
        new Controller();
    }

    private static boolean isXMLFile(File file) {
        return file.toString().toLowerCase().endsWith(".xml");
    }
}
