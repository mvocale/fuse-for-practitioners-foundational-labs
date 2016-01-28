
package account;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
    "name",
    "geo",
    "active"
})
public class Company {

    @JsonProperty("name")
    private String name;
    @JsonProperty("geo")
    private String geo;
    @JsonProperty("active")
    private boolean active;

    /**
     * 
     * @return
     *     The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The geo
     */
    @JsonProperty("geo")
    public String getGeo() {
        return geo;
    }

    /**
     * 
     * @param geo
     *     The geo
     */
    @JsonProperty("geo")
    public void setGeo(String geo) {
        this.geo = geo;
    }

    /**
     * 
     * @return
     *     The active
     */
    @JsonProperty("active")
    public boolean isActive() {
        return active;
    }

    /**
     * 
     * @param active
     *     The active
     */
    @JsonProperty("active")
    public void setActive(boolean active) {
        this.active = active;
    }

}
