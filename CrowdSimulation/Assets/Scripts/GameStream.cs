using System;
using System.IO;
using UnityEngine;
using System.Collections.Generic;

namespace AssemblyCSharp
{
	public class AgentStream {
		private StreamReader input;
		public static Texture2D LoadTextureFromFile(string fileName){
			Texture2D t2d = new Texture2D (2, 2);
			byte[] fileData = File.ReadAllBytes (fileName);
			t2d.LoadImage (fileData);
			return t2d;
		}

		private Dictionary<string, Texture2D> textures;

		public void Start(){
			Dictionary<string, string> textureMap = new Dictionary<string, string> ()
			{
				{"angry", "./Assets/Faces/Angry.png"},
				{"ashamed", "./Assets/Faces/Ashamed.png"}, 
				{"confused", "./Assets/Faces/Confused.png"},
				{"disconcerted", "./Assets/Faces/Disconcerted.png"},
				{"furious", "./Assets/Faces/Furious.png"},
				{"happy", "./Assets/Faces/Happy.png"},
				{"sad", "./Assets/Faces/Sad.png"},
				{"shocked", "./Assets/Faces/Shocked.png"},
				{"sick", "./Assets/Faces/Sick.png"},
				{"skeptical", "./Assets/Faces/Skeptical.png"}
			};
			textures = new Dictionary<string, Texture2D>();
			foreach(var entry in textureMap){
				Texture2D t2d = LoadTextureFromFile(entry.Value);
				t2d.alphaIsTransparency = true;
				t2d.wrapMode = TextureWrapMode.Clamp;
				textures.Add (entry.Key, t2d);
			}
		}

		public AgentStream (StreamReader stream) {
			this.input = stream;
		}

		//an action that takes a list of our agents, and updates each one
		public Action<Dictionary<int, AgentScript>> getFrameUpdate(AgentScript gameObject) {
			Action<Dictionary<int, AgentScript>> res = dict => {};
			string str;
			while ((str = input.ReadLine ()) != null) {
				if(str.Equals("(endframe)")){
					return res;
				}else {
					string[] vals = str.Split('=');
					int id = int.Parse(vals[0]);
					string[] changes = vals[1].Split(';');
					Action<AgentScript> del = agent => {};

					for(int i = 0; i < changes.Length; i++){
						string[] keyVal = changes[i].Split(':'); 

						switch(keyVal[0]){
						case "mood":{
							string[] rgb = keyVal[1].Split(',');
							float r = float.Parse(rgb[0]),
							    g = float.Parse(rgb[1]),
								b = float.Parse(rgb[2]);
							Color c = new Color(r, g, b);
							//add a delegate in each case that changes the 
							//agent's appearance
							del += a => {
								a.ChangeMoodColor(c);
							};
							break;}
						case "position":{
							string[] xy = keyVal[1].Split(',');
							float x = float.Parse(xy[0]), 
								y = float.Parse(xy[1]);
							Vector2 pos = new Vector2(x, y);
							del += a => {
								a.ChangePosition(pos);
							};
							break;
						}
						case "direction":{
							string[] xy = keyVal[1].Split(',');
							float x = float.Parse(xy[0]), 
								y = float.Parse(xy[1]);
							Vector2 dir = new Vector2(x, y);
							del += a => {
								a.ChangeDirection(dir);
							};
							break;
						}
						case "face":{
							string faceType = keyVal[1];
							del += a => a.ChangeFace(textures[faceType]);
							break;
						}
						case "attacking":{
							bool isAttacking = bool.Parse(keyVal[1]);
							del += a => {
								a.ChangeAttacking(isAttacking);
							};
							break;
						}
						case "dead":{
							bool isDead = bool.Parse(keyVal[1]);
							if(isDead){
								del +=  a => a.SetDead();
							}
							break;
						}
						case "team":{
							switch(keyVal[1]){
							case "red":
								del += a => a.SetTeamColor(Color.red);
								break;
							case "blue":
								del += a => a.SetTeamColor(Color.blue);
								break;
							}
							break;
						}
						}
					}
					//function that takes our dictionary, finds the correct id, 
					//then applies the delegate fn to all relevant ones
					res += (dict => {
						if(!dict.ContainsKey(id)){
							AgentScript ag = AgentScript.Instantiate (gameObject) as AgentScript;
							//initialize a new AgentScript, place into dictionary
							dict.Add(id, ag);
						}
						del(dict[id]);
					});
				}
			}
			return null;
		}
		public bool HasNext(){
			try{
				return input.Peek () >= 0;
			} catch(Exception e){ 
				return false;
			}
		}

		public void Close(){
			input.Close();
		}
	}
}

