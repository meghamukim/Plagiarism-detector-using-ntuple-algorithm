# Plagiarism-detector-using-ntuple-algorithm
### Problem Description:

Write a command-line program that performs plagiarism detection using a N-tuple comparison algorithm allowing for synonyms in the text.

Your program should take in 3 required arguments, and one optional.  In other cases such as no arguments, the program should print out usage instructions.
1.	file name for a list of synonyms
2.	input file 1
3.	input file 2
4.	(optional) the number N, the tuple size.  If not supplied, the default should be N=3.
The synonym file has lines each containing one group of synonyms.  For example a line saying "run sprint jog" means these words should be treated as equal.
The input files should be declared plagiarized based on the number of N-tuples in file1 that appear in file2, where the tuples are compared by accounting for synonyms as described above.  For example, the text "go for a run" has two 3-tuples, ["go for a", "for a run"] both of which appear in the text "go for a jog".
The output of the program should be the percent of tuples in file1 which appear in file2.  So for the above example, the output would be one line saying "100%".  In another example, for texts "go for a run" and "went for a jog" and N=3 we would output "50%" because only one 3-tuple in the first text appears in the second one.

### Assumptions:

1. Only TXT filetypes are accepted for file inputs. Example the filename should have the extensions ".txt"
2. Only alphanumeric strings allowed. Any other character like " , . ' will be treated as a word separator. For example, Let's & Let us are not treated equal. Also, Let's is broken into
[let & s]
3. All the comparisons are case-insensitive.
4. The application handles only String data types for comparisons & synonyms.
5. If the either of the input files are empty then plagiarism is 0%.

### Design decisions:

1. Used List<Strings> where each String in the List represents a tuple of size N.
2. All Tuple related processing is implemented in the NTupleService class which accesses the data structure NTuples. It is just a wrapper around the data type List<Strings>.
3. Using a Map to store the tuples in inputFile2 for faster lookup of exact string. This will have a tighter bound on time complexity on an average case.
In worst case this Map lookup might not be useful and we will have to compare each string in the tuples.
4. TupleService is a Singleton class.



### Upgrades:

1. We can store the repository data file in memory so that we do not have to create tuples for that base file each time the application is run.
2. If we want to make this application more extendible, we can use templates instead of a specific data type. This will make it work for more generic data types.
Note: I am pretty rusty with Java hence did not want to go in that direction given the limited time, since this already took a lot of time to brush up my Java knowledge.
3. Create an Interface for basic Detector Services which can then be extended and used to implement more specific text analysis detectors.
