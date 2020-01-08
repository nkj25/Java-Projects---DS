package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		
		
		TrieNode theroot = new TrieNode(null, null, null);
		if(allWords.length == 0) return theroot;
		theroot.firstChild = new TrieNode(new Indexes(0, (short)(0), (short)(allWords[0].length() - 1)), null, null);
		
		TrieNode theptr = theroot.firstChild, lastSeen = theroot.firstChild;
		
		int indexWord = -1;
		int indexStart = -1;
		int indexEnd = -1;
		int sameUpto = -1;
		
		int index =1;
		while(index < allWords.length) {
			String theword = allWords[index];
			
			while(theptr != null) {
				indexWord = theptr.substr.wordIndex;
				indexEnd = theptr.substr.endIndex;
				indexStart = theptr.substr.startIndex;
				
				if(indexStart > theword.length()) {
					lastSeen = theptr;
					theptr = theptr.sibling;
					continue;
				}
				
				sameUpto = sameUntil(allWords[indexWord].substring(indexStart, indexEnd+1), theword.substring(indexStart)); 
				
				if(sameUpto != -1) sameUpto += indexStart;
				
				if(sameUpto == -1) {
					lastSeen = theptr;
					theptr = theptr.sibling;
				} else {
					if (sameUpto < indexEnd){
						lastSeen = theptr;
						break;
					} else if(sameUpto == indexEnd) {
						lastSeen = theptr;
						theptr = theptr.firstChild;
					}
					
				}
				
			}
		
			if(theptr == null) {
				Indexes indexes = new Indexes(index, (short)indexStart, (short)(theword.length()-1));
				lastSeen.sibling = new TrieNode(indexes, null, null);
			} else {
				
				Indexes indexCurrent = lastSeen.substr; 
				Indexes newidexwordCurrent = new Indexes(indexCurrent.wordIndex, (short)(sameUpto+1), indexCurrent.endIndex);
				indexCurrent.endIndex = (short)sameUpto; 
				TrieNode child1Current = lastSeen.firstChild; 
			
			
				lastSeen.firstChild = new TrieNode(newidexwordCurrent, null, null);
				lastSeen.firstChild.firstChild = child1Current;
				lastSeen.firstChild.sibling = new TrieNode(new Indexes((short)index, (short)(sameUpto+1), (short)(theword.length()-1)), null, null);
			}
			
			sameUpto = indexStart = indexEnd = indexWord = -1;
			theptr = lastSeen = theroot.firstChild;
			
			index++;
		}
		
		return theroot;
	}
	
	private static int sameUntil(String inTrie, String insert) {
		int until = 0;
		while(until < inTrie.length() && until < insert.length() && inTrie.charAt(until) == insert.charAt(until))
			until++;
		
		return (until-1);
	}

		
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		/** COMPLETE THIS METHOD **/
		
		if(root == null) return null;
		
		TrieNode theptr = root;
		ArrayList<TrieNode> matches = new ArrayList<>();
		
		
		while(theptr != null) {
			
			if(theptr.substr == null) 
				theptr = theptr.firstChild;
			
			String sOne = allWords[theptr.substr.wordIndex];
			String sTwo = sOne.substring(0, theptr.substr.endIndex+1);
			if(prefix.startsWith(sTwo) || sOne.startsWith(prefix)) {
				if(theptr.firstChild != null) { 
					matches.addAll(completionList(theptr.firstChild, allWords, prefix));
					theptr = theptr.sibling;
				} else { 
					matches.add(theptr);
					theptr = theptr.sibling;
				}
			} else {
				theptr = theptr.sibling;
			}
		}
		
		return matches;
	}

	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
