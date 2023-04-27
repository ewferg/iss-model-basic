package iss.models.geometry;

import gov.nasa.jpl.aerie.contrib.models.Clock;
import org.hipparchus.util.FastMath;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.orbits.KeplerianOrbit;
import org.orekit.orbits.PositionAngle;
import org.orekit.propagation.SpacecraftState;
import org.orekit.propagation.analytical.KeplerianPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;

import java.time.Instant;

public class SpacecraftGeometry {

    private double m_trueAnomaly;

    private KeplerianPropagator kepler;

    private TimeScale utc = TimeScalesFactory.getUTC();

    private Clock m_clock;

    public double getTrueAnomaly() {
        AbsoluteDate currentDate = new AbsoluteDate(m_clock.getTime().toString(), utc);
        SpacecraftState currentState = kepler.propagate(currentDate);
        m_trueAnomaly = new KeplerianOrbit(currentState.getOrbit()).getTrueAnomaly();
        return m_trueAnomaly;
    }

    public SpacecraftGeometry(Clock clock) {
        m_clock = clock;
        Frame inertialFrame = FramesFactory.getEME2000();

        AbsoluteDate initialDate = new AbsoluteDate(m_clock.getTime().toString(), utc);

        double mu =  3.986004415e+14;

        double a = 24396159;                     // semi major axis in meters
        double e = 0.72831215;                   // eccentricity
        double i = FastMath.toRadians(7);        // inclination
        double omega = FastMath.toRadians(180);  // perigee argument
        double raan = FastMath.toRadians(261);   // right ascension of ascending node
        double lM = 0;                           // mean anomaly

        KeplerianOrbit initialOrbit = new KeplerianOrbit(a, e, i, omega, raan, lM, PositionAngle.MEAN,
                inertialFrame, initialDate, mu);
        this.m_trueAnomaly = initialOrbit.getTrueAnomaly();
        kepler = new KeplerianPropagator(initialOrbit);
    };

}
