package com.infineon.esim.lpa.lpa.task;

import com.infineon.esim.lpa.core.dtos.result.remote.HandleNotificationsResult;
import com.infineon.esim.lpa.lpa.LocalProfileAssistant;

import java.util.concurrent.Callable;

public class HandleAllNotificationsTask implements Callable<Boolean> {
    private final LocalProfileAssistant lpa;

    public HandleAllNotificationsTask(LocalProfileAssistant lpa) {
        this.lpa = lpa;
    }

    @Override
    public Boolean call() throws Exception {
        // handle the pending notifications
        HandleNotificationsResult handleNotificationsResult = lpa.handleNotifications();

        return handleNotificationsResult.getSuccess();
    }
}
