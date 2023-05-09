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
    this("","");
  }
}