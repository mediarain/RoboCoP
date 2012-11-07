package de.mdsdacp.logic;

public class PackageName {
	
	private final static boolean isAcceptablePackageName(String uri)
	{
		return uri.matches("^[a-z_][a-z_0-9]*(\\.([a-z_][a-z_0-9]*))*$");
	}
	
	public final static String getNameByURI(String uri)
	{
		final String lc = uri.toLowerCase(); 
		if (isAcceptablePackageName(lc))
			return lc;
		return "de.mdsdacp";
	}
	
	public final static String getPathByURI(String uri)
	{
		final String lc = uri.toLowerCase();
		if (isAcceptablePackageName(lc))
			return lc.replace(".", "/");
		return "de/mdsdacp";
	}
}
