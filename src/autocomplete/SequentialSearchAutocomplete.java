package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sequential search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class SequentialSearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> terms;

    /**
     * Constructs an empty instance.
     */
    public SequentialSearchAutocomplete() {
        this.terms = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> result = new ArrayList<>();
        if (prefix == null || prefix.length() == 0) {
            return result;
        }
        for (CharSequence term : terms) {
            if (isPrefixOf(prefix, term)) {
                result.add(term);
            }
//            if (prefix.length() <= term.length()) {
//                CharSequence part = term.subSequence(0, prefix.length());
//                if (CharSequence.compare(prefix, part) == 0) {
//                    result.add(term);
//                }
//            }
        }
        return result;
    }

    private static boolean isPrefixOf(CharSequence prefix, CharSequence term) {
        if (prefix.length() <= term.length()) {
            CharSequence part = term.subSequence(0, prefix.length());
            return CharSequence.compare(prefix, part) == 0;
        }
        return false;
    }
}
