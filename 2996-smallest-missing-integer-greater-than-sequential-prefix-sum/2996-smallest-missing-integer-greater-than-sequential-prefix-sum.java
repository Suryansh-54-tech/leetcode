import java.util.*;
class Solution {
    public int missingInteger(int[] nums) {
        int sum = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1] + 1) {
                sum += nums[i];
            } else {
                break;
            }
        }
        Set<Integer> seen = new HashSet<>();
        for (int num : nums) {
            seen.add(num);
        }
        while (seen.contains(sum)) {
            sum++;
        }
        return sum;
    }
}