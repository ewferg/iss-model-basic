package iss.activities;

import iss.mission.Configuration;
import iss.mission.generated.GeneratedModelType;
import gov.nasa.jpl.aerie.merlin.driver.ActivityDirective;
import gov.nasa.jpl.aerie.merlin.driver.ActivityDirectiveId;
import gov.nasa.jpl.aerie.merlin.driver.DirectiveTypeRegistry;
import gov.nasa.jpl.aerie.merlin.driver.MissionModel;
import gov.nasa.jpl.aerie.merlin.driver.MissionModelBuilder;
import gov.nasa.jpl.aerie.merlin.driver.SimulationDriver;
import gov.nasa.jpl.aerie.merlin.driver.SimulationResults;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerie.merlin.protocol.types.SerializedValue;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static gov.nasa.jpl.aerie.merlin.protocol.types.Duration.HOURS;

public class SimulationTest {
    @Test
    void testSimulation() {
        final var simulationStartTime = Instant.now();
        final var simulationDuration = Duration.of(10, HOURS);

        final Map<ActivityDirectiveId, ActivityDirective> schedule = new HashMap<>();

        final String tle1 = "1 25544U 98067A   23129.07180156  .00014291  00000+0  25740-3 0  9992";
        final String tle2 = "2 25544  51.6395 162.4026 0006247 325.7260 126.2385 15.50045677395729";

//        schedule.put(new ActivityDirectiveId(1L), new ActivityDirective(
//                Duration.ZERO,
//                "GncChangeControlMode",
//                Map.of("gncControlMode", SerializedValue.of("THRUSTERS")),
//                null,
//                true
//        ));


        final var results = simulate(new Configuration(tle1, tle2), simulationStartTime, simulationDuration, schedule);
//        for (final var segment : results.discreteProfiles.get("/gncControlMode").getRight()) {
//            System.out.println(segment.extent() + " " + segment.dynamics());
//        }
    }

    public SimulationResults simulate(
            Configuration configuration,
            Instant simulationStartTime,
            Duration simulationDuration,
            Map<ActivityDirectiveId, ActivityDirective> schedule
    ) {
        return SimulationDriver.simulate(
                makeMissionModel(new MissionModelBuilder(), simulationStartTime, configuration),
                schedule,
                simulationStartTime,
                simulationDuration,
                simulationStartTime,
                simulationDuration
        );
    }

    private static MissionModel<?> makeMissionModel(final MissionModelBuilder builder, final Instant planStart, final Configuration config) {
        final var factory = new GeneratedModelType();
        final var registry = DirectiveTypeRegistry.extract(factory);
        final var model = factory.instantiate(planStart, config, builder);
        return builder.build(model, registry);
    }
}