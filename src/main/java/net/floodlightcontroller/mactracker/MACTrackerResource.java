package net.floodlightcontroller.mactracker;

import net.floodlightcontroller.core.types.MacDpidPair;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import java.util.ArrayList;

public class MACTrackerResource extends ServerResource {
    @Get("json")
    public ArrayList<MacDpidPair> retrieve() {
        IMACTrackerService mt = (IMACTrackerService) getContext().getAttributes().get(IMACTrackerService.class.getCanonicalName());
        return mt.getMACAdresses();
    }
}