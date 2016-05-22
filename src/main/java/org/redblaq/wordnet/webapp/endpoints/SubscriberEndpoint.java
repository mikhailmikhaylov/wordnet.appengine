package org.redblaq.wordnet.webapp.endpoints;

import org.redblaq.wordnet.webapp.services.ChannelService;
import org.redblaq.wordnet.webapp.services.CompositionRoot;
import org.redblaq.wordnet.webapp.util.ServletHelper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class SubscriberEndpoint extends HttpServlet {

    private ChannelService channelService = CompositionRoot.INSTANCE.resolve(ChannelService.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final String clientId = UUID.randomUUID().toString();

        final String channelId = channelService.openChannel(clientId);
        ServletHelper.respondRaw(resp, channelId);
    }
}
