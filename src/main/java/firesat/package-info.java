@MissionModel(model = Mission.class)
@WithMappers(BasicValueMappers.class)
@WithConfiguration(Configuration.class)
@WithActivityType(GncChangeControlMode.class)
@WithActivityType(Sleep.class)
@WithActivityType(Exercise.class)
@WithActivityType(Meal.class)
@WithActivityType(PreSleep.class)
@WithActivityType(PostSleep.class)
@WithActivityType(CrewMaintenance.class)
@WithActivityType(Research.class)
package firesat;

import firesat.activities.gnc.GncChangeControlMode;
import iss.activities.crew_common.*;
import gov.nasa.jpl.aerie.contrib.serialization.rulesets.BasicValueMappers;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithActivityType;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithConfiguration;
import gov.nasa.jpl.aerie.merlin.framework.annotations.MissionModel.WithMappers;
