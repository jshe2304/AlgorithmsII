/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordnet;

    public Outcast(WordNet wordnet) {
        this.wordnet = wordnet;
    }        // constructor takes a WordNet object

    public String outcast(String[] nouns) {
        int max = 0;
        String outcast = "";

        for (int i = 0; i < nouns.length; i++) {
            int sum = 0;

            for (int j = 0; j < i; j++) sum += wordnet.distance(nouns[i], nouns[j]);
            for (int j = i; j < nouns.length; j++) sum += wordnet.distance(nouns[i], nouns[j]);

            if (sum > max) {
                max = sum;
                outcast = nouns[i];
            }
        }

        return outcast;
    }  // given an array of WordNet nouns, return an outcast

    public static void main(String[] args) {
        WordNet wordnet = new WordNet("txt/synsets.txt", "txt/hypernyms.txt");
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    } // see test client below
}
