/*Given a dictionary of words and a start and an end word find the shortest transformation sequences from start word to end word modifying only one character one letter at a time and each resulting word should exist in dictionary*/

/*Approach-If we only wanted to find the shortest length of sequence we would only use vanilla BFS but here we want to list all the sequences with shortest length,thus DFS followed by BFS looks like a good approach.*/ 

import java.util.*;
public class Assignment
{
public static void main(String args[])
	{
		//A set of words(dictionary)

	 	HashSet<String> dictionary=new HashSet<String>();
	 	dictionary.add("hot");
	 	dictionary.add("dot");
	 	dictionary.add("dog");
	 	dictionary.add("lot");
	 	dictionary.add("log");
	 	dictionary.add("hog");

	 	//Start word
	 	String startWord="hit";

	 	//End word
	 	String endWord="lag";

		//ArrayList of ArrayList to store the shortest paths
		ArrayList<ArrayList<String>> allPaths = new ArrayList<ArrayList<String>>();  
	
		//HashMap to keep record of visited
		Map<String, ArrayList<Format>> visited = new HashMap<String, ArrayList<Format>>(); 

		//finding optimum path length and parent(BFS)
		optimize(startWord, endWord, dictionary, visited);  

		//listing all possible shortest paths(DFS)
		listPaths(endWord, startWord, visited, new LinkedList<String>(), allPaths);  


		System.out.println(allPaths);
	}



   
 //Using BFS to find shortest transformation sequence length and also the parent of corresponding word in dictionary
 private static void optimize(String startWord, String endWord, HashSet<String> dictionary,Map<String, ArrayList<Format>> visited) 
	{  
		// A queue to keep words that are in dictionary and also obtained from previous word(BFS queue)  
		Queue<Format> queue = new LinkedList<Format>();  
   		queue.add(new Format(startWord, 1));  
   		dictionary.add(endWord);  
   		int endLevel = 0;  
   		while (!queue.isEmpty()) 
		{  
     			Format format = queue.poll();  
     			if (endLevel > 0 && format.distance >= endLevel) 
			{
				break;
			}  
     		// find new words by replacing characters individually  
     			for (int i=0; i<format.word.length(); ++i) 
			{  
       				StringBuilder sb = new StringBuilder(format.word);  
       				char original = sb.charAt(i);  
       				for (char c='a'; c<='z'; ++c) 
				{  
         				if (c == original) 
					{
						continue;  
					}
         				sb.setCharAt(i, c);  
        			 	String s = sb.toString();  
        				// if the new word is the end word  
         				if (s.equals(endWord)) 
					{  
           					if (endLevel == 0) 
	   					{	
							endLevel = format.distance + 1;  
	   					}
         				}  
					//if the new word is in dictionary
         				if (dictionary.contains(s) && !s.equals(startWord)) 
					{  
           				ArrayList<Format> pres = visited.get(s);  
           				if (pres == null) 
					{  
             					// enqueue unvisited word  
             					queue.add(new Format(s, format.distance+1));  
             					pres = new ArrayList<Format>();  
             					visited.put(s, pres);  
             					pres.add(format);  
           				} 
					else if (pres.get(0).distance == format.distance)
					{  
             					// Avoiding circular graph  
             					pres.add(format);  
           				}  
         				}  
       				}  
     			}  
   		}  
		return ;  
	}  
   
 /* Use DFS to back trace all paths from end to start. */  
 private static void listPaths(String str, String startWord, Map<String, ArrayList<Format>> visited,  LinkedList<String> path, ArrayList<ArrayList<String>> paths) 
	{  
   
		path.add(0, str);  
   		if (str.equals(startWord)) 
		{  
     			ArrayList<String> p = new ArrayList<String>(path);  
     			paths.add(p);  
   		} 
		else 
		{  
     			ArrayList<Format> prev = visited.get(str);  
     			if (prev != null)
			{  
       				for (Format pre : prev)
				{  
         				listPaths(pre.word, startWord, visited, path, paths);  
       				}  
     			}  
   		}  
		path.remove(0);  
 	}  
}
class Format 
{  
	String word;  
	int distance;  
	public Format(String w, int d) 
	{  
		word = w; distance = d;  
	}  
}  
