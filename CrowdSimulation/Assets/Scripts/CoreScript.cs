using UnityEngine;
using System;
using System.Collections;
using System.Collections.Generic;
using AssemblyCSharp;

//this is the core script that is used to prepare the input XML files 
//for reading, which hands an input stream to be read by each agent script
public class CoreScript : MonoBehaviour {
	private Dictionary<int, AgentScript> players;
	private Dictionary<string, FlagScript> flags;

	private GameStream input;
	public AgentScript agentPrefab;
	public FlagScript flagPrefab;

	private float prevTime;
	private AgentAndFlagTuple agentsAndFlags;
	// Use this for initialization
	void Start () {
		players = new Dictionary<int, AgentScript>();
		flags = new Dictionary<string, FlagScript> ();
		agentsAndFlags = new AgentAndFlagTuple (players, flags);
		input =  new GameStream(new System.IO.StreamReader("./Assets/Core/corescript"), players, flags);
		input.Start ();
		prevTime = -1.0f;
	}
	private float fps = 1.0f / 15.0f;
	// Update is called once per frame
	void Update () {
		if (prevTime < 0f || Time.time - prevTime  > fps) {
			prevTime = Time.time;
			if (input.HasNext ()) {
				input.getFrameUpdate (agentPrefab, flagPrefab) (agentsAndFlags);
			} else {
				input.Close ();
			}
		}
	}
}

/**
 * note : tuples not working/found in the system for some reason.
 * This is our quick fix
 */
public class AgentAndFlagTuple{
	private Dictionary<int, AgentScript> agents;
	private Dictionary<string, FlagScript> flags;
	public AgentAndFlagTuple(Dictionary<int, AgentScript> agents, Dictionary<string, FlagScript> flags){
		this.agents = agents;
		this.flags = flags;
	}
	public Dictionary<int, AgentScript> getAgents(){
		return agents;
	}
	public Dictionary<string, FlagScript> getFlags(){
		return flags;
	}
}