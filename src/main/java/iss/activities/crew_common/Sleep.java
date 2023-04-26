package iss.activities.crew_common;

import firesat.Mission;
import iss.models.crew_common.CrewMember;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.EffectModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.ActivityType.ControllableDuration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Parameter;


/**
 * @subsystem Crew Common
 * @brief_description Crew Sleep Activity
 */
@ActivityType("Sleep")
public final class Sleep {
  @Parameter
  public CrewMember crewMember = CrewMember.ISSCDR;

  @Parameter
  public double activityDurHr = 8.5;

  @EffectModel
  public void run(final Mission mission) {
    // Wait for the specified duration
    delay(Duration.roundNearest(activityDurHr, Duration.HOURS));
  }

}
