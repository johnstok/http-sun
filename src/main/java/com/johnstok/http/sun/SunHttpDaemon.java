/*-----------------------------------------------------------------------------
 * Copyright © 2013 Keith Webster Johnston.
 * All rights reserved.
 *
 * This file is part of http-sun.
 *
 * http-sun is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * http-sun is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License
 * along with http-sun. If not, see <http://www.gnu.org/licenses/>.
 *---------------------------------------------------------------------------*/
package com.johnstok.http.sun;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import com.johnstok.http.sync.Handler;
import com.johnstok.http.sync.Server;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


/**
 * A Daemon that uses Sun's JRE HTTP server.
 *
 * @author Keith Webster Johnston.
 */
@SuppressWarnings("restriction")
public class SunHttpDaemon
    implements
        Server, HttpHandler {

    private HttpServer _hs;
    private final Handler _handler;
    private final Charset _charset;


    /**
     * Constructor.
     *
     * @param handler
     */
    public SunHttpDaemon(final Handler handler, final Charset charset) {
        _handler = handler; // FIXME: Check not null.
        _charset = charset; // FIXME: Check not null.
    }


    /** {@inheritDoc} */
    @Override
    public void startup(final InetSocketAddress address) throws IOException {
        // FIXME: Check not started.
        _hs = HttpServer.create(address, 100); // FIXME: Check address not null.
        _hs.createContext("/", this);                              //$NON-NLS-1$
        _hs.start();
    }


    /** {@inheritDoc} */
    @Override
    public void shutdown() throws IOException {
        // FIXME: Check not stopped.
        _hs.stop(5);
        _hs = null;
    }


    /** {@inheritDoc} */
    @Override
    public void handle(final HttpExchange exchange) {
        try {
            _handler.handle(
                new SunHttpRequest(exchange, _charset),
                new SunHttpResponse(exchange));

        } catch (final IOException e) {
            e.printStackTrace(); // FIXME: WTF.

        } finally {
            exchange.close();
        }
    }
}
