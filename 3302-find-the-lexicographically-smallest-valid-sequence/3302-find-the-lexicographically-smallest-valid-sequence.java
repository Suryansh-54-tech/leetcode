import java.util.*;

class Solution {

    public int[] validSequence(String word1, String word2) {

        int n = word1.length();
        int m = word2.length();

        // Positions of every character in word1
        List<Integer>[] pos = new ArrayList[26];

        for (int i = 0; i < 26; i++) {
            pos[i] = new ArrayList<>();
        }

        for (int i = 0; i < n; i++) {
            pos[word1.charAt(i) - 'a'].add(i);
        }

        /*
         * exact[i] = latest position from which
         * word2[i...] can be matched completely exactly.
         */
        int[] exact = new int[m + 1];
        Arrays.fill(exact, -1);
        exact[m] = n;

        for (int i = m - 1; i >= 0; i--) {

            List<Integer> list = pos[word2.charAt(i) - 'a'];

            int index = lowerBound(list, exact[i + 1]);

            if (index > 0) {
                exact[i] = list.get(index - 1);
            }
        }

        /*
         * runEnd[i] = first index after the consecutive run
         * containing word1[i].
         *
         * Example:
         * word1 = "aaabb"
         *
         * runEnd[0] = 3
         * runEnd[1] = 3
         * runEnd[2] = 3
         * runEnd[3] = 5
         * runEnd[4] = 5
         */
        int[] runEnd = new int[n];

        runEnd[n - 1] = n;

        for (int i = n - 2; i >= 0; i--) {

            if (word1.charAt(i) == word1.charAt(i + 1)) {
                runEnd[i] = runEnd[i + 1];
            } else {
                runEnd[i] = i + 1;
            }
        }

        /*
         * flexible[i] = latest position from which
         * word2[i...] can be formed with at most ONE mismatch.
         */
        int[] flexible = new int[m + 1];
        Arrays.fill(flexible, -1);
        flexible[m] = n;

        for (int i = m - 1; i >= 0; i--) {

            char target = word2.charAt(i);
            int best = -1;

            /*
             * Option 1:
             * Match current character exactly.
             * The remaining suffix may contain the mismatch.
             */
            List<Integer> list = pos[target - 'a'];

            int index = lowerBound(list, flexible[i + 1]);

            if (index > 0) {
                best = list.get(index - 1);
            }

            /*
             * Option 2:
             * Use the mismatch at the current position.
             * Therefore, the remaining suffix must match exactly.
             */
            int limit = exact[i + 1];

            if (limit != -1) {

                int candidate = limit - 1;

                if (candidate >= 0 &&
                    word1.charAt(candidate) == target) {

                    candidate = runEnd[ candidate ] - 1;
                }

                if (candidate >= 0) {
                    best = Math.max(best, candidate);
                }
            }

            flexible[i] = best;
        }

        int[] answer = new int[m];

        int previous = -1;
        boolean changed = false;

        for (int i = 0; i < m; i++) {

            char target = word2.charAt(i);

            int start = previous + 1;

            /*
             * Earliest exact occurrence >= start.
             */
            List<Integer> list = pos[target - 'a'];

            int index = lowerBound(list, start);

            int exactPosition = -1;

            if (index < list.size()) {
                exactPosition = list.get(index);
            }

            /*
             * If we use an exact match here, can the rest
             * still be completed?
             */
            boolean exactWorks = false;

            if (exactPosition != -1) {

                if (changed) {
                    exactWorks =
                        exact[i + 1] != -1 &&
                        exactPosition < exact[i + 1];
                } else {
                    exactWorks =
                        flexible[i + 1] != -1 &&
                        exactPosition < flexible[i + 1];
                }
            }

            /*
             * Earliest possible mismatch position.
             *
             * Instead of scanning through equal characters,
             * runEnd lets us jump over the entire run.
             */
            int mismatchPosition = n;

            if (!changed && start < n) {

                if (word1.charAt(start) != target) {
                    mismatchPosition = start;
                } else {
                    mismatchPosition = runEnd[start];
                }
            }

            /*
             * If we use the mismatch here, the remaining
             * characters must match exactly.
             */
            boolean mismatchWorks =
                !changed &&
                mismatchPosition < n &&
                exact[i + 1] != -1 &&
                mismatchPosition < exact[i + 1];

            /*
             * We already used our one modification.
             */
            if (changed) {

                if (!exactWorks) {
                    return new int[0];
                }

                answer[i] = exactPosition;
                previous = exactPosition;

                continue;
            }

            /*
             * Neither choice works.
             */
            if (!exactWorks && !mismatchWorks) {
                return new int[0];
            }

            /*
             * Pick the smaller index.
             * This guarantees lexicographically smallest answer.
             */
            if (exactWorks &&
                (!mismatchWorks ||
                 exactPosition < mismatchPosition)) {

                answer[i] = exactPosition;
                previous = exactPosition;

            } else {

                answer[i] = mismatchPosition;
                previous = mismatchPosition;
                changed = true;
            }
        }

        return answer;
    }

    /*
     * First index whose value is >= target.
     */
    private int lowerBound(List<Integer> list, int target) {

        int left = 0;
        int right = list.size();

        while (left < right) {

            int mid = left + (right - left) / 2;

            if (list.get(mid) >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left;
    }
}