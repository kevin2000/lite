package com.lite.core.auth;

import java.util.List;

public class Permission {
	
	private String api;
	private List<String> methods;
	
	
    public List<String> getMethods() {
		return methods;
	}

	public void setMethods(List<String> methods) {
		this.methods = methods;
	}

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }
}
