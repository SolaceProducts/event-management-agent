package com.solace.maas.ep.event.management.agent.route.scheduler;

import com.solace.maas.ep.event.management.agent.plugin.constants.RouteConstants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SchedulerRouteBuilder extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("seda://jobScheduler")
                .choice()
                .when(simple("${header.SCHEDULER_TYPE} == 'CRON'"))
                .bean(this, "createCronRoute(${header." + RouteConstants.SCHEDULE_ID + "}," +
                        "${header.CRON_EXPRESSION}, ${header.DESTINATION}, ${body})")
                .otherwise()
                .bean(this, "createIntervalRoute(${header." + RouteConstants.SCHEDULE_ID + "}," +
                        "${header.DESTINATION}, ${header.SCHEDULER_INTERVAL}, ${header.SCHEDULER_REPEAT_COUNT}," +
                        "${body})")
                .end();

        from("seda://stopScheduler")
                .toD("${header.DESTINATION}")
                .bean(this, "stopScheduler(${header." + RouteConstants.SCHEDULE_ID + "})");
    }

    public void createIntervalRoute(String schedulerId, String destination, Integer interval, Integer repeatCount,
                                    Object signal) throws Exception {
        addRoutes(getContext(), rbc -> rbc.from("quartz://intervalScheduler/" + schedulerId +
                        "?triggerStartDelay=" + interval + "&trigger.repeatCount=" + repeatCount)
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
