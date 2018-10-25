/* *****************************************************************************
 *  Name:           BaseballElimination.java
 *  Date:           20181025
 *  Description:    棒球淘汰问题
 *  Author:         huipengly
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {
    private final int number;
    // private final String[] team;
    private final HashMap<String, Integer> teamMap;     // 队名和编号的map
    private final int[] w, l, r;
    private final int g[][];

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        number = Integer.parseInt(in.readLine());
        // team = new String[number];
        teamMap = new HashMap<>();
        w = new int[number];
        l = new int[number];
        r = new int[number];
        g = new int[number][number];
        for (int i = 0; i != number; ++i) {
            String line = in.readLine();
            String[] teamInfo = line.split("\\s+");
            // team[i] = teamInfo[0];
            teamMap.put(teamInfo[0], i);
            w[i] = Integer.parseInt(teamInfo[1]);
            l[i] = Integer.parseInt(teamInfo[2]);
            r[i] = Integer.parseInt(teamInfo[3]);
            for (int j = 0; j != number; ++j) {
                g[i][j] = Integer.parseInt(teamInfo[4 + j]);
            }
        }
    }

    // number of teams
    public int numberOfTeams() {
        return number;
    }

    // all teams
    public Iterable<String> teams() {
        return teamMap.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        return w[teamMap.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        return l[teamMap.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return r[teamMap.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return g[teamMap.get(team1)][teamMap.get(team2)];
    }

    private boolean isTrivialElimination(int teamNumber) {
        int maxWin = w[teamNumber] + r[teamNumber];
        for (int i = 0; i != number; ++i) {
            if (maxWin < w[i]) {
                return false;
            }
        }
        return true;
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (isTrivialElimination(teamMap.get(team))) {
            return false;
        }
        return true;
    }

    // // subset R of teams that eliminates given team; null if not eliminated
    // public Iterable<String> certificateOfElimination(String team) {
    //
    // }

    public static void main(String[] args) {
        BaseballElimination baseballElimination = new BaseballElimination(args[0]);
        if (baseballElimination.isEliminated("Montreal")) {
            StdOut.print("Montreal is eliminated.");
        }
    }
}
