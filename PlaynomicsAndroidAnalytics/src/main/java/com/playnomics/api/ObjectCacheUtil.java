package com.playnomics.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;

class ObjectCacheUtil {
	
	public static boolean saveObject(Object obj, Context context, String fileName) {
	
		final File suspend_f = new File(context.getCacheDir(), fileName);
		
		FileOutputStream fos = null;
		ObjectOutputStream oos = null;
		boolean keep = true;
		
		try {
			fos = new FileOutputStream(suspend_f);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(obj);
		} catch (Exception e) {
			keep = false;
			
		} finally {
			try {
				if (oos != null)
					oos.close();
				if (fos != null)
					fos.close();
				if (keep == false)
					suspend_f.delete();
			} catch (Exception e) { 
				/* do nothing */
			}
		}
		
		return keep;		
	}
	
	public static Object getObject(Context c, String fileName)
	{
	
		final File suspend_f = new File(c.getCacheDir(), fileName);
		
		Object simpleClass = null;
		FileInputStream fis = null;
		ObjectInputStream is = null;
		// boolean keep = true;
		
		try {
			
			fis = new FileInputStream(suspend_f);
			is = new ObjectInputStream(fis);
			simpleClass = is.readObject();
		} catch (Exception e)
		{
			return null;
			
		} finally {
			try {
				if (fis != null)
					fis.close();
				if (is != null)
					is.close();
				
			} catch (Exception e) {
			}
		}
		
		return simpleClass;		
	}	
}
