import java.util.*;

public class LinearSearchAutocomplete implements Autocomplete {
    private final List<CharSequence> terms;

    public LinearSearchAutocomplete() {
        this.terms = new ArrayList<>();
    }

    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
    }

    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> result = new ArrayList<>();
        // Iterate through all the terms
        for(CharSequence term : terms) {
            if (prefix.length() <= term.length()) {
            // I just copied this from TreeSetAutocomplete    
                CharSequence part = term.subSequence(0, prefix.length());
                if (CharSequence.compare(prefix, part) == 0) {
                    result.add(term);
                }
            }
        }
        return result;
    }
}
