import java.util.Arrays;

public class SuffixArray {
    private static final int DEFAULT_ALPHABET_SIZE = 256;

    private final int size;
    private int[] T;
    private int[] sa;
    private int[] lcp;

    private boolean constructedSa = false;
    private boolean constructedLcpArray = false;

    int alphabetSize;
    int[] sa2, rank, tmp, c;

    public SuffixArray(String text) {
        this(toIntArray(text), DEFAULT_ALPHABET_SIZE);
    }

    public SuffixArray(int[] text) {
        this(text, DEFAULT_ALPHABET_SIZE);
    }

    public SuffixArray(int[] text, int alphabetSize) {
        this.T = text;
        this.size = text.length;
        this.alphabetSize = alphabetSize;
    }

    public int getTextSize() {
        return T.length;
    }

    public int[] getSa() {
        buildSuffixArray();
        return sa;
    }

    public int[] getLcpArray() {
        buildLcpArray();
        return lcp;
    }

    private void buildSuffixArray() {
        if(constructedSa) return;
        construct();
        constructedSa = true;
    }

    private void buildLcpArray() {
        if(constructedLcpArray) return;
        buildSuffixArray();
        kasai();
        constructedLcpArray = true;
    }

    private static int[] toIntArray(String s) {
        int[] t = new int[s.length()];
        for(int i = 0; i < s.length(); i++) t[i] = s.charAt(i);
        return t;
    }

    private void construct() {
        sa = new int[size];
        sa2 = new int[size];
        rank = new int[size];
        c = new int[Math.max(alphabetSize, size)];

        int i, p, r;
        for (i = 0; i < size; ++i) c[rank[i] = T[i]]++;
        for (i = 1; i < alphabetSize; ++i) c[i] += c[i - 1];
        for (i = size - 1; i >= 0; --i) sa[--c[T[i]]] = i;
        for (p = 1; p < size; p <<= 1) {
            for (r = 0, i = size - p; i < size; ++i) sa2[r++] = i;
            for (i = 0; i < size; ++i) if (sa[i] >= p) sa2[r++] = sa[i] - p;
            Arrays.fill(c, 0, alphabetSize, 0);
            for (i = 0; i < size; ++i) c[rank[i]]++;
            for (i = 1; i < alphabetSize; ++i) c[i] += c[i - 1];
            for (i = size - 1; i >= 0; --i) sa[--c[rank[sa2[i]]]] = sa2[i];
            for (sa2[sa[0]] = r = 0, i = 1; i < size; ++i) {
                if (!(rank[sa[i - 1]] == rank[sa[i]]
                        && sa[i - 1] + p < size
                        && sa[i] + p < size
                        && rank[sa[i - 1] + p] == rank[sa[i] + p])) r++;
                sa2[sa[i]] = r;
            }
            tmp = rank;
            rank = sa2;
            sa2 = tmp;
            if (r == size - 1) break;
            alphabetSize = r + 1;
        }
    }

    private void kasai() {
        lcp = new int[size];
        int[] inv = new int[size];
        for(int i = 0; i < size; i++) inv[sa[i]] = i;
        for(int i = 0, len = 0; i < size; i++) {
            if(inv[i] > 0) {
                int k = sa[inv[i] - 1];
                while((i + len < size) && (k + len < size) && T[i + len] == T[k + len]) len++;
                lcp[inv[i]] = len;
                if(len > 0) len--;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("-----i-----SA-----LCP---Suffix\n");

        for (int i = 0; i < size; i++) {
            int suffixLen = size - sa[i];
            char[] suffixArray = new char[suffixLen];
            for (int j = sa[i], k = 0; j < size; j++, k++) suffixArray[k] = (char) T[j];
            String suffix = new String(suffixArray);
            String formattedStr = String.format("% 7d % 7d % 7d %s\n", i, sa[i], lcp[i], suffix);
            sb.append(formattedStr);
        }
        return sb.toString();
    }
}
