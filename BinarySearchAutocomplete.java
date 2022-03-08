import java.util.*;

public class BinarySearchAutocomplete implements Autocomplete {
    private final List<CharSequence> terms;

    public BinarySearchAutocomplete() {
        this.terms = new ArrayList<>();
    }

    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
        Collections.sort(this.terms, CharSequence::compare);
    }

    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> result = new ArrayList<>();

        // Find a random match with binary search
        int wordWithPrefix = Collections.binarySearch(this.terms, prefix, CharSequence::compare);
        
        // If prefix itself is not in terms, check insertion point
        if(wordWithPrefix < 0) {
            wordWithPrefix = -(wordWithPrefix + 1);
            // If insertion point >= size of terms, no words have prefix in terms and we return empty results
            if(wordWithPrefix >= terms.size()) {
                return result;
            }
        }

        int first = wordWithPrefix;
        int last = wordWithPrefix;

        // Go left until we find the first word with prefix
        while(first > -1 && hasPrefix(prefix, terms.get(first))) {
            first--;
        };
        // Go right until we find the last word with prefix
        while(last < terms.size() && hasPrefix(prefix, terms.get(last))) {
            last++;
        };
        
        // Add words between first and last to result
        for(int i = first + 1; i < last; i++) {
            result.add(terms.get(i));
        }

        return result;
    }
    
    private boolean hasPrefix(CharSequence prefix, CharSequence term) { 
        // Long word can't be a prefix of a shorter word
        if(prefix.length() > term.length()) {
            return false;
        }
        CharSequence part = term.subSequence(0, prefix.length());
        return CharSequence.compare(prefix, part) == 0;
    }
}
