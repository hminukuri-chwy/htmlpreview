package com.chewy.helper;

import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import org.xhtmlrenderer.pdf.ITextRenderer;


@Factory
public class ITextRendererFactory {
    @Bean
    public ITextRenderer returnITextRenderer(){
        FixBaseFont.fixBuiltinFonts();
        return new ITextRenderer();
    }
}
