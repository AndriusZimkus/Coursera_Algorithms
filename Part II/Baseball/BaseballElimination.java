import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class BaseballElimination {

    private final int numberOfTeams;
    private final ArrayList<String> teamNames;
    private final int[] winsCount;
    private final int[] loss;
    private final int[] left;
    private final int[][] h2h;


    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {

        In in = new In(filename);

        // Read file, set variables
        numberOfTeams = in.readInt();
        teamNames = new ArrayList<>();
        winsCount = new int[numberOfTeams];
        loss = new int[numberOfTeams];
        left = new int[numberOfTeams];
        h2h = new int[numberOfTeams][numberOfTeams];

        for (int i = 0; i < numberOfTeams; i++) {
            teamNames.add(in.readString());
            winsCount[i] = in.readInt();
            loss[i] = in.readInt();
            left[i] = in.readInt();

            for (int j = 0; j < numberOfTeams; j++) {
                h2h[i][j] = in.readInt();
            }
        }


    }

    // number of teams
    public int numberOfTeams() {
        return numberOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teamNames;
    }

    // number of wins for given team
    public int wins(String team) {
        if (!teamNames.contains(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        return winsCount[teamNames.indexOf(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teamNames.contains(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        return loss[teamNames.indexOf(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teamNames.contains(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        return left[teamNames.indexOf(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teamNames.contains(team1) || !teamNames.contains(team2)) {
            throw new IllegalArgumentException("invalid team");
        }
        return h2h[teamNames.indexOf(team1)][teamNames.indexOf(team2)];
    }

    private FlowNetwork teamFN(String team) {
        if (!teamNames.contains(team)) {
            throw new IllegalArgumentException("invalid team");
        }
        int teamIndex = teamNames.indexOf(team);
        int teamVertexCount = numberOfTeams - 1;
        int h2hMatchupCount = ((numberOfTeams - 1) * (numberOfTeams - 2)) / 2;
        int vertexCount = 2 + teamVertexCount + h2hMatchupCount;

        FlowNetwork divisionFlow = new FlowNetwork(vertexCount);
        int currentH2HVertexNumber = 1;

        for (int j = 0; j < numberOfTeams - 1; j++) {
            if (j == teamIndex) {
                continue;
            }
            for (int k = j + 1; k < numberOfTeams; k++) {
                if (k == teamIndex) {
                    continue;
                }

                // connect source to matchup verteces
                FlowEdge currentEdge = new FlowEdge(0, currentH2HVertexNumber, h2h[j][k]);
                divisionFlow.addEdge(currentEdge);


                // connect matchup to team verteces
                int firstTeamIndexCorrected = j;
                if (j > teamIndex) {
                    firstTeamIndexCorrected--;
                }
                int secondTeamIndexCorrected = k;
                if (k > teamIndex) {
                    secondTeamIndexCorrected--;
                }

                int firstTeamVertexNumber = vertexCount - 1 - (teamVertexCount - firstTeamIndexCorrected);
                int secondTeamVertexNumber = vertexCount - 1 - (teamVertexCount - secondTeamIndexCorrected);

                currentEdge = new FlowEdge(currentH2HVertexNumber, firstTeamVertexNumber, Double.POSITIVE_INFINITY);
                divisionFlow.addEdge(currentEdge);

                currentEdge = new FlowEdge(currentH2HVertexNumber, secondTeamVertexNumber, Double.POSITIVE_INFINITY);
                divisionFlow.addEdge(currentEdge);

                currentH2HVertexNumber++;

            }
        }

        for (int i = 0; i < numberOfTeams; i++) {
            if (i == teamIndex) {
                continue;
            }

            int teamIndexCorrected = i;
            if (teamIndexCorrected > teamIndex) {
                teamIndexCorrected--;
            }
            int teamVertexNumber = vertexCount - 1 - (teamVertexCount - teamIndexCorrected);

            int winDifferential = winsCount[teamIndex] + left[teamIndex] - winsCount[i];

            if (winDifferential < 0) {
                winDifferential = 0;
            }
            // connect team to sink vertex
            if (winDifferential >= 0) {
                FlowEdge currentEdge = new FlowEdge(teamVertexNumber, vertexCount - 1, winDifferential);
                divisionFlow.addEdge(currentEdge);
            }


        }

        return divisionFlow;
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teamNames.contains(team)) {
            throw new IllegalArgumentException("invalid team");
        }

        // trivial elimination
        for (int i = 0; i < numberOfTeams; i++) {
            if (wins(team) + remaining(team) < winsCount[i]) {
                return true;
            }
        }


        // non trivial elimination
        FlowNetwork teamFlowNetwork = teamFN(team);

        // side effect usage
        new FordFulkerson(teamFlowNetwork, 0, teamFlowNetwork.V() - 1);

        for (FlowEdge x : teamFlowNetwork.adj(0)) {
            if (x.flow() != x.capacity()) {
                return true;
            }
        }

        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {

        if (!isEliminated(team)) {
            return null;
        }
        ArrayList<String> teams = new ArrayList<>();

        // trivial elimination
        for (int i = 0; i < numberOfTeams; i++) {
            if (wins(team) + remaining(team) < winsCount[i]) {
                teams.add(teamNames.get(i));
                return teams;
            }
        }


        FlowNetwork teamFlowNetwork = teamFN(team);

        FordFulkerson teamFN = new FordFulkerson(teamFlowNetwork, 0, teamFlowNetwork.V() - 1);

        int teamIndex = teamNames.indexOf(team);
        int teamVertexCount = numberOfTeams - 1;
        int firstTeamVertex = teamFlowNetwork.V() - 1 - teamVertexCount;
//        StdOut.println(teamFlowNetwork);
        for (int i = firstTeamVertex; i < teamFlowNetwork.V(); i++) {
            if (teamFN.inCut(i)) {
                int currentTeamIndex = numberOfTeams - (teamFlowNetwork.V() - i);
                if (currentTeamIndex >= teamIndex) {
                    currentTeamIndex++;
                }
                String teamName = teamNames.get(currentTeamIndex);
                teams.add(teamName);
            }

        }
//        StdOut.println(teamFlowNetwork);
//        StdOut.println(teamFN.value());

        return teams;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);

        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }

}


