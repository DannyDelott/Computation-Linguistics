HashTagSegmenter
=======================

This Java class provides functionality for segmenting English words inside a Social Media Hashtags. 

For example:

    #anapple => [an apple]

    #running4president => [running 4 president]
    
##How it Works

HashTagSegmenter loads the word list into a Java Hashtable and checks the input text against the Hashtable key.  If the input text does not exist as a word in the Hashtable, it removes the last character from the input text and checks the new text against the Hashtable.  This process continues until all word segments are found and the input text is empty, or until no segments were found.  

##Demo
To use the tool, place the HashTagSegmenter.java class in your project and envoke it using:

    HashTagSegmenter hts = new HashTagSegmenter("path/to/wordlist.txt");
    List<String> segments = hts.segmentWordsInHashTaggedToken("#thehashtag");
    
    //print segments
    hts.printList(segments);

##Note

If multiple segmentation outputs are possible, the longest word from the left is selected. For example:

    #alwaysahead => [always a head] and [always ahead]
    
This would return

    [always ahead]
    

If valid segments cannot be found, it returns the the original string without the '#' for hashtags. For example:

    #slamdunkkkk => [slamdunkkkk]

##Word List
The segmentation is based on a modified version of the list of 109,583 English words and word variations available here:

  (http://www-01.sil.org/linguistics/wordlists/english/)

Modifications to remove common suffixes brought the list down to 109,551 words.  These suffixes can be found here:
    
  (http://en.wiktionary.org/wiki/Category:English_suffixes)

Numbers and decimals are segmented using regular expressions.
    
##Possible Issues

Segmenting by longest word from the left may cause trouble with singular nouns which pluralize with "-s", when the following word begins with an 's'.  For example:

    #diskspace => [disks pace]
    
Also, input text beginning with one-letter words may cause issues for the segmenter, either by incorrect segmentation or not segmenting it at all.  For example:

    #adinnertable => [ad inner table]
    #adentist => [adentist]
    
##Credits
HashTagSegmenter is based on Shyam Shankar's [Hashtag and URL Segmentation](https://github.com/shyam057cs/Machine-Learning/tree/master/Hashtag%20and%20Url%20Segmentation) script in Python.

Special thanks to Sheng Lundquist for his assistance with rewriting Shankar's script in Java and optimizing it with Hashtables.
