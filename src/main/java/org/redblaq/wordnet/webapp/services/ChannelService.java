package org.redblaq.wordnet.webapp.services;

import com.google.appengine.api.channel.ChannelMessage;
import com.google.appengine.api.channel.ChannelServiceFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * AppEngine Channel Api wrapper.
 */
@SuppressWarnings("WeakerAccess") // API
// TODO:WEAK:mikhail.mikhaylov: Leaked ids cleanup required (64 bytes/request, 32 bytes/client).
public class ChannelService {

    private final com.google.appengine.api.channel.ChannelService channelService;
    private final Map<String, String> taskChannels = new HashMap<>();

    /* package */ ChannelService() {
        channelService = ChannelServiceFactory.getChannelService();
    }

    /**
     * Opens a new channel.
     * <p>
     * Note: you should only have one channel per page.
     *
     * @param credential channel credential
     * @return opened channel's token id.
     */
    public String openChannel(String credential) {
        return channelService.createChannel(credential);
    }

    /**
     * Sends a message to a chosen client.
     */
    public void sendMessage(String channelId, String message) {
        channelService.sendMessage(new ChannelMessage(channelId, message));
    }

    public void registerTask(String taskId, String channelId) {
        taskChannels.put(channelId, taskId);
    }

    public String getTaskChannel(String taskId) {
        return taskChannels.get(taskId);
    }
}
