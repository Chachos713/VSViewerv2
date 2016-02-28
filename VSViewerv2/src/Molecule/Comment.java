package Molecule;
public class Comment {
	private String comment;

	public Comment(String c) {
		comment = new String(c.toLowerCase());
	}
	
	public Comment(Comment other){
		this(other.comment);
	}

	public String getComment() {
		return comment;
	}
}
