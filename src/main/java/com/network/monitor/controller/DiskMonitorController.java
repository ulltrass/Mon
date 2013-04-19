package com.network.monitor.controller;

import com.network.monitor.domain.Server;
import com.network.monitor.service.DiskMonitorService;
import java.util.List;

/**
 *
 * @author 
 */
public class DiskMonitorController {
    
    DiskMonitorService diskMonitorService = new DiskMonitorService();
    
    
    public List<Server> getDiskUsageForAllServers(){
        return diskMonitorService.getDiskUsageForAllServers();
    }
    
}
