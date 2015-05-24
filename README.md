# CrowdSimulation
Modular crowd simulation framework with independent, autonomous agents that convey emotions. 
See ./writeup/final-writeup.pdf for a full description of the project, as well as the mathematical
theory behind the framework.


## Directory Structure
./CrowdSimulation contains a Unity project which displays the front-end side of the action. 
This merely reads from an input file to display the front-end features of each agent

./CrowdSimulationBackend contains a java project which creates a simulation and writes 
the front-end information to a file.

The source for the backend is in ./CrowdSimulationBackend/src
The main file is in ./CrowdSimulationBackend/src/edu/cmu/cs464/p3/drivers/Main
All classes regarding the agent's ai is in the ai package. Various utilites are in the util package.

In terms of ai, the ai.core package contains classes regarding an agent's interaction 
with the game, and the creation and organization of agents. This is outlined in Section 3
in our paper.

The ai.internal package contains classes regarding the internal state of an agent. 
This is defined in Sections 4, 5 in our paper.

The ai.perception package contains classes regarding the perception of an agent.
This is defined in Section 6 in our paper.

The ai.action package contains classes regarding the actions an agent may take.
This is defined in Section 7 in our paper.
