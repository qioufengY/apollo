package com.ctrip.framework.apollo.metaservice.controller;

import com.ctrip.framework.apollo.core.dto.ServiceDTO;
import com.ctrip.framework.apollo.metaservice.service.DiscoveryService;
import com.ecwid.consul.v1.agent.model.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/services")
public class ServiceController {

    private final DiscoveryService discoveryService;

    private static Function<Service, ServiceDTO> instanceInfoToServiceDTOFunc = instance -> {
        ServiceDTO service = new ServiceDTO();
        service.setAppName(instance.getService());
        service.setInstanceId(instance.getId());
        service.setHomepageUrl("http://" + instance.getAddress() + ":" + instance.getPort());
        return service;
    };

    public ServiceController(final DiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }


    @RequestMapping("/meta")
    public List<ServiceDTO> getMetaService() {
        List<Service> instances = discoveryService.getMetaServiceInstances();
        List<ServiceDTO> result = instances.stream().map(instanceInfoToServiceDTOFunc).collect(Collectors.toList());
        return result;
    }

    @RequestMapping("/config")
    public List<ServiceDTO> getConfigService(
            @RequestParam(value = "appId", defaultValue = "") String appId,
            @RequestParam(value = "ip", required = false) String clientIp) {
        List<Service> instances = discoveryService.getConfigServiceInstances();
        List<ServiceDTO> result = instances.stream().map(instanceInfoToServiceDTOFunc).collect(Collectors.toList());
        return result;
    }

    @RequestMapping("/admin")
    public List<ServiceDTO> getAdminService() {
        List<Service> instances = discoveryService.getAdminServiceInstances();
        List<ServiceDTO> result = instances.stream().map(instanceInfoToServiceDTOFunc).collect(Collectors.toList());
        return result;
    }
}
