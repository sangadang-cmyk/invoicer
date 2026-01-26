package tech.sangdang.cucumber;

import io.cucumber.spring.ScenarioScope;
import lombok.Data;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Component
// make sure the context persists across singleton step definitions
@ScenarioScope(proxyMode = ScopedProxyMode.TARGET_CLASS) 
public class ScenarioContext {
    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor loggedInSession;
    private List<MvcResult> apiResults = new ArrayList<>();
    private Map<String, Object> data = new HashMap<>();
    
    public void clearLoggedInSession() {
        this.loggedInSession = null;
    }
    
    public void addApiResult(MvcResult result) {
        apiResults.add(result);
    }
    
    public MvcResult getLatestApiResult() {
        if (apiResults.isEmpty()) {
            return null;
        }
        return apiResults.getLast();
    }
    
    public MvcResult getApiResult(int index) {
        if (index < 0 || index >= apiResults.size()) {
            return null;
        }
        return apiResults.get(index);
    }
    
    public void putData(String key, Object value) {
        data.put(key, value);
    }
    
    public <T> T getData(String key) {
        //noinspection unchecked
        return (T) data.get(key);
    }
    
    public void resetData() {
        data.clear();
    }
}
