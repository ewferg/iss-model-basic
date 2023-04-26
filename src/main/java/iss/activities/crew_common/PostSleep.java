package iss.activities.crew_common;

import firesat.Mission;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import iss.models.crew_common.CrewMember;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.ControllableDuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;

@ActivityType("PostSleep")
public final class PostSleep {
  @Parameter
  public CrewMember crewMember = CrewMember.ISSCDR;

  @Parameter
  public double activityDurHr = 2;

  @EffectModel
  public void run(final Mission mission) {
    // Wait for the specified duration
    delay(Duration.roundNearest(activityDurHr, Duration.HOURS));
  }
}
