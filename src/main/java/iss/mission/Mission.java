package iss.mission;

import gov.nasa.jpl.aerie.contrib.models.Accumulator;
import gov.nasa.jpl.aerie.contrib.models.Clock;
import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.models.SampledResource;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.*;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import iss.models.geometry.SpacecraftGeometry;
import iss.models.time.TimeTrackerDaemon;
import org.orekit.data.ClasspathCrawler;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;

import java.io.File;
import java.time.Instant;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.spawn;

public final class Mission {
  public final Accumulator cumulativeResearchHours = new Accumulator(0.0, 0.0);

  public Clock clock;

  public SpacecraftGeometry spacecraftGeometry;

  public SampledResource solarPhase;

  public SampledResource dayNight;

  public SampledResource altitude;

  public Register<String> dayOfYear = Register.forImmutable("");

  public Register<String> month = Register.forImmutable("");

  public Register<String> hour = Register.forImmutable("");

  private DataProvidersManager manager;
  private ClasspathCrawler crawler;

  public TimeTrackerDaemon timeTrackerDaemon;

  public Mission(final Registrar registrar, final Instant planStart, Configuration config) {
    registrar.real("/cumulativeResearchHours", this.cumulativeResearchHours);

    manager = DataContext.getDefault().getDataProvidersManager();
    crawler = new ClasspathCrawler("orekit-data.zip");
    manager.addProvider(crawler);

    clock = new Clock(planStart);
    spacecraftGeometry = new SpacecraftGeometry(clock, config);
    solarPhase = new SampledResource(() -> this.spacecraftGeometry.getSolarPhase(), value -> value, 60.0);
    dayNight = new SampledResource(() -> this.spacecraftGeometry.isDayOrNight(), value -> value, 60.0);
    altitude = new SampledResource(() -> this.spacecraftGeometry.getAltitude(), value -> value, 60.0);

    registrar.discrete("/solarPhase", this.solarPhase, new DoubleValueMapper());
    registrar.discrete("/altitude", this.altitude, new DoubleValueMapper());
    registrar.discrete( "/dayNight", this.dayNight, new EnumValueMapper<DayNight>(DayNight.class));
    registrar.discrete("/month", this.month, new StringValueMapper());
    registrar.discrete("/dayOfYear", this.dayOfYear, new StringValueMapper());
    registrar.discrete("/hour", this.hour, new StringValueMapper());

    timeTrackerDaemon = new TimeTrackerDaemon(this);

    spawn(timeTrackerDaemon::run);

  }
}
