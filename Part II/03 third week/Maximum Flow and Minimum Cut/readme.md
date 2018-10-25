# [Programming Assignment 3: Baseball Elimination](http://coursera.cs.princeton.edu/algs4/assignments/baseball.html)

## 1. 问题阅读
一个赛季的某时间，给定队伍的排名信息，判断队伍是否已经失去了竞争第一可能。

**baseball elimination problem.** 有n个队伍，在赛季的某一个时间，队伍i的胜w[i]、负;[i]、剩余场次r[i]、和队伍j的剩余场次g[i][j]。当一个队伍无法在数学上获得第一，那么他就被淘汰。这个问题的目标是计算那些队伍被彻底淘汰。为了简化，假设到最后队伍没有相同的胜场并且每场比赛都按计划进行。

**A maxflow formulation.** 将棒球淘汰问题转换为一个最大流问题求解。考虑两种情况。
- 无关紧要的淘汰。当一个队伍最大能赢得局数小于现在某个队伍已经获胜的局数，则被淘汰。
- 不平凡的淘汰。除了上面的情况，我们构造一个流网络，求解最大流问题。在这个网络里，可行的积分流量对应剩余的时间表比赛。里面有顶点对应除了x意外的队伍，和剩余的不包含x的比赛。直观上，流中的每一个单元对应一个余下的比赛。当流从s到t，穿过一个代表队伍i和j进行比赛的顶点，之后穿过i和j其中一个顶点，表示赢得比赛的队伍。

更加确切的说，流网络包含了下面的边和容量。

1. 我们连接一个人造的顶点s到每一个比赛顶点i-j，并且将它的容量存储到g[i][j]。如果一个流使用这条边所有的容量g[i][j]，我们认为这个表示进行了所有的比赛，并在顶点i，j之间分配了胜负。
2. 我们将顶点i-j连接到相对应的两个队伍顶点以确保其中一个队伍拥有胜利。我们不需要限制这种边的容量。
3. 最后，我们将每一个队伍顶点连接到一个人造的接收顶点t。我们想知道，是否存在某种方式，当结束所有比赛时，队伍x最终赢得和队伍i同样数量的比赛。因为队伍x最多可以赢得w[x] + r[x]的比赛局数，我们通过将从队伍到接收顶点的容量设置为w[x] + r[x] - w[i]的方法，来阻止队伍i赢得超过这个数。

在找到的最大流中，如果每个从s出发的边都是满的，那么说明按照这种方法进行剩余的比赛，不会有队伍的获胜局数超过队伍x。如果有边不满，那么就不存在一种脚本，使的x可以成为第一。在下面的流网络中，Detroit是队伍x = 4.

[!flow network](resources/baseball.png)

**最小切分将告诉我们什么？** 通过求解最大流问题，我们可以判断一个给定的队伍是否在数学上被淘汰。我们乐意使用非技术的方法来解释队伍被淘汰的原因。Detroit如果赢得所有剩余的比赛将获得76胜。剩余的队伍总共获得了278胜，他们之间还余下27场比赛。那么最终剩余的队伍将获得305胜，平均每个队伍获得76.25胜。所以一定有一个队伍的获胜场次为77，因此Detroit被淘汰。

实际上，当一个队伍在数学上被淘汰，将存在一个使人信服的淘汰证明，设R为其他队伍的一个子集，你总是可以使用最小切分在流网络中找到一个子集R。虽然我们求解了一个最大流/最小切分问题，但是一旦找到了这个自己R，那么这个争论就只需要用到小学代数。

**任务** 实现一个不可变数据结构`BaseballElimination`表示一个赛区，并且绝对那个队伍被数学上淘汰了。

<details>

```java
public BaseballElimination(String filename)                    // create a baseball division from given filename in format specified below
public              int numberOfTeams()                        // number of teams
public Iterable<String> teams()                                // all teams
public              int wins(String team)                      // number of wins for given team
public              int losses(String team)                    // number of losses for given team
public              int remaining(String team)                 // number of remaining games for given team
public              int against(String team1, String team2)    // number of remaining games between team1 and team2
public          boolean isEliminated(String team)              // is given team eliminated?
public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
```

</details>

**输入格式** 每一个队有一行，队伍数目在第一行。每一行包含了队伍的名字（内部不会包含空格），获胜数目，失利数目，剩余比赛数目，和赛区每个队伍的剩余比赛数目。

你可以假设队伍数目`n >= 1`，并且输入格式按照规定，且内部没有矛盾。队伍剩余场次不需要等于和其余队伍剩余比赛的和。因为还有一些队伍没有被提供。

**输出格式** 使用提供的格式。

## 2. 题目分析

### 构造函数
构造函数需要读取文件，按照string读取一行，然后使用空格切分string。构造w[i]、l[i]、r[i]、g[i][j]、队伍名的set。

### 判断是否被淘汰
输入一个队伍x，首先判断是否被无关紧要的淘汰。若无法判断，则使用不平凡的淘汰判断。

**无关紧要的判断**，只需要使用`w[i] + r[i]`和其余的`w[j]`比较。

**不平凡的淘汰**，构造流网络，~~网络顶点数等于`2 + 和队伍x还剩余比赛的队伍组合数量 + 与队伍x还剩与比赛的队伍数量`~~网路顶点数等于`2 + 除队伍x以外队伍组合数量 + 除队伍x以外的队伍数量`。顶点s到比赛队伍组合的容量为比赛数目，若无比赛，则不建立边；队伍组合到队伍的容量为正无穷，队伍到顶点t的容量为x最大获胜容量减去已经获胜数量。

在构造的流网络中，查找最大流。在最大流中，若s到比赛队伍的流量均为满的，则未被淘汰。否则被淘汰。

### 输出决定x被淘汰的子集R
与上面判断是否被淘汰相同，只是不查找最大流，找到从顶点s开始的最小切分。遍历队伍组合是否在最小切分中。

**关于遍历队伍组合** 两个for嵌套，内层for从上一层+1开始。队伍组合按照这种顺序从1开始标号。（顶点0为s顶点）