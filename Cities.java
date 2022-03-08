import java.io.*;
import java.util.*;
import java.util.zip.*;

public class Cities {
    // Maximum number of matches to print
    private static final int MAX_MATCHES = 10;
    // Maximum number of cities to parse
    private static final int MAX_CITIES = 1000000;

    public static void main(String[] args) throws IOException {
        Map<String, Integer> cities = new LinkedHashMap<>();
        Scanner input = new Scanner(new GZIPInputStream(new FileInputStream("cities.tsv.gz")));
        for (int i = 0; i < MAX_CITIES && input.hasNextLine(); i += 1) {
            Scanner line = new Scanner(input.nextLine());
            int population = line.nextInt();
            String city = line.next(); // Only the first term
            cities.putIfAbsent(city, population);
        }
        Autocomplete autocomplete = new TreeSetAutocomplete();
        autocomplete.addAll(cities.keySet());

        Scanner stdin = new Scanner(System.in);
        System.out.print("Query: ");
        while (stdin.hasNextLine()) {
            String prefix = stdin.nextLine();
            if (prefix.isEmpty()) {
                System.exit(0);
            }
            List<CharSequence> matches = autocomplete.allMatches(prefix);
            System.out.println(matches.size() + " matches");
            Collections.sort(matches, Comparator.comparingInt(cities::get).reversed());
            for (int i = 0; i < Math.min(matches.size(), MAX_MATCHES); i += 1) {
                System.out.println(matches.get(i));
            }
            System.out.print("Query: ");
        }
    }
}
