package cs350s22.component.ui.parser;

import cs350s22.component.sensor.reporter.A_Reporter;
import cs350s22.component.sensor.reporter.ReporterChange;
import cs350s22.component.sensor.reporter.ReporterFrequency;
import cs350s22.component.sensor.watchdog.*;
import cs350s22.component.sensor.watchdog.mode.WatchdogModeAverage;
import cs350s22.component.sensor.watchdog.mode.WatchdogModeInstantaneous;
import cs350s22.component.sensor.watchdog.mode.WatchdogModeStandardDeviation;
import cs350s22.support.Identifier;

import java.util.ArrayList;
import java.util.List;

public class HieuParser {

    private A_ParserHelper parserHelper;
    private String commandText;

    public HieuParser(A_ParserHelper parserHelper, String commandText) {
        this.parserHelper = parserHelper;
        this.commandText = commandText;
    }

    public void parse() {
        String[] parseCommandText = commandText.toUpperCase().split(" ");

        if (parseCommandText[1].equals("WATCHDOG")) {
            WatchDog(parseCommandText);
        }
        if(parseCommandText[1].equals("REPORTER")) {
            Reporter(parseCommandText);
        }
    }
    private void Reporter(String[] parseCommandText) {

        Identifier id = Identifier.make(parseCommandText[3]);
        List<Identifier> ids = new ArrayList<>();
        List<Identifier> groups = new ArrayList<>();
        ReporterChange reporterChange;

        if(parseCommandText[2].equals("CHANGE")) {
            int deltaThreshold = Integer.parseInt(parseCommandText[parseCommandText.length - 1]);
            for(int i = 0; i < parseCommandText.length; i++) {
                if(parseCommandText[i].equals("IDS")) {
                    for(int j = i + 1; j < parseCommandText.length; j++ ) {
                        if(!(parseCommandText[j].equals("GROUPS"))) {
                            ids.add(Identifier.make(parseCommandText[j]));
                        }else {
                            break;
                        }
                    }
                }
                if(parseCommandText[i].equals("GROUPS")) {
                    for(int j = i + 1; j < parseCommandText.length; j++ ) {
                        if(!(parseCommandText[j].equals("DELTA"))) {
                            groups.add(Identifier.make(parseCommandText[j]));
                        }else {
                            break;
                        }
                    }
                }
            }

            if(groups.isEmpty()) {
                reporterChange = new ReporterChange(ids, deltaThreshold);
            }else {
                reporterChange = new ReporterChange(ids, groups, deltaThreshold);
            }
            parserHelper.getSymbolTableReporter().add(id, reporterChange);
        }
        else if(parseCommandText[2].equals("FREQUENCY")) {
            for(int i = 0; i < parseCommandText.length; i++) {
                if(parseCommandText[i].equals("IDS")) {
                    for(int j = i + 1; j < parseCommandText.length; j++ ) {
                        if(!(parseCommandText[j].equals("GROUPS"))) {
                            ids.add(Identifier.make(parseCommandText[j]));
                        }else {
                            break;
                        }
                    }
                }
                if(parseCommandText[i].equals("GROUPS")) {
                    for(int j = i + 1; j < parseCommandText.length; j++ ) {
                        if(!(parseCommandText[j].equals("FREQUENCY"))) {
                            groups.add(Identifier.make(parseCommandText[j]));
                        }else {
                            break;
                        }
                    }
                }
            }

        }
    }

    private void WatchDog(String[] parseCommandText) {
        String watchdogType = parseCommandText[2];
        Identifier id = Identifier.make(parseCommandText[3]);
        String mode = parseCommandText[5];
        WatchdogModeInstantaneous instantaneous = new WatchdogModeInstantaneous();
        WatchdogModeAverage average =  new WatchdogModeAverage();
        WatchdogModeStandardDeviation standardDeviation = new WatchdogModeStandardDeviation();
        switch(watchdogType) {
            case "ACCELERATION":
                if(mode.equals("INSTANTANEOUS")) {
                    if(parseCommandText.length == 11) {
                        WatchdogAcceleration watchdogAcceleration = new WatchdogAcceleration(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), instantaneous);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogAcceleration);
                    }
                    if(parseCommandText.length == 13) {
                        WatchdogAcceleration watchdogAcceleration = new WatchdogAcceleration(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), instantaneous, Integer.parseInt(parseCommandText[12]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogAcceleration);
                    }
                }
                if(mode.equals("AVERAGE")) {
                    if (parseCommandText.length == 11) {
                        WatchdogAcceleration watchdogAcceleration = new WatchdogAcceleration(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), average);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogAcceleration);
                    }
                    if (parseCommandText.length == 12) {
                        WatchdogModeAverage watchdogModeAverage = new WatchdogModeAverage(Integer.parseInt(parseCommandText[6]));
                        WatchdogAcceleration watchdogAcceleration = new WatchdogAcceleration(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), watchdogModeAverage);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogAcceleration);
                    }
                    if (parseCommandText.length == 13) {
                        WatchdogAcceleration watchdogAcceleration = new WatchdogAcceleration(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), average, Integer.parseInt(parseCommandText[12]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogAcceleration);
                    }
                    if (parseCommandText.length == 14) {
                        WatchdogModeAverage watchdogModeAverage = new WatchdogModeAverage(Integer.parseInt(parseCommandText[6]));
                        WatchdogAcceleration watchdogAcceleration = new WatchdogAcceleration(Double.parseDouble(parseCommandText[9]), Double.parseDouble(parseCommandText[11]), watchdogModeAverage, Integer.parseInt(parseCommandText[13]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogAcceleration);
                    }
                }
                if(mode.equals("STANDARD")) {
                    if(parseCommandText.length == 12) {
                        WatchdogAcceleration watchdogAcceleration = new WatchdogAcceleration(Double.parseDouble(parseCommandText[9]), Double.parseDouble(parseCommandText[11]), standardDeviation);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogAcceleration);
                    }
                    if(parseCommandText.length == 13) {
                        WatchdogModeStandardDeviation watchdogModeStandardDeviation = new WatchdogModeStandardDeviation(Integer.parseInt(parseCommandText[7]));
                        WatchdogAcceleration watchdogAcceleration = new WatchdogAcceleration(Double.parseDouble(parseCommandText[10]), Double.parseDouble(parseCommandText[12]), watchdogModeStandardDeviation);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogAcceleration);
                    }
                    if(parseCommandText.length == 14) {
                        WatchdogAcceleration watchdogAcceleration = new WatchdogAcceleration(Double.parseDouble(parseCommandText[9]), Double.parseDouble(parseCommandText[11]), standardDeviation, Integer.parseInt(parseCommandText[13]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogAcceleration);
                    }
                    if(parseCommandText.length == 15) {
                        WatchdogModeStandardDeviation watchdogModeStandardDeviation = new WatchdogModeStandardDeviation(Integer.parseInt(parseCommandText[7]));
                        WatchdogAcceleration watchdogAcceleration = new WatchdogAcceleration(Double.parseDouble(parseCommandText[10]), Double.parseDouble(parseCommandText[12]), watchdogModeStandardDeviation, Integer.parseInt(parseCommandText[14]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogAcceleration);
                    }
                }
                break;
            case "BAND":
                if(mode.equals("INSTANTANEOUS")) {
                    if(parseCommandText.length == 11) {
                        WatchdogBand watchdogBand = new WatchdogBand(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), instantaneous);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogBand);
                    }
                    if(parseCommandText.length == 13) {
                        WatchdogBand watchdogBand = new WatchdogBand(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), instantaneous, Integer.parseInt(parseCommandText[12]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogBand);
                    }
                }
                if(mode.equals("AVERAGE")) {
                    if (parseCommandText.length == 11) {
                        WatchdogBand watchdogBand = new WatchdogBand(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), average);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogBand);
                    }
                    if (parseCommandText.length == 12) {
                        WatchdogModeAverage watchdogModeAverage = new WatchdogModeAverage(Integer.parseInt(parseCommandText[6]));
                        WatchdogBand watchdogBand = new WatchdogBand(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), watchdogModeAverage);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogBand);
                    }
                    if (parseCommandText.length == 13) {
                        WatchdogBand watchdogBand = new WatchdogBand(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), average, Integer.parseInt(parseCommandText[12]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogBand);
                    }
                    if (parseCommandText.length == 14) {
                        WatchdogModeAverage watchdogModeAverage = new WatchdogModeAverage(Integer.parseInt(parseCommandText[6]));
                        WatchdogBand watchdogBand = new WatchdogBand(Double.parseDouble(parseCommandText[9]), Double.parseDouble(parseCommandText[11]), watchdogModeAverage, Integer.parseInt(parseCommandText[13]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogBand);
                    }
                }
                if(mode.equals("STANDARD")) {
                    if(parseCommandText.length == 12) {
                        WatchdogBand watchdogBand = new WatchdogBand(Double.parseDouble(parseCommandText[9]), Double.parseDouble(parseCommandText[11]), standardDeviation);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogBand);
                    }
                    if(parseCommandText.length == 13) {
                        WatchdogModeStandardDeviation watchdogModeStandardDeviation = new WatchdogModeStandardDeviation(Integer.parseInt(parseCommandText[7]));
                        WatchdogBand watchdogBand = new WatchdogBand(Double.parseDouble(parseCommandText[10]), Double.parseDouble(parseCommandText[12]), watchdogModeStandardDeviation);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogBand);
                    }
                    if(parseCommandText.length == 14) {
                        WatchdogBand watchdogBand = new WatchdogBand(Double.parseDouble(parseCommandText[9]), Double.parseDouble(parseCommandText[11]), standardDeviation, Integer.parseInt(parseCommandText[13]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogBand);
                    }
                    if(parseCommandText.length == 15) {
                        WatchdogModeStandardDeviation watchdogModeStandardDeviation = new WatchdogModeStandardDeviation(Integer.parseInt(parseCommandText[7]));
                        WatchdogBand watchdogBand = new WatchdogBand(Double.parseDouble(parseCommandText[10]), Double.parseDouble(parseCommandText[12]), watchdogModeStandardDeviation, Integer.parseInt(parseCommandText[14]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogBand);
                    }
                }
                break;
            case "NOTCH":
                if(mode.equals("INSTANTANEOUS")) {
                    if(parseCommandText.length == 11) {
                        WatchdogNotch watchdogNotch = new WatchdogNotch(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), instantaneous);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogNotch);
                    }
                    if(parseCommandText.length == 13) {
                        WatchdogNotch watchdogNotch = new WatchdogNotch(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), instantaneous, Integer.parseInt(parseCommandText[12]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogNotch);
                    }
                }
                if(mode.equals("AVERAGE")) {
                    if (parseCommandText.length == 11) {
                        WatchdogNotch watchdogNotch = new WatchdogNotch(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), average);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogNotch);
                    }
                    if (parseCommandText.length == 12) {
                        WatchdogModeAverage watchdogModeAverage = new WatchdogModeAverage(Integer.parseInt(parseCommandText[6]));
                        WatchdogNotch watchdogNotch = new WatchdogNotch(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), watchdogModeAverage);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogNotch);
                    }
                    if (parseCommandText.length == 13) {
                        WatchdogNotch watchdogNotch = new WatchdogNotch(Double.parseDouble(parseCommandText[8]), Double.parseDouble(parseCommandText[10]), average, Integer.parseInt(parseCommandText[12]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogNotch);
                    }
                    if (parseCommandText.length == 14) {
                        WatchdogModeAverage watchdogModeAverage = new WatchdogModeAverage(Integer.parseInt(parseCommandText[6]));
                        WatchdogNotch watchdogNotch = new WatchdogNotch(Double.parseDouble(parseCommandText[9]), Double.parseDouble(parseCommandText[11]), watchdogModeAverage, Integer.parseInt(parseCommandText[13]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogNotch);
                    }
                }
                if(mode.equals("STANDARD")) {
                    if(parseCommandText.length == 12) {
                        WatchdogNotch watchdogNotch = new WatchdogNotch(Double.parseDouble(parseCommandText[9]), Double.parseDouble(parseCommandText[11]), standardDeviation);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogNotch);
                    }
                    if(parseCommandText.length == 13) {
                        WatchdogModeStandardDeviation watchdogModeStandardDeviation = new WatchdogModeStandardDeviation(Integer.parseInt(parseCommandText[7]));
                        WatchdogNotch watchdogNotch = new WatchdogNotch(Double.parseDouble(parseCommandText[10]), Double.parseDouble(parseCommandText[12]), watchdogModeStandardDeviation);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogNotch);
                    }
                    if(parseCommandText.length == 14) {
                        WatchdogNotch watchdogNotch = new WatchdogNotch(Double.parseDouble(parseCommandText[9]), Double.parseDouble(parseCommandText[11]), standardDeviation, Integer.parseInt(parseCommandText[13]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogNotch);
                    }
                    if(parseCommandText.length == 15) {
                        WatchdogModeStandardDeviation watchdogModeStandardDeviation = new WatchdogModeStandardDeviation(Integer.parseInt(parseCommandText[7]));
                        WatchdogNotch watchdogNotch = new WatchdogNotch(Double.parseDouble(parseCommandText[10]), Double.parseDouble(parseCommandText[12]), watchdogModeStandardDeviation, Integer.parseInt(parseCommandText[14]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogNotch);
                    }
                }
                break;
            case "LOW":
                if(mode.equals("INSTANTANEOUS")) {
                    if(parseCommandText.length == 8) {
                        WatchdogLow watchdogLow = new WatchdogLow(Double.parseDouble(parseCommandText[7]), instantaneous);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogLow);
                    }
                    if(parseCommandText.length == 10) {
                        WatchdogLow watchdogLow = new WatchdogLow(Double.parseDouble(parseCommandText[7]), instantaneous, Integer.parseInt(parseCommandText[9]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogLow);
                    }
                }
                if(mode.equals("AVERAGE")) {
                    if(parseCommandText.length == 8) {
                        WatchdogLow watchdogLow = new WatchdogLow(Double.parseDouble(parseCommandText[7]), average);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogLow);
                    }
                    if(parseCommandText.length == 9) {
                        WatchdogModeAverage watchdogModeAverage = new WatchdogModeAverage(Integer.parseInt(parseCommandText[6]));
                        WatchdogLow watchdogLow = new WatchdogLow(Double.parseDouble(parseCommandText[8]), watchdogModeAverage);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogLow);
                    }
                    if(parseCommandText.length == 10) {
                        WatchdogLow watchdogLow = new WatchdogLow(Double.parseDouble(parseCommandText[7]), average, Integer.parseInt(parseCommandText[9]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogLow);

                    }
                    if(parseCommandText.length == 11) {
                        WatchdogModeAverage watchdogModeAverage = new WatchdogModeAverage(Integer.parseInt(parseCommandText[6]));
                        WatchdogLow watchdogLow = new WatchdogLow(Double.parseDouble(parseCommandText[8]), watchdogModeAverage, Integer.parseInt(parseCommandText[10]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogLow);
                    }
                }
                if(mode.equals("STANDARD")) {
                    if(parseCommandText.length == 9) {
                        WatchdogLow watchdogLow = new WatchdogLow(Double.parseDouble(parseCommandText[8]), standardDeviation);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogLow);
                    }
                    if(parseCommandText.length == 10) {
                        WatchdogModeStandardDeviation watchdogModeStandardDeviation = new WatchdogModeStandardDeviation(Integer.parseInt(parseCommandText[7]));
                        WatchdogLow watchdogLow = new WatchdogLow(Double.parseDouble(parseCommandText[9]), watchdogModeStandardDeviation);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogLow);
                    }
                    if(parseCommandText.length == 11) {
                        WatchdogLow watchdogLow = new WatchdogLow(Double.parseDouble(parseCommandText[8]), standardDeviation, Integer.parseInt(parseCommandText[10]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogLow);
                    }
                    if(parseCommandText.length == 12) {
                        WatchdogModeStandardDeviation watchdogModeStandardDeviation = new WatchdogModeStandardDeviation(Integer.parseInt(parseCommandText[7]));
                        WatchdogLow watchdogLow = new WatchdogLow(Double.parseDouble(parseCommandText[9]), watchdogModeStandardDeviation, Integer.parseInt(parseCommandText[11]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogLow);
                    }
                }
                break;

            case "HIGH":
                if(mode.equals("INSTANTANEOUS")) {
                    if(parseCommandText.length == 8) {
                        WatchdogHigh watchdogHigh = new WatchdogHigh(Double.parseDouble(parseCommandText[7]), instantaneous);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogHigh);
                    }
                    if(parseCommandText.length == 10) {
                        WatchdogHigh watchdogHigh = new WatchdogHigh(Double.parseDouble(parseCommandText[7]), instantaneous, Integer.parseInt(parseCommandText[9]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogHigh);
                    }
                }
                if(mode.equals("AVERAGE")) {
                    if(parseCommandText.length == 8) {
                        WatchdogHigh watchdogHigh = new WatchdogHigh(Double.parseDouble(parseCommandText[7]), average);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogHigh);
                    }
                    if(parseCommandText.length == 9) {
                        WatchdogModeAverage watchdogModeAverage = new WatchdogModeAverage(Integer.parseInt(parseCommandText[6]));
                        WatchdogHigh watchdogHigh = new WatchdogHigh(Double.parseDouble(parseCommandText[8]), watchdogModeAverage);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogHigh);
                    }
                    if(parseCommandText.length == 10) {
                        WatchdogHigh watchdogHigh = new WatchdogHigh(Double.parseDouble(parseCommandText[7]), average, Integer.parseInt(parseCommandText[9]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogHigh);

                    }
                    if(parseCommandText.length == 11) {
                        WatchdogModeAverage watchdogModeAverage = new WatchdogModeAverage(Integer.parseInt(parseCommandText[6]));
                        WatchdogHigh watchdogHigh = new WatchdogHigh(Double.parseDouble(parseCommandText[8]), watchdogModeAverage, Integer.parseInt(parseCommandText[10]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogHigh);
                    }
                }
                if(mode.equals("STANDARD")) {
                    if(parseCommandText.length == 9) {
                        WatchdogHigh watchdogHigh = new WatchdogHigh(Double.parseDouble(parseCommandText[8]), standardDeviation);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogHigh);
                    }
                    if(parseCommandText.length == 10) {
                        WatchdogModeStandardDeviation watchdogModeStandardDeviation = new WatchdogModeStandardDeviation(Integer.parseInt(parseCommandText[7]));
                        WatchdogHigh watchdogHigh = new WatchdogHigh(Double.parseDouble(parseCommandText[9]), watchdogModeStandardDeviation);
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogHigh);
                    }
                    if(parseCommandText.length == 11) {
                        WatchdogHigh watchdogHigh = new WatchdogHigh(Double.parseDouble(parseCommandText[8]), standardDeviation, Integer.parseInt(parseCommandText[10]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogHigh);
                    }
                    if(parseCommandText.length == 12) {
                        WatchdogModeStandardDeviation watchdogModeStandardDeviation = new WatchdogModeStandardDeviation(Integer.parseInt(parseCommandText[7]));
                        WatchdogHigh watchdogHigh = new WatchdogHigh(Double.parseDouble(parseCommandText[9]), watchdogModeStandardDeviation, Integer.parseInt(parseCommandText[11]));
                        parserHelper.getSymbolTableWatchdog().add(id, watchdogHigh);
                    }
                    break;
                }
            default:
                throw new RuntimeException("Command cannot be parsed");

        }
    }
}
