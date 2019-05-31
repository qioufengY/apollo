package com.ctrip.framework.apollo.metaservice.service;

import com.ctrip.framework.apollo.core.ServiceNameConsts;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.AgentClient;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DiscoveryService {

    private final AgentClient agentClient;

    public DiscoveryService(final AgentClient agentClient) {
        this.agentClient = agentClient;
    }

    public List<com.ecwid.consul.v1.agent.model.Service> getConfigServiceInstances() {
        List<com.ecwid.consul.v1.agent.model.Service> list = this.getNodeDate(ServiceNameConsts.APOLLO_CONFIGSERVICE);
        if (CollectionUtils.isEmpty(list)) {
            Tracer.logEvent("Apollo.ConsulDiscovery.NotFound", ServiceNameConsts.APOLLO_CONFIGSERVICE);
        }
        return list;
    }

    public List<com.ecwid.consul.v1.agent.model.Service> getMetaServiceInstances() {
        List<com.ecwid.consul.v1.agent.model.Service> list = this.getNodeDate(ServiceNameConsts.APOLLO_METASERVICE);
        if (CollectionUtils.isEmpty(list)) {
            Tracer.logEvent("Apollo.ConsulDiscovery.NotFound", ServiceNameConsts.APOLLO_CONFIGSERVICE);
        }
        return list;
    }

    public List<com.ecwid.consul.v1.agent.model.Service> getAdminServiceInstances() {
        List<com.ecwid.consul.v1.agent.model.Service> list = this.getNodeDate(ServiceNameConsts.APOLLO_ADMINSERVICE);
        if (CollectionUtils.isEmpty(list)) {
            Tracer.logEvent("Apollo.ConsulDiscovery.NotFound", ServiceNameConsts.APOLLO_CONFIGSERVICE);
        }
        return list;
    }

    private List<com.ecwid.consul.v1.agent.model.Service> getNodeDate(String name) {
        Response<Map<String, com.ecwid.consul.v1.agent.model.Service>> response = agentClient.getAgentServices();
        if (response == null || response.getValue() == null) {
            return Collections.emptyList();
        }
        List<com.ecwid.consul.v1.agent.model.Service> list = Lists.newArrayList();
        response.getValue().forEach((key, value) -> {
            if (key.startsWith(name)) {
                list.add(value);
            }
        });
        return list;
    }
}
