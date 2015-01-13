package cz.muni.ucn.opsi.wui.gwt.client.remote;

/**
 * Interface for all remote requests to server
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
public interface RemoteRequestCallback<V> {

	/**
	 * Method called when callback finishes
	 *
	 * @param v object passed by server response
	 */
	void onRequestSuccess(V v);

	/**
	 * Called when callback fails
	 *
	 * @param th Exception thrown
	 */
	void onRequestFailed(Throwable th);

}