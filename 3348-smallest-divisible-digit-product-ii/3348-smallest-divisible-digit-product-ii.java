
class Solution {
    private int A, B, C, D;       
    private int dimB, dimC, dimD;   
    private int[] dp;               
    private final int[] e2 = new int[10];
    private final int[] e3 = new int[10];
    private final int[] e5 = new int[10];
    private final int[] e7 = new int[10];
    public String smallestNumber(String num, long t) {
        long tt = t;
        int a = 0, b = 0, c = 0, d = 0;
        while (tt % 2 == 0) { tt /= 2; a++; }
        while (tt % 3 == 0) { tt /= 3; b++; }
        while (tt % 5 == 0) { tt /= 5; c++; }
        while (tt % 7 == 0) { tt /= 7; d++; }
        if (tt != 1) return "-1"; 
        A = a; B = b; C = c; D = d;
        dimB = B + 1; dimC = C + 1; dimD = D + 1;
        e2[2] = 1; e2[4] = 2; e2[6] = 1; e2[8] = 3;
        e3[3] = 1; e3[6] = 1; e3[9] = 2;
        e5[5] = 1;
        e7[7] = 1;
        int size = (A + 1) * (B + 1) * (C + 1) * (D + 1);
        dp = new int[size];
        for (int ra = 0; ra <= A; ra++) {
            for (int rb = 0; rb <= B; rb++) {
                for (int rc = 0; rc <= C; rc++) {
                    for (int rd = 0; rd <= D; rd++) {
                        int idx = index(ra, rb, rc, rd);
                        if (ra == 0 && rb == 0 && rc == 0 && rd == 0) {
                            dp[idx] = 0;
                            continue;
                        }
                        int best = Integer.MAX_VALUE;
                        for (int dig = 2; dig <= 9; dig++) {
                            int na = Math.max(0, ra - e2[dig]);
                            int nb = Math.max(0, rb - e3[dig]);
                            int nc = Math.max(0, rc - e5[dig]);
                            int nd = Math.max(0, rd - e7[dig]);
                            if (na == ra && nb == rb && nc == rc && nd == rd) continue; 
                            int val = dp[index(na, nb, nc, nd)] + 1;
                            if (val < best) best = val;
                        }
                        dp[idx] = best;
                    }
                }
            }
        }
        int n = num.length();
        int[] prefixA = new int[n + 1];
        int[] prefixB = new int[n + 1];
        int[] prefixC = new int[n + 1];
        int[] prefixD = new int[n + 1];
        prefixA[0] = A; prefixB[0] = B; prefixC[0] = C; prefixD[0] = D;
        int firstZero = n; 
        for (int j = 0; j < n; j++) {
            int dig = num.charAt(j) - '0';
            if (dig == 0 && firstZero == n) firstZero = j;
            prefixA[j + 1] = Math.max(0, prefixA[j] - e2[dig]);
            prefixB[j + 1] = Math.max(0, prefixB[j] - e3[dig]);
            prefixC[j + 1] = Math.max(0, prefixC[j] - e5[dig]);
            prefixD[j + 1] = Math.max(0, prefixD[j] - e7[dig]);
        }
        if (firstZero == n && prefixA[n] == 0 && prefixB[n] == 0
                && prefixC[n] == 0 && prefixD[n] == 0) {
            return num;
        }
        int top = Math.min(firstZero, n - 1); 
        for (int i = top; i >= 0; i--) {
            int ra = prefixA[i], rb = prefixB[i], rc = prefixC[i], rd = prefixD[i];
            int startDig = (num.charAt(i) - '0') + 1;
            int suffixLen = n - 1 - i;
            for (int dig = startDig; dig <= 9; dig++) {
                int na = Math.max(0, ra - e2[dig]);
                int nb = Math.max(0, rb - e3[dig]);
                int nc = Math.max(0, rc - e5[dig]);
                int nd = Math.max(0, rd - e7[dig]);
                int k = dp[index(na, nb, nc, nd)];
                if (k <= suffixLen) {
                    StringBuilder sb = new StringBuilder(n);
                    sb.append(num, 0, i);
                    sb.append((char) ('0' + dig));
                    int pad = suffixLen - k;
                    for (int p = 0; p < pad; p++) sb.append('1');
                    for (int rdig : reconstruct(na, nb, nc, nd, k)) sb.append((char) ('0' + rdig));
                    return sb.toString();
                }
            }
        }
        int fullNeeded = dp[index(A, B, C, D)];
        int L = Math.max(n + 1, fullNeeded);
        StringBuilder sb = new StringBuilder(L);
        int pad = L - fullNeeded;
        for (int p = 0; p < pad; p++) sb.append('1');
        for (int rdig : reconstruct(A, B, C, D, fullNeeded)) sb.append((char) ('0' + rdig));
        return sb.toString();
    }
    private int index(int ra, int rb, int rc, int rd) {
        return ((ra * dimB + rb) * dimC + rc) * dimD + rd;
    }
    private int[] reconstruct(int ra, int rb, int rc, int rd, int k) {
        int[] res = new int[k];
        for (int idx = 0; idx < k; idx++) {
            int remaining = k - idx;
            for (int dig = 2; dig <= 9; dig++) {
                int na = Math.max(0, ra - e2[dig]);
                int nb = Math.max(0, rb - e3[dig]);
                int nc = Math.max(0, rc - e5[dig]);
                int nd = Math.max(0, rd - e7[dig]);
                if (dp[index(na, nb, nc, nd)] == remaining - 1) {
                    res[idx] = dig;
                    ra = na; rb = nb; rc = nc; rd = nd;
                    break;
                }
            }
        }
        return res;
    }
}