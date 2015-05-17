using UnityEngine;
using System.Collections;

public class CameraScript : MonoBehaviour {

	// Use this for initialization
	void Start () {}

	float flySpeed = .3f;
	// Update is called once per frame
	void Update () {
		if (Input.GetKey(KeyCode.W)) {
			float y = transform.position.y;
			transform.Translate(Vector3.forward* flySpeed);
			transform.position = new Vector3(transform.position.x, y, transform.position.z);
		}
		if (Input.GetKey (KeyCode.S)) {
			float y = transform.position.y;
			transform.Translate(Vector3.back * flySpeed);
			transform.position = new Vector3(transform.position.x, y, transform.position.z);
		}
		if (Input.GetKey(KeyCode.A)) {
			float y = transform.position.y;
			transform.Translate(Vector3.left * flySpeed);
			transform.position = new Vector3(transform.position.x, y, transform.position.z);
		}
		if (Input.GetKey (KeyCode.D)) {
			float y = transform.position.y;
			transform.Translate(Vector3.right * flySpeed);
			transform.position = new Vector3(transform.position.x, y, transform.position.z);
		}
		if (Input.GetKey(KeyCode.E)) {
			transform.Translate(Vector3.up * flySpeed * .5f);
		}
		else if (Input.GetKey(KeyCode.Q)) {	
			transform.Translate(Vector3.down * flySpeed * .5f);
		}

		if(Input.GetKey(KeyCode.DownArrow)) {
			transform.Rotate(new Vector3(1.0f, 0f,0f));
		}
		if(Input.GetKey(KeyCode.UpArrow)) {
			transform.Rotate(new Vector3(-1.0f, 0f,0f));
		}
		if(Input.GetKey(KeyCode.LeftArrow)) {
			transform.RotateAround(transform.position, Vector3.up, -1.0f);
		}
		if(Input.GetKey(KeyCode.RightArrow)) {
			transform.RotateAround(transform.position, Vector3.up, 1.0f);
		}
	}
}
