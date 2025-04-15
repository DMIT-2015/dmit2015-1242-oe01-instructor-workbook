package dmit2015.batch.listener;

import dmit2015.ejb.EmailSessionBean;
import jakarta.batch.api.listener.AbstractJobListener;
import jakarta.batch.runtime.context.JobContext;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.ArrayList;
import java.util.List;
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

    @Inject
    private EmailSessionBean mailBean;

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

        var templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/thymeleaf/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCharacterEncoding("UTF-8");

        var templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        var thymeleafContext = new Context();

        List<Object[]> enforcementZoneCentrePoints = entityManager
                .createNativeQuery("""
SELECT TOP (5) [site_id]
      ,[latitude]
      ,[longitude]
      ,[speed_limit]
      ,[location_description]
  FROM [DMIT2015CourseDB].[dbo].[edmonton_scheduled_photo_enforcement_zone_centre_point]
""")
                .getResultList();

        thymeleafContext.setVariable("columnNames",
                new String[] {"site_id","latitude","longitude","speed_limit","location_description"});
        thymeleafContext.setVariable("matrix", enforcementZoneCentrePoints);
        thymeleafContext.setVariable("pageTitle", "Enforcement Zone Centre Point");
        thymeleafContext.setVariable("tableName", "<h2>edmonton_scheduled_photo_enforcement_zone_centre_point table contents</h2>");

        String htmlMailBodyContent = templateEngine.process("html-table-email-template", thymeleafContext);
        mailBean.sendHtmlEmail("yourusername@nait.ca","DMIT2015-OE01 Email Demo", htmlMailBodyContent);
        _logger.info("email sent");
    }

}