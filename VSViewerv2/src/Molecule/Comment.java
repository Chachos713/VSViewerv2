package Molecule;

/**
 * This class is used to store the comments for molecules. I do realize that
 * this is probably a waste os space, but the intetion is if we do add users to
 * the project, then this will help keep track of which user added which
 * comment.
 * 
 * @author Kyle Diller
 *
 */
public class Comment {
	private String comment;

	/**
	 * Creates a comment
	 * 
	 * @param c
	 *            the comment
	 */
	public Comment(String c) {
		comment = new String(c.toLowerCase());
	}

	/**
	 * Copies a comment
	 * 
	 * @param other
	 *            the comment to copy
	 */
	public Comment(Comment other) {
		this(other.comment);
	}

	/**
	 * @return the comment within this class
	 */
	public String getComment() {
		return comment;
	}
}
