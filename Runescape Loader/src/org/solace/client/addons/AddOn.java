package org.solace.client.addons;

import java.io.File;
import java.net.URI;

@SuppressWarnings("serial")
public abstract class AddOn extends File {
	
	public AddOn(URI uri) {
		super(uri);
	}
	
	public abstract String getName();
	
	public abstract String getAuthor();
	
}
