# [Programming Assignment 1: WordNet](http://coursera.cs.princeton.edu/algs4/assignments/wordnet.html)

## 1. 题目分析

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
 - 计算两个词的最短距离
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