//represents the capture the flag game


// make "objective" DFA --
//  each transition will have an associated payoff value
//  example for capture the flag : 
//   node A : grab enemy flag
//   node B : bring enemy flag back to base

// when the flag carryer has been detroyed while on node B, 
// the DFA retransitions to node A. 

// we also associate other tasks in this DFA. 
// for example, if our flag is taken, we consider a great fear 
// and risk in letting the enemy escape. 

// if our team is taking the enemy flag, we find a greater payoff in 
// helping the flag carryer. 

// for this scenario, each side has a DFA with five nodes
//    A - neither flags taken
//    B - our flag taken, enemy not
//    C - our flag not tken, enemy flag taken
//    D - both flags taken
//    E - exit state (one of the flags made it to the other side)

//of course, for this game, the additional effects are just cumulative based on 
//the status of the flag
//  

// have high level idea of everything
// need to implement specifics
// 