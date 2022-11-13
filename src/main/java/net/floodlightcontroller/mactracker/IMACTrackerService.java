package net.floodlightcontroller.mactracker;

import net.floodlightcontroller.core.module.IFloodlightService;
import net.floodlightcontroller.core.types.MacDpidPair;

import java.util.ArrayList;

public interface IMACTrackerService extends IFloodlightService {
    ArrayList<MacDpidPair> getMACAdresses();
}
