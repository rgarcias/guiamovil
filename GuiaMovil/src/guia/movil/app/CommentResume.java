package guia.movil.app;

public class CommentResume {
	private String nick;
	private String comment;

	public CommentResume (String nick, String comment)
	{
		this.nick=nick;
		this.comment=comment;
		
	}
	
	
	public String getNick()
	{
		return this.nick;
	}
	
	public String getComment()
	{
		return this.comment;
	}
	
	
}
