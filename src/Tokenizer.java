// Date of program submission:- 12-01-2014
/* Program for Ranked Retrieval by asv130130 (Name:- AMOL VAZE )*/

// Code for Tokenizer

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Tokenizer {

	private ParseData fileParser;
	private Map<String, Integer> Index_table;
	private Porter porterStemmer;

	public Tokenizer() throws Exception {
		fileParser = new ParseData();
		porterStemmer = new Porter();
	}

	public Map<String, Integer> buildTermFreqTable(File file) throws Exception {
		Index_table = new HashMap<String, Integer>();
		if (file.isFile()) {
			String plainText = fileParser.parse(file);
			processPlainText(plainText);
		}
		return Index_table;
	}

	public Map<String, Integer> buildTermFreqTable(String plainText)
			throws Exception {
		Index_table = new HashMap<String, Integer>();
		processPlainText(plainText);
		return Index_table;
	}

	private void processPlainText(String plainText) throws Exception {
		plainText = plainText.replaceAll("[^\\w\\s-']+", " ").toLowerCase();
		Scanner scanner = new Scanner(plainText);
		while (scanner.hasNext()) {
			String token = scanner.next();
			processAndInsertToken(token);
		}
		scanner.close();
	}

	private void processAndInsertToken(String token) throws Exception {
		if (token.endsWith("'s")) {
			insertInToTable(token.replace("'s", ""));
		} else if (token.contains("-")) {
			splitAndInsertToken(token, "-");
		} else if (token.contains("_")) {
			splitAndInsertToken(token, "_");
		} else {
			insertInToTable(token);
		}

	}

	private void splitAndInsertToken(String token, String splitBy)
			throws Exception {
		String[] newTokens = token.split(splitBy);
		for (String newToken : newTokens) {
			insertInToTable(newToken);
		}
	}

	/*
	 * private void insertInToTable(String token) {
	 * 
	 * token = token.replaceAll("[']+", ""); if(token.length() > 0 ) token =
	 * porterStemmer.stripAffixes(token); if(token.length() > 0 ){ if
	 * (Index_table.containsKey(token)) { Index_table.put(token,
	 * Index_table.get(token) + 1); } else { Index_table.put(token, 1); } } }
	 */

	private void insertInToTable(String input_token) throws Exception {
		Set<String> stopWords = Main.readStopWords(Main.STOP_WORD_FILE_NAME);
		/*
		 * String Filename = "C:/Computer_Data/Workspace/Token/stem.bin";
		 * FileWriter f = new FileWriter(Filename, true); BufferedWriter bf =
		 * new BufferedWriter(f);
		 */
		input_token = input_token.replaceAll("[']+", "");
		if (input_token.length() > 0)
			input_token = porterStemmer.stripAffixes(input_token);
		if (input_token.length() > 0) {
			if (!(stopWords.contains(input_token))) {
				if (Index_table.containsKey(input_token)) {
					Index_table.put(input_token,
							Index_table.get(input_token) + 1);
				} else {
					Index_table.put(input_token, 1);
				}

				/*
				 * bf.write(input_token); bf.newLine();
				 */
			}
		}
		// bf.close();
	}

}