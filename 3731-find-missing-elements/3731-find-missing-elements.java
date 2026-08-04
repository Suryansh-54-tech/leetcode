import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Solution {
    public List<Integer> findMissingElements(int[] nums) {
        // Find the minimum and maximum values in the array
        int min = nums[0];
        int max = nums[0];
        for (int num : nums) {
            if (num < min) min = num;
            if (num > max) max = num;
        }

        // Use a boolean array to mark which numbers exist
        // Size is (max - min + 1) to cover the full range
        boolean[] present = new boolean[max - min + 1];
        for (int num : nums) {
            present[num - min] = true;
        }

        // Collect all missing numbers
        List<Integer> missing = new ArrayList<>();
        for (int i = 0; i < present.length; i++) {
            if (!present[i]) {
                missing.add(i + min);
            }
        }

        return missing;
    }
}