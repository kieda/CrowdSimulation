using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using AssemblyCSharp;

public class FlagScript : MonoBehaviour {
	private AgentScript holder = null;
	private string groupName;
	private const float flagDist = 0.43f;

	// Use this for initialization
	void Start () {
		flagHoldingOffsetY = new Vector3 (
			0.0f, 1.1f, 0.0f);
		localPos = new Vector3 (0.0f, 1.0f, 0.0f);
	}
	public void init(string groupName, Dictionary<string, Texture2D> flagTextures){
		this.groupName = groupName;

		Color poleColor = Color.black;
		Color flagColor = Color.black;
		Texture2D flagTexture = flagTextures[groupName];
		switch (groupName) {
		case "red":
			flagColor = new Color(255.0f/255.0f, 121.0f/255.0f, 105.0f/255.0f);
			poleColor = new Color(247.0f/255.0f, 90.0f/255.0f, 65.0f/255.0f);
			break;
		case "blue":
			flagColor = new Color(105.0f/255.0f, 205.0f/255.0f, 255.0f/255.0f);
			poleColor = new Color(65.0f/255.0f, 217.0f/255.0f, 247.0f/255.0f);
			break;
		}
		transform.Find ("FlagFace").GetComponent<MeshRenderer> ().material.SetTexture("_OcclusionMap", flagTexture);
		
		GetComponent<MeshRenderer> ().material.color = poleColor;
		transform.Find ("FlagFace").GetComponent<MeshRenderer> ().material.color = flagColor;
	}
	private Vector3 flagHoldingOffsetY;
	private Vector3 localPos;
	
	void Update () {}

	public void ChangePosition(Vector2 v) {
		transform.position = new Vector3 (v.x, 1.0f, v.y);
	}
	public void PickUp(AgentScript holder){
		//1. translate slightly to look like player picked it up
		//2. apply rotation from agent's rot
		if (this.holder == null) {
			Vector3 dir = transform.position - holder.transform.position;
			dir.y = 0.0f;
			float gamma = flagDist / dir.magnitude; 
			//dir.x^2 + dir.z^2 // Sqrt * gamma = k
			// gamma = k / Sqrt(dir.x ^2 + dir.z^2)
			//dir' = dir.setY(0) * gamma;
			transform.position = holder.transform.position;
			transform.localPosition += gamma * dir + flagHoldingOffsetY;
			transform.parent = holder.transform;
			this.holder = holder;
		}
	}
	public void Drop(Vector2 position){
		if (this.holder != null) {
			this.holder = null;
			transform.parent = null;
			transform.localPosition -= flagHoldingOffsetY;
		}
		transform.position = new Vector3 (position.x, 1.0f, position.y);
	}
}
