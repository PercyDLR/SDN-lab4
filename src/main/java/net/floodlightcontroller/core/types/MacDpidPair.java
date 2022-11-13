package net.floodlightcontroller.core.types;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.floodlightcontroller.core.web.serializers.MacDpidPairSerializer;

@JsonSerialize(using= MacDpidPairSerializer.class)
public class MacDpidPair {
    private final String mac;
    private final String dpid;

    public MacDpidPair(String mac, String dpid) {
        this.mac = mac;
        this.dpid = dpid;
    }

    public String getMac() {
        return mac;
    }
    public String getDpid() {
        return dpid;
    }
}
