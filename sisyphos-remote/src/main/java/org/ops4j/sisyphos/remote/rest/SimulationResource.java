/*
 * Copyright 2018 OPS4J Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ops4j.sisyphos.remote.rest;

import java.io.OutputStream;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.StreamingOutput;

import org.ops4j.sisyphos.api.simulation.SimulationBuilder;
import org.ops4j.sisyphos.api.simulation.SimulationRegistry;
import org.ops4j.sisyphos.core.log.OutputStreamSubscriber;
import org.ops4j.sisyphos.remote.runner.RemoteSimulationRunner;

@ApplicationScoped
@Path("simulation")
public class SimulationResource {

    @Inject
    private RemoteSimulationRunner runner;

    @GET
    @Produces({MediaType.TEXT_PLAIN, MediaType.APPLICATION_JSON})
    public List<String> findSimulations() {
        return SimulationRegistry.findNames();
    }

    @POST
    @Path("{simulation}")
    @Produces(MediaType.TEXT_PLAIN)
    public StreamingOutput runSimulation(@PathParam("simulation") String simulationName) {
        return os -> runSimulation(os, simulationName);
    }

    private void runSimulation(OutputStream os, String simulationName) {
        SimulationBuilder simulation = SimulationRegistry.find(simulationName)
            .getOrElseThrow(() -> new IllegalArgumentException("Simulation not found:" + simulationName));

        runner.addMessageSubscriber(new OutputStreamSubscriber(os));
        runner.runSimulation(simulation);
    }
}
