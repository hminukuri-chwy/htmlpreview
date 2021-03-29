package com.chewy.helper;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.micronaut.core.annotation.Introspected;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Singleton;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;

@Singleton
@Slf4j
@Introspected
public class FreemarkerMessageComposer implements MessageComposer {
    private final String DEFAULT_ENCODING = "UTF-8";

    private final FreeMarkerMessageComposerFactory freeMarkerMessageComposerFactory;

    public FreemarkerMessageComposer(@NonNull   FreeMarkerMessageComposerFactory freeMarkerMessageComposerFactory) {
      this.freeMarkerMessageComposerFactory = freeMarkerMessageComposerFactory;
    }

    /**
     * Compose message with freemarker based on the provided template name and parameters
     * @param templateName name of the template to use
     * @param messageParams parameters to pass to the template
     * @return
     * @throws TemplateException
     * @throws IOException
     */
    public String composeMessage(
        String templateName, Map<String, Object> messageParams) throws TemplateException, IOException {
      Template t = freeMarkerMessageComposerFactory.freemarkerConfiguration().getTemplate(templateName, DEFAULT_ENCODING);
      ByteArrayOutputStream stream = new ByteArrayOutputStream();
      Writer out = new OutputStreamWriter(stream, DEFAULT_ENCODING);
      t.process(messageParams, out);
      return stream.toString();
    }

}