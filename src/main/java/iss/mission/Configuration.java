package iss.mission;

import gov.nasa.jpl.aerie.merlin.framework.annotations.Export;

import static gov.nasa.jpl.aerie.merlin.framework.annotations.Export.Template;

//public record Configuration() {
//  public static @Template Configuration defaultConfiguration() {
//    return new Configuration();
//  }
//}

public final class Configuration {
  @Export.Parameter
  public String tleLine1;

  @Export.Parameter
  public String tleLine2;

  public Configuration(final String tleLine1, final String tleLine2) {
    this.tleLine1 = tleLine1;
    this.tleLine2 = tleLine2;
  }

  public Configuration() {
    this("1 25544U 98067A   23138.40295236  .00010513  00000+0  18991-3 0  9998",
            "2 25544  51.6384 116.1630 0006492 359.1313   0.9664 15.50291469397177");
  }
}