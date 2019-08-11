/*
* Service to detect and return the plagiarized ratio
**/

package com.detector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PlagiarismDetectorService {

    private static PlagiarismDetectorService plagiarismDetectorServiceInstance = null;
    private PlagiarismDetectorService() {
    }

    public static PlagiarismDetectorService getPlagiarismDetectorServiceInstance () {
        return plagiarismDetectorServiceInstance == null ? new PlagiarismDetectorService() : plagiarismDetectorServiceInstance;
    }

    public Map<String, Set<String>> generateSynonymsMap(String synonymsFile) throws IOException {
        File file = new File(synonymsFile);
        if (file.length() == 0) {
            System.out.println("\nSynonyms file is empty. \n Cannot proceed further");
            System.exit(-1);
        }
        BufferedReader reader = null;
        reader = new BufferedReader(new FileReader(synonymsFile));
        String line;
        Map<String, Set<String>> synonymsMap = new HashMap<String, Set<String>>();
        while ((line = reader.readLine()) != null) {
            String words[] = line.toLowerCase().split("\\W+");
            for (int i = 0; i < words.length; i++) {
                Set<String> synonymSet = new HashSet<String>(Arrays.asList(words));
                if (synonymsMap.containsKey(words[i])) {
                    // If the word is already present, this means it was encountered in a different line of synonyms
                    // than previous list. Add the existing set of synonyms to to the new one.
                    synonymSet.addAll(synonymsMap.get(words[i]));
                }
                synonymsMap.put(words[i], synonymSet);
            }
        }
        reader.close();
        return synonymsMap;
    }



    public double calculatePlagiarismPercentage(String synonymFilePath, String clientFilePath,
                                             String repoFilePath, int tupleSize) throws IOException {
        double plagiarizedPercent = 0;
        double ratio = 0;

        NTuples repoTuples = NTupleService.getNTupleServiceInstance().generateAllTuples(repoFilePath, tupleSize);
        NTuples clientTuples = NTupleService.getNTupleServiceInstance().generateAllTuples(clientFilePath, tupleSize);
        // If either of the files were empty, assume that plagiarism was 0%
        if (repoTuples.tuples.size() == 0 || clientTuples.tuples.size() == 0) {
            return plagiarizedPercent;
        }

        Map<String, Set<String>> synonymsMap = generateSynonymsMap(synonymFilePath);
        ratio =  NTupleService.getNTupleServiceInstance().getTuplesMatchedUnmatchedRatio(repoTuples, clientTuples, synonymsMap);
        plagiarizedPercent = ratio * 100;
        return plagiarizedPercent;
    }
}
