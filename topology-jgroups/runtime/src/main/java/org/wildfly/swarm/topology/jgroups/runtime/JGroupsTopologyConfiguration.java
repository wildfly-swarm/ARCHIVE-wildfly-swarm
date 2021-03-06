/**
 * Copyright 2015-2016 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.wildfly.swarm.topology.jgroups.runtime;

import java.util.ArrayList;
import java.util.List;

import org.jboss.msc.service.ServiceActivator;
import org.wildfly.swarm.container.runtime.AbstractServerConfiguration;
import org.wildfly.swarm.topology.jgroups.JGroupsTopologyFraction;

/**
 * @author Bob McWhirter
 */
public class JGroupsTopologyConfiguration extends AbstractServerConfiguration<JGroupsTopologyFraction> {

    public JGroupsTopologyConfiguration() {
        super(JGroupsTopologyFraction.class);
    }

    @Override
    public List<ServiceActivator> getServiceActivators(JGroupsTopologyFraction fraction) {
        List<ServiceActivator> activators = new ArrayList<>();
        activators.add(new JGroupsTopologyConnectorActivator());
        return activators;
    }

}
