package it.unibo.oop.lab.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * This class will contain four utility functions on lists and maps, of which the first one is provided as example.
 * 
 * All such methods take as second argument a functional interface from the Java library (java.util.function).
 * This enables calling them by using the concise lambda syntax, as it's done in the main function.
 *
 * Realize the three methods **WITHOUT** using the Stream library, but only leveraging the lambdas.
 *
 */
public final class LambdaUtilities {

    private LambdaUtilities() {
    }

    /**
     * @param list
     *            the input list
     * @param op
     *            the process to run on each element
     * @param <T>
     *            element type
     * @return a new list containing, for each element of list, the element and
     *         a processed version
     */
    public static <T> List<T> dup(final List<T> list, final UnaryOperator<T> op) {
        final List<T> l = new ArrayList<>(list.size() * 2);
        list.forEach(t -> {
            l.add(t);
            l.add(op.apply(t));
        });
        return l;
    }

    /**
     * @param list
     *            input list
     * @param pre
     *            predicate to execute
     * @param <T>
     *            element type
     * @return a list where each value is an Optional, holding the previous
     *         value only if the predicate passes, and an Empty optional
     *         otherwise.
     */
    public static <T> List<Optional<T>> optFilter(final List<T> list, final Predicate<T> pre) {
        /*
         * Suggestion: consider Optional.filter
         */
        
        return list.stream()
            .map(el -> Optional.of(el))
            .map(el -> el.filter(pre))
            .toList();
                
    }

    /**
     * @param list
     *            input list
     * @param op
     *            a function that, for each element, computes a key
     * @param <T>
     *            element type
     * @param <R>
     *            key type
     * @return a map that groups into categories each element of the input list,
     *         based on the mapping done by the function
     */
    public static <R, T> Map<R, Set<T>> group(final List<T> list, final Function<T, R> op) {
        /*
         * Suggestion: consider Map.merge
         */
        final Map<R, Set<T>> map = new HashMap<>();
        list.forEach(t -> {
            map.merge(op.apply(t), new HashSet<>(Arrays.asList(t)), (t1, t2) -> {
                t1.addAll(t2);
                return t1;
            });
        });
        return map;
    }

    /**
     * @param map
     *            input map
     * @param def
     *            the supplier
     * @param <V>
     *            element type
     * @param <K>
     *            key type
     * @return a map whose non present values are filled with the value provided
     *         by the supplier
     */
    public static <K, V> Map<K, V> fill(final Map<K, Optional<V>> map, final Supplier<V> def) {
        final Map<K,V> _result = new HashMap<K,V>();
        map.forEach((k,v)->{
            K key=k;
            if(!v.isPresent()){
                _result.put(key, def.get());
            }else{
                _result.put(key, v.get());
            }
            
        });

        return _result;
    }

    /**
     * @param args
     *            ignored
     */
    @SuppressWarnings("PMD.SystemPrintln")
    public static void main(final String[] args) {
        
        Map<String,Set<Integer>> res= LambdaUtilities.group(List.of(1, 2, 3, 4, 5), x -> x % 2 == 0 ? "even" : "odd");
        res.forEach((k,v)->{
            System.out.println(k);
            System.out.println("\t");
            v.stream().peek(elem -> System.out.println(elem));
        });
    }
}
