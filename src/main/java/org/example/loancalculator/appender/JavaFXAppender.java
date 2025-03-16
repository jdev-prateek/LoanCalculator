package org.example.loancalculator.appender;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import javafx.scene.control.TextArea;

public class JavaFXAppender extends AppenderBase<ILoggingEvent> {
    private static TextArea logArea;
    private PatternLayout patternLayout;

    public static void setLogArea(TextArea logArea) {
        JavaFXAppender.logArea = logArea;
    }


    @Override
    public void start() {
        super.start();
        patternLayout = new PatternLayout();
        patternLayout.setContext(getContext());
        patternLayout.setPattern("%d{HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n");
        patternLayout.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (logArea != null) {
//            String logMessage = eventObject.getFormattedMessage();
            String logMessage = patternLayout.doLayout(eventObject);
            logArea.appendText(logMessage);
        }
    }
}
