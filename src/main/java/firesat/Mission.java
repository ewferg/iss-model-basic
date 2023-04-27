package firesat;

import firesat.models.gnc.GncControlMode;
import gov.nasa.jpl.aerie.contrib.models.Accumulator;
import gov.nasa.jpl.aerie.contrib.models.Clock;
import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.contrib.models.SampledResource;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.DoubleValueMapper;
import gov.nasa.jpl.aerie.contrib.serialization.mappers.EnumValueMapper;
import gov.nasa.jpl.aerie.merlin.framework.Registrar;
import iss.models.geometry.SpacecraftGeometry;
import org.orekit.data.ClasspathCrawler;
import org.orekit.data.DataProvidersManager;
import org.orekit.data.DirectoryCrawler;

import java.io.File;
import java.time.Instant;

public final class Mission {
  public final Register<GncControlMode> gncControlMode = Register.forImmutable(GncControlMode.THRUSTERS);
  public final Accumulator cumulativeResearchHours = new Accumulator(0.0, 0.0);

  public Clock clock;

  public SpacecraftGeometry spacecraftGeometry;

  public SampledResource trueAnomaly;

  private DataProvidersManager manager;
  private ClasspathCrawler crawler;

  public Mission(final Registrar registrar, final Instant planStart, Configuration config) {
    registrar.discrete("/gncControlMode", this.gncControlMode, new EnumValueMapper<>(GncControlMode.class));
    registrar.real("/cumulativeResearchHours", this.cumulativeResearchHours);

    manager = new DataProvidersManager();
    crawler = new ClasspathCrawler("orekit-data.zip");
    manager.addProvider(crawler);

    clock = new Clock(planStart);
    spacecraftGeometry = new SpacecraftGeometry(clock);
    trueAnomaly = new SampledResource(() -> this.spacecraftGeometry.getTrueAnomaly(), value -> value);

    registrar.discrete("/trueAnomaly", this.trueAnomaly, new DoubleValueMapper());
  }
}
