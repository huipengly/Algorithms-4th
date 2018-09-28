# [Programming Assignment 5: Kd-Trees](http://coursera.cs.princeton.edu/algs4/assignments/kdtree.html)

## 1. 问题重述
这个问题其实就是[Kd-Trees](https://www.coursera.org/learn/algorithms-part1/lecture/Yionu/kd-trees)那节课讲的内容，按照老师的思路来实现就好了。

这个问题就是在一个平面上有N个点，需要实现两个功能。
    
1. 在给定一个平面内区域，让你寻找在区域中点的个数。
2. 给定一个点，寻找离这个点最近的M个点

## 2. 分析

题目要求用两种方法求解，一种暴力方法，一种2d-Tree。

### 2.1 暴力搜索

功能1，将每个点都和需要搜索的区域比较。

功能2，计算给定点和每个点的距离，拿个优先队列存M个。

点的插入、删除使用红黑树，时间就能满足O(logn)。两个功能就是O(N)。

### 2.2 构建2d-Tree

就按照课程讲的做就是了