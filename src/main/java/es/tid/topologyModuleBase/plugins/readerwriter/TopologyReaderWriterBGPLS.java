package es.tid.topologyModuleBase.plugins.readerwriter;

import es.tid.bgp.bgp4Peer.peer.BGPPeer;
import es.tid.topologyModuleBase.ReaderParamsEnv;
import es.tid.topologyModuleBase.TopologyModuleParams;
import es.tid.topologyModuleBase.database.TopologiesDataBase;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

/**
 * Created by <a href="mailto:jgm1986@hotmail.com">Javier Gusano Martinez</a> on 23/09/2016.
 */
public class TopologyReaderWriterBGPLS extends TopologyReaderWriter{
    /**
     * This flag is used for analize if the current thread is running.
     */
    private boolean isRunning;

    ReaderParamsEnv envfile;
    /**
     * Class constructor.
     * @// TODO: 23/09/2016 Write javadoc constructor fields description.
     * @param ted
     * @param actualLittleParams
     * @param lock
     */
    public TopologyReaderWriterBGPLS(TopologiesDataBase ted, TopologyModuleParams actualLittleParams, Lock lock, ReaderParamsEnv envfile) {
        // @// TODO: 23/09/2016 Develop current constructor.
        super(ted, actualLittleParams,lock);
        this.envfile= envfile;
    }

    /**
     * Read/Write topology BGPLS
     * @// TODO: 23/09/2016 Write javadoc constructor fields description.
     */
    @Override
    public void readServeTopology() throws IOException {
        // @// TODO: 23/09/2016
        log.info("Acting as BGP Peer");
        BGPPeer bgpPeer = new BGPPeer();
        bgpPeer.configure(params.getBGPSConfigurationFile(), envfile);
        bgpPeer.setIntraTEDBs(ted.getTeds());
        bgpPeer.setMultiDomainTEDB(ted.getMdTed());
        bgpPeer.createUpdateDispatcher();
        log.info("Testing change");
        bgpPeer.startClient();
        bgpPeer.startServer();
        bgpPeer.startManagementServer();
        bgpPeer.startSendTopology();
        bgpPeer.CheckUpdateTime();
    }

    /**
     * Returns true if it's running, false otherwise.
     */
    @Override
    public boolean isRunning() {
        return isRunning;
    }


    /**
     * Returns the plugin name.
     */
    @Override
    public String getPluginName() {
        return "BGPLS importer/exporter peer";
    }


    /**
     * Shows the information about the current plugin object.
     */
    @Override
    public String displayInfo() {
        String str=getPluginName()+"\n";
        str+="Status: ";
        if(isRunning())str+="running";
        else str+="stop";
        str+="\nParameters file:"+params.getBGPSConfigurationFile();
        return str;
    }


    /**
     * Launch reader and writer methods.
     */
    @Override
    public void run() {
        try {
            readServeTopology();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
