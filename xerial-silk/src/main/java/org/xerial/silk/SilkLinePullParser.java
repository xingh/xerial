/*--------------------------------------------------------------------------
 *  Copyright 2009 Taro L. Saito
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *--------------------------------------------------------------------------*/
//--------------------------------------
// XerialJ
//
// SilkPullParser.java
// Since: Jan 28, 2009 1:00:02 PM
//
// $URL$
// $Author$
//--------------------------------------
package org.xerial.silk;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.xerial.core.XerialException;
import org.xerial.util.ArrayDeque;

/**
 * Pull parser of the Silk format. Pull-style means each parsing event is
 * generated when next() method is called, suited to stream processing.
 * 
 * @author leo
 * 
 */
public class SilkLinePullParser {

    private final int eventQueueMax = 10000;
    volatile private boolean foundEOF = false;

    private ArrayBlockingQueue<SilkEvent> eventQueue = new ArrayBlockingQueue<SilkEvent>(
            eventQueueMax);

    private SilkLinePushParser parser;
    private ExecutorService threadPool;

    public SilkLinePullParser(URL silkURL) throws IOException {
        this(silkURL.openStream());
    }

    public SilkLinePullParser(InputStream input) throws IOException {
        this(new InputStreamReader(input));
    }

    public SilkLinePullParser(Reader input) throws IOException {
        threadPool = Executors.newFixedThreadPool(1);

        parser = new SilkLinePushParser(input);
        threadPool.submit(new SilkEventProducer());
    }

    private class SilkEventProducer implements Callable<Boolean>, SilkEventHandler {
        public SilkEventProducer() {}

        public void handle(SilkEvent event) throws XerialException {
            try {
                if (!Thread.currentThread().isInterrupted())
                    eventQueue.put(event);
            }
            catch (InterruptedException e) {

            }
        }

        public Boolean call() throws Exception {
            parser.parse(this);
            foundEOF = true;
            threadPool.shutdownNow();
            return true;
        }

    }

    private ArrayDeque<SilkEvent> prefetchedEventQueue = new ArrayDeque<SilkEvent>();

    public boolean hasNext() throws XerialException {
        if (!prefetchedEventQueue.isEmpty())
            return true;

        if (foundEOF) {
            return !eventQueue.isEmpty();
        }

        fetchNext();

        return hasNext();
    }

    public SilkEvent next() throws XerialException {
        if (!prefetchedEventQueue.isEmpty())
            return prefetchedEventQueue.removeFirst();

        if (foundEOF)
            return eventQueue.poll();

        fetchNext();

        return next();
    }

    private void fetchNext() throws XerialException {
        try {
            SilkEvent e = null;
            while (!foundEOF && (e = eventQueue.poll(1, TimeUnit.MILLISECONDS)) == null) {}
            if (e != null)
                prefetchedEventQueue.addLast(e);
        }
        catch (InterruptedException e) {
            foundEOF = true;
        }
        return;
    }

    public long getNumReadLine() {
        return parser.getNumReadLine();
    }

}
