package jordan_jefferson.com.gasbudgeter.network;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "menuItem")
public class ClientItem implements Serializable{
    @Element(name = "text")
    private String text;

    @Element(name = "value")
    private String value;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
