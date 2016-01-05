package org.fuse.usecase;

import org.apache.directory.server.core.api.DirectoryService;
import org.apache.directory.server.core.api.partition.Partition;
import org.apache.directory.server.core.factory.DefaultDirectoryServiceFactory;
import org.apache.directory.server.core.factory.DirectoryServiceFactory;
//import org.apache.directory.server.core.partition.Partition;
//import org.apache.directory.server.core.partition.impl.btree.jdbm.JdbmPartition;
import org.apache.directory.server.core.partition.impl.avl.AvlPartition;
import org.apache.directory.server.ldap.LdapServer;
import org.apache.directory.server.protocol.shared.store.LdifFileLoader;
import org.apache.directory.server.protocol.shared.transport.TcpTransport;
import org.apache.directory.shared.ldap.model.name.Dn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class EmbedLDAPServer {

    private File workDir;
    private DirectoryService dirService;
    private LdapServer ldapServer;

    private String ldif;

    private static final Logger LOG = LoggerFactory.getLogger(EmbedLDAPServer.class);

    public static void main(String[] args) throws Exception {
        LOG.info("Start LDAP Server ...");
        EmbedLDAPServer server = new EmbedLDAPServer();
        server.init();
        LOG.info("LDAP Server started");
    }

    public void init() throws Exception {
        DirectoryServiceFactory lFactory = new DefaultDirectoryServiceFactory();
        lFactory.init("Standalone");
        LOG.info("Factory created");

        DirectoryService lService = lFactory.getDirectoryService();
        lService.getChangeLog().setEnabled(false);
        lService.setShutdownHookEnabled(true);

/*        Partition lPartition = new AvlPartition(lService.getSchemaManager());
        lPartition.setId("demo");
        lPartition.setSuffixDn(new Dn(lService.getSchemaManager(), "ou=ActiveMQ,ou=system"));
        lPartition.initialize();
        lService.addPartition(lPartition);*/

        LdapServer lServer = new LdapServer();
        lServer.setTransports(new TcpTransport("localhost", 33389));
        lServer.setDirectoryService(lService);
        LOG.info("Server initialized");

        lService.startup();
        lServer.start();

        new LdifFileLoader(lService.getAdminSession(), getResourceAsStream(ldif), null).execute();
        LOG.info("LDIF data loaded");
    }

    private File getResourceAsStream(String name) {
        URL url = EmbedLDAPServer.class.getResource(name);
        File f;
        try {
            f = new File(url.toURI());
        } catch (URISyntaxException e) {
            f = new File(url.getPath());
        }
        return f;
    }

    public String getLdif() {
        return ldif;
    }

    public void setLdif(String ldif) {
        this.ldif = ldif;
    }

}
