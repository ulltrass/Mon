package com.network.monitor.controller;

import com.network.monitor.domain.Server;
import com.network.monitor.service.SystemMonitorService;
import java.util.List;

/**
 *
 * @author 
 */
public class DiskMonitorController {
    
    SystemMonitorService diskMonitorService = new SystemMonitorService();
    
    
    public List<Server> getDiskUsageForAllServers(){
        return diskMonitorService.getSystemInfoForAllServers();
    }
    
}
