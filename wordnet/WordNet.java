/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.HashMap;

public class WordNet {

    private final int V;
    private final HashMap<String, Bag<Integer>> nouns;
    private final String[] synsets;
    private final SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null) throw new IllegalArgumentException();

        // Build synsets

        nouns = new HashMap<String, Bag<Integer>>();

        String[] synsetsLines = (new In(synsets)).readAllLines();
        V = synsetsLines.length;
        this.synsets = new String[V];

        for (int i = 0; i < V; i++) {
            String synset = synsetsLines[i].split(",")[1];
            for (String syn : synset.split(" ")) {
                if (!nouns.containsKey(syn)) nouns.put(syn, new Bag<Integer>());
                nouns.get(syn).add(i);
            }
            this.synsets[i] = synset;
        }

        // Build Digraph

        Digraph wordnet = new Digraph(V);

        String[] hypernymsLines = (new In(hypernyms)).readAllLines();

        for (String line : hypernymsLines) {
            String[] fields = line.split(",");

            int syn = Integer.parseInt(fields[0]);

            for (int i = 1; i < fields.length; i++) {
                int hypernym = Integer.parseInt(fields[i]);
                wordnet.addEdge(syn, hypernym);
            }
        }

        // Verify DAG

        if (!(new Topological(wordnet)).hasOrder()) throw new IllegalArgumentException();

        sap = new SAP(wordnet);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException();
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB
    public int distance(String nounA, String nounB) {
        if ((nounA == null) || (nounB == null)) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path
    public String sap(String nounA, String nounB) {
        if ((nounA == null) || (nounB == null)) throw new IllegalArgumentException();
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException();

        return synsets[sap.ancestor(nouns.get(nounA), nouns.get(nounB))];
    }

    // do unit testing of this class
    public static void main(String[] args) {
        System.out.println("-----Constructor-----");
        WordNet wordnet = new WordNet("txt/synsets15.txt", "txt/hypernyms15Path.txt");

        System.out.println(wordnet.distance("a", "b"));
    }
}