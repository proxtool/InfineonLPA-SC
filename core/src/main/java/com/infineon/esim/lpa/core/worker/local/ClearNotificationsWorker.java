/*
 * THE SOURCE CODE AND ITS RELATED DOCUMENTATION IS PROVIDED "AS IS". INFINEON
 * TECHNOLOGIES MAKES NO OTHER WARRANTY OF ANY KIND,WHETHER EXPRESS,IMPLIED OR,
 * STATUTORY AND DISCLAIMS ANY AND ALL IMPLIED WARRANTIES OF MERCHANTABILITY,
 * SATISFACTORY QUALITY, NON INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE.
 *
 * THE SOURCE CODE AND DOCUMENTATION MAY INCLUDE ERRORS. INFINEON TECHNOLOGIES
 * RESERVES THE RIGHT TO INCORPORATE MODIFICATIONS TO THE SOURCE CODE IN LATER
 * REVISIONS OF IT, AND TO MAKE IMPROVEMENTS OR CHANGES IN THE DOCUMENTATION OR
 * THE PRODUCTS OR TECHNOLOGIES DESCRIBED THEREIN AT ANY TIME.
 *
 * INFINEON TECHNOLOGIES SHALL NOT BE LIABLE FOR ANY DIRECT, INDIRECT OR
 * CONSEQUENTIAL DAMAGE OR LIABILITY ARISING FROM YOUR USE OF THE SOURCE CODE OR
 * ANY DOCUMENTATION, INCLUDING BUT NOT LIMITED TO, LOST REVENUES, DATA OR
 * PROFITS, DAMAGES OF ANY SPECIAL, INCIDENTAL OR CONSEQUENTIAL NATURE, PUNITIVE
 * DAMAGES, LOSS OF PROPERTY OR LOSS OF PROFITS ARISING OUT OF OR IN CONNECTION
 * WITH THIS AGREEMENT, OR BEING UNUSABLE, EVEN IF ADVISED OF THE POSSIBILITY OR
 * PROBABILITY OF SUCH DAMAGES AND WHETHER A CLAIM FOR SUCH DAMAGE IS BASED UPON
 * WARRANTY, CONTRACT, TORT, NEGLIGENCE OR OTHERWISE.
 *
 * (C)Copyright INFINEON TECHNOLOGIES All rights reserved
 */

package com.infineon.esim.lpa.core.worker.local;

import com.gsma.sgp.messages.rspdefinitions.NotificationEvent;
import com.gsma.sgp.messages.rspdefinitions.ListNotificationResponse;
import com.gsma.sgp.messages.rspdefinitions.NotificationMetadata;
import com.gsma.sgp.messages.rspdefinitions.NotificationSentResponse;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.lpa.core.es10.definitions.NotificationEvents;
import com.infineon.esim.util.Bytes;
import com.infineon.esim.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ClearNotificationsWorker {
    private static final String TAG = ClearNotificationsWorker.class.getName();

    private final Es10Interface es10Interface;

    public ClearNotificationsWorker(Es10Interface es10Interface) {
        this.es10Interface = es10Interface;
    }

    public List<Integer> clearNotifications() throws Exception {
        Log.debug(TAG, "Clearing all pending notifications...");

        List<Integer> returnValues = new ArrayList<>();

        // Get pending notifications (ENABLE/DISABLE) as list
        ListNotificationResponse listNotificationResponse = es10Interface.es10b_listNotification(NotificationEvents.CHANGE);

        // Remove notifications one by one
        for (NotificationMetadata notification : listNotificationResponse.getNotificationMetadataList().getNotificationMetadata()) {
            String seqNo = notification.getSeqNumber().toString();
            if(notification.getProfileManagementOperation().toString().equals("0001")) //01,001,delete0001
                Log.debug(TAG,"Ignoring remove notification with seqNo " + seqNo);
            else {
                Log.debug(TAG,"Removing notification with seqNo " + seqNo);
                NotificationSentResponse notificationSentResponse = es10Interface.es10b_removeNotificationFromList(notification.getSeqNumber());
                Log.debug(TAG,"Remove notification response for seqNo " + seqNo + ": " + notificationSentResponse);
                returnValues.add(notificationSentResponse.getDeleteNotificationStatus().intValue());
            } 
        }

        return returnValues;
    }
}
