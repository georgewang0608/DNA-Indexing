import java.io.*;
import java.util.*;
import java.util.zip.*;

public class CitiesMultiTest {
    // Maximum number of matches to print
    private static final int MAX_MATCHES = 10;
    // Maximum number of cities to parse
    private static final int MAX_CITIES = 500000;

    // Print up to the first MAX_MATCHES given matches.
    private static void printMatches(List<CharSequence> matches) {
        System.out.println(matches.size() + " matches");
        for (int i = 0; i < Math.min(matches.size(), MAX_MATCHES); i += 1) {
            System.out.println(matches.get(i));
        }
    }

    public static void main(String[] args) throws IOException {
        Set<String> unique = new HashSet<>(MAX_CITIES, 1.0f);
        Scanner input = new Scanner(new GZIPInputStream(new FileInputStream("cities.tsv.gz")));
        while (input.hasNextLine() && unique.size() < MAX_CITIES) {
            Scanner line = new Scanner(input.nextLine());
            int population = line.nextInt();
            String city = line.next(); // Only the first term
            unique.add(city);
        }
        System.out.println(unique.size() + " cities loaded");

        // Reference implementation.
        Autocomplete reference = new TreeSetAutocomplete();
        reference.addAll(unique);

        // Testing implementations.
        Map<String, Autocomplete> implementations = Map.of(
            "LinearSearchAutocomplete", new LinearSearchAutocomplete(),
            "BinarySearchAutocomplete", new BinarySearchAutocomplete(),
            "TernarySearchTreeAutocomplete", new TernarySearchTreeAutocomplete()
        );
        // Add cities to each testing implementation.
        for (Autocomplete autocomplete : implementations.values()) {
            autocomplete.addAll(unique);
        }

        Scanner stdin = new Scanner(System.in);
        System.out.print("Query: ");
        while (stdin.hasNextLine()) {
            String prefix = stdin.nextLine();
            if (prefix.isEmpty()) {
                System.exit(0);
            }

            // Ground truth for the given query.
            List<CharSequence> referenceMatches = reference.allMatches(prefix);
            // Sort the output so things can be compared.
            Collections.sort(referenceMatches, CharSequence::compare);
            printMatches(referenceMatches);

            // Check each implementation against the reference matches.
            for (String name : implementations.keySet()) {
                Autocomplete autocomplete = implementations.get(name);
                List<CharSequence> matches = autocomplete.allMatches(prefix);
                Collections.sort(matches, CharSequence::compare);
                if (matches.equals(referenceMatches)) {
                    System.out.println();
                    System.out.println(name + " Output MATCHES Reference :)");
                } else {
                    System.out.println();
                    System.out.println(name + " Output DOES NOT MATCH Reference :(");
                    printMatches(matches);
                }
            }
            System.out.println();
            System.out.print("Query: ");
        }
    }
}
