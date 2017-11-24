package es.tid.topologyModuleBase.plugins.reader;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.logging.Logger;

import es.tid.topologyModuleBase.ReaderParamsEnv;
import es.tid.topologyModuleBase.TopologyModuleParams;
import es.tid.topologyModuleBase.database.TopologiesDataBase;
import es.tid.topologyModuleBase.plugins.TMPlugin;
/**
 * 
 * @author jaume
 *
 */
public abstract class TopologyReader implements TMPlugin
{
	/**
	 * Logger
	 */
	protected Logger log=Logger.getLogger("TMController");
	
	protected TopologiesDataBase ted;
	protected TopologyModuleParams params;
	protected Lock lock;
	protected ReaderParamsEnv envfile;
	
	public TopologyReader(TopologiesDataBase ted, TopologyModuleParams params, Lock lock, ReaderParamsEnv envfile)
	{
		this.ted = ted;
		this.params = params;
		this.lock = lock;
		this.envfile= envfile;
	}
	
	abstract public void readTopology() throws IOException;
	
}
