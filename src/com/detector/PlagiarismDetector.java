/*
* Driver class to invoke the service for plagiarism detection using n-tuple algorithm
* @author: Megha Mukim
*/
package com.detector;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.String;

public class PlagiarismDetector {

    private static final int DEFAULT_TUPLE_SIZE = 3;
    private static int tupleSize = DEFAULT_TUPLE_SIZE;

    private void validateInputs(String [] args) {
        String usageStr = "This program takes in 3 required and one optional arguments." +
                "1.\tfile name for a list of synonyms\n" +
                "2.\tinput file 1\n" +
                "3.\tinput file 2\n" +
                "4.\t(optional) the number N, the tuple size\n";

        //Input error handling
        if (args.length < 3 || args.length > 4) {
            System.out.println("No arguments specified. Please refer to the usage instructions below:");
            System.out.println("Usage:");
            System.out.println(usageStr);
            System.exit(0);
        }

        if (!hasTxtExtension(args[0])) {
            System.out.println("Invalid synonyms file type. Only TXT file types are accepted.\nEXITING the application!");
            System.exit(0);
        }

        if (!hasTxtExtension(args[1])) {
            System.out.println("Invalid inputFile1 type. Only TXT file types are accepted.\nEXITING the application!");
            System.exit(0);
        }

        if (!hasTxtExtension(args[2])) {
            System.out.println("Invalid inputFile2 type. Only TXT file types are accepted.\nEXITING the application!");
            System.exit(0);
        }

        if (args.length == 4) {
            tupleSize = Integer.parseInt(args[3]);
            if (tupleSize < 1) {
                System.out.println("Invalid tuple size provided. EXITING the application!");
                System.exit(0);
            }
        }
    }

    private boolean hasTxtExtension(String filename) {
        return (filename.substring(filename.lastIndexOf('.') + 1).toLowerCase().equals("txt")) ? true : false;
    }

    public static void main(String[] args) {
        PlagiarismDetector detector = new PlagiarismDetector();
        detector.validateInputs(args);

	    // If all inputs are valid, read the input files, prepare the synonyms table & tuples and compare for plagiarism
        String synonymsFilePath = args[0];
        String clientFilePath = args[1];
        String repoFilePath = args[2];


        try {
            double plagiarizedPercent = PlagiarismDetectorService.getPlagiarismDetectorServiceInstance()
                    .calculatePlagiarismPercentage(synonymsFilePath, clientFilePath, repoFilePath, tupleSize);

            System.out.println(plagiarizedPercent + "%");
        } catch (FileNotFoundException e) {
            System.out.println("FILE NOT FOUND EXCEPTION");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("I/O EXCEPTION");
            e.printStackTrace();
        }

    }
}
