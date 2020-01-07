package jettyjersey;

import java.util.List;
import java.util.Map;

public class DocsResponse {
    private List<Map<String, Object>> response;

    public DocsResponse() {
    }

    public DocsResponse(List<Map<String, Object>> response) {
        this.response = response;
    }

    public List<Map<String, Object>> getResponse() {
        return response;
    }
}
