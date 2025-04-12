package dmit2015.ejb;

import jakarta.annotation.Resource;
import jakarta.batch.operations.JobOperator;
import jakarta.batch.runtime.BatchRuntime;
import jakarta.batch.runtime.BatchStatus;
import jakarta.batch.runtime.JobExecution;
import jakarta.ejb.*;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.util.logging.Logger;

/**
 * A stateless session bean for monitoring the status of Batch Job started with an EJB Timer.
 * This class requires the following name/value pairs to be defined in the
 * Microprofile Config configuration file `src/main/resources/META-INF/microprofile-config.properties`.
 *
 * {@snippet file=/resources/META-INF/microprofile-properties.config lang="properties" :
 * batch.monitor.initialDuration=5000
 * batch.monitor.intervalDuration=30000
 * }
 */
@Stateless
public class MonitorBatchJobSessionBean {

    private final Logger _logger = Logger.getLogger(MonitorBatchJobSessionBean.class.getName());

    @Resource
    private TimerService _timerService;

    @Inject
    @ConfigProperty(name="batch.monitor.initialDuration", defaultValue = "5000")
    private long _initialDuration;  // in milliseconds

    @Inject
    @ConfigProperty(name="batch.monitor.intervalDuration", defaultValue = "3000")
    private long _intervalDuration;  // in milliseconds

    @Timeout
    public void checkBatchJobStatus(Timer timer) {
        // Extract the jobId from the timer
        long jobId = (long) timer.getInfo();
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        JobExecution jobExecution = jobOperator.getJobExecution(jobId);
        if (jobExecution.getBatchStatus() == BatchStatus.COMPLETED) {
            timer.cancel();
            _logger.info("BATCH job " + jobId + " COMPLETED");
            // send email to notified batch has COMPLETED
        } else if (jobExecution.getBatchStatus() == BatchStatus.FAILED) {
            // send email to notified batch job has FAILED
            timer.cancel();
            _logger.info("BATCH job " + jobId + " FAILED");
        }
    }

    public Timer createTimer(long jobId) {
        return _timerService.createTimer(_initialDuration, _intervalDuration, jobId);
    }

}