package com.chewy.helper;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Map;

public interface MessageComposer {

  String composeMessage(String templateName, Map<String, Object> messageParmas) throws TemplateException, IOException;

}