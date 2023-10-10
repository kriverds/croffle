package com.soluvis.croffle.v1.gcloud.engine.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mypurecloud.sdk.v2.extensions.notifications.ChannelMetadataNotification;
import com.mypurecloud.sdk.v2.extensions.notifications.NotificationEvent;

public class ChannelMetadataListener implements com.mypurecloud.sdk.v2.extensions.notifications.NotificationListener<ChannelMetadataNotification>{

	private final Logger logger = LoggerFactory.getLogger(ChannelMetadataListener.class);

    @Override
	public String getTopic() {
        return "channel.metadata";
    }

    @Override
	public Class<?> getEventBodyClass() {
        return ChannelMetadataNotification.class;
    }

    @Override
	public void onEvent(NotificationEvent<?> notificationEvent) {
    	logger.info("{}", ("[channel.metadata] " +  ((ChannelMetadataNotification)notificationEvent.getEventBody()).getMessage()) );
    }
}