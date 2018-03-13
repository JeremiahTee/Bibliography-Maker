package bibCreator;

//-----------------------------------------------------
//Assignment #3
//Task #2
//Written by: 
//Jeremiah Tiongson, 40055477
//Yun Shi Lin, 40055867
//-----------------------------------------------------

/**
 * 
 * @author Jeremiah Tiongson & Yun Shi Lin
 *
 */
public class FileInvalidException extends Exception{
	/**
	 * Default FileInvalidException constructor with default error message
	 */
	public FileInvalidException(){
		super("Error: Input file cannot be parsed due to missing information (i.e. month={}, title={}, etc.)");
	}
	
	/**
	 * Parametrized FileInvalidException constructor with parameter message for custom error
	 */
	public FileInvalidException(String message){
		super(message);
	}
}