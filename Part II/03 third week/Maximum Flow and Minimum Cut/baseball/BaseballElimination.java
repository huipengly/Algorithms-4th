/* *****************************************************************************
 *  Name:           BaseballElimination.java
 *  Date:           20181025
 *  Description:    棒球淘汰问题
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {
    private final int numberOfTeam;
    private final HashMap<String, Integer> teamMap;
    private final HashMap<Integer, String> numberMap;     // 队名和编号的map
    private final int[] w, l, r;
    private final int[][] g;
    private FordFulkerson fordFulkerson;
    private FlowNetwork flowNetwork;
    private final int s, t, teamStart;                  // 起止点的编号
    private int restGame;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numberOfTeam = Integer.parseInt(in.readLine());
        teamMap = new HashMap<>();
        numberMap = new HashMap<>();
        w = new int[numberOfTeam];
        l = new int[numberOfTeam];
        r = new int[numberOfTeam];
        g = new int[numberOfTeam][numberOfTeam];
        for (int i = 0; i != numberOfTeam; ++i) {
            String line = in.readLine();
            String[] teamInfo = line.trim().split("\\s+");  // 去除字符串首位空格
            teamMap.put(teamInfo[0], i);
            numberMap.put(i, teamInfo[0]);
            w[i] = Integer.parseInt(teamInfo[1]);
            l[i] = Integer.parseInt(teamInfo[2]);
            r[i] = Integer.parseInt(teamInfo[3]);
            for (int j = 0; j != numberOfTeam; ++j) {
                g[i][j] = Integer.parseInt(teamInfo[4 + j]);
            }
        }
        s = 0;
        // 起止点两个 + c(n, 2) + n。因为从0开始编号，所以减1
        teamStart = 1 + (numberOfTeam - 1) * (numberOfTeam - 2) / 2;
        t = 1 + teamStart + (numberOfTeam - 1) - 1;
    }

    // numberOfTeam of teams
    public int numberOfTeams() {
        return numberOfTeam;
    }

    // all teams
    public Iterable<String> teams() {
        return teamMap.keySet();
    }

    // numberOfTeam of wins for given team
    public int wins(String team) {
        if (!teamMap.containsKey(team)) {
            throw new java.lang.IllegalArgumentException("don't have " + team);
        }
        return w[teamMap.get(team)];
    }

    // numberOfTeam of losses for given team
    public int losses(String team) {
        if (!teamMap.containsKey(team)) {
            throw new java.lang.IllegalArgumentException("don't have " + team);
        }
        return l[teamMap.get(team)];
    }

    // numberOfTeam of remaining games for given team
    public int remaining(String team) {
        if (!teamMap.containsKey(team)) {
            throw new java.lang.IllegalArgumentException("don't have " + team);
        }
        return r[teamMap.get(team)];
    }

    // numberOfTeam of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teamMap.containsKey(team1)) {
            throw new java.lang.IllegalArgumentException("don't have " + team1);
        }
        if (!teamMap.containsKey(team2)) {
            throw new java.lang.IllegalArgumentException("don't have " + team2);
        }
        return g[teamMap.get(team1)][teamMap.get(team2)];
    }

    private int isTrivialElimination(int x) {
        int maxWin = w[x] + r[x];
        for (int i = 0; i != numberOfTeam; ++i) {
            if (maxWin < w[i]) {
                return i;
            }
        }
        return -1;
    }

    private void buildFlowNetWork(int x) {
        flowNetwork = new FlowNetwork(t + 1);   // 共有t + 1个顶点
        restGame = 0;
        // 连接起点和比赛，比赛和队伍
        // 遍历所有比赛，如果存在则添加边。注意要跳过x
        int counter = 1;
        for (int i = 0; i != numberOfTeam - 1; ++i) {
            for (int j = i + 1; j != numberOfTeam - 1; ++j) {       // j从i+1开始，组合不重复
                int t1 = i < x ? i : i + 1;     // 表示不包括x的队伍，如果编号在x之前，则队伍编号就是循环的数。不然就要+1，跳过x
                int t2 = j < x ? j : j + 1;
                if (g[t1][t2] != 0) {
                    FlowEdge edge = new FlowEdge(s, counter, g[t1][t2]);     // 起点和比赛的边
                    restGame += g[t1][t2];
                    flowNetwork.addEdge(edge);
                    // 比赛和队伍的边
                    edge = new FlowEdge(counter, i + teamStart, Double.POSITIVE_INFINITY);
                    flowNetwork.addEdge(edge);
                    edge = new FlowEdge(counter, j + teamStart, Double.POSITIVE_INFINITY);
                    flowNetwork.addEdge(edge);
                }
                ++counter;
            }
        }
        // 队伍和终点
        counter = 0;
        for (int i = 0; i != numberOfTeam - 1; ++i) {
            int team = i < x ? i : i + 1;
            FlowEdge edge = new FlowEdge(counter + teamStart, t, w[x] + r[x] - w[team]);
            flowNetwork.addEdge(edge);
            ++counter;
        }
        // StdOut.print(flowNetwork.toString());
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teamMap.containsKey(team)) {
            throw new java.lang.IllegalArgumentException("don't have " + team);
        }
        int x = teamMap.get(team);
        if (isTrivialElimination(x) != -1) {
            return true;
        }
        buildFlowNetWork(x);
        fordFulkerson = new FordFulkerson(flowNetwork, s, t);
        if (fordFulkerson.value() < restGame) {
            return true;
        }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teamMap.containsKey(team)) {
            throw new java.lang.IllegalArgumentException("don't have " + team);
        }
        int x = teamMap.get(team);
        SET<String> subsetR = new SET<>();
        int trivialElimination = isTrivialElimination(x);
        if (trivialElimination != -1) {
            subsetR.add(numberMap.get(trivialElimination));
            return subsetR;
        }
        buildFlowNetWork(x);
        fordFulkerson = new FordFulkerson(flowNetwork, s, t);
        int number = 1;
        for (int i = 0; i != numberOfTeam - 1; ++i) {
            for (int j = i + 1; j != numberOfTeam - 1; ++j) {
                int t1 = i < x ? i : i + 1;
                int t2 = j < x ? j : j + 1;
                if (fordFulkerson.inCut(number)) {
                    subsetR.add(numberMap.get(t1));
                    subsetR.add(numberMap.get(t2));
                }
                ++number;
            }
        }
        return subsetR.isEmpty() ? null : subsetR;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            // String team = "Ghaddafi";
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
