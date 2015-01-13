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
 * <p/>
 * Use it only when in devel environment.
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

	public static SocketFactory getDefault() {
		return blindSSLSocketFactory;
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

}