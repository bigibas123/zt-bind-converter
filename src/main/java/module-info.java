module zt.bind.converter {
	//requires ch.qos.logback.classic;
	requires com.google.gson;
	opens com.github.bigibas123.zt.bind.converter.model.networklist to com.google.gson;
	opens com.github.bigibas123.zt.bind.converter.model.memberlist to com.google.gson;
	requires inet.ipaddr;
	requires org.apache.httpcomponents.httpclient.fluent;
	requires org.apache.httpcomponents.httpclient;
	requires static lombok;
	requires org.slf4j;
}