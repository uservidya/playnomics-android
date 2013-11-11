package com.playnomics.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import android.content.Context;

public class CacheFile {
	
	public interface ICacheFileHandler{
		void onReadSetComplete(Set<String> data);
	}
	
	
	private Util util;
	private Context context;
	private String fileName;
	
	public void setContext(Context context){
		this.context = context;
	}
	
	public CacheFile(Util util, IConfig config){
		this.util = util;
		this.fileName = config.getCacheFileName();
	}
	
	@SuppressWarnings("unchecked")
	public Runnable readSetFromFile(final ICacheFileHandler handler){
		return new Runnable() {
			public void run() {
				File file = util.getContextCacheFile(context, fileName);

				Set<String> set = null;
				FileInputStream fileStream = null;
				ObjectInputStream objectStream = null;

				try {

					fileStream = new FileInputStream(file);
					objectStream = new ObjectInputStream(fileStream);
					
					Object object =  objectStream.readObject();
					set = (Set<String>) object;
				} catch (Exception e) {
				} finally {
					try {
						if (fileStream != null)
							fileStream.close();
						if (objectStream != null)
							objectStream.close();
					} catch (Exception e) {
					}
				}

				handler.onReadSetComplete(set);
			}
		};
	}

	public Runnable writeSetToFile(final Set<String> set){
		return new Runnable() {
			public void run() {
				File file = util.getContextCacheFile(context, fileName);
				
				FileOutputStream fileStream = null;
				ObjectOutputStream objectStream = null;
				boolean writeSuccessful = false;
		
				try {
					fileStream = new FileOutputStream(file);
					objectStream = new ObjectOutputStream(fileStream);
					objectStream.writeObject(set);
					writeSuccessful = true;
				} catch (Exception e) {
					
				} finally {
					try {
						if (objectStream != null)
							objectStream.close();
						if (fileStream != null)
							fileStream.close();
						if (!writeSuccessful)
							file.delete();
					} catch (Exception e) {
						/* do nothing */
					}
				}
			}
		};
	}
	
}
