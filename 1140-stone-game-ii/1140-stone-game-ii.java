class Solution {

    private int[][] dp;
    private int[] suffix;
    private int n;
    public int stoneGameII(int[] piles) {
        n = piles.length;
        dp = new int[n][n + 1];
        suffix = new int[n + 1];
        for (int i = n - 1; i >= 0; i--) {
            suffix[i] = suffix[i + 1] + piles[i];
        }

        return solve(0, 1);
    }
    private int solve(int index, int m) {
        if (index >= n) {
            return 0;
        }
        if (index + 2 * m >= n) {
            return suffix[index];
        }
        if (dp[index][m] != 0) {
            return dp[index][m];
        }
        int best = 0;
        for (int x = 1; x <= 2 * m; x++) {
            int taken = suffix[index] - suffix[index + x];
            int nextM = Math.max(m, x);
            int opponent = solve(index + x, nextM);
            int alice = suffix[index] - opponent;
            best = Math.max(best, alice);
        }

        return dp[index][m] = best;
    }
}