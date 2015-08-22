// Date of program submission:- 12-01-2014
/* Program for Ranked Retrieval by asv130130 (Name:- AMOL VAZE )*/

// Code for query processing

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public class Index_Query {
	Map<String, Build_Index.DictionaryEntry> dictionary;
	Set<String> stop_wrds;
	Tokenizer t;
	Map<Integer, Build_Index.DocumentInfo> doc_info;
	double avg_document_len;

	public Index_Query(Map<String, Build_Index.DictionaryEntry> index,
			Map<Integer, Build_Index.DocumentInfo> doc_Detail,
			Set<String> stopWords, int avgDoclength) throws Exception {
		this.dictionary = index;
		this.stop_wrds = stopWords;
		this.doc_info = doc_Detail;
		this.t = new Tokenizer();
		this.avg_document_len = avgDoclength;
	}

	public void process(String query) throws Exception {
		Map<String, Integer> termFreqTable = t.buildTermFreqTable(query);
		removeStopWords(termFreqTable);
		Map<Integer, Double> W1_table = new HashMap<>();
		Map<Integer, Double> W2_table = new HashMap<>();
		@SuppressWarnings("unused")
		int queryLenght = get_query_len(termFreqTable);
		int collectionSize = doc_info.size();
		for (String queryTerm : termFreqTable.keySet()) {
			Build_Index.DictionaryEntry dictEntry = dictionary.get(queryTerm);
			if (dictEntry == null) {
				continue;
			}

			int docFreq = dictEntry.docFrequency;
			for (Build_Index.PostingEntry postingEntry : dictEntry.postingList) {
				int termFreq = postingEntry.frequency;
				int maxTermFreq = (int) doc_info.get(postingEntry.docID).maxFreq;
				int docLenght = doc_info.get(postingEntry.docID).docLength
						.intValue();

				double w1 = W1(termFreq, maxTermFreq, docFreq, collectionSize);
				double w2 = W2(termFreq, docLenght, avg_document_len, docFreq,
						collectionSize);

				Insert_weights(W1_table, postingEntry.docID, w1);
				Insert_weights(W2_table, postingEntry.docID, w2);
			}
		}

		System.out.print("Query Words After Processing/(Stemmed Queries): ");
		for (String queryTerm : termFreqTable.keySet()) {
			System.out.print(queryTerm+ " ");
		}
		System.out.println();
		System.out.println("\nTop 10 Documents Returned By W1 can be given as follows:- ");
		Display_Top_10(W1_table);
		System.out.println("\nTop 10 Documents Returned By W2 can nbe given as follows:- ");
		Display_Top_10(W2_table);
	}

	private int get_query_len(Map<String, Integer> termFreqTable) {
		int len = 0;
		for (String queryTerm : termFreqTable.keySet()) {
			len += termFreqTable.get(queryTerm);
		}
		return len;
	}

	private void Insert_weights(Map<Integer, Double> wt_table, int docID, double ch) {
		if (wt_table.get(docID) == null) {
			wt_table.put(docID, ch);
			return;
		}
		wt_table.put(docID, ch + wt_table.get(docID));
	}

	public double W1(int termFreq, int maxTermFreq, int docFreq,
			int collec_size) {
		double x = 0;
		try {
			x = (0.4 + 0.6 * Math.log(termFreq + 0.5)
					/ Math.log(maxTermFreq + 1.0))
					* (Math.log(collec_size / docFreq) / Math
							.log(collec_size));
		} catch (Exception e) {
			x = 0;
		}
		return x;
	}

	public double W2(int termFreq, int document_len, double avgDoclength,
			int docFreq, int collec_eize) {
		double y = 0;
		try {
			y = (0.4 + 0.6
					* (termFreq / (termFreq + 0.5 + 1.5 * (document_len / avgDoclength)))
					* Math.log(collec_eize / docFreq)
					/ Math.log(collec_eize));
		} catch (Exception e) {
			y = 0;
		}
		return y;
	}

	private void removeStopWords(Map<String, Integer> termFreqTable) {
		Iterator<String> iterator = termFreqTable.keySet().iterator();
		while (iterator.hasNext()) {
			if (stop_wrds.contains(iterator.next())) {
				iterator.remove();
			}
		}
	}

	private void Display_Top_10(Map<Integer, Double> w1_table) {
		TreeSet<Entry<Integer, Double>> sort_SET = new TreeSet<Entry<Integer, Double>>(
				new ValueComparator());
		sort_SET.addAll(w1_table.entrySet());
		//System.out.println("Rank : " + "\t Weight   " + "    : " + " Document Identifier"+ " : " + "Document Name" + " : " + " Headline");
		System.out.println("Rank : " + "\t Score   " + "    : " + " DocID"
				+ " Headline");
		Iterator<Entry<Integer, Double>> iterator = sort_SET.iterator();
		for (int i = 0; i <10 && iterator.hasNext(); i++) {
			Entry<Integer, Double> entry = iterator.next();
			Build_Index.DocumentInfo documentInfo = doc_info
					.get(entry.getKey());
			System.out.println((i+1) + " : " + entry.getValue() + " :      "
					+ documentInfo.docId + " : " 
					+ documentInfo.title);
		}

	}

	class ValueComparator implements Comparator<Entry<Integer, Double>> {
		@Override
		public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
			if (o1.getValue() < o2.getValue()) {
				return 1;
			}
			return -1;
		}
	}
}