package iss.models.time;

import gov.nasa.jpl.aerie.contrib.models.Clock;
import gov.nasa.jpl.aerie.contrib.models.Register;
import gov.nasa.jpl.aerie.merlin.protocol.types.Duration;
import iss.mission.Mission;
import org.orekit.time.*;

import static gov.nasa.jpl.aerie.merlin.framework.ModelActions.delay;

public class TimeTrackerDaemon {

    private final TimeScale utc = TimeScalesFactory.getUTC();

    private Clock m_clock;

    private AbsoluteDate currentDate;

    private DateComponents dateComp;

    private TimeComponents timeComp;

    public Mission m_mission;

    public TimeTrackerDaemon(final Mission mission) {
        m_mission = mission;
    }

    private void setTimeResources(AbsoluteDate now) {
        dateComp = now.getComponents(utc).getDate();
        timeComp = now.getComponents(utc).getTime();

        m_mission.month.set( dateComp.getMonthEnum().toString() );
        m_mission.dayOfYear.set( Integer.toString(dateComp.getDayOfYear()) );
        m_mission.hour.set( Integer.toString(timeComp.getHour()) );

    }

    public void run(){

        // Determine current time to correctly set current resource values
        currentDate = new AbsoluteDate(m_mission.clock.getTime().toString(), utc);
        setTimeResources(currentDate);

        //
        // Determine duration to next time boundary and progress time
        //

        // Shift time forward to right on the next hour
        AbsoluteDate nextDate = currentDate.shiftedBy( 3600.0 - (timeComp.getMinute()*60.0 + timeComp.getSecond()) );
        double duration = nextDate.durationFrom(currentDate);
        delay(Duration.roundNearest(duration, Duration.SECONDS ));

        // Set resource values
        setTimeResources(nextDate);

        // Progress time by standard duration, set resources, and loop until end of simulation
        while(true) {
            nextDate = nextDate.shiftedBy(3600.0 );
            delay(Duration.HOUR);
            setTimeResources(nextDate);
        }

    }
}
