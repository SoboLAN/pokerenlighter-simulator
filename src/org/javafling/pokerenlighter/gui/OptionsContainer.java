package org.javafling.pokerenlighter.gui;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author Radu Murzea
 */
public class OptionsContainer
{
    private static OptionsContainer _instance;
    
    private static final String preferencesFile = "config/config.info";
    private static final int minRounds = 60000;
    private static final int maxRounds = 200000;
    
    private HashMap<String, String> options;
    
    //available values
    private String[] availableLanguages = {"English", "Romanian"};
    private String[] availableLNFs = {
        "Nimbus",
        "System",
        "Motif",
        "EaSynth",
        "NimROD",
        "SeaGlass",
        "Substance-Business",
        "Substance-BusinessBlack",
        "Substance-BusinessBlue",
        "Substance-Challenger",
        "Substance-Creme",
        "Substance-CremeCoffee",
        "Substance-Dust",
        "Substance-DustCoffee",
        "Substance-Emerald",
        "Substance-Magellan",
        "Substance-MistAqua",
        "Substance-MistSilver",
        "Substance-Moderate",
        "Substance-Nebula",
        "Substance-NebulaBrick",
        "Substance-OfficeBlue",
        "Substance-OfficeSilver",
        "Substance-Raven",
        "Substance-Sahara",
        "Substance-Twilight"
    };
    
    //default values
    private String defLanguage = availableLanguages[0];
    private String defLNF = availableLNFs[0];
    private int defRounds = 105000;
    private boolean defShowLabels = true;
    private boolean defStackedGraph = false;
    private boolean def3DGraph = false;
    private int[][] defColors = 
    {
        {38, 206, 70},    //wins
        {220, 45, 47},    //loses
        {70, 94, 91}    //ties
    };
    
    //map keys
    //stored here separately because they're used throughout the class
    //minimizes risk of mistyping
    private String languageKey = "language";
    private String lnfKey = "lookandfeel";
    private String roundsKey = "rounds";
    private String showLabelsKey = "graphshowlabels";
    private String winsRedKey = "winsred";
    private String winsGreenKey = "winsgreen";
    private String winsBlueKey = "winsblue";
    private String losesRedKey = "losesred";
    private String losesGreenKey = "losesgreen";
    private String losesBlueKey = "losesblue";
    private String tiesRedKey = "tiesred";
    private String tiesGreenKey = "tiesgreen";
    private String tiesBlueKey = "tiesblue";
    private String graphStackedKey = "graphstacked";
    private String graph3DKey = "graph3d";
    
    public static OptionsContainer getOptionsContainer()
    {
        if (_instance == null) {
            _instance = new OptionsContainer();
        }
        
        return _instance;
    }
    
    private OptionsContainer()
    {
        options = new HashMap<>();

        loadConfiguration();
    }
    
    public String getLanguage()
    {
        return options.get(languageKey);
    }
    
    public String getLookAndFeel()
    {
        return options.get(lnfKey);
    }
    
    public int getRounds()
    {
        String rounds = options.get(roundsKey);
        
        return Integer.parseInt(rounds);
    }
    
    public boolean getShowGraphLabels()
    {
        return options.get(showLabelsKey).equals("true");
    }
    
    public int getWinsRedValue()
    {
        String winsRed = options.get(winsRedKey);
        
        return Integer.parseInt(winsRed);
    }
    
    public int getWinsGreenValue()
    {
        String winsGreen = options.get(winsGreenKey);
        
        return Integer.parseInt(winsGreen);
    }
    
    public int getWinsBlueValue()
    {
        String winsBlue = options.get(winsBlueKey);
        
        return Integer.parseInt(winsBlue);
    }
    
    public int getLosesRedValue()
    {
        String losesRed = options.get(losesRedKey);
        
        return Integer.parseInt(losesRed);
    }
    
    public int getLosesGreenValue()
    {
        String losesGreen = options.get(losesGreenKey);
        
        return Integer.parseInt(losesGreen);
    }
    
    public int getLosesBlueValue()
    {
        String losesBlue = options.get(losesBlueKey);
        
        return Integer.parseInt(losesBlue);
    }
    
    public int getTiesRedValue()
    {
        String tiesRed = options.get(tiesRedKey);
        
        return Integer.parseInt(tiesRed);
    }
    
    public int getTiesGreenValue()
    {
        String tiesGreen = options.get(tiesGreenKey);
        
        return Integer.parseInt(tiesGreen);
    }
    
    public int getTiesBlueValue()
    {
        String tiesBlue = options.get(tiesBlueKey);
        
        return Integer.parseInt(tiesBlue);
    }
    
    public String[] getAvailableLanguages()
    {
        return availableLanguages;
    }
    
    public String[] getAvailableLookAndFeels()
    {
        return availableLNFs;
    }
    
    public boolean getGraphStacked()
    {
        String graphStacked = options.get(graphStackedKey);
        
        return Boolean.parseBoolean(graphStacked);
    }
    
    public boolean getGraph3D()
    {
        String graph3D = options.get(graph3DKey);
        
        return Boolean.parseBoolean(graph3D);
    }
    
    public void setLanguage(int newIndex)
    {
        if (newIndex < 0 || newIndex >= availableLanguages.length) {
            return;
        }
        
        options.put(languageKey, availableLanguages[newIndex]);
    }
    
    public void setLookAndFeel(int newIndex)
    {
        if (newIndex < 0 || newIndex >= availableLNFs.length) {
            return;
        }
        
        options.put(lnfKey, availableLNFs[newIndex]);
    }
    
    public void setRounds(int newRounds)
    {
        if (newRounds < minRounds || newRounds > maxRounds) {
            return;
        }
        
        options.put(roundsKey, Integer.toString(newRounds));
    }
    
    public void setWinsRed(int newWinsRed)
    {
        if (newWinsRed < 0 || newWinsRed > 255) {
            return;
        }
        
        options.put(winsRedKey, Integer.toString(newWinsRed));
    }
    
    public void setWinsGreen(int newWinsGreen)
    {
        if (newWinsGreen < 0 || newWinsGreen > 255) {
            return;
        }
        
        options.put(winsGreenKey, Integer.toString(newWinsGreen));
    }
    
    public void setWinsBlue(int newWinsBlue)
    {
        if (newWinsBlue < 0 || newWinsBlue > 255) {
            return;
        }
        
        options.put(winsBlueKey, Integer.toString(newWinsBlue));
    }
    
    public void setLosesRed(int newLosesRed)
    {
        if (newLosesRed < 0 || newLosesRed > 255) {
            return;
        }
        
        options.put(losesRedKey, Integer.toString(newLosesRed));
    }
    
    public void setLosesGreen(int newLosesGreen)
    {
        if (newLosesGreen < 0 || newLosesGreen > 255) {
            return;
        }
        
        options.put(losesGreenKey, Integer.toString(newLosesGreen));
    }
    
    public void setLosesBlue(int newLosesBlue)
    {
        if (newLosesBlue < 0 || newLosesBlue > 255) {
            return;
        }
        
        options.put(losesBlueKey, Integer.toString(newLosesBlue));
    }
    
    public void setTiesRed(int newTiesRed)
    {
        if (newTiesRed < 0 || newTiesRed > 255) {
            return;
        }
        
        options.put(tiesRedKey, Integer.toString(newTiesRed));
    }
    
    public void setTiesGreen(int newTiesGreen)
    {
        if (newTiesGreen < 0 || newTiesGreen > 255) {
            return;
        }
        
        options.put(tiesGreenKey, Integer.toString(newTiesGreen));
    }
    
    public void setTiesBlue(int newTiesBlue)
    {
        if (newTiesBlue < 0 || newTiesBlue > 255) {
            return;
        }
        
        options.put(tiesBlueKey, Integer.toString(newTiesBlue));
    }
    
    public void setDisplayLabel(boolean showLabel)
    {
        options.put(showLabelsKey, Boolean.toString(showLabel));
    }
    
    public void setGraphStacked(boolean graphStacked)
    {
        options.put(graphStackedKey, Boolean.toString(graphStacked));
    }
    
    public void setGraph3D(boolean graph3D)
    {
        options.put(graph3DKey, Boolean.toString(graph3D));
    }
    
    private void loadConfiguration()
    {
        Properties prop = new Properties();
        
        try(FileInputStream fileStream = new FileInputStream(preferencesFile)) {
            prop.load(fileStream);
        } catch(Exception e) {
            setDefaultValues();
            saveConfiguration();
            
            return;
        }
        
        boolean languageOK = isValidLanguage(prop.getProperty(languageKey));
        boolean lnfOK = isValidLNF(prop.getProperty(lnfKey));
        boolean roundsOK = isValidNumericValue(prop.getProperty(roundsKey), minRounds, maxRounds);
        boolean showLabelsOK = isValidBoolean(prop.getProperty(showLabelsKey));
        boolean winsRedOK = isValidNumericValue(prop.getProperty(winsRedKey), 0, 255);
        boolean winsGreenOK = isValidNumericValue(prop.getProperty(winsGreenKey), 0, 255);
        boolean winsBlueOK = isValidNumericValue(prop.getProperty(winsBlueKey), 0, 255);
        boolean losesRedOK = isValidNumericValue(prop.getProperty(losesRedKey), 0, 255);
        boolean losesGreenOK = isValidNumericValue(prop.getProperty(losesGreenKey), 0, 255);
        boolean losesBlueOK = isValidNumericValue(prop.getProperty(losesBlueKey), 0, 255);
        boolean tiesRedOK = isValidNumericValue(prop.getProperty(tiesRedKey), 0, 255);
        boolean tiesGreenOK = isValidNumericValue(prop.getProperty(tiesGreenKey), 0, 255);
        boolean tiesBlueOK = isValidNumericValue(prop.getProperty(tiesBlueKey), 0, 255);
        boolean graphStackOK = isValidBoolean(prop.getProperty(graphStackedKey));
        boolean graph3DOK = isValidBoolean(prop.getProperty(graph3DKey));
        
        options.put(languageKey, languageOK ? prop.getProperty(languageKey) : defLanguage);
        options.put(lnfKey, lnfOK ? prop.getProperty(lnfKey) : defLNF);
        options.put(roundsKey, roundsOK ? prop.getProperty(roundsKey) : Integer.toString(defRounds));
        options.put(showLabelsKey, showLabelsOK ? prop.getProperty(showLabelsKey) : Boolean.toString(defShowLabels));
        options.put(winsRedKey, winsRedOK ? prop.getProperty(winsRedKey) : Integer.toString(defColors[0][0]));
        options.put(winsGreenKey, winsGreenOK ? prop.getProperty(winsGreenKey) : Integer.toString(defColors[0][1]));
        options.put(winsBlueKey, winsBlueOK ? prop.getProperty(winsBlueKey) : Integer.toString(defColors[0][2]));
        options.put(losesRedKey, losesRedOK ? prop.getProperty(losesRedKey) : Integer.toString(defColors[1][0]));
        options.put(losesGreenKey, losesGreenOK ? prop.getProperty(losesGreenKey) : Integer.toString(defColors[1][1]));
        options.put(losesBlueKey, losesBlueOK ? prop.getProperty(losesBlueKey) : Integer.toString(defColors[1][2]));
        options.put(tiesRedKey, tiesRedOK ? prop.getProperty(tiesRedKey) : Integer.toString(defColors[2][0]));
        options.put(tiesGreenKey, tiesGreenOK ? prop.getProperty(tiesGreenKey) : Integer.toString(defColors[2][1]));
        options.put(tiesBlueKey, tiesBlueOK ? prop.getProperty(tiesBlueKey) : Integer.toString(defColors[2][2]));
        options.put(graphStackedKey, graphStackOK ? prop.getProperty(graphStackedKey) : Boolean.toString(defStackedGraph));
        options.put(graph3DKey, graph3DOK ? prop.getProperty(graph3DKey) : Boolean.toString(def3DGraph));
        
        boolean everythingOK = languageOK &&
                                lnfOK &&
                                roundsOK &&
                                showLabelsOK &&
                                winsRedOK &&
                                winsGreenOK &&
                                winsBlueOK &&
                                losesRedOK &&
                                losesGreenOK &&
                                losesBlueOK &&
                                tiesRedOK &&
                                tiesGreenOK &&
                                tiesBlueOK &&
                                graphStackOK &&
                                graph3DOK;
        
        if (! everythingOK) {
            saveConfiguration();
        }
    }
    
    private boolean isValidLanguage(String language)
    {
        if (language == null) {
            return false;
        }
        
        boolean languageFound = false;
        
        for (int i = 0; i < availableLanguages.length; i++) {
            if (language.equals(availableLanguages[i])) {
                languageFound = true;
                break;
            }
        }

        return languageFound;
    }
    
    private boolean isValidBoolean(String value)
    {
        if (value == null) {
            return false;
        }
        
        return (value.equals("true") || value.equals("false"));
    }
    
    private boolean isValidLNF(String lnf)
    {
        if (lnf == null) {
            return false;
        }
        
        boolean lnfFound = false;
        
        for (int i = 0; i < availableLNFs.length; i++) {
            if (lnf.equals(availableLNFs[i])) {
                lnfFound = true;
                break;
            }
        }

        return lnfFound;
    }
    
    private boolean isValidNumericValue(String value, int min, int max)
    {
        if (value == null) {
            return false;
        }
        
        int parsedValue = 0;
        
        try {
            parsedValue = Integer.parseInt(value);
        } catch(NumberFormatException e) {
            return false;
        }
        
        return (parsedValue >= min && parsedValue <= max);
    }
    
    public void saveConfiguration()
    {
        Properties prop = new Properties();
        
        prop.setProperty(languageKey, options.get(languageKey));
        prop.setProperty(lnfKey, options.get(lnfKey));
        prop.setProperty(roundsKey, options.get(roundsKey));
        prop.setProperty(showLabelsKey, options.get(showLabelsKey));
        prop.setProperty(winsRedKey, options.get(winsRedKey));
        prop.setProperty(winsGreenKey, options.get(winsGreenKey));
        prop.setProperty(winsBlueKey, options.get(winsBlueKey));
        prop.setProperty(losesRedKey, options.get(losesRedKey));
        prop.setProperty(losesGreenKey, options.get(losesGreenKey));
        prop.setProperty(losesBlueKey, options.get(losesBlueKey));
        prop.setProperty(tiesRedKey, options.get(tiesRedKey));
        prop.setProperty(tiesGreenKey, options.get(tiesGreenKey));
        prop.setProperty(tiesBlueKey, options.get(tiesBlueKey));
        prop.setProperty(graphStackedKey, options.get(graphStackedKey));
        prop.setProperty(graph3DKey, options.get(graph3DKey));

        try(FileOutputStream fileStream = new FileOutputStream(preferencesFile, false)) {
            prop.store(fileStream, null);
        } catch(Exception e) {
            GUIUtilities.showErrorDialog(
                null,
                "There was an error while writing the configuration",
                "Save Error"
            );
        }
    }
    
    private void setDefaultValues()
    {
        options.put(languageKey, defLanguage);
        options.put(lnfKey, defLNF);
        options.put(roundsKey, Integer.toString(defRounds));
        options.put(showLabelsKey, Boolean.toString(defShowLabels));
        options.put(winsRedKey, Integer.toString(defColors[0][0]));
        options.put(winsGreenKey, Integer.toString(defColors[0][1]));
        options.put(winsBlueKey, Integer.toString(defColors[0][2]));
        options.put(losesRedKey, Integer.toString(defColors[1][0]));
        options.put(losesGreenKey, Integer.toString(defColors[1][1]));
        options.put(losesBlueKey, Integer.toString(defColors[1][2]));
        options.put(tiesRedKey, Integer.toString(defColors[2][0]));
        options.put(tiesGreenKey, Integer.toString(defColors[2][1]));
        options.put(tiesBlueKey, Integer.toString(defColors[2][2]));
        options.put(graphStackedKey, Boolean.toString(defStackedGraph));
        options.put(graph3DKey, Boolean.toString(def3DGraph));
    }
}
