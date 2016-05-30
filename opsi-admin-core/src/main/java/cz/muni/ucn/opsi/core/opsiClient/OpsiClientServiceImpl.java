package cz.muni.ucn.opsi.core.opsiClient;

import java.io.IOException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import cz.muni.ucn.opsi.api.opsiClient.ProductPropertyState;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.CommonsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.client.Hardware;
import cz.muni.ucn.opsi.api.instalation.Installation;
import cz.muni.ucn.opsi.api.opsiClient.OpsiClientService;

/**
 * Implementation class which handles communication with OPSI server.
 *
 * @author Jan Dosoudil
 * @author Martin Kuba makub@ics.muni.cz
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public class OpsiClientServiceImpl implements OpsiClientService, InitializingBean {

	private RestTemplate template;

	private URL opsiUrl;
	private String userName;
	private String password;

	private AtomicInteger sequence = new AtomicInteger();

	@Override
	public void afterPropertiesSet() throws Exception {

		try {
			new URL("https://0.0.0.0/").getContent();
		} catch (IOException e) {
			// This invocation will always fail, but it will register the
			// default SSL provider to the URL class.
		}

		// set SSL context
		try {

			SSLContext sslc;

			sslc = SSLContext.getInstance("TLS");

			TrustManager[] trustManagerArray = { new X509TrustManager() {

				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return new X509Certificate[0];
				}

				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {

				}

				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1)
				throws CertificateException {

				}
			} };
			sslc.init(null, trustManagerArray, null);

			HttpsURLConnection.setDefaultSSLSocketFactory(sslc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});

		} catch(Exception e){
			e.printStackTrace();
		}

		// register SSL context to HTTPS connection
		Protocol easyhttps = new Protocol("https",(ProtocolSocketFactory) new EasySSLProtocolSocketFactory(), 443);
		Protocol.registerProtocol("https", easyhttps);

		// create HTTP client and set credentials
		HttpClient httpClient = new HttpClient();
		Credentials credentials = new UsernamePasswordCredentials(userName, password);
		int port = opsiUrl.getPort();
		if (-1 == port) {
			port = "https".equalsIgnoreCase(opsiUrl.getProtocol()) ? 443 : 80;
		}
		httpClient.getState().setCredentials(new AuthScope(opsiUrl.getHost(), port), credentials);

		// create request template used to exchange data
		CommonsClientHttpRequestFactory requestFactory = new CommonsClientHttpRequestFactory(httpClient);
		template = new RestTemplate(requestFactory);
		List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
		messageConverters.add(new MappingJacksonHttpMessageConverter());
		template.setMessageConverters(messageConverters);

	}

	@Override
	public void createClient(Client client) {

		String name = client.getName();
		int indexOf = name.indexOf(".");

		String hostname = name.substring(0, indexOf);
		String domainname = name.substring(indexOf + 1);
		String description = client.getDescription();
		String notes = client.getNotes();
		String ipaddress = client.getIpAddress();
		String macaddress = client.getMacAddress();

		callOpsi("createClient", hostname, domainname, description, notes, ipaddress, macaddress);

	}

	@Override
	public void deleteClient(Client client) {
		callOpsi("deleteClient", client.getName());
	}

	@Override
	public void updateClient(Client client) {

		callOpsi("setMacAddress", client.getName(), client.getMacAddress());

		String description = client.getDescription();
		if (null == description) {
			description = "";
		}
		String notes = client.getNotes();
		if (null == notes) {
			notes = "";
		}

		callOpsi("setHostDescription", client.getName(), description);

		callOpsi("setHostNotes", client.getName(), notes);

	}

	@Override
	public List<Installation> listInstallations() {

		OpsiResponse responseProds = callOpsi("getProducts_hash");

		@SuppressWarnings("unchecked")
		Map<String, Map<String, Map<String, String>>> depoMap =
			(Map<String, Map<String, Map<String, String>>>) responseProds.getResult();
		Map<String, Map<String, String>> prodMap = depoMap.values().iterator().next();

		OpsiResponse response = callOpsi("getNetBootProductIds_list", null, null);

		@SuppressWarnings("unchecked")
		List<Object> res = (List<Object>) response.getResult();
		List<Installation> ret = new ArrayList<Installation>();
		for (Object o : res) {
			Installation inst = new Installation();
			String id = (String) o;
			inst.setId(id);
			Map<String, String> prod = prodMap.get(id);
			if (null != prod) {
				inst.setName(prod.get("name"));
			} else {
				inst.setId(id);
			}
			ret.add(inst);
		}
		return ret;
	}

	@Override
	public Installation getInstallationById(String instalationId) {
		OpsiResponse responseProds = callOpsi("getProduct_hash", instalationId);

		@SuppressWarnings("unchecked")
		Map<String, String> produktMap = (Map<String, String>) responseProds.getResult();

		Installation inst = new Installation();
		String id = produktMap.get("productId");
		inst.setId(id);
		inst.setName(produktMap.get("name"));

		return inst;
	}

	@Override
	public void clientInstall(Client client, Installation i) {
		callOpsi("setProductActionRequest", i.getId(), client.getName(), "setup");

	}

	@Override
	public List<Client> listClientsForImport() {
		OpsiResponse response = callOpsi("getClients_listOfHashes");

		@SuppressWarnings("unchecked")
		List<Map<String, String>> clientMaps = (List<Map<String, String>>) response.getResult();
		List<Client> ret = new ArrayList<Client>(clientMaps.size());
		for (Map<String,String> map : clientMaps) {
			Client c = new Client(UUID.randomUUID());
			c.setName(map.get("hostId"));
			c.setDescription(map.get("description"));
			c.setNotes(map.get("notes"));

			String macAddress = map.get("hardwareAddress");
			if (StringUtils.isBlank(macAddress)) {
				macAddress = null;
			}
			c.setMacAddress(macAddress);

			String ipAddress = map.get("ipAddress");
			if (StringUtils.isBlank(ipAddress)) {
				ipAddress = null;
			}
			c.setIpAddress(ipAddress);
			ret.add(c);
		}

		return ret;
	}

	@Override
	public List<Hardware> listHardware(Client client) {
		OpsiResponse response = callOpsi("getHardwareInformation_listOfHashes");

		List<Hardware> hw = new ArrayList<Hardware>();
		return hw;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductPropertyState> getProductProperties(final String objectId) {
		//prepare params
		List<Object> attributes = Collections.emptyList();
        Map<String,String> filter = new HashMap<String, String>() {{put("objectId",objectId);}};
		//call
        OpsiResponse response = callOpsi("productPropertyState_getObjects", attributes, filter);
		//get result
		List<Map<String,Object>> result = (List<Map<String, Object>>) response.getResult();
		List<ProductPropertyState> l = new ArrayList<ProductPropertyState>(result.size());
		for (Map<String, Object> pmap : result) {
			List<String> values = new ArrayList<String>();
			for (Object obj : pmap.values()) {
				values.add(obj != null ? obj.toString() : null);
			}
			l.add(new ProductPropertyState(pmap.get("objectId").toString(), pmap.get("productId").toString(),
					pmap.get("propertyId").toString(), values
					));
		}
		return l;
	}

	@Override
	public void setProductProperties(List<ProductPropertyState> props) {
		callOpsi("productPropertyState_updateObjects", props);
	}

	/**
	 * Get thread save unique ID for requests.
	 *
	 * @return unique request ID.
	 */
	protected int getRequestId() {
		return sequence.incrementAndGet();
	}

	/**
	 * Call method from API on OPSI server.
	 *
	 * @param method method from OPSI API to call
	 * @param args method callback payload
	 *
	 * @return OPSI server response
	 */
	protected OpsiResponse callOpsi(String method, Object... args) {

		// if passed param is string, replace new-lines
		for (int i = 0; i < args.length; i++) {
			if (!(args[i] instanceof String)) {
				continue;
			}
			args[i] = sanityString((String) args[i]);
		}

		// create OPSI request
		OpsiRequest requestProds = new OpsiRequest();
		requestProds.setParams(Arrays.asList(args));
		requestProds.setId(getRequestId());
		requestProds.setMethod(method);

		// Call to OPSI must be thread save
		synchronized (this) {

			// exchange data with OPSI
			HttpEntity<OpsiRequest> requestEntity = new HttpEntity<OpsiRequest>(requestProds);
			ResponseEntity<OpsiResponse> responseProdsEntity = template.exchange(opsiUrl.toString(), HttpMethod.POST, requestEntity, OpsiResponse.class);

			// read response
			OpsiResponse response = responseProdsEntity.getBody();

			// if response was exception, get text and throw it
			Map<String, Object> error = response.getError();
			if (null != error && error.containsKey("message")) {
				throw new OpsiException(error.get("message").toString());
			}

			return response;

		}

	}

	/**
	 * Remove CR characters from values
	 *
	 * @param string String with CR characters
	 * @return String without CR characters
	 */
	private String sanityString(String string) {
		return StringUtils.replace(string, "\r", "");
	}

	/**
	 * Set URL of OSPI server to contact
	 *
	 * @param opsiUrl the opsiUrl to set
	 */
	@Required
	public void setOpsiUrl(URL opsiUrl) {
		this.opsiUrl = opsiUrl;
	}

	/**
	 * Set username used to login to OSPI server
	 *
	 * @param userName the userName to set
	 */
	@Required
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * Set password used to login to OSPI server
	 *
	 * @param password the password to set
	 */
	@Required
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Class representing request sent to OPSI server.
	 *
	 * @author Jan Dosoudil
	 */
	public static class OpsiRequest {

		private int id;
		private String method;
		private List<Object> params;

		/**
		 * Get ID of request
		 *
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * Set ID of request
		 *
		 * @param id the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * Get name of method from OPSI server API
		 *
		 * @return the method
		 */
		public String getMethod() {
			return method;
		}

		/**
		 * Set name of method from OPSI server API
		 *
		 * @param method the method to set
		 */
		public void setMethod(String method) {
			this.method = method;
		}

		/**
		 * Get list of parameters sent with request
		 *
		 * @return the params
		 */
		public List<Object> getParams() {
			return params;
		}

		/**
		 * Set list of parameters sent with request
		 *
		 * @param params the params to set
		 */
		public void setParams(List<Object> params) {
			this.params = params;
		}

	}

	/**
	 * Class representing response from OPSI server.
	 *
	 * @author Jan Dosoudil
	 */
	public static class OpsiResponse {

		private int id;
		private Object result;
		private Map<String, Object> error;

		/**
		 * Get ID of original request
		 *
		 * @return the id
		 */
		public int getId() {
			return id;
		}

		/**
		 * Set ID of original request
		 *
		 * @param id the id to set
		 */
		public void setId(int id) {
			this.id = id;
		}

		/**
		 * Get resulting object (OPSI server response)
		 *
		 * @return the result
		 */
		public Object getResult() {
			return result;
		}

		/**
		 * Set resulting object (OPSI server response)
		 *
		 * @param result the result to set
		 */
		public void setResult(Object result) {
			this.result = result;
		}

		/**
		 * Get resulting object if was an error (OPSI server response)
		 *
		 * @return the error
		 */
		public Map<String, Object> getError() {
			return error;
		}

		/**
		 * Set resulting object if was an error (OPSI server response)
		 *
		 * @param error the error to set
		 */
		public void setError(Map<String, Object> error) {
			this.error = error;
		}

	}

	/**
	 * Class representing Error object returned from OPSI server.
	 *
	 * @author Jan Dosoudil
	 */
	public static class OpsiError {

		private String message;
		private String messageClass;

		/**
		 * @return the message
		 */
		public String getMessage() {
			return message;
		}

		/**
		 * @param message the message to set
		 */
		public void setMessage(String message) {
			this.message = message;
		}

		/**
		 * @return the messageClass
		 */
		@JsonProperty("class")
		public String getMessageClass() {
			return messageClass;
		}

		/**
		 * @param messageClass the messageClass to set
		 */
		public void setMessageClass(String messageClass) {
			this.messageClass = messageClass;
		}

	}

}