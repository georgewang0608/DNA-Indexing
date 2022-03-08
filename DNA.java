import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DNA {
    // Maximum number of matches to print
    private static final int MAX_MATCHES = 10;

    public static void main(String[] args) throws IOException {
        Autocomplete autocomplete = new TreeSetAutocomplete();
        autocomplete.addAll(new SuffixCollection(
            Files.readString(Path.of("EColi-3000.txt"))
        ));

        Scanner stdin = new Scanner(System.in);
        System.out.print("Query: ");
        while (stdin.hasNextLine()) {
            String prefix = stdin.nextLine();
            if (prefix.isEmpty()) {
                System.exit(0);
            }
            List<CharSequence> matches = autocomplete.allMatches(prefix);
            System.out.println(matches.size() + " matches");
            for (int i = 0; i < Math.min(matches.size(), MAX_MATCHES); i += 1) {
                CharSequence match = matches.get(i);
                if (match.length() >= 97) {
                    match = match.subSequence(0, 97) + "...";
                }
                System.out.println(match);
            }
            System.out.print("Query: ");
        }
    }

    // Memory-efficient Collection<CharSequence> for all non-empty suffixes of the given string
    private static class SuffixCollection extends AbstractCollection<CharSequence> {
        private final String data;

        public SuffixCollection(String data) {
            this.data = data;
        }

        public Iterator<CharSequence> iterator() {
            return new Iterator<>() {
                private int index = 0;

                public boolean hasNext() {
                    return index < data.length() - 1;
                }

                public CharSequence next() {
                    CharSequence result = new Substring(index, data.length());
                    index += 1;
                    return result;
                }
            };
        }

        private class Substring implements CharSequence {
            private final int offset;
            private final int length;

            public Substring(int lo, int hi) {
                this.offset = lo;
                this.length = hi - lo;
            }

            public char charAt(int index) {
                return data.charAt(offset + index);
            }

            public int length() {
                return length;
            }

            public CharSequence subSequence(int start, int end) {
                if (start < 0 || start > end || end > length) {
                    throw new IndexOutOfBoundsException(
                        "start " + start + ", end " + end + ", length " + length
                    );
                }
                return new Substring(offset + start, offset + end);
            }

            public String toString() {
                StringBuilder result = new StringBuilder(length);
                for (int i = 0; i < length; i += 1) {
                    result.append(charAt(i));
                }
                return result.toString();
            }
        }

        public int size() {
            return data.length() - 1;
        }
    }
}
