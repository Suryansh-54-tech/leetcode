class Solution {
    public int maximumLengthSubstring(String s) {
        int[] freq = new int[26];
        int left = 0;
        int answer = 0;
        for (int right = 0; right < s.length(); right++) {
            int current = s.charAt(right) - 'a';
            freq[current]++;
            while (freq[current] > 2) {
                freq[s.charAt(left) - 'a']--;
                left++;
            }
            answer = Math.max(answer, right - left + 1);
        }
        return answer;
    }
}