package org.ops4j.sisyphos.remote;

import org.junit.Test;
import org.ops4j.sisyphos.api.simulation.SimulationBuilder;
import org.ops4j.sisyphos.api.simulation.SimulationRegistry;
import org.ops4j.sisyphos.itest.Simulations;
import org.ops4j.sisyphos.remote.runner.RemoteSimulationRunner;
import org.slf4j.bridge.SLF4JBridgeHandler;

public class RemoteRunnerIT {

    @Test
    public void shouldRunRemoteSimulation() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        Simulations simulations = new Simulations();
        simulations.defineSimulations(null);

        String simulationName = "jsonplaceholder";
        SimulationBuilder simulation = SimulationRegistry.find(simulationName)
            .getOrElseThrow(() -> new IllegalArgumentException("Simulation not found:" + simulationName));

        RemoteSimulationRunner runner = new RemoteSimulationRunner();
        runner.init();
        //runner.setSubscriber(new OutputStreamSubscriber(System.out));
        runner.runSimulation(simulation);
        runner.close();
    }
}
