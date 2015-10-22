import
	edu.cmu.cs.graphics.crowdsim.serialize.*
	edu.cmu.cs.graphics.crowdsim.drivers.*
	edu.cmu.cs.graphics.crowdsim.ai.core.*
in
	Game: has
		ArgsListenerModule
		
		NewFrameListenerModule
		
		SerializeGame : has
			FileSerializationOut 
			SerializeListenerModule
		end
		
		BasicInitialStateGenerator
	end
end