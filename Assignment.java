/*Given a dictionary of words and a start and an end word find the shortest transformation sequences from start word to end word modifying only one character one letter at a time and each resulting word should exist in dictionary*/

/*Approach-If we only wanted to find the shortest length of sequence we would only use vanilla BFS but here we want to list all the sequences with shortest length,thus DFS followed by BFS looks like a good approach.*/ 

/*TIme complexity-The time complexity of the given solution is O((26^N/2)*N*log(N))*/


import java.util.*;
import java.io.*;

/**
 * @author      Jasdeep Singh Chhabra <jasdeepchhabra94@gmail.com>
                 
 */

public class test3
{
	public static void main(String args[])
	{
		

	 	HashSet<String> dictionary=new HashSet<String>();
		Scanner sc=new Scanner(System.in);
	 	String filePath="dictionary.csv";
		
		
		makeDictionary(filePath,dictionary);
		
		while(true)
		{
	 		//Start word
			System.out.print("Enter the start word ");
	 		String startWord=sc.nextLine();

	 		//End word
			System.out.print("Enter the end word ");
	 		String endWord=sc.nextLine();

			//ArrayList of ArrayList to store the shortest paths
			ArrayList<ArrayList<String>> allPaths = new ArrayList<ArrayList<String>>();  
	
			//HashMap to keep record of visited
			Map<String, ArrayList<Format>> visited = new HashMap<String, ArrayList<Format>>(); 

			//finding optimum path length and parent(BFS)
			optimize(startWord, endWord, dictionary, visited);  

			//listing all possible shortest paths(DFS)
			listPaths(endWord, startWord, visited, new LinkedList<String>(), allPaths);  

			if(allPaths.isEmpty())
			{
				System.out.println("No such sequence found");

			}
			else
			{
				System.out.println(allPaths);
			}
		}
	}



   
/**
 * Function to find the shortest sequence length using BFS.
 *<p>
 *In this function we use a Format Queue as a standard BFS queue                            
 * to keep track of new words formed that are part of dictionary.
 * Elements are popped from this queue and combinations are found
 * by varying characters of the popped element.If this new word 
 *exists in dictionary then it is popped in queue.
 *</p>
 *
 * @param  startWord  Starting word          
 * @param  endWord    Ending word
 * @param  dictionary HashSet dictionary of words
 * @param  visited    to keep record of visited words
 */

	private static void optimize(String startWord, String endWord, HashSet<String> dictionary,Map<String, 							ArrayList<Format>> visited) 
	{  
		// A queue to keep words that are in dictionary and also obtained from previous word(BFS queue)  
		Queue<Format> queue = new LinkedList<Format>();
		dictionary.remove(startWord);  
   		queue.add(new Format(startWord, 1));  
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
         				endLevel=isEndWord(s,endWord,endLevel,format);  
					//if the new word is in dictionary
         				isInDictionary(s,startWord,format,dictionary,visited,queue);  
       				}  
     			}  
   		} 
		dictionary.add(startWord);
		return ;  
	}  
   
 /**
 * Function to find the shortest path using DFS.
 * @param  startWord  Starting word          (3)
 * @param  paths      to store sequence in form of array lists 
 * @param  dictionary HashSet dictionary of words
 * @param  visited    to keep record of visited words
 */

	private static void listPaths(String str, String startWord, Map<String, ArrayList<Format>> visited,  							LinkedList<String> path, ArrayList<ArrayList<String>> paths) 
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


/**
 * Function to create dictionary of words.
 *
 * @param  filePath	Path of csv file           
 * @param  set    	set of words(dictionary)
 
 */



	public static void makeDictionary(String filePath,HashSet<String> set)
	{
		String csvSplitBy="\n";
		String line="";
		try
		{
			BufferedReader br=new BufferedReader(new FileReader(filePath));
	
			while((line=br.readLine())!=null)
			{
				String[] words=line.split(csvSplitBy);
				for(String s :words)
				{	
					set.add(s);
				}
         

			}
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}	
	
	}


/**
 * Function to find if the newly formed word is the endWord.
 * @param  word       Word to be checked         
 * @param  endWord    Ending word
 * @param  endLevel   length of sequence
 * @param  format     instance of Format class

 */


	public static int isEndWord(String word,String endWord,int endLevel,Format format)
	{
		if (word.equals(endWord)) 
		{  
           		if (endLevel == 0) 
	   		{	
				endLevel = format.distance + 1;  
	   		}
       		}  
		return endLevel; 
	}


/**
 * Function to find if the newly formed word is in the dictionary.
 * @param  word       Word to be checked          
 * @param  startWord  Starting word
 * @param  dictionary dict of words
 * @param  format     instance of Format class
 * @param  visited    to keep record of visited words
 * @param  queue	the BFS queue
 */



	public static void isInDictionary(String word,String startWord,Format format,HashSet<String> dictionary,Map<String, 						ArrayList<Format>> visited,Queue<Format> queue)
	{
		if (dictionary.contains(word) && !word.equals(startWord)) 
		{  
           		ArrayList<Format> pres = visited.get(word);  
           		if (pres == null) 
			{  
             			// enqueue unvisited word  
             			queue.add(new Format(word, format.distance+1));  
             			pres = new ArrayList<Format>();  
             			visited.put(word, pres);  
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


class Format 
{  
	String word;  
	int distance;  
	public Format(String w, int d) 
	{  
		word = w; 
		distance = d;  
	}  
}

