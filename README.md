This is a camel and activemq project.

project-a is a sender sending messages to activemq every 10 seconds.
project-b is reading the messages from the same queue.

to run an activemq, execute the below command in docker cmd

	docker run -p 61616:61616 -p 8161:8161 rmohr/activemq
