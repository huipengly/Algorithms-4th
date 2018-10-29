# [Programming Assignment 4: Boggle](http://coursera.cs.princeton.edu/algs4/assignments/boggle.html)

## 1. 题目阅读

编写程序去玩Boggle游戏。

**Boggle游戏** 一个文字游戏，他包含了16个骰子，骰子的每一面都印着一个字母。游戏最初，摇晃16个骰子，并且随机的分配到4*4的托盘中，只有骰子的顶面可以被看到。玩家根据以下规则，使用骰子建立有效的单词来竞争积分。

- 一个有效的单词必须通过横向，纵向，斜向相连。
- 一个有效的单词只能使用一个骰子一次。
- 一个有效的单词至少包括3个字母
- 一个有效的单词必须在字典中

例子见原网页

**评分** 
| word length | points |
| ----------- | ------ |
| 0–2         | 0      |
| 3–4         | 1      |
| 5           | 2      |
| 6           | 3      |
| 7           | 5      |
| 8+          | 11     |

**Qu特例** 在英语中，字母Q后总跟着u，所以，骰子上使用Qu取代Q，当组成单词时，这两个字母都需要被用到。当评分时，Qu被算作两个字母。

**你的任务** 编写一个Boggle求解器，使用给定的字典，找到给定Boggle板中所有有效的单词。使用下面的API实现一个不变的数据结构BoggleSolver。

<details>

```java
public class BoggleSolver
{
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary)

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
}
```

</details>

如何搜索、存储板中包含的单词和用来检查的字典的实现由你确定。

**板类型** 我们为BoggleBoard板提供一个不变的数据结构。他包含了从16个随机的骰子中创建板子，按照英语单词频率、通过一个文件或一个字母数组创建。包含了读取独立字母的方法，一个用来调试的打印board的方法。

**性能** 处理得当，可以在一秒内得到答案。

## 2. 问题分析

这个问题可以分为两点，第一是如何获得板中所有可能的字母连接，第二是如何判断字符串是有效的单词。

**获得所有字母连接** 第一反应是把这个板当作一个无向图，然后从每个字母开始进行深度优先搜索，然后判断。这里我有一个疑惑，就是深度优先搜索能否搜到所有的可能，因为这个方式只会通过一个节点一次。关于这个疑惑，checklist里面有一个剪枝的策略，就是判断当前的字母组合是否为一个有效的单词的prefix，若不是则剪枝。剪枝后，原先走过的点还可以走，只是不能走剪掉的路了。这样就能确保搜到所有的节点组合。

**判断字符串是否有效** 这里还需要判断prefix，考虑使用一个tst。

具体实现。

构造函数需要使用给定的字典新建一个tst。

分数计算，声明一个长度对应分数的数组，然后查表返回。

实现一个dfs搜索board，dfs返回时将点置为可走，并且参数多一个之前字母组成的string。每次dfs首先判断在字典是否有单词有prefix，然后向八个方向搜索。

第一遍性能太差了。

## 3. 性能优化

自己实现一个26way-trie.对于prefix判断，不需要像tst一样，返回迭代器，只需要迭代判断字母组合每一位，当未迭代完就有null，则无prefix；当迭代完还有下一位，则一定有符合的单词，也不需要再判断，~~并且可以判断当前字母组合是否为一个单词~~。

上面的优化还不够。

考虑保留每次搜索prefix到达的node，然后从这个node开始搜索。

这个优化还是不够，还是引入了bug。

20181029 ： 暂时不写了。96/100.之后再来写。