package iss.mission;

import gov.nasa.jpl.aerie.contrib.models.Accumulator;
import gov.nasa.jpl.aerie.contrib.models.Clock;
import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.models.SampledResource;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.EnumValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import iss.models.geometry.SpacecraftGeometry;
import org.orekit.data.ClasspathCrawler;
import org.orekit.data.DataContext;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;

import java.io.File;
import java.time.Instant;

public final class Mission {
  public final Accumulator cumulativeResearchHours = new Accumulator(0.0, 0.0);

  public Clock clock;

  public SpacecraftGeometry spacecraftGeometry;

  // public SampledResource trueAnomaly;

  public SampledResource solarPhase;

  //public Register<DayNight> dayNight;
  public SampledResource dayNight;

  private DataProvidersManager manager;
  private ClasspathCrawler crawler;

  public Mission(final Registrar registrar, final Instant planStart, Configuration config) {
    registrar.real("/cumulativeResearchHours", this.cumulativeResearchHours);

    manager = DataContext.getDefault().getDataProvidersManager();
    crawler = new ClasspathCrawler("orekit-data.zip");
    manager.addProvider(crawler);

    clock = new Clock(planStart);
    spacecraftGeometry = new SpacecraftGeometry(clock, config);
    //trueAnomaly = new SampledResource(() -> this.spacecraftGeometry.getTrueAnomaly(), value -> value);
    solarPhase = new SampledResource(() -> this.spacecraftGeometry.getSolarPhase(), value -> value);
    dayNight = new SampledResource(() -> this.spacecraftGeometry.isDayOrNight(), value -> value);

    //registrar.discrete("/trueAnomaly", this.trueAnomaly, new DoubleValueMapper());
    registrar.discrete("/solarPhase", this.solarPhase, new DoubleValueMapper());
    registrar.discrete( "/dayNight", this.dayNight, new EnumValueMapper<DayNight>(DayNight.class));
  }
}
