using UnityEngine;
using System.Collections;
using AssemblyCSharp;
using System;
using System.Collections.Generic;

public class AgentScript : MonoBehaviour {
	public Material swordMaterial;

	void Start () {}
	
	// Update is called once per frame
	void Update () {}

	public void SetTeamColor(Color c){
		Transform t = transform.Find ("Team");
		t.GetComponent<MeshRenderer> ().material.color = c;
	}
	public void ChangeMoodColor(Color c){
		Transform t = transform.Find ("MoodColor");
		t.GetComponent<MeshRenderer> ().material.color = c;
	}

	public void ChangePosition(Vector2 v){
		transform.position = new Vector3 (v.x, 0.5f, v.y);
	}

	public void ChangeDirection(double theta){
		transform.LookAt (new Vector3((float)Math.Cos(theta), 0f, (float)Math.Sin(theta)) + transform.position);
	}

	public void ChangeAttacking(bool b){
		GetComponent<Renderer> ().enabled = b;
	}
	public void SetDead() {
		Destroy (gameObject);
	}
	public void ChangeFace(Texture2D texture){
		transform.Find ("Face").GetComponent<MeshRenderer> ().material.mainTexture = texture;
	}
}
