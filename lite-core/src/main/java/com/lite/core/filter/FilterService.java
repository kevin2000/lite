package com.lite.core.filter;

import java.util.List;

import org.springframework.util.PathMatcher;

public class FilterService {
	
	public static boolean matchExcludePath(String path, List<String> excludeList, PathMatcher pathMatcher) {
		if (excludeList != null) {
			for (String ignore : excludeList) {
				if (pathMatcher.match(ignore, path)) {
					return true;
				}
			}
		}
		return false;
	}
}
