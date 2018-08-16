package jordan_jefferson.com.gasbudgeter.network;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "menuItems", strict = false)
public class ClientListItems {
    @ElementList(inline = true, required = false)
    public List<ClientItem> clientItemList;
}

