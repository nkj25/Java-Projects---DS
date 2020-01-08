package friends;

import java.util.ArrayList;

import structures.Queue;
import structures.Stack;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2.
	 * Chain is returned as a sequence of names starting with p1,
	 * and ending with p2. Each pair (n1,n2) of consecutive names in
	 * the returned chain is an edge in the graph.
	 * 
	 * @param g Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null if there is no
	 *         path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		
		/** COMPLETE THIS METHOD **/
		
		ArrayList<String> theshortest = new ArrayList<>();

		if (p1 == null || p2 == null || g == null) {
			return null;
		}
		else if (p1.length() == 0 || p2.length() == 0){
		   return null;
		}

		p2 = p2.toLowerCase();
		p1 = p1.toLowerCase();
		
		
		if (p1.equals(p2)){
		   theshortest.add(g.members[g.map.get(p1)].name);
		   return theshortest;
		}

		if (g.map.get(p2) == null || g.map.get(p1) == null){
		   return null;
		}

		
		Queue<Integer> thequeue = new Queue<>();
		int[] predict = new int[g.members.length];
		int[] totaldist = new int[g.members.length];
		
		boolean[] verticesvisit = new boolean[g.members.length];

		int i = 0;
		while (i < verticesvisit.length){
			predict[i] = -1;
			totaldist[i] = Integer.MAX_VALUE;
			verticesvisit[i] = false;
			i++;
		}

		//starting point
		int indexStart = g.map.get(p1);
		//Person personStart = g.members[indexStart];

		//distance to itself is 0
		totaldist[indexStart] = 0; 
		verticesvisit[indexStart] = true;

		thequeue.enqueue(indexStart); // start loop by enqueing 

		while (!thequeue.isEmpty()){
			int currvindx = 0;
			currvindx = thequeue.dequeue();
			Person currpers = g.members[currvindx];
			
			Friend ptr = currpers.first;
			while (ptr != null){
				int fnum = ptr.fnum;

				if (!verticesvisit[fnum]){ //not visited
					totaldist[fnum] = totaldist[currvindx] + 1;
					predict[fnum] = currvindx;
					verticesvisit[fnum] = true;
					thequeue.enqueue(fnum);
				}
				ptr = ptr.next;
			}
		}

		int marker = g.map.get(p2); // path from p2
		Stack<String> p2path = new Stack<>();
		
		
		if (!verticesvisit[marker])	{ // check for island - can't reach
		   return null;
		}


		while (marker != -1){
			p2path.push(g.members[marker].name);
			marker = predict[marker];
		}


		while (!p2path.isEmpty())
		{
		   theshortest.add(p2path.pop());
		}
		
		return theshortest;
	}
	
	
	
	
	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null if there is no student in the
	 *         given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		
		/** COMPLETE THIS METHOD **/
			
		ArrayList<ArrayList<String>> result = new ArrayList<>();
		
		if (school.length() == 0) {
		   return null;
		}
		if  (school == null || g == null) {
			return null;
		}
		
		school = school.toLowerCase();
	
		boolean[] checked = new boolean[g.members.length];
		int i = 0;
		while (i < checked.length) {
			checked[i] = false;
			i ++ ;
		}
	
		for (Person member : g.members) {
		   
		   if (member.school != null && !checked[g.map.get(member.name)] &&  member.school.equals(school)){
			   Queue<Integer> queue = new Queue<>();
			   ArrayList<String> clique = new ArrayList<>();
			
			   int indexStart = g.map.get(member.name);
			   checked[indexStart] = true;
			
			   queue.enqueue(indexStart);
			   clique.add(member.name);
			   while (!queue.isEmpty()){
		 	
				   int vertex = queue.dequeue(); 
				   Person theperson = g.members[vertex];
		 	
				   Friend ptr = theperson.first;
				   while ( ptr != null) {
		  	
					   int fnum = ptr.fnum;
					   Person friend = g.members[fnum];
		  
					   if (friend.school != null && !checked[fnum] && friend.school.equals(school)){
						   checked[fnum] = true;
						   queue.enqueue(fnum);
						   clique.add(g.members[fnum].name);
					   }
					   ptr = ptr.next;
				   }
			   }
			
			   result.add(clique);
		   	}
		}
		return result;
	}


	
	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null if there are no connectors.
	 */
	public static ArrayList<String> connectors(Graph g) {
		
		
/** COMPLETE THIS METHOD **/
		
		boolean[] checked = new boolean[g.members.length]; 
		               
		
		int[] end = new int[g.members.length];
		int[] thedfsnum = new int[g.members.length];
		
		ArrayList<String> result = new ArrayList<>();

		for (Person member : g.members){
		
		   if (!checked[g.map.get(member.name)]){
		   
			   thedfsnum = new int[g.members.length];
		   dfs(g.map.get(member.name), g.map.get(member.name), g, checked, thedfsnum, end, result);
		   }
		}

		// below error checking

		int i = 0;
		while (i < result.size()) {
			Friend ptr = g.members[g.map.get(result.get(i))].first;
			
			int count = 0;
			while (ptr != null) {
				ptr = ptr.next;
				count++;
			}

		   if (count == 1 || count == 0){
			   result.remove(i);
		   }
		   i++;
		}

		
		for (Person member : g.members) { // if no neighbor then it is a connector
		   
			if (!result.contains(g.members[member.first.fnum].name) && member.first.next == null) {
				result.add(g.members[member.first.fnum].name);
			}
		}

		return result;
		}

	// use to find size of dfsnum since counting up & passing back in recursion not good
	private static int arraySize(int[] array){
		int total = 0;
		int i = 0;
		while (i < array.length){
		   if (array[i] != 0) {
		    total++;
		   }
		   i++;
		}
		return total;
	}

	// use for recursive dfs

	
	private static void dfs(int vertex, int thestart, Graph g, boolean[] checked, int[] thedfsnum, int[] theback, ArrayList<String> result){
		
		Person theperson = g.members[vertex];
		checked[g.map.get(theperson.name)] = true;
		int ttlcount = arraySize(thedfsnum) + 1;

		if (theback[vertex] == 0 && thedfsnum[vertex] == 0){
		   thedfsnum[vertex] = ttlcount;
		   theback[vertex] = thedfsnum[vertex];
		}
		
		Friend ptr = theperson.first;
		while (ptr != null) { //neighbors
			
			if (!checked[ptr.fnum]) {
				dfs(ptr.fnum, thestart, g, checked, thedfsnum, theback, result);
				
				if (thedfsnum[vertex] > theback[ptr.fnum]) {
					theback[vertex] = Math.min(theback[vertex], theback[ptr.fnum]);
				} else {
					if (theback[ptr.fnum] == 1 && vertex == thestart && Math.abs(thedfsnum[vertex] - thedfsnum[ptr.fnum]) <= 1 && Math.abs(thedfsnum[vertex] - theback[ptr.fnum]) < 1) {
						continue;
					}

					if ((vertex != thestart || theback[ptr.fnum] == 1) && thedfsnum[vertex] <= theback[ptr.fnum]) { // if there is a connector
						if (!result.contains(g.members[vertex].name)){
							result.add(g.members[vertex].name);
						}
					}

				}
		   
			} else {
				theback[vertex] = Math.min(theback[vertex], thedfsnum[ptr.fnum]); // if checked already, update back
			}
			ptr = ptr.next;
		}
	
		return;
		
	}
}
