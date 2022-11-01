package com.solace.maas.ep.event.management.agent.route.scheduler;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.SchedulerConstants;
import com.solace.maas.ep.event.management.agent.plugin.constants.SchedulerType;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SchedulerRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("seda://jobScheduler")
                .choice()
                .when(simple("${header." + SchedulerConstants.SCHEDULER_TYPE + "} == '" + SchedulerType.CRON.name() + "'"))
                .bean(this, "createCronRoute(${header." + RouteConstants.SCHEDULE_ID + "}," +
                        "${header." + SchedulerConstants.CRON_EXPRESSION + "}, ${header." +
                        SchedulerConstants.SCHEDULER_DESTINATION + "}, ${body})")
                .otherwise()
                .bean(this, "createIntervalRoute(${header." + RouteConstants.SCHEDULE_ID + "}," +
                        "${header." + SchedulerConstants.SCHEDULER_DESTINATION + "}, " +
                        "${header." + SchedulerConstants.SCHEDULER_START_DELAY + "}, " +
                        "${header." + SchedulerConstants.SCHEDULER_INTERVAL + "}, " +
                        "${header." + SchedulerConstants.SCHEDULER_REPEAT_COUNT + "}," +
                        "${body})")
                .end();

        from("seda://stopScheduler")
                .toD("${header." + SchedulerConstants.SCHEDULER_DESTINATION + "}")
                .bean(this, "stopScheduler(${header." + RouteConstants.SCHEDULE_ID + "})");
    }

    public void createIntervalRoute(String schedulerId, String destination, Integer startDelay, Integer interval,
                                    Integer repeatCount, Object signal) throws Exception {
        addRoutes(getContext(), rbc -> rbc.from("quartz://intervalScheduler/" + schedulerId +
                        "?triggerStartDelay=" + startDelay + "&trigger.repeatInterval=" + interval +
                        "&trigger.repeatCount=" + repeatCount)
                .routeId(schedulerId)
                .setHeader(RouteConstants.SCHEDULE_ID, constant(schedulerId))
                .setHeader("SIGNAL", constant(signal))
                .log("INTERVAL SCHEDULER EXECUTED!")
                .setBody(constant(signal))
                .toD(destination));
    }

    public void createCronRoute(String schedulerId, String cronExpression, String destination, Object signal) throws Exception {
        addRoutes(getContext(), rbc -> rbc.from("quartz://cronScheduler/" + schedulerId + "?cron=" + cronExpression)
                .routeId(schedulerId)
                .setHeader(RouteConstants.SCHEDULE_ID, constant(schedulerId))
                .setHeader("SIGNAL", constant(signal))
                .log("CRON SCHEDULER EXECUTED!")
                .setBody(constant(signal))
                .toD(destination));
    }

    public void stopScheduler(String schedulerId) throws Exception {
        getContext().getRouteController().stopRoute(schedulerId);
        getContext().getRouteController().resumeRoute(schedulerId);
    }
}
