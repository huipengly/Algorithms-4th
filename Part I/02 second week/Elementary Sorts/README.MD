## My Confusion 
QUESTION:

How many compares does shellsort (using the 3x+1 increment sequence) make on an input array that is *already sorted? 

ANSWER:

linearithmic (look at table above) EXPLANATION: Since successive increment values of h differ by at least a a factor of 3, there are ~log_3(N) increment values. For each increment value h, the array is already h-sorted so it will make ~ N compares.

confusion:

每一次的比较应该是N-h次吧，所以认为是近似N，~N?