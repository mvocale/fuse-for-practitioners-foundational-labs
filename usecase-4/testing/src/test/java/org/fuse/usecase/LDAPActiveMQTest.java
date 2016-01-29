package org.fuse.usecase;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.JMSSecurityException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerFactory;
import org.apache.activemq.broker.BrokerService;
import org.apache.directory.server.annotations.CreateLdapServer;
import org.apache.directory.server.annotations.CreateTransport;
import org.apache.directory.server.core.annotations.ApplyLdifFiles;
import org.apache.directory.server.core.integ.AbstractLdapTestUnit;
import org.apache.directory.server.core.integ.FrameworkRunner;
import org.apache.directory.server.ldap.LdapServer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

@RunWith(FrameworkRunner.class)
@CreateLdapServer(transports = { @CreateTransport(protocol = "LDAP", port = 1024) })
@ApplyLdifFiles("org/fuse/usecase/activemq.ldif")
public class LDAPActiveMQTest extends AbstractLdapTestUnit {

	public BrokerService broker;
	public static LdapServer ldapServer;
	
	@Rule 
	public ExpectedException thrown= ExpectedException.none();

	@Before
	public void setup() throws Exception {
		System.setProperty("ldapPort",
				String.valueOf(getLdapServer().getPort()));
		broker = BrokerFactory
				.createBroker("xbean:org/fuse/usecase/activemq-broker.xml");
		broker.start();
		broker.waitUntilStarted();
	}

	@After
	public void shutdown() throws Exception {
		broker.stop();
		broker.waitUntilStopped();
	}

	@Test
	public void testFailCreateSessionNotEnoughRight() throws Exception {
		
	}

	@Test
	public void testCreateQueuePublishConsume() throws Exception {
		String uri = "tcp://localhost:61616?jms.watchTopicAdvisories=false";
		Connection connection = null;
		Session session = null;
        try {
        	ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(uri);
            connection = factory.createConnection("jdoe", "sunflower");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("usecase-input");
            MessageProducer producer = session.createProducer(queue);
            TextMessage test = session.createTextMessage("test-message");
            producer.send(test);
            
            /**MessageConsumer consumer = session.createConsumer(queue);
            Message message = consumer.receive(4000);
            if (message != null) {
                if (message instanceof TextMessage) {
                    String text = ((TextMessage) message).getText();
                    System.out.println("Ci passo!!!!!!!!!!!!!!!!!");
                    Assert.assertEquals(text, "test-message");
                }
            }**/
        } finally {
        	if (connection != null) {
        		connection.close();
        	}
        	if (session != null) {
        		session.close();
        	}
        }
	}

	@Test
	public void testFailCreateQueuePublishConsume() throws Exception {
		thrown.expect(JMSException.class);
        thrown.expectMessage("User jdoe is not authorized to write to: queue://usecase-input2");
		
		
		String uri = "tcp://localhost:61616?jms.watchTopicAdvisories=false";
		Connection connection = null;
		Session session = null;
        try {
        	ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(uri);
            connection = factory.createConnection("jdoe", "sunflower");
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = session.createQueue("usecase-input2");
            MessageProducer producer = session.createProducer(queue);
            TextMessage test = session.createTextMessage("test-message");
            producer.send(test);
            thrown.expect(JMSException.class);
            thrown.expectMessage("User jdoe is not authorized to write to: queue://usercase-input2");
        } finally {
        	if (connection != null) {
        		connection.close();
        	}
        	if (session != null) {
        		session.close();
        	}
        }
	}

}