package com.ctrip.framework.apollo.metaservice.service;

import com.ctrip.framework.apollo.core.ServiceNameConsts;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.AgentClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DiscoveryService {

    private final AgentClient agentClient;

    public DiscoveryService(final AgentClient agentClient) {
        this.agentClient = agentClient;
    }

    public com.ecwid.consul.v1.agent.model.Service getConfigServiceInstances() {
        com.ecwid.consul.v1.agent.model.Service service = this.getNodeDate(ServiceNameConsts.APOLLO_CONFIGSERVICE);
        if (service.getId() == null) {
            Tracer.logEvent("Apollo.ConsulDiscovery.NotFound", ServiceNameConsts.APOLLO_CONFIGSERVICE);
        }
        return service;
    }

    public com.ecwid.consul.v1.agent.model.Service getMetaServiceInstances() {
        com.ecwid.consul.v1.agent.model.Service service = this.getNodeDate(ServiceNameConsts.APOLLO_METASERVICE);
        if (service.getId() == null) {
            Tracer.logEvent("Apollo.ConsulDiscovery.NotFound", ServiceNameConsts.APOLLO_METASERVICE);
        }
        return service;
    }

    public com.ecwid.consul.v1.agent.model.Service getAdminServiceInstances() {
        com.ecwid.consul.v1.agent.model.Service service = this.getNodeDate(ServiceNameConsts.APOLLO_ADMINSERVICE);
        if (service.getId() == null) {
            Tracer.logEvent("Apollo.ConsulDiscovery.NotFound", ServiceNameConsts.APOLLO_ADMINSERVICE);
        }
        return service;
    }

    private com.ecwid.consul.v1.agent.model.Service getNodeDate(String name) {
        Response<Map<String, com.ecwid.consul.v1.agent.model.Service>> services = agentClient.getAgentServices();
        if (services == null) {
            return new com.ecwid.consul.v1.agent.model.Service();
        }
        return services.getValue().get(name);
    }
}
