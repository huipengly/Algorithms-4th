# [Programming Assignment 2: Seam Carving](http://coursera.cs.princeton.edu/algs4/assignments/seam.html)

## 1. 题目阅读

这个题是课上讲过的，每次删除能量最小的一条路径。

Seam Carving是一种图像处理方法。在裁剪图片尺寸的同时，保留图像中最有趣的特征。横向缩减时，删除横向上的最短路径，纵向裁剪时，删除纵向上的最短路径。

- 符号，图像的像素从0开始计数。
- 能力计算，计算每个点的能量，越高越重要。这里要实现双梯度能量方程。