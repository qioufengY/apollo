package com.ctrip.framework.apollo.metaservice.controller;

import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.metaservice.service.DiscoveryService;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final DiscoveryService discoveryService;

    private List<ServiceDTO> setValue(com.ecwid.consul.v1.agent.model.Service consul) {
        List<ServiceDTO> result = Lists.newArrayList();
        ServiceDTO service = new ServiceDTO();
        service.setAppName(consul.getId());
        service.setInstanceId(consul.getAddress());
        service.setHomepageUrl("http://" + consul.getAddress() + ":" + consul.getPort());
        result.add(service);
        return result;
    }

    public ServiceController(final DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }


    @RequestMapping("/meta")
    public List<ServiceDTO> getMetaService() {
        com.ecwid.consul.v1.agent.model.Service service = discoveryService.getMetaServiceInstances();
        List<ServiceDTO> result = setValue(service);
        return result;
    }


    @RequestMapping("/config")
    public List<ServiceDTO> getConfigService(
            @RequestParam(value = "appId", defaultValue = "") String appId,
            @RequestParam(value = "ip", required = false) String clientIp) {
        com.ecwid.consul.v1.agent.model.Service service = discoveryService.getConfigServiceInstances();
        List<ServiceDTO> result = setValue(service);
        return result;
    }

    @RequestMapping("/admin")
    public List<ServiceDTO> getAdminService() {
        com.ecwid.consul.v1.agent.model.Service service = discoveryService.getAdminServiceInstances();
        List<ServiceDTO> result = setValue(service);
        return result;
    }
}
