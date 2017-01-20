package com.pantherman594;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by david on 1/18.
 *
 * @author david
 */
public class Punnett {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Parent 1 Genotype: ");
        String p1 = input.nextLine();
        System.out.print("Parent 2 Genotype: ");
        String p2 = input.nextLine();

        if (p1.length() != p2.length()) {
            System.out.println("Invalid input! Make sure the genotypes have the same number of traits.");
            return;
        }
        if (p1.length() % 2 != 0) {
            System.out.println("Invalid input! Make sure the genotypes have an even number of traits.");
            return;
        }

        int numTraits = p1.length() / 2;

        Set<String> p1Gametes = getGametes(p1, 0);
        Set<String> p2Gametes = getGametes(p2, 0);
        Map<String, Integer> phenotypes = new LinkedHashMap<>();

        System.out.print(String.join("", Collections.nCopies(numTraits + 2, " ")));
        System.out.print(String.join(String.join("", Collections.nCopies(numTraits + 1, " ")), p1Gametes));
        for (String p2Gamete : p2Gametes) {
            System.out.println();
            System.out.print(p2Gamete + "  ");
            for (String p1Gamete : p1Gametes) {
                String genotype = alternateJoin(p1Gamete.toCharArray(), p2Gamete.toCharArray());
                System.out.print(genotype + " ");

                String phenotype = getPhenotype(genotype);
                if (!phenotypes.containsKey(phenotype)) phenotypes.put(phenotype, 0);
                phenotypes.put(phenotype, phenotypes.get(phenotype) + 1);
            }
        }
        System.out.println();

        phenotypes = phenotypes.entrySet().stream().sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        System.out.println("Phenotypes:");
        int smallest = Integer.MAX_VALUE;
        for (String phenotype : phenotypes.keySet()) {
            System.out.println("  " + phenotype + ": " + phenotypes.get(phenotype));

            if (phenotypes.get(phenotype) < smallest) smallest = phenotypes.get(phenotype);
        }

        int gcf = 1;
        for (int i = smallest; i > 0; i--) {
            boolean allDivisible = true;
            for (int val : phenotypes.values()) {
                if (val % smallest != 0) {
                    allDivisible = false;
                    break;
                }
            }

            if (allDivisible) {
                gcf = smallest;
            }
        }

        StringBuilder ratio = new StringBuilder("Ratio: ");
        for (int val : phenotypes.values()) {
            ratio.append(val / gcf);
            ratio.append(":");
        }
        ratio.deleteCharAt(ratio.length() - 1);
        System.out.println(ratio.toString());
    }

    private static Set<String> getGametes(String genotype, int index) {
        Set<String> gametes = new TreeSet<>();
        if (index == genotype.length() - 2) {
            gametes.add(String.valueOf(genotype.charAt(index)));
            gametes.add(String.valueOf(genotype.charAt(index + 1)));
            return gametes;
        }

        for (String gamete : getGametes(genotype, index + 2)) {
            gametes.add(genotype.charAt(index) + gamete);
            gametes.add(genotype.charAt(index + 1) + gamete);
        }
        return gametes;
    }

    private static String getPhenotype(String genotype) {
        StringBuilder phenotype = new StringBuilder();
        for (int i = 0; i < genotype.length() / 2; i++) {
            char a = genotype.charAt(2 * i);
            char b = genotype.charAt(2 * i + 1);

            if (Character.isUpperCase(a) || Character.isUpperCase(b)) {
                phenotype.append(Character.toUpperCase(a));
            } else {
                phenotype.append(Character.toLowerCase(a));
            }
        }
        return phenotype.toString();
    }

    private static String alternateJoin(char[] a, char[] b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            if (Character.isUpperCase(b[i])) {
                result.append(b[i]).append(a[i]);
            } else {
                result.append(a[i]).append(b[i]);
            }
        }
        return result.toString();
    }
}
