/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.samza.example;

import com.google.common.collect.ImmutableList;
import org.apache.samza.application.StreamApplication;
import org.apache.samza.config.Config;
import org.apache.samza.operators.MessageStream;
import org.apache.samza.operators.OutputStream;
import org.apache.samza.operators.StreamGraph;
import org.apache.samza.runtime.LocalApplicationRunner;
import org.apache.samza.util.CommandLine;

public class MergeExample implements StreamApplication {

  @Override
  public void init(StreamGraph graph, Config config) {
    MessageStream<Object> inputStream1 = graph.getInputStream("inputStream1", (k, m) -> m);
    MessageStream<Object> inputStream2 = graph.getInputStream("inputStream2", (k, m) -> m);
    MessageStream<Object> inputStream3 = graph.getInputStream("inputStream3", (k, m) -> m);
    OutputStream<Integer, Object, Object> outputStream = graph
        .getOutputStream("outputStream", Object::hashCode, m -> m);

    MessageStream.mergeAll(ImmutableList.of(inputStream1, inputStream2, inputStream3))
        .sendTo(outputStream);
  }

  // local execution mode
  public static void main(String[] args) throws Exception {
    CommandLine cmdLine = new CommandLine();
    Config config = cmdLine.loadConfig(cmdLine.parser().parse(args));
    LocalApplicationRunner localRunner = new LocalApplicationRunner(config);
    localRunner.run(new MergeExample());
  }
}