package net.floodlightcontroller.mactracker;

import net.floodlightcontroller.core.types.MacDpidPair;
import net.floodlightcontroller.forwarding.Forwarding;
import net.floodlightcontroller.restserver.IRestApiService;
import org.projectfloodlight.openflow.protocol.OFType;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.types.MacAddress;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IOFMessageListener;
import net.floodlightcontroller.core.IOFSwitch;
import net.floodlightcontroller.core.module.FloodlightModuleContext;
import net.floodlightcontroller.core.module.FloodlightModuleException;
import net.floodlightcontroller.core.module.IFloodlightModule;
import net.floodlightcontroller.core.module.IFloodlightService;


import net.floodlightcontroller.core.IFloodlightProviderService;

import java.util.*;
import net.floodlightcontroller.packet.Ethernet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MACTracker implements IOFMessageListener, IFloodlightModule, IMACTrackerService {

    protected IFloodlightProviderService floodlightProvider;
    protected ArrayList<MacDpidPair> macAddresses;
    protected static Logger logger;

    protected IRestApiService restApi;

    public String getName() {
        return MACTracker.class.getSimpleName();
    }

    // Este módulo se ejecuta después de Forwarding
    public boolean isCallbackOrderingPrereq(OFType type, String name) {
        return name.equals(Forwarding.class.getSimpleName());
    }

    public boolean isCallbackOrderingPostreq(OFType type, String name) {
        return false;
    }

    public Collection<Class<? extends IFloodlightService>> getModuleServices() {
        Collection<Class<? extends IFloodlightService>> l = new ArrayList<>();
        l.add(IMACTrackerService.class);
        return l;
    }

    public Map<Class<? extends IFloodlightService>, IFloodlightService> getServiceImpls() {
        Map<Class<? extends IFloodlightService>, IFloodlightService> m = new HashMap<>();
        m.put(IMACTrackerService.class, this);
        return m;
    }

    public Collection<Class<? extends IFloodlightService>> getModuleDependencies() {
        Collection<Class<? extends IFloodlightService>> l = new ArrayList<>();
        l.add(IFloodlightProviderService.class);
        l.add(IRestApiService.class);
        return l;
    }

    public void init(FloodlightModuleContext context) throws FloodlightModuleException {
        floodlightProvider = context.getServiceImpl(IFloodlightProviderService.class);
        macAddresses = new ArrayList<>();
        restApi = context.getServiceImpl(IRestApiService.class);
        logger = LoggerFactory.getLogger(MACTracker.class);
    }

    public void startUp(FloodlightModuleContext context) throws FloodlightModuleException {
        floodlightProvider.addOFMessageListener(OFType.PACKET_IN, this);
        restApi.addRestletRoutable(new MACTrackerWebRoutable());
    }

    @Override
    public Command receive(IOFSwitch sw, OFMessage msg, FloodlightContext cntx) {
        Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);

        String sourceMACAddress = eth.getSourceMACAddress().toString();
        boolean encontrado = false;
        for(MacDpidPair par : macAddresses) {
            if (par.getMac().equals(sourceMACAddress)) {
                encontrado = true;
                break;
            }
        }
        if(!encontrado){
            macAddresses.add(new MacDpidPair(sourceMACAddress,sw.getId().toString()));
            logger.info("Dirección MAC: {} vista por el switch: {}", sourceMACAddress, sw.getId().toString());
        }

        return Command.CONTINUE;
    }

    @Override
    public ArrayList<MacDpidPair> getMACAdresses() {
        return macAddresses;
    }
}
