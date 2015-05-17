using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using AssemblyCSharp;

//this is the core script that is used to prepare the input XML files 
//for reading, which hands an input stream to be read by each agent script
public class CoreScript : MonoBehaviour {
	private Dictionary<int, AgentScript> players;
	private AgentStream input;
	public AgentScript prefab;
	private float prevTime;

	// Use this for initialization
	void Start () {
		players = new Dictionary<int, AgentScript>();
		input =  new AgentStream(new System.IO.StreamReader("./Assets/Core/corescript"));
		input.Start ();
		prevTime = -1.0f;
	}

	// Update is called once per frame
	void Update () {
		if (prevTime < 0f || Time.time - prevTime  > 1.0f / 15.0f) {
			prevTime = Time.time;
			if (input.HasNext ()) {
				input.getFrameUpdate (prefab) (players);
			} else {
				input.Close ();
			}
		}
	}
}
