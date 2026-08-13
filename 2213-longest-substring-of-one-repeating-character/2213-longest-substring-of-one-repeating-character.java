class Solution {

    static class Node {
        char leftChar, rightChar;
        int length;
        int prefix, suffix, best;
        Node() {}

        Node(char c) {
            leftChar = rightChar = c;
            length = prefix = suffix = best = 1;
        }
    }
    private Node[] tree;
    private String s;
    public int[] longestRepeating(String s, String queryCharacters,
                                  int[] queryIndices) {
        this.s = s;
        int n = s.length();
        tree = new Node[4 * n];
        build(1, 0, n - 1);
        int[] answer = new int[queryIndices.length];
        for (int i = 0; i < queryIndices.length; i++) {
            int index = queryIndices[i];
            char c = queryCharacters.charAt(i);
            update(1, 0, n - 1, index, c);
            answer[i] = tree[1].best;
        }
        return answer;
    }
    private void build(int node, int left, int right) {
        if (left == right) {
            tree[node] = new Node(s.charAt(left));
            return;
        }
        int mid = left + (right - left) / 2;
        build(node * 2, left, mid);
        build(node * 2 + 1, mid + 1, right);
        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }
    private void update(int node, int left, int right,
                        int index, char c) {
        if (left == right) {
            tree[node] = new Node(c);
            return;
        }
        int mid = left + (right - left) / 2;
        if (index <= mid) {
            update(node * 2, left, mid, index, c);
        } else {
            update(node * 2 + 1, mid + 1, right, index, c);
        }
        tree[node] = merge(tree[node * 2], tree[node * 2 + 1]);
    }
    private Node merge(Node a, Node b) {
        Node res = new Node();
        res.length = a.length + b.length;
        res.leftChar = a.leftChar;
        res.rightChar = b.rightChar;
        res.prefix = a.prefix;
        if (a.prefix == a.length && a.rightChar == b.leftChar) {
            res.prefix = a.length + b.prefix;
        }
        res.suffix = b.suffix;
        if (b.suffix == b.length && a.rightChar == b.leftChar) {
            res.suffix = b.length + a.suffix;
        }
        res.best = Math.max(a.best, b.best);
        if (a.rightChar == b.leftChar) {
            res.best = Math.max(res.best, a.suffix + b.prefix);
        }
        return res;
    }
}