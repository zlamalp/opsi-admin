## OPSI Admin WUI

This application provides an alternative web user interface to OPSI and also enchants OPSI own possibilities.

### Goals

* Provide simple and localized (Czech) UI with required functionality only.
* Organize clients into groups.
* Set access rights to groups, clients and netboot products.
* Import / Export client entries between OPSI servers.

This app consist of 2 main parts:

* Client web app, which provides simplified UI for OPSI and apps own purpose.
* Server part, which handles authorization of users to respective agendas, allow grouping of clients and communicate with the rest of systems.

This app require own DB (MySQL) for storing groups information and LDAP server to provide authorization for users.

### Build

This app uses Spring MVC, Hibernate, GWT 2.3 (GXT - Sencha) with some closed source libraries from the original author. In order to run or compile the app you need to gather all parts.

You can then use Maven to build production war package:

```
mvn clean install -Pmysql
```

### Development and local run

Due to secured environment you must setup a lot of resources locally with valid server certificates in order to run the app in GWT development mode (own MySQL, LDAP).
If you have any existing (pre-production) environment, we recommend to use it and connect by SSH tunnels (change hostname/port in config files to localhost).

```
# tunnel for OPSI server RPC
ssh -L 4447:localhost:4447 login@server.domain

# tunnel for MySQL DB
ssh -L 3306:localhost:3306 login@server.domain

# tunnel for LDAP
ssh -L 10636:localhost:636 login@server.domain
```

* You must choose port above 10000 (e.g. 10636) for LDAP since privileged ports (below 10000) can't be redirected.
* You must have a valid user entry in LDAP with password, because otherwise you won't pass apps login screen.

In order to skip server certificates validation (necessary when redirecting ports) you must add following into bean definition of *contextSource* in *security.xml* file.

```xml
<!-- Skip cert verification for devel purpose -->
<property name="baseEnvironmentProperties">
	<map>
		<entry key="java.naming.ldap.factory.socket" value="cz.muni.ucn.opsi.wui.security.BlindSSLSocketFactory"/>
	</map>
</property>
```

You also need to create above mentioned java class:

```java
package cz.muni.ucn.opsi.wui.security;

import javax.net.SocketFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * SSLSocketFactory that does not check certificates.
 * Set up using java.naming.ldap.factory.socket=cz.muni.ucn.opsi.wui.security.BlindSSLSocketFactory for LDAP environment.
 *
 * @author Martin Kuba makub@ics.muni.cz
 */
public class BlindSSLSocketFactory extends SSLSocketFactory {

	static TrustManager[] trustAllCerts = new TrustManager[]{
			new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
					if (certs != null && certs.length > 0) {
						System.out.println("accepting SSL cert " + certs[0].getSubjectX500Principal().getName());
					}
				}
			}
	};
	static SSLSocketFactory socketFactory;
	static BlindSSLSocketFactory blindSSLSocketFactory = new BlindSSLSocketFactory();

	static {
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			socketFactory = sc.getSocketFactory();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String[] getDefaultCipherSuites() {
		return socketFactory.getDefaultCipherSuites();
	}

	@Override
	public String[] getSupportedCipherSuites() {
		return socketFactory.getSupportedCipherSuites();
	}

	@Override
	public Socket createSocket(Socket socket, String s, int i, boolean b) throws IOException {
		return socketFactory.createSocket(socket, s, i, b);
	}

	@Override
	public Socket createSocket() throws IOException {
		return socketFactory.createSocket();
	}

	@Override
	public Socket createSocket(String s, int i) throws IOException {
		return socketFactory.createSocket(s, i);
	}

	@Override
	public Socket createSocket(String s, int i, InetAddress inetAddress, int i1) throws IOException {
		return socketFactory.createSocket(s, i, inetAddress, i1);
	}

	@Override
	public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
		return socketFactory.createSocket(inetAddress, i);
	}

	@Override
	public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress1, int i1) throws IOException {
		return socketFactory.createSocket(inetAddress, i, inetAddress1, i1);
	}

	public static SocketFactory getDefault() {
		return blindSSLSocketFactory;
	}
}
```

Now you can start the app by running command inside ``opsi-admin-wui/`` folder:

```
mvn gwt:run -DskipTests -Pmysql
```

You might need to compile *opsi-admin-api* and *opsi-admin-core* projects first or when changed and then restart development mode.

Since the app is using GWT 2.3, you will need an old browser (eg. Firefox 24 ESR) with development plugin in order to run the app in devel mode. If you don't want to debug the app, just run it - you can simply ``clean install`` it and then when you start it using ``gwt:run`` omit codeserver part of the URL.