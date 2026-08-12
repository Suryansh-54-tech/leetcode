import java.util.*;
class Solution {
    public int maxSubarrayLength(int[] nums, int k) {
        HashMap<Integer, Integer> freq = new HashMap<>();
        int left = 0;
        int answer = 0;
        for (int right = 0; right < nums.length; right++) {
            int value = nums[right];
            freq.put(value, freq.getOrDefault(value, 0) + 1);
            while (freq.get(value) > k) {
                int removed = nums[left];
                freq.put(removed, freq.get(removed) - 1);
                left++;
            }
            answer = Math.max(answer, right - left + 1);
        }
        return answer;
    }
}