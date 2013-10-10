package com.playnomics.util;

import android.content.Context;
import android.content.pm.PackageManager;

public class ServiceManager {
	
	private Context context;
	
	public ServiceManager(Context context){
		this.context = context;
	}
	
	public PackageManager getPackageManager(){
		return context.getPackageManager();
	}
}
