package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> terms;

    /**
     * Constructs an empty instance.
     */
    public BinarySearchAutocomplete() {
        this.terms = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        this.terms.addAll(terms);
        Collections.sort(this.terms, CharSequence::compare);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> result = new ArrayList<>();
        if (prefix == null || prefix.length() == 0) {
            return result;
        }
        int ind = Collections.binarySearch(terms, prefix, CharSequence::compare);
        if (ind < 0) {
            ind = -(ind + 1);
        }
        List<CharSequence> sub = terms.subList(ind, terms.size());
        if (sub.size() < 1) { // this necessary? improve run time for empty case?
            return result;
        }
        for (CharSequence term : sub) {
            if (isPrefixOf(prefix, term)) {
                result.add(term);
            } else {
                return result;
            }
//            if (prefix.length() <= term.length()) {
//                CharSequence part = term.subSequence(0, prefix.length());
//                if (CharSequence.compare(prefix, part) == 0) {
//                    result.add(term);
//                } else {
//                    return result;
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
