package com.jeremias.dev.exception;

public class GalleryPhotosException extends RuntimeException {
	
	public GalleryPhotosException(String message){
		super(message);
	}
	 public GalleryPhotosException(String exMessage, Exception exception) {
	        super(exMessage, exception);
	    }

}
