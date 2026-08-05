import java.util.*;

class Solution {
    public List<Integer> remainingMethods(int n, int k, int[][] invocations) {

        // Build graph
        List<Integer>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] edge : invocations) {
            graph[edge[0]].add(edge[1]);
        }

        // Step 1: Find all suspicious methods
        boolean[] suspicious = new boolean[n];
        Queue<Integer> queue = new ArrayDeque<>();

        queue.offer(k);
        suspicious[k] = true;

        while (!queue.isEmpty()) {
            int current = queue.poll();

            for (int next : graph[current]) {
                if (!suspicious[next]) {
                    suspicious[next] = true;
                    queue.offer(next);
                }
            }
        }

        // Step 2: Check whether any safe method invokes a suspicious one
        for (int[] edge : invocations) {
            int from = edge[0];
            int to = edge[1];

            if (!suspicious[from] && suspicious[to]) {
                List<Integer> ans = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    ans.add(i);
                }
                return ans;
            }
        }

        // Step 3: Return remaining methods
        List<Integer> remaining = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            if (!suspicious[i]) {
                remaining.add(i);
            }
        }

        return remaining;
    }
}