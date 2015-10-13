package edu.cmu.cs.graphics.crowdsim.util;

/**
 * This is the structure that generates the sight range for each agent.
 * 
 * This calculates the intervals of other agents in view in $O(n)$, where $n$
 * is the number of agents in view.
 * 
 * This structure relies on a few assumptions
 *   1) the intervals are inserted with respect to the distance they are from 
 *      the player. Interval $i$ will be behind all other intervals $j \lt i$.
 *   2) intervals in the back will have a smaller length than all other 
 *      intervals.
 * @author zkieda
 */
public class SightRangeTable {

}
