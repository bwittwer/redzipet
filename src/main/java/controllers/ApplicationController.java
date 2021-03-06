/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import com.google.inject.Singleton;

import metrics_influxdb.HttpInfluxdbProtocol;
import metrics_influxdb.measurements.HttpInlinerSender;
import metrics_influxdb.measurements.Measure;
import metrics_influxdb.measurements.Sender;
import ninja.Result;
import ninja.Results;
import ninja.metrics.Timed;
import ninja.params.PathParam;

@Singleton
public class ApplicationController {

	@Timed
	public Result index() {

		return Results.html();

	}

	@Timed
	public Result helloWorldJson() {

		SimplePojo simplePojo = new SimplePojo();
		simplePojo.content = "Hello World! Hello Json!";



		return Results.json().render(simplePojo);

	}

	public Result reportMeasure(@PathParam("name") String name, @PathParam("value") int value) {

		HttpInfluxdbProtocol protocol = new HttpInfluxdbProtocol("http", "192.168.0.35", 8086, "admin", "admin",
				"javametrics");
		Sender sender = new HttpInlinerSender(protocol);
		Measure m = new Measure(name, value);
		sender.send(m);
		sender.flush();
		
		
		return Results.json().render(m);

	}

	public static class SimplePojo {

		public String content;

	}
}
