package iss.activities;

import iss.mission.Configuration;
import iss.mission.Mission;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MerlinExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GncChangeControlModeTest {

  private Mission mission;

  final String tle1 = "1 25544U 98067A   23129.07180156  .00014291  00000+0  25740-3 0  9992";
  final String tle2 = "2 25544  51.6395 162.4026 0006247 325.7260 126.2385 15.50045677395729";

  public GncChangeControlModeTest(final Registrar registrar) {
    this.mission = new Mission(registrar, Instant.now(), new Configuration(tle1, tle2));

  }

  @Test
  public void testDefaultSerializationDoesNotThrow() {

  }
}
