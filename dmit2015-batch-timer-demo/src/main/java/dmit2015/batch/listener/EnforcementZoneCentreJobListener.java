package dmit2015.batch.listener;

import jakarta.batch.api.listener.AbstractJobListener;
import jakarta.batch.runtime.context.JobContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.logging.Logger;

/**
 * This listener contains methods that executes before and after a job execution runs.
 * To apply this listener to a batch job you must define a listener element in the Job Specification Language (JSL) file
 * BEFORE the first step element as follows:
 * <pre>{@code
 *
 * <listeners>
 *      <listener ref="enforcementZoneCentreJobListener" />
 * </listeners>
 *
 * }</pre>
 */
@Named
@Dependent
public class EnforcementZoneCentreJobListener extends AbstractJobListener {

    @Inject
    private JobContext _jobContext;

    private Logger _logger = Logger.getLogger(EnforcementZoneCentreJobListener.class.getName());

    private long _startTime;

    @PersistenceContext(unitName = "mssql-jpa-pu")
    private EntityManager entityManager;

    @Transactional
    @Override
    public void beforeJob() throws Exception {
        _logger.info(_jobContext.getJobName() + " beforeJob");
        _startTime = System.currentTimeMillis();

        _logger.info("deleting all rows from edmonton_scheduled_photo_enforcement_zone_centre_point");
        entityManager
                .createNativeQuery("delete from edmonton_scheduled_photo_enforcement_zone_centre_point")
                .executeUpdate();
    }

    @Override
    public void afterJob() throws Exception {
        _logger.info(_jobContext.getJobName() + "afterJob");
        long endTime = System.currentTimeMillis();
        long durationSeconds = (endTime - _startTime) / 1000;
        String message = _jobContext.getJobName() + " completed in " + durationSeconds + " seconds";
        _logger.info(message);

    }

}