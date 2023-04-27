package firesat.activities;

import firesat.Configuration;
import firesat.Mission;
import firesat.activities.gnc.GncChangeControlMode;
import firesat.generated.activities.gnc.GncChangeControlModeMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import gov.nasa.jpl.aerie.merlin.framework.junit.MerlinExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MerlinExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GncChangeControlModeTest {

  private Mission mission;

  public GncChangeControlModeTest(final Registrar registrar) {
    this.mission = new Mission(registrar, Instant.EPOCH, new Configuration());
  }

  @Test
  public void testDefaultSerializationDoesNotThrow() {

  }
}
