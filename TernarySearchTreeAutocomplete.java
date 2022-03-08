import java.util.*;

public class TernarySearchTreeAutocomplete implements Autocomplete {

    private Node overallRoot; // Root of TST

    private static class Node {
        public char data;             // Character
        public boolean isTerm;        // If the node is a term node
        public Node left, mid, right; // Left, middle, right subtrees

        public Node(char data, boolean isTerm) {
            this.data = data;
            this.isTerm = isTerm;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }

    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }
    
    /**
     * Puts all terms in the collection into the tree
     * @param terms collection of terms to add to the tree
     */
    public void addAll(Collection<? extends CharSequence> terms) {
        for (CharSequence term : terms) {
            put(term);
        }
    }

    /**
     * Returns the node of the term if it's in the tree, return null otherwise.
     * @param term the term
     * @return node of term if the given term is in the tree, null otherwise
     * @throws NullPointerException if key is null
     * @throws IllegalArgumentException if key is empty
     */
    public Node get(CharSequence term) {
        if (term == null) {
            throw new NullPointerException("calls get() with null argument");
        } else if (term.length() == 0) {
            throw new IllegalArgumentException("key must have length >= 1");
        }
        // Start searching from the root
        Node x = get(overallRoot, term, 0);
        return x;
    }

    // Returns subtree corresponding to given key
    private Node get(Node x, CharSequence term, int d) {        
        if (x == null) return null;
        // Get current character
        char c = term.charAt(d);
        // Go left of tree if smaller, right if larger
        if      (c < x.data)            return get(x.left,  term, d);
        else if (c > x.data)            return get(x.right, term, d);
        // Otherwise, we will use the character at current node
        // If there's still characters in the term, go down middle to check the next character
        else if (d < term.length() - 1) return get(x.mid, term, d + 1);
        else                            return x;
    }

    /**
     * Inserts the term into the tree
     * @param term the term
     * @throws NullPointerException if key or val is null
     * @throws IllegalArgumentException if key is empty
     */
    public void put(CharSequence term) {
        if (term == null) {
            throw new NullPointerException("calls put() with null term");
        }
        /*if (!containsTerm(term)) {
            overallRoot = put(overallRoot, term, true, 0);
        }*/
        overallRoot = put(overallRoot, term, true, 0);
    }

    private Node put(Node x, CharSequence term, boolean isTerm, int d) {
        // Get current character
        char c = term.charAt(d);
        // Create node if necessary
        if (x == null) {
            x = new Node(c, false);
        }
        // If current char is smaller than char in root, put current char on left, otherwise right
        if      (c < x.data)            x.left = put(x.left, term, isTerm, d);
        else if (c > x.data)            x.right = put(x.right, term, isTerm, d);
        // If same as char at current node, go down if theres more characters in term
        else if (d < term.length() - 1) x.mid = put(x.mid, term, isTerm, d + 1);  
        // Otherwise, mark this node as a term       
        else                            x.isTerm = isTerm;        
        return x;
    }

    public List<CharSequence> allMatches(CharSequence prefix) {
        return keysWithPrefix(prefix);
    }

    public List<CharSequence> keysWithPrefix(CharSequence prefix) {
        if (prefix == null) {
            throw new NullPointerException("calls keysWithPrefix() with null argument");
        } else if (prefix.length() == 0) {
            throw new IllegalArgumentException("prefix must have length >= 1");
        }
        List<CharSequence> list = new LinkedList<CharSequence>();
        Node x = get(overallRoot, prefix, 0);
        if (x == null) return list;
        if (x.isTerm) list.add(prefix);
        collect(x.mid, new StringBuilder(prefix), list);
        return list;
    }

    // Collects all keys in subtree rooted at x with the given prefix
    private void collect(Node x, StringBuilder prefix, List<CharSequence> list) {
        if (x == null) return;
        collect(x.left, prefix, list);
        if (x.isTerm) list.add(prefix.toString() + x.data);
        prefix.append(x.data);
        collect(x.mid, prefix, list);
        prefix.deleteCharAt(prefix.length() - 1);
        collect(x.right, prefix, list);
    }
}
