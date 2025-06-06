package dmit2015.batch.listener;

import jakarta.batch.api.listener.AbstractStepListener;
import jakarta.batch.runtime.context.JobContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * This executes before and after a step execution runs.
 * To apply this listener to a batch job you must define a listener element in the Job Specification Language (JSL) file
 * BEFORE the step element as follows:
 *
 <listeners>
    <listener ref="itemStepListener" />
 </listeners>
 */
@Named
@Dependent
public class ItemStepListener extends AbstractStepListener {

    @Inject
    private JobContext _jobContext;
    private long _startTime;

    @Override
    public void beforeStep() throws Exception {
        System.out.println("beforeStep");
        _startTime = System.currentTimeMillis();

        // Delete the output if it exists

        Properties jobParameters = _jobContext.getProperties();
        String output_file = jobParameters.getProperty("output_file");
        if (output_file != null) {
            Path outputFilePath = Paths.get(output_file);
            Files.deleteIfExists(outputFilePath);
        }
    }

    @Override
    public void afterStep() throws Exception {
        System.out.println("afterStep");
        long endTime = System.currentTimeMillis();
        long duration = endTime - _startTime;
        String message = _jobContext.getJobName() + " completed in " + duration + " milliseconds";
        System.out.println(message);

    }
}