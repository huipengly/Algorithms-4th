# [Programming Assignment 1: WordNet](http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html)

## 1. 题目阅读

### WordNet定义
WordNet是指一个包含唯一根的有向无环图，图中每一组词表示同一集合，每一条边v→w表示w是v的上位词。和树不同的地方是，每一个子节点可以有许多父节点。

### 输入格式

 - 同义词表

文件中每行包含一次同义名词。首先是序号；然后是词，用空格分开。若为词组，则使用下划线连接词组。最后是同义名词的注释

```36,AND_circuit AND_gate,a circuit in a computer that fires only when all of its inputs fire ``` 

 - 上位词表

文件中包含序号i的上位词j。若有多个上位词，则使用空格分隔。

```34,47569,48084```

### WordNet数据类型
需要实现
 - 构造函数，通过同义词表和上位词表
 - 迭代器
 - 判断词是否在WordNet里
 - 计算两个词的最短祖先路径距离
 - 计算两个词的最短祖先路径

最短祖先路径是指两个点v、w到达相同点距离和最短的点x。

另外，还可以给定两个集合A、B，两个集合中的各自拥有一个点v、w到达相同点距离和最短的点，对比第一种，还需要从集合中找出合适的v、w。

### SAP数据类型（最短祖先路径）
需要实现
 - 构造函数
 - 求两点的最短祖先编号、距离
 - 求两集合的最短祖先编号、距离

### Outcast detection
给定一串词，给出哪个词和其他词最不相关

## 2. 题目分析
按照我认为的实现顺序分析
### SAP实现
构造函数：构造digraph

祖先编号，路径：
寻找两个点v、w到达相同点距离的最短点x。

第一步，对v、w进行BFS搜索，找到从v、w到其他点的最短距离。BFS是O(E + V)

第二步，将v、w到相同点的距离相加，若其中一个点不能到达，则这个点的最短祖先距离为-1。在这个遍历过程中，找到相加距离最短的点则为最短祖先距离。O(V)

整个算法性能：O(E + V)

卡在这里了一会，主要是读题目的checklist时候看到bfs当成深度优先了，然后思想就被禁锢dfs了

### WordNet实现
构造函数：文件读取，使用java的string分割。然后构造一个digraph。

迭代器：返回digraph的迭代器

最短祖先路径、距离：调用sap

### Outcast detection
构造函数：构造digraph

outcast：计算每个输入词和其他词的距离，输出最大的