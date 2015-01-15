package cz.muni.ucn.opsi.wui.remote.client;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.validation.Validator;

import cz.muni.ucn.opsi.api.opsiClient.OpsiClientService;
import cz.muni.ucn.opsi.api.opsiClient.ProductPropertyState;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVReader;
import cz.muni.ucn.opsi.api.client.Client;
import cz.muni.ucn.opsi.api.client.ClientService;
import cz.muni.ucn.opsi.api.client.Hardware;
import cz.muni.ucn.opsi.api.instalation.Installation;
import cz.muni.ucn.opsi.api.instalation.InstallationService;

/**
 * Server side API (controller) for handling requests on Clients.
 *
 * @see cz.muni.ucn.opsi.wui.gwt.client.client.ClientService for client side of this API.
 *
 * @author Jan Dosoudil
 * @author Pavel Zlámal <zlamal@cesnet.cz>
 */
@Controller
public class ClientController {

	private ClientService clientService;
	private InstallationService installationService;
	private OpsiClientService opsiService;
	private Validator validator;
	private ObjectMapper mapper;

	/**
	 * Setter for ClientService
	 *
	 * @param clientService the clientService to set
	 */
	@Autowired
	public void setClientService(ClientService clientService) {
		this.clientService = clientService;
	}

	/**
	 * Setter for InstallationService
	 *
	 * @param installationService the installationService to set
	 */
	@Autowired
	public void setInstallationService(InstallationService installationService) {
		this.installationService = installationService;
	}
	/**
	 * Setter for OpsiService
	 *
	 * @param opsiService the opsiService to set
	 */
	@Autowired
	public void setOpsiService(OpsiClientService opsiService) {
		this.opsiService = opsiService;
	}

	/**
	 * Setter for validator
	 *
	 * @param validator the validator to set
	 */
	@Autowired
	public void setValidator(Validator validator) {
		this.validator = validator;
	}

	/**
	 * Setter for mapper
	 *
	 * @param mapper the mapper to set
	 */
	@Autowired
	public void setMapper(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@RequestMapping(value = "/clients/create", method = RequestMethod.GET)
	public @ResponseBody Client createClient(@RequestParam String groupUuid) {
		return clientService.createClient(UUID.fromString(groupUuid));
	}

	@RequestMapping(value = "/clients/edit", method = RequestMethod.POST)
	public @ResponseBody Client editClient(@RequestParam String uuid) {
		return clientService.editClient(UUID.fromString(uuid));
	}

	@RequestMapping(value = "/clients/save", method = RequestMethod.PUT)
	public @ResponseBody void saveClient(@RequestBody Client client) {
		Set<ConstraintViolation<Client>> validation = validator.validate(client);
		if (!validation.isEmpty()) {
			throw new ValidationException("Požadavek obsahuje chyby");
		}
		clientService.saveClient(client);
	}

	@RequestMapping(value = "/clients/delete", method = RequestMethod.DELETE)
	public @ResponseBody void deleteClient(@RequestBody Client client) {
		clientService.deleteClient(client);
	}

	@RequestMapping(value = "/clients/install", method = RequestMethod.PUT)
	public @ResponseBody void installClient(@RequestBody Client client, @RequestParam String installId) {
		Installation i = installationService.getInstallationById(installId);
		clientService.installClient(client, i);
	}

	@RequestMapping(value = "/clients/list", method = RequestMethod.GET)
	public @ResponseBody List<Client> listClients(@RequestParam String groupUuid) {
		return clientService.listClients(UUID.fromString(groupUuid));
	}

	@RequestMapping(value = "/clients/import/list", method = RequestMethod.GET)
	public @ResponseBody List<Client> listClientsForImport(@RequestParam String groupUuid, @RequestParam String opsi) {
		return clientService.listClientsForImport(UUID.fromString(groupUuid), opsi);
	}

	@RequestMapping(value = "/clients/hardware/list", method = RequestMethod.GET)
	public @ResponseBody List<Hardware> listHardware(@RequestParam String uuid) {
		return clientService.listHardware(UUID.fromString(uuid));
	}

	@RequestMapping(value = "/clients/importCsv", method = RequestMethod.POST)
	public void importCSV(@RequestParam String groupUuid, @RequestParam("importFile") MultipartFile file, HttpServletResponse response, OutputStream os) throws IOException {

		UUID group = UUID.fromString(groupUuid);

		List<Client> clients = new ArrayList<Client>();

		CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()), ';');
		String[] record;
		while((record  = reader.readNext()) != null ) {
			if (record.length != 4) {
				continue;
			}
			Client c = clientService.createClient(group);
			c.setName(record[0]);
			if (StringUtils.isNotBlank(record[1])) c.setMacAddress(record[1]);
			if (StringUtils.isNotBlank(record[2])) c.setDescription(record[2]);
			if (StringUtils.isNotBlank(record[3])) c.setNotes(record[3]);
			//if (StringUtils.isNotBlank(record[4])) c.setIpAddress(record[4]);
			clients.add(c);
		}
		response.setContentType("text/html");

		try {
			mapper.writeValue(os, clients);
			reader.close();
		} catch (Exception ex) {
			mapper.writeValue(os, ex);
			reader.close();
		} finally {
			reader.close();
		}

	}

	@RequestMapping(value = "/clients/productProperty/list", method = RequestMethod.GET)
	public @ResponseBody List<ProductPropertyState> listProductProperties(@RequestParam String client) {
		return opsiService.getProductProperties(client);
	}

	@RequestMapping(value = "/clients/productProperty/update", method = RequestMethod.POST)
	public @ResponseBody boolean updateProductProperties(@RequestBody ProductPropertyList properties) {
		opsiService.setProductProperties(properties);
		return true;
	}

	@SuppressWarnings("serial")
	public static class ProductPropertyList extends ArrayList<ProductPropertyState> { }

}