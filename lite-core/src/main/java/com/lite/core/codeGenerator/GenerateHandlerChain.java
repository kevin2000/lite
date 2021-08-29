package com.lite.core.codeGenerator;

import java.util.ArrayList;
import java.util.List;

public class GenerateHandlerChain implements GenerateHandler{
	private List<GenerateHandler> generateHandlers = new ArrayList<>();
	
	public GenerateHandlerChain add(GenerateHandler handler) {
		generateHandlers.add(handler);
		return this;
	}
	
	public GenerateHandlerChain remove(Class<?> handlerClz) {
		for (GenerateHandler handler : generateHandlers) {
			if (handler.getClass().equals(handlerClz)) {
				generateHandlers.remove(handler);
				break;
			}
		}
		return this;
	}

	@Override
	public boolean handle(Class<?> clz) {
		for (GenerateHandler handler : generateHandlers) {
			if (!handler.handle(clz)) {
				break;
			}
		}
		return true;
	}
}
