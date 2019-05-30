package com.ctrip.framework.apollo.metaservice.controller;

import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.metaservice.service.DiscoveryService;
import com.ecwid.consul.v1.agent.model.Check;
import com.google.common.collect.Lists;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final DiscoveryService discoveryService;

    private List<ServiceDTO> setValue(Check check) {
        List<ServiceDTO> result = Lists.newArrayList();
        ServiceDTO service = new ServiceDTO();
        service.setAppName(check.getServiceName());
        service.setInstanceId(check.getServiceId());
        service.setHomepageUrl(null);
        result.add(service);
        return result;
    }

    public ServiceController(final DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }


    @RequestMapping("/meta")
    public List<ServiceDTO> getMetaService() {
        Check check = discoveryService.getMetaServiceInstances();
        List<ServiceDTO> result = setValue(check);
        return result;
    }


    @RequestMapping("/config")
    public List<ServiceDTO> getConfigService(
            @RequestParam(value = "appId", defaultValue = "") String appId,
            @RequestParam(value = "ip", required = false) String clientIp) {
        Check check = discoveryService.getConfigServiceInstances();
        List<ServiceDTO> result = setValue(check);
        return result;
    }

    @RequestMapping("/admin")
    public List<ServiceDTO> getAdminService() {
        Check check = discoveryService.getAdminServiceInstances();
        List<ServiceDTO> result = setValue(check);
        return result;
    }
}
