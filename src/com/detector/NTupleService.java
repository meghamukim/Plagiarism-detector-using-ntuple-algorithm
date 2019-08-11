/**
 * NTupleService.java
 * Class to create tuples and implement any related processing
 */
package com.detector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class NTupleService {
    private static NTupleService nTupleServiceInstance = null;

    private NTupleService() {
    }

    public static NTupleService getNTupleServiceInstance() {
        return nTupleServiceInstance == null ? new NTupleService() : nTupleServiceInstance;
    }

    /**
     *
     * @param inputFilePath
     * @param tupleSize size of each tuple to be created
     * @return List of tuples (Ntuples data type)
     * @throws IOException
     */
    public NTuples generateAllTuples(String inputFilePath, int tupleSize) throws IOException {
        NTuples tuples = new NTuples();
        File file = new File(inputFilePath);
        if (file.length() != 0) {
            BufferedReader reader = null;
            reader = new BufferedReader(new FileReader(inputFilePath));
            String line;
            while ((line = reader.readLine()) != null) {
                tuples.addAll(generateTuplesFromLine(line, tupleSize));
            }
            reader.close();
        }
        return tuples;
    }

    /***
     *
     * @param line - Line to be used for creating n-gram tuples
     * @param tupleSize - Size of the tuples
     * @return List of tuples of size tupleSize
     */
    public NTuples generateTuplesFromLine(String line, int tupleSize) {
        NTuples lineTuples = new NTuples();
        String[] words = line.toLowerCase().split("\\W+");
        if (words.length < tupleSize) {
            return lineTuples;
        }

        for (int i = 0; i < words.length - tupleSize + 1; i++) {
            StringBuilder tuple = new StringBuilder(words[i]);
            for (int j = i+1; j < i + tupleSize; j++) {
                tuple.append(" ");
                tuple.append(words[j]);
            }
            lineTuples.add(tuple.toString());
        }
        return lineTuples;
    }

    public Map<String, Integer> addTuplesToMap (NTuples tuples) {
        Map<String, Integer> repoTuplesMap = new HashMap<String, Integer>();
        for (String str : tuples.tuples) {
            if (!repoTuplesMap.containsKey(str)) {
                repoTuplesMap.put(str, 1);
            } else {
                repoTuplesMap.put(str, repoTuplesMap.get(str) + 1);
            }
        }
        return repoTuplesMap;
    }

    public boolean IsMatch(String client, String repo, Map<String, Set<String>> synonymsMap) {
        String []cwords = client.split(" ");
        String []rwords = repo.split(" ");

        for (int i = 0; i < cwords.length; i++) {
            if (cwords[i].compareTo(rwords[i]) != 0) {
                if (!synonymsMap.isEmpty() && synonymsMap.containsKey(cwords[i]) && synonymsMap.containsKey(rwords[i])) {
                    synonymsMap.get(cwords[i]).retainAll(synonymsMap.get(rwords[i]));
                    // Check if there was an intersection in the synonyms list for the two words being compared
                    if (synonymsMap.get(cwords[i]).size() < 0) {
                        return false;
                    }
                    continue;
                }
                else {
                    return false;
                }
            }
        }
        return true;
    }

    /***
    * @param repoTuples: List of tuples in file 1
    * @param clientTuples: List of tuples in file 2
    * @param synonymsMap: Map of each word to its list of synonyms
    * @return the ratio of the n-tuples in file 1 that appear in file2
    * */
    public double getTuplesMatchedUnmatchedRatio(NTuples repoTuples, NTuples clientTuples, Map<String, Set<String>> synonymsMap) {
        int matchedCount = 0, unmatchedCount = 0;
        Map<String, Integer> repoTuplesMap = addTuplesToMap(repoTuples);
        for (int idx = 0; idx < clientTuples.tuples.size(); idx++) {
            boolean matched = false;
            if (repoTuplesMap.containsKey(clientTuples.tuples.get(idx))) {
                matchedCount++;
                matched = true;
                continue;
            } else {
                // If there was not an exact match then check if there is a synonym present
                for (int jdx = 0; jdx < repoTuples.tuples.size(); jdx++) {
                    if (IsMatch(clientTuples.tuples.get(idx), repoTuples.tuples.get(jdx), synonymsMap)) {
                        matchedCount++;
                        matched = true;
                        break;
                    }
                }
                if (!matched) {
                    unmatchedCount++;
                }
            }
        }
        return (float)matchedCount/(matchedCount + unmatchedCount);
    }
}
