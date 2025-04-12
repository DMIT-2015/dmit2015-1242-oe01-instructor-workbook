package dmit2015.batch.listener;

import jakarta.batch.api.listener.JobListener;
import jakarta.batch.runtime.context.JobContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.util.logging.Logger;

@Named
@Dependent
public class EtlProcessForDWPubsSalseListener implements JobListener {

    @Inject
    private JobContext _jobContext;

    @Inject
    private Logger _logger;

    private long _startTime;

    @Override
    public void beforeJob() throws Exception {
        _logger.info(_jobContext.getJobName() + " beforeJob");
        _startTime = System.currentTimeMillis();


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
