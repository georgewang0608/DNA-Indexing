import java.util.*;

public interface Autocomplete {

    // Adds the given terms as possible autocompletion options.
    public void addAll(Collection<? extends CharSequence> terms);

    // Returns all terms that start with the given prefix.
    // If the prefix is null or empty, returns an empty list.
    public List<CharSequence> allMatches(CharSequence prefix);
}
