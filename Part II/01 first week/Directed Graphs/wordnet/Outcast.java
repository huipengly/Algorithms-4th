/* *****************************************************************************
 *  Name:           Outcast.java
 *  Date:           20181018
 *  Description:    计算一组词中，距离其他词最远的词
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {
    private final WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    private int distToOthers(String noun, String[] nouns) {     // 计算和其他词的距离和
        int d = 0;
        for (String n : nouns) {
            d += wordNet.distance(noun, n);
        }
        return d;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        String outcast = null;
        int maxDist = -1;
        for (String s : nouns) {                            // 计算每个词距离其他词的距离和，求出最大的
            int dist = distToOthers(s, nouns);
            if (dist > maxDist) {
                outcast = s;
                maxDist = dist;
            }
        }
        return outcast;
    }

    // see test client below
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
