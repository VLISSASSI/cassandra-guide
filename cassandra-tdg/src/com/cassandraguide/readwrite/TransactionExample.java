/*
 * Copyright (C) 2016 Jeff Carpenter
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cassandraguide.readwrite;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.SimpleStatement;

public class TransactionExample {
	
	public static void main(String[] args) {
		
		Cluster cluster = Cluster.builder().addContactPoint("127.0.0.1")
				//.withCredentials("jeff", "i6XJsj!k#9")
				.build();
		
		// create session on the "hotel" keyspace
		Session session = cluster.connect("hotel");
		
		// create parameterized INSERT statement
		SimpleStatement hotelInsert = session.newSimpleStatement(
				"INSERT INTO hotels (id, name, phone) VALUES (?, ?, ?) IF NOT EXISTS",
				"AZ123", "Super Hotel at WestWorld", "1-888-999-9999");
		
		ResultSet hotelInsertResult = session.execute(hotelInsert);
		
		// result metadata
		System.out.println(hotelInsertResult);
		System.out.println(hotelInsertResult.wasApplied());
		System.out.println(hotelInsertResult.getExecutionInfo());
		System.out.println(hotelInsertResult.getExecutionInfo().getQueryTrace());
		
		// print results
		for (Row row : hotelInsertResult) {
			System.out.format("id: %s, name: %s, phone: %s\n\n", row.getString("id"), row.getString("name"), row.getString("phone"));
		}
		
		// close and exit
		cluster.close();
		System.exit(0);
	}
		
		
}
