package com.ctrip.framework.apollo.metaservice.service;

import com.ctrip.framework.apollo.core.ServiceNameConsts;
import com.ctrip.framework.apollo.tracer.Tracer;
import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.agent.AgentClient;
import com.ecwid.consul.v1.agent.model.Check;
import com.ecwid.consul.v1.kv.model.GetValue;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class DiscoveryService {

    private final AgentClient agentClient;

    public DiscoveryService(final AgentClient agentClient) {
        this.agentClient = agentClient;
    }

    public Check getConfigServiceInstances() {
        Check check = this.getNodeDate(ServiceNameConsts.APOLLO_CONFIGSERVICE);
        if (check.getName() == null) {
            Tracer.logEvent("Apollo.ConsulDiscovery.NotFound", ServiceNameConsts.APOLLO_CONFIGSERVICE);
        }
        return check;
    }

    public Check getMetaServiceInstances() {
        Check check = this.getNodeDate(ServiceNameConsts.APOLLO_METASERVICE);
        if (check.getName() == null) {
            Tracer.logEvent("Apollo.ConsulDiscovery.NotFound", ServiceNameConsts.APOLLO_METASERVICE);
        }
        return check;
    }

    public Check getAdminServiceInstances() {
        Check check = this.getNodeDate(ServiceNameConsts.APOLLO_ADMINSERVICE);
        if (check.getName() == null) {
            Tracer.logEvent("Apollo.ConsulDiscovery.NotFound", ServiceNameConsts.APOLLO_ADMINSERVICE);
        }
        return check;
    }

    private Check getNodeDate(String name) {
        Response<Map<String, Check>> checks = agentClient.getAgentChecks();
        if (checks == null) {
            return new Check();
        }
        return checks.getValue().get("service:" + name);
    }
}
