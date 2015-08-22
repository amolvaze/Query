// Date of program submission:- 12-01-2014
/* Program for Ranked Retrieval by asv130130 (Name:- AMOL VAZE )*/

// Code for Main Function

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class Main {
	// public static String DIRECTORY_PATH = "./Cranfield";
	public static String DIRECTORY_PATH;
	public static String STOP_WORD_FILE_NAME = "./stopwords";
	//public static String QueryFile = "./hw3.queries";
	public static String QueryFile = "./queries.txt";

	public static void main(String[] args) throws Exception {
		DIRECTORY_PATH = args[0];
		Set<String> stopWords = readStopWords(STOP_WORD_FILE_NAME);
		Build_Index ib = new Build_Index(stopWords);

		System.currentTimeMillis();
		Map<String, Build_Index.DictionaryEntry> uncompressedIndex = ib
				.buildIndex(DIRECTORY_PATH);
		System.currentTimeMillis();
		int average_Doc_Length = ib.getAvgDocLength();
		//System.out.println(" Average document length is: "+average_Doc_Length);

		Data_Encoding
				.getSizeOfUnCompressedIndex(uncompressedIndex);

		Map<String, Data_Encoding.DictionaryEntry> compressedIndex = Data_Encoding
				.createCompressedIndex(uncompressedIndex);
		Data_Encoding
				.getSizeOfCompressedIndex(compressedIndex);

		System.out.println("****************************Output***************************************");
		/*System.out.println("1. Time needed to build the index is = "
				+ (ending_time - starting_time) + " milliseconds");
		System.out.println("2. The size of the uncompressed index is = "
				+ lenghtWithoutCompression + " bytes");
		System.out.println("3. The size of the compressed index is  = "
				+ lenghtWithCompression + " bytes");
		System.out
				.println("4. The total number of inverted lists in the index are = "
						+ uncompressedIndex.size());*/
		Index_Query query = new Index_Query(uncompressedIndex,
				ib.documentDetails, stopWords, average_Doc_Length);
		List<String> querySet = readQueries(QueryFile);
		for (int i = 0; i < querySet.size(); i++) {
			if(i==0){
				continue;
			}
			System.out.println("Original query is as follows:- ");
			System.out.println("\nQuery" + (i) + " : " + querySet.get(i));
			query.process(querySet.get(i));
		}

	}

	private static List<String> readQueries(String filename) throws Exception {
		String data = new String(
				Files.readAllBytes(new File(filename).toPath()));
		String[] p = Pattern.compile("[Q0-9:]+").split(data);
		List<String> queries = new ArrayList<>();
		for (String part : p) {
			String query = part.trim().replaceAll("\\r\\n", " ");
			/*if (query.length() > 0) {
				queries.add(query);
			}*/
			queries.add(query);
		}
		return queries;
	}

	public static Set<String> readStopWords(String filename)
			throws FileNotFoundException {
		Set<String> stopWords = new HashSet<>();
		Scanner scanner = new Scanner(new File(filename));
		while (scanner.hasNext()) {
			stopWords.add(scanner.next());
		}
		scanner.close();
		return stopWords;
	}
}