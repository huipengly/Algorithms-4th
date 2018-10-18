/* *****************************************************************************
 *  Name:           WordNet.java
 *  Date:           20181018
 *  Description:    WordNet
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class WordNet {
    // synset用来存词对应的id，使用一个hashmap，key为单词，value为序号的合集
    private final HashMap<String, Bag<Integer>> synsets = new HashMap<>();
    // synsetsId用来存id对应的单词
    private final HashMap<Integer, String> synsetsId = new HashMap<>();
    // 上位词的关系存在sap的digraph里
    private SAP sap;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new java.lang.IllegalArgumentException("argument is null");
        }
        int v = readSynsets(new In(synsets));
        readHypernyms(new In(hypernyms), v);
    }

    // 返回顶点数目
    private int readSynsets(In in) {
        int v = 0;
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] info = line.split(",");            // 按照逗号切割，总共会切割成三份。0为id，1为noun，2为gloss
            int id = Integer.parseInt(info[0]);
            if (!synsetsId.containsKey(id)) {
                synsetsId.put(id, info[1]);             // 添加id对应的单词
            }
            for (String noun : info[1].split("\\s")) {  // 将noun按照空格分开处理，这里不是直接一个空格
                if (!synsets.containsKey(noun)) {       // 如果第一次出现，则需要新建一个bag
                    synsets.put(noun, new Bag<>());
                }
                synsets.get(noun).add(id);              // 添加单词对应的id
            }

            v = id > v ? id : v;                        // 用来记录最大的id
        }
        return v + 1;   // 顶点数目需要将序号加一
    }

    private void readHypernyms(In in, int v) {
        Digraph G = new Digraph(v);                     // 构造一个v个顶点的空digraph
        boolean[] out = new boolean[v];                 // 记录顶点是否有出去的通路，没有的顶点为根
        int rootNumber = v;                             // 记录顶点个数，每排除1个减1
        while (!in.isEmpty()) {
            String line = in.readLine();
            String[] info = line.split(",");            // 切割为n份，第一份为序号i，剩余为他的上位词
            int nounId = Integer.parseInt(info[0]);     // 当前处理的单词，将要记录他所有的上位词
            for (int i = 1; i != info.length; ++i) {
                G.addEdge(nounId, Integer.parseInt(info[i]));
            }
            if (!out[nounId] && info.length != 1) {     // 一行只有一个点，则这个点是没有增加出度的
                out[nounId] = true;
                --rootNumber;
            }
        }

        // 判断是否有环
        DirectedCycle directedCycle = new DirectedCycle(G);
        if (directedCycle.hasCycle()) {
            for (int i : directedCycle.cycle()) {
                StdOut.print(i + " ");
            }
            throw new java.lang.IllegalArgumentException("digraph has cycle.");
        }

        // 是否只有一个根
        if (rootNumber != 1) {
            throw new java.lang.IllegalArgumentException("not only one root");
        }

        sap = new SAP(G);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsets.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new java.lang.IllegalArgumentException("null argument");
        }
        return synsets.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException("argument is not a WordNet noun.");
        }
        return sap.length(synsets.get(nounA), synsets.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException("argument is not a WordNet noun.");
        }
        // 这里就是返回所有的词，还是按照原来空格分开的
        return synsetsId.get(sap.ancestor(synsets.get(nounA), synsets.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordnet = new WordNet("synsets6.txt", "hypernyms6TwoAncestors.txt");
        StdOut.print(wordnet.sap("b", "c"));
    }
}
