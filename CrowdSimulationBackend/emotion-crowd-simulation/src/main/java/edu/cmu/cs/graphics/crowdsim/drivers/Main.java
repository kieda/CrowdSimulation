package edu.cmu.cs.graphics.crowdsim.drivers;

import edu.cmu.cs.graphics.crowdsim.ai.core.BasicInitialStateGenerator;
import edu.cmu.cs.graphics.crowdsim.ai.core.Game;
import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        Options o = new Options();
        o.addOption(new Option(PRECISION, false, 
                "the precision of the number of players on each team"));
        o.addOption(new Option(PLAYERS, false, 
                "the expected number of players on each team"));
        
        CommandLineParser clp = new DefaultParser();
        CommandLine cl = clp.parse(o, args, true);
        
        String fileName;
        switch(cl.getArgs().length){
            case 0: fileName = "../CrowdSimulation/Assets/core/corescript";
                break;
            default: fileName = cl.getArgs()[0];
        }
        int numPlayers = cl.hasOption(PLAYERS) ? 
            Integer.parseInt(cl.getOptionValue(PLAYERS)) : 10;
        double precision = cl.hasOption(PRECISION) ? 
            Double.parseDouble(cl.getOptionValue(PRECISION)) : 0.5;
        
        Game g = Game.makeGame(new FileOutputStream(fileName), new BasicInitialStateGenerator(numPlayers, precision));
            g.run();
    }
    private static final String PLAYERS = "players";
    private static final String PRECISION = "precision";
}
