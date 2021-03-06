package org.wildfly.swarm.topology.consul.runtime;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.net.HostAndPort;
import com.orbitz.consul.cache.ConsulCache.Listener;
import com.orbitz.consul.model.health.Service;
import com.orbitz.consul.model.health.ServiceHealth;
import org.wildfly.swarm.topology.runtime.Registration;
import org.wildfly.swarm.topology.runtime.TopologyManager;

/**
 * Service-cache listener.
 *
 * This cache listener is responsible for receiving notifications
 * of cache changes and calculating the differences to apply to the
 * underlying TopologyManager.
 *
 * @author John Hovell
 * @author Bob McWhirter
 */
public class ServiceCacheListener implements Listener<HostAndPort, ServiceHealth> {

    private final String name;

    private final TopologyManager topologyManager;

    public ServiceCacheListener(String name, TopologyManager topologyManager) {
        this.name = name;
        this.topologyManager = topologyManager;
    }

    @Override
    public void notify(Map<HostAndPort, ServiceHealth> newValues) {
        Set<Registration> previousEntries = topologyManager.registrationsForService(this.name);

        Set<Registration> newEntries = newValues.values().stream()
                .map(e -> new Registration("consul",
                        this.name,
                        e.getService().getAddress(),
                        e.getService().getPort())
                        .addTags(e.getService().getTags())
                )
                .collect(Collectors.toSet());

        previousEntries.stream()
                .filter(e -> !newEntries.contains(e))
                .forEach(e -> {
                    this.topologyManager.unregister(e);
                });

        newEntries.stream()
                .filter(e -> !previousEntries.contains(e))
                .forEach(e -> {
                    this.topologyManager.register(e);
                });
    }
}
