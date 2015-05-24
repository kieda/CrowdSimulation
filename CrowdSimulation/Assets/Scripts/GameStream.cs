using System;
using System.IO;
using UnityEngine;
using System.Collections.Generic;

namespace AssemblyCSharp
{
	public class GameStream {
		private StreamReader input;
		public static Texture2D LoadTextureFromFile(string fileName){
			Texture2D t2d = new Texture2D (2, 2);
			byte[] fileData = File.ReadAllBytes (fileName);
			t2d.LoadImage (fileData);
			return t2d;
		}

		private Dictionary<string, Texture2D> textures;
		private Dictionary<string, Texture2D> flagTextures;

		public static Dictionary<string, Texture2D> loadTexs(Dictionary<string, string> textureMap){
			Dictionary<string, Texture2D> texs = new Dictionary<string, Texture2D>();
			foreach(var entry in textureMap){
				Texture2D t2d = LoadTextureFromFile(entry.Value);
				t2d.alphaIsTransparency = true;
				t2d.wrapMode = TextureWrapMode.Clamp;
				texs.Add (entry.Key, t2d);
			}
			return texs;
		}
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
			textures = loadTexs (textureMap);

			flagTextures = GameStream.loadTexs (new Dictionary<string, string> (){
				{"red", "./Assets/Standard Assets/CrossPlatformInput/Sprites/ButtonBrakeUpSprite.png"},
				{"blue", "./Assets/Standard Assets/CrossPlatformInput/Sprites/ButtonAcceleratorUpSprite.png"}
			});
		}

		private AgentScript currentFlagHolder;
		private Dictionary<int, AgentScript> agents;
		private Dictionary<string, FlagScript> flags;

		public GameStream (StreamReader stream, Dictionary<int, AgentScript> agents, Dictionary<string, FlagScript> flags) {
			this.input = stream;
			this.agents = agents;
			this.flags = flags;
		}

		private Vector2 parseVec2(String val){
			string[] xy = val.Split(',');
			float x = float.Parse(xy[0]), 
			y = float.Parse(xy[1]);
			Vector2 pos = new Vector2(x, y);
			return pos;
		}
		//an action that takes a list of our agents, and updates each one

		// getFrameUpdate : AgentScript x FlagScript -> Dictionary<int, AgentScript> -> ()
		public Action<AgentAndFlagTuple> getFrameUpdate(AgentScript agentPrefab, FlagScript flagPrefab) {
			Action<Dictionary<int, AgentScript>> res = dict => {};
			Action<Dictionary<string, FlagScript>> resFlag = dict => {};
			string str;
			while ((str = input.ReadLine ()) != null) {
				if(str.StartsWith("flag-")) {
					string flagGroup = str.Substring("flag-".Length, str.IndexOf('=') - "flag-".Length);
					string[] changes = (str.Split('=')[1]).Split(';');
					Action<FlagScript> del = flag => {};
					for(int i = 0; i < changes.Length; i++) {
						string[] keyVal = changes[i].Split(':');
						switch(keyVal[0]) {
							case "position":{
								Vector2 pos = parseVec2(keyVal[1]);
								del += flag => {
									flag.ChangePosition(pos);
								};
								break;
							}
							case "drop":{
									Vector2 pos = parseVec2(keyVal[1]);
									del += flag => {
										flag.Drop(pos);
									};
									break;
								}
							case "pickedUp":{
								int pickedUp = int.Parse(keyVal[1]);
								del += flag => {flag.PickUp(agents[pickedUp]);};
								break;
							}
						}
					}
					resFlag += (dict => {
						if(!dict.ContainsKey(flagGroup)){
							FlagScript fl = FlagScript.Instantiate (flagPrefab) as FlagScript;
							fl.init(flagGroup, flagTextures);
							dict.Add(flagGroup, fl);
						}
						del(dict[flagGroup]);
					});
				} else if(str.Equals("(endframe)")){
					return tuple => {
						res (tuple.getAgents());
						resFlag (tuple.getFlags());
					};
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
							break;
						}
						case "position":{
							Vector2 pos = parseVec2(keyVal[1]);
							del += a => {
								a.ChangePosition(pos);
							};
							break;
						}
						case "direction":{
							double dir = Double.Parse(keyVal[1]);
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
							AgentScript ag = AgentScript.Instantiate (agentPrefab) as AgentScript;
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

