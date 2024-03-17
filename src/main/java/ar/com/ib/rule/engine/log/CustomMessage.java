package ar.com.ib.rule.engine.log;

import java.util.Map;

import org.apache.logging.log4j.message.Message;

import io.vertx.core.json.JsonObject;

public class CustomMessage implements Message {

    private static final long serialVersionUID = 8845997432658834089L;

    private final transient Map<String, Object> messageLog;

    public CustomMessage(Map<String, Object> messageLog) {
        this.messageLog = messageLog;
    }

    @Override
    public String getFormattedMessage() {
        var jsonBody = new JsonObject(messageLog);
        return jsonBody.toString();
    }

    @Override
    public String getFormat() {
        return messageLog.toString();
    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }

    @Override
    public Throwable getThrowable() {
        return null;
    }

}
