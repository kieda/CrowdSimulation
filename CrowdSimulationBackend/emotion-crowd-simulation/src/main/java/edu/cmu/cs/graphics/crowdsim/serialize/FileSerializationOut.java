package edu.cmu.cs.graphics.crowdsim.serialize;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

import edu.cmu.cs.graphics.crowdsim.drivers.ArgsListenerModule.ArgsModifier;
import edu.cmu.cs.graphics.crowdsim.module.SubModule;
import edu.cmu.cs.graphics.crowdsim.serialize.SerializeGame.GameSerializationOut;

public class FileSerializationOut extends SubModule implements GameSerializationOut, ArgsModifier {
	private String fileName;
	
	@Override
	public OutputStream get() throws IOException {
		return new FileOutputStream(fileName);
	}

	@Override public void modifyOptions(Options o) {}

	@Override
	public void result(CommandLine cl) {
        switch(cl.getArgs().length){
            case 0: fileName = "../CrowdSimulation/Assets/core/corescript";
                break;
            default: fileName = cl.getArgs()[0];
        }
	}

}
