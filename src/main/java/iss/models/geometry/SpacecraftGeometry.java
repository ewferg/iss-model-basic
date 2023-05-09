package iss.models.geometry;

import gov.nasa.jpl.aerie.contrib.models.Clock;
import iss.mission.Configuration;
import iss.mission.DayNight;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.PVCoordinates;

import java.time.Instant;

public class SpacecraftGeometry {

    private PVCoordinates spacecraftState;

    private TLEPropagator tleprop;

    //private KeplerianPropagator kepler;

    //private KeplerianOrbit currentOrbit;

    private TimeScale utc;

    private Clock m_clock;

    private AbsoluteDate currentDate;

    private CelestialBody sun  = CelestialBodyFactory.getSun();

    private CelestialBody earth = CelestialBodyFactory.getEarth();

    Vector3D sunInEME2000;

    Vector3D earthInEME2000;

    private double solarPhase;


    // True Anomaly in Deg
//    public double getTrueAnomaly() {
//        if (new AbsoluteDate(m_clock.getTime().toString(), utc).isAfter(currentDate)) {
//            propagateState();
//        }
//        return FastMath.toDegrees(currentOrbit.getTrueAnomaly());
//    }

    // Solar Phase in Deg
    public double getSolarPhase() {
        if (new AbsoluteDate(m_clock.getTime().toString(), utc).isAfter(currentDate)) {
            propagateState();
        }
        solarPhase = FastMath.toDegrees(
                Vector3D.angle( spacecraftState.getPosition().subtract(earthInEME2000),
                                sunInEME2000.subtract(earthInEME2000)));
        return solarPhase;
    }

    public DayNight isDayOrNight() {
        if (solarPhase >= 90.0 ) {
            return DayNight.NIGHT;
        } else {
            return DayNight.DAY;
        }
    }

    private void propagateState() {
        currentDate = new AbsoluteDate(m_clock.getTime().toString(), utc);
        spacecraftState = tleprop.getPVCoordinates(currentDate);
//        spacecraftState = kepler.propagate(currentDate);
//        currentOrbit = new KeplerianOrbit(spacecraftState.getOrbit());
        sunInEME2000 = sun.getPVCoordinates(currentDate, FramesFactory.getEME2000()).getPosition();
        earthInEME2000 = earth.getPVCoordinates(currentDate, FramesFactory.getEME2000()).getPosition();

    }

    public SpacecraftGeometry(Clock clock, Configuration config) {
        m_clock = clock;
        utc = TimeScalesFactory.getUTC();
        currentDate = new AbsoluteDate(m_clock.getTime().toString(), utc);

//        Frame inertialFrame = FramesFactory.getEME2000();

        TLE tle = new TLE(config.tleLine1, config.tleLine2);
        tleprop = TLEPropagator.selectExtrapolator(tle);

        spacecraftState = tleprop.getPVCoordinates(currentDate);

//        double mu =  3.986004415e+14;
//
//        double a = 24396159;                     // semi major axis in meters
//        double e = 0.72831215;                   // eccentricity
//        double i = FastMath.toRadians(7);        // inclination
//        double omega = FastMath.toRadians(180);  // perigee argument
//        double raan = FastMath.toRadians(261);   // right ascension of ascending node
//        double lM = 0;                           // mean anomaly

//        currentOrbit = new KeplerianOrbit(a, e, i, omega, raan, lM, PositionAngle.MEAN,
//                inertialFrame, currentDate, mu);
//        kepler = new KeplerianPropagator(currentOrbit);
//        spacecraftState = kepler.propagate(currentDate);
        sunInEME2000 = sun.getPVCoordinates(currentDate, FramesFactory.getEME2000()).getPosition();
        earthInEME2000 = earth.getPVCoordinates(currentDate, FramesFactory.getEME2000()).getPosition();
        solarPhase = FastMath.toDegrees(
                Vector3D.angle( spacecraftState.getPosition().subtract(earthInEME2000),
                        sunInEME2000.subtract(earthInEME2000)));
    };

}
