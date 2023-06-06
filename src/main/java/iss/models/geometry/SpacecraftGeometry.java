package iss.models.geometry;

import gov.nasa.jpl.aerie.contrib.models.Clock;
import iss.mission.Configuration;
import iss.mission.DayNight;
import org.hipparchus.geometry.euclidean.threed.Vector3D;
import org.hipparchus.util.FastMath;
import org.orekit.bodies.CelestialBody;
import org.orekit.bodies.CelestialBodyFactory;
import org.orekit.bodies.GeodeticPoint;
import org.orekit.bodies.OneAxisEllipsoid;
import org.orekit.frames.Frame;
import org.orekit.frames.FramesFactory;
import org.orekit.frames.Transform;
import org.orekit.propagation.analytical.tle.TLE;
import org.orekit.propagation.analytical.tle.TLEPropagator;
import org.orekit.time.AbsoluteDate;
import org.orekit.time.TimeScale;
import org.orekit.time.TimeScalesFactory;
import org.orekit.utils.Constants;
import org.orekit.utils.IERSConventions;
import org.orekit.utils.PVCoordinates;

import java.time.Instant;

public class SpacecraftGeometry {

    private PVCoordinates spacecraftState;

    private TLEPropagator tleprop;

    private TimeScale utc;

    private Clock m_clock;

    private AbsoluteDate currentDate;

    private CelestialBody sun  = CelestialBodyFactory.getSun();

    private CelestialBody earth = CelestialBodyFactory.getEarth();

    private OneAxisEllipsoid earthBody = new OneAxisEllipsoid( Constants.IERS2010_EARTH_EQUATORIAL_RADIUS,
            Constants.IERS2010_EARTH_FLATTENING, FramesFactory.getITRF( IERSConventions.IERS_2010, true));

    Vector3D sunInEME2000;

    Vector3D earthInEME2000;

    GeodeticPoint spacecraftPosGeo;

    private double solarPhase;

    // km above Earth
    public double getAltitude() {
        if (new AbsoluteDate(m_clock.getTime().toString(), utc).isAfter(currentDate)) {
            propagateState();
        }
        return spacecraftPosGeo.getAltitude() / 1000.0;
    }

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
        Transform fromTEMEtoEME2000 = tleprop.getFrame().getTransformTo( FramesFactory.getEME2000(), currentDate);
        spacecraftState = fromTEMEtoEME2000.transformPVCoordinates( tleprop.getPVCoordinates(currentDate));

        sunInEME2000 = sun.getPVCoordinates(currentDate, FramesFactory.getEME2000()).getPosition();
        earthInEME2000 = earth.getPVCoordinates(currentDate, FramesFactory.getEME2000()).getPosition();
        spacecraftPosGeo =  earthBody.transform( spacecraftState.getPosition(),
                FramesFactory.getEME2000(), currentDate );

    }



    public SpacecraftGeometry(Clock clock, Configuration config) {
        m_clock = clock;
        utc = TimeScalesFactory.getUTC();
        currentDate = new AbsoluteDate(m_clock.getTime().toString(), utc);

        TLE tle = new TLE(config.tleLine1, config.tleLine2);
        tleprop = TLEPropagator.selectExtrapolator(tle);
        Transform fromTEMEtoEME2000 = tleprop.getFrame().getTransformTo( FramesFactory.getEME2000(), currentDate);
        spacecraftState = fromTEMEtoEME2000.transformPVCoordinates( tleprop.getPVCoordinates(currentDate));

        sunInEME2000 = sun.getPVCoordinates(currentDate, FramesFactory.getEME2000()).getPosition();
        earthInEME2000 = earth.getPVCoordinates(currentDate, FramesFactory.getEME2000()).getPosition();
        solarPhase = FastMath.toDegrees(
                Vector3D.angle( spacecraftState.getPosition().subtract(earthInEME2000),
                        sunInEME2000.subtract(earthInEME2000)));
        spacecraftPosGeo =  earthBody.transform( spacecraftState.getPosition(),
                FramesFactory.getEME2000(), currentDate );
    };

}
