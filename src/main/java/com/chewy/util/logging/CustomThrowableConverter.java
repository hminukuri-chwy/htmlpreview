package com.chewy.util.logging;

import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class CustomThrowableConverter extends ThrowableHandlingConverter {

    @Override
    public String convert(ILoggingEvent event) {
        IThrowableProxy itp = event.getThrowableProxy();
        if (itp instanceof ThrowableProxy) {
            ThrowableProxy tp = (ThrowableProxy) itp;
            return ThrowableProxyUtil.asString(tp).replace("\n", "\r");
        }
        return "";
    }
}