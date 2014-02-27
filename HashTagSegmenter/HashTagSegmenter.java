/*
 * HashTagSegmenter.java
 *
 * Author: DANNY DELOTT <DANNYDELOTT@gmail.com>
 * Licensed under GPL Version 3
 *
 * A Java class to segment words in a hash tag
*/


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.math.NumberUtils;

public class HashTagSegmenter {

	private String wordListLocation;
	private Hashtable<String, String> wordHashTable = new Hashtable<String, String>();

	/* CONSTRUCTOR */
	HashTagSegmenter(String wll) throws IOException {
		wordListLocation = wll;
		wordHashTable = getWordListHashTable();

	}

	/*
	 * Segments the words of the token (eg: "#iwant2eatfood")
	 */
	List<String> segmentWordsInHashTaggedToken(String text) {

		// holds crude segments from number split
		List<String> crudeSegments = new ArrayList<String>();

		// holds completely segmented tokens
		List<String> tempSegments = new ArrayList<String>();
		List<String> finalSegments = new ArrayList<String>();

		// sets the token to lower case
		StringBuilder tokenText = new StringBuilder(text.toLowerCase());

		// checks for hashtag
		if (tokenText.charAt(0) == '#') {

			// deletes the hashtag
			tokenText = tokenText.deleteCharAt(0);

			// splits the token text into crude segments when a number exists
			// eg: "iwant2eatfood" -> ['iwant', '2', 'eatfood']
			Matcher m = Pattern.compile("[\\d.]+|\\D+").matcher(tokenText);
			while (m.find()) {
				crudeSegments.add(m.group());
			}

			// segments items from crude segments list
			// eg: temp[0] = ['iwant'] ->
			// segments = ['i','want']
			for (int i = 0; i < crudeSegments.size(); i++) {

				// if crude item is a number, add it to the segments list
				if (NumberUtils.isNumber(crudeSegments.get(i))) {
					finalSegments.add(crudeSegments.get(i));
				} else {

					// if crude item is not a number, segment and add each
					// new item to the segments list
					tempSegments = getSegments(crudeSegments.get(i));

					// adds new segments list to final segments
					if (tempSegments != null) {
						for (int j = 0; j < tempSegments.size(); j++) {
							finalSegments.add(tempSegments.get(j));
						}
					} else {
						// adds crude segment to list if it cannot be segmented
						finalSegments.add(crudeSegments.get(i));
					}
				}
			}

		}

		return finalSegments;
	}

	private List<String> getSegments(String text) {
		List<String> segments = new ArrayList<String>();

		// begins segmenting the text from the beginning
		segments = segment(text);

		if (segments != null) {
			return segments;
		} else {
			return null;
		}
	}

	private List<String> segment(String text) {
		List<String> segments = new ArrayList<String>();
		String currentSegment = "";
		StringBuilder trimmedText = new StringBuilder(text);
		StringBuilder finalText = new StringBuilder(text);
		boolean foundLastWord = true;

		while (trimmedText.length() >= 0) {

			// returns text if text is empty or the last word is not found
			if ((trimmedText.length() == 0 && segments.size() == 0)
					|| foundLastWord == false) {
				segments.clear();
				segments.add(text);
				return segments;
			}
			// returns the segments if crude segment text has no more characters
			else if (trimmedText.length() == 0 && segments.size() > 0) {
				return segments;
			}
			// segments the crude segment text if not empty
			else if (trimmedText.length() > 0) {

				// adds text to segments list if text exists in Hashtable
				if (wordHashTable.containsKey(trimmedText.toString())) {

					// stores the segment for easy removal
					currentSegment = trimmedText.toString();

					// newText only contains the key, adds it to list
					segments.add(currentSegment);

					// deletes the current segment from front of finalText
					finalText = new StringBuilder(finalText.delete(0,
							currentSegment.length()));

					// resets newText
					trimmedText = new StringBuilder(finalText.toString());

				}
				// trims last letter of crude segment text if key doesn't exist
				else {

					trimmedText = trimmedText
							.deleteCharAt(trimmedText.length() - 1);

					if (trimmedText.length() == 0) {
						foundLastWord = false;
					}
				}

			}

		}

		// returns null if unable to segment
		return null;

	}

	/* Returns the word list specified in the constructor */
	private Hashtable<String, String> getWordListHashTable() throws IOException {
		Hashtable<String, String> tempWordList = new Hashtable<String, String>();
		File file = new File(wordListLocation);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line;
		while ((line = br.readLine()) != null) {
			// process the line.
			tempWordList.put(line, line);

		}
		br.close();

		return tempWordList;
	}

	void printList(List<String> list) {
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
		}
	}

}
