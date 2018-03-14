package bibCreator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BibCreator {
	public static int invalidCounter;
	public static int validCounter;
	public static String lineSeperator = "\n===================================================\n";
	public static String articleSeperator = "\n-----------------------------------------------\n";
	
	//TASK 5
	public static void processFilesForValidation(Scanner[] sc, File[] files) {
		//Regex pattern for @ARTICLE{
		Pattern startArticle = Pattern.compile("\\@ARTICLE\\{\\s*");
		//Regex pattern for single { 
		Pattern endArticle = Pattern.compile("^\\}\\s*");
		String currentLine = "";
		Matcher m1, m2;
		String author = " ", journal = " ", title = " ", year = " ", volume = " ", number = " ", pages = " ",
				doi = " ", month = " ";
		List<String> authors = null;
		//Loop through array of Scanners
		for(int i = 0; i < sc.length; i++) {
			//read file until the end
			System.out.println("Reading file " + files[i].getName());
			outerloop: //label for the outerloop
			while(sc[i].hasNextLine()){
				currentLine = sc[i].nextLine();
				m1 = startArticle.matcher(currentLine);
				//marks the beginning of the article
				if(m1.matches()){
					while(sc[i].hasNextLine()){
						currentLine = sc[i].nextLine();
						//assign '}' matcher
						m2 = endArticle.matcher(currentLine);
						//break out of while loop when line starts with '}'  (end of article)
						if(m2.matches()) {
							break;
						}else { //if we're inside the article, process it
							if(currentLine.contains("author")) {
								author = parse(currentLine);
								if(author.isEmpty()){
									processInvalid(files, "author", i);
									break outerloop;
								}else{
									authors = Arrays.asList(author.split(" and "));
								}
							}
							
							if(currentLine.contains("journal")) {
								journal = parse(currentLine);		
								if(journal.isEmpty()){
									processInvalid(files, "journal", i);
									break outerloop;
								}
							}
							
							if(currentLine.contains("title")) {
								title = parse(currentLine);
								if(title.isEmpty()){
									processInvalid(files, "title", i);
									break outerloop;
								}
							}
							
							if(currentLine.contains("year")) {
								year = parse(currentLine);
								if(year.isEmpty()){
									processInvalid(files, "year", i);
									break outerloop;
								}
							}
							
							if(currentLine.contains("volume")) {
								volume = parse(currentLine);
								if(volume.isEmpty()){
									processInvalid(files, "volume", i);
									break outerloop;
								}
							}
							
							if(currentLine.contains("number")) {
								number = parse(currentLine);
								if(number.isEmpty()){
									processInvalid(files, "number", i);
									break outerloop;
								}
							}
							
							if(currentLine.contains("pages")) {
								pages = parse(currentLine);
								if(number.isEmpty()){
									processInvalid(files, "pages", i);
									break outerloop;
								}
							}
							
							if(currentLine.contains("doi")) {
								doi = parse(currentLine);
								if(number.isEmpty()){
									processInvalid(files, "doi", i);
									break outerloop;
								}
							}
							
							if(currentLine.contains("month")) {
								month = parse(currentLine);
								if(month.isEmpty()){
									processInvalid(files, "month", i);
									break outerloop;
								}
							}
						}
					}
					//At the end of article
					//Build the three formats & output to output files
					System.out.println(articleSeperator + "Printing each field of article below: " + articleSeperator);
					System.out.print("authors: ");
					for(int j = 0; j < authors.size(); j++){
						System.out.print(authors.get(j) + "\t");
					}
					System.out.println("\njournal: " + journal + "\ntitle: " + title  + "\nyear: " + year  +
							"\nvolume: " + volume  + "\nnumber: " + number  + "\npages: " + pages  + "\ndoi: " + doi  + "\nmonth: " + month);
				}
			}	
			System.out.println(articleSeperator + "End of the file." + lineSeperator);
		}
	}
	
	
	public static void processInvalid(File[] files, String field, int index){
		try{
			invalidCounter++;
			delete(files, index);
			System.out.println("Error: Detected Empty Field!\n================================");
			throw new FileInvalidException("Problem detected with input file " + files[index].getName() + 
					"\nFile is invalid: Field " + "\"" + field + "\" is Empty"
					+ "Processing stopped at this point. Other empty fields may be present as well!\n");
		}catch(FileInvalidException e){
			System.out.println(e.getMessage());
		}
	}
	
	public static String parse(String s) {
		int start = s.indexOf('{');
		int end = s.indexOf('}');
		s  = s.substring(start + 1, end);
		return s;
	}
	
	public static void delete(File[] files, int index){
		for(File file: files){
			if(file.getName().contains(Integer.toString(index))){
				file.delete();
			}
		}
	}
	
	public static void main(String[] args) {
		Scanner[] sc = null;
		PrintWriter[] pwIEEE = null;
		PrintWriter[] pwACM = null;
		PrintWriter[] pwNJ = null;
 		File folder = new File("C:\\Users\\Jeremiah\\workspace\\Assignment3\\files");
 		String path = "C:\\Users\\Jeremiah\\workspace\\Assignment3\\output files\\";
		
		//Creating an array of files from the folder
		File[] files = folder.listFiles();
		//Creating array of Scanner for reading & array of PrintWriter for output
		sc = new Scanner[files.length];
		pwIEEE = new PrintWriter[files.length];
		pwACM = new PrintWriter[files.length];
		pwNJ = new PrintWriter[files.length];
		int openErrorIndex = 0;
		
		//TASK 3: Opening all input files, might throw a FileNotFoundException
		try{
			for(int i = 0; i < files.length; i++){
				//If the file exist, open it
				if(files[i].exists()){
					sc[i] = new Scanner(files[i]);
				}else{
					//will get the index of the last attempted opened file
					openErrorIndex = i;
					throw new FileNotFoundException();
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("Could not open input file" + files[openErrorIndex].getName() + " for reading. Please"
					+ " check if file exists! Program will terminate after closing any opened files.");
			//Closing all opened input files
			for(int i = 0; i < openErrorIndex; i++){
				sc[i].close();
			}
			//Exit program
			System.exit(0);
		}
		
		//TASK 4: Creating all output files, might throw a FileNotFoundException
		try{
			for(int i = 0; i <  files.length; i++){
				String IEEEname = "", ACMname = "", NJname = "";
				//If files exists, create output file
				if(files[i].exists()){
					IEEEname = "IEEE" + (i + 1) + ".json";
					pwIEEE[i] = new PrintWriter(path + IEEEname);
				}else{
					System.out.println(IEEEname + "could not be created.");
					throw new FileNotFoundException();
				}
				
				if(files[i].exists()){
					ACMname = "ACM" + (i + 1) + ".json";
					pwACM[i] = new PrintWriter(path + ACMname);
				}else{
					System.out.println(ACMname + "could not be created.");
					throw new FileNotFoundException();
				}
				
				if(files[i].exists()){
					NJname = "NJ" + (i + 1) + ".json";
					pwNJ[i] = new PrintWriter(path + NJname);
				}else{
					System.out.println(NJname + "could not be created.");
					throw new FileNotFoundException();
				}
			}
		}catch(FileNotFoundException e){
			System.out.println("There was an error creating files. Processing has stopped.");
			//Deleting all files
			for(File file: files) 
			    if (!file.isDirectory()) 
			        file.delete();
			
			for(Scanner scan : sc) {
				scan.close();
			}
		}
		
		System.out.println("Welcome to Bib Creator! Programmed by Jeremiah Tiongson & Yun Shi Lin" + lineSeperator);
		processFilesForValidation(sc, files);
		System.out.println("A total of " + invalidCounter + "files were invalid, and could not be processed. All other + " + validCounter + " \"Valid\" files have been created.");
	}
}
