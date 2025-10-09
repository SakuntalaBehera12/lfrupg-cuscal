package au.com.cuscal.transactions.services;

//@Service("ticketAttachmentService")
public class TicketAttachmentServiceImpl implements TicketAttachmentService {
/*
	*//**
	 * Logger object
	 *//*
	private static Logger logger = LoggerFactory
			.getLogger(TicketAttachmentServiceImpl.class);

	*//**
	 * Logger object for performance logging
	 *//*
	private static Logger perfLogger_ = LoggerFactory.getLogger("perfLogging");

	*//**
	 * Audit object
	 *//*
	private static final Audit _audit = new Log4jAuditor();
	private static final Auditor audit = new Auditor(_audit);

	*//**
	 * ticketAttachmentService object
	 *//*
	@Autowired
	@Qualifier("ticketTransferService")
	private TicketTransferServiceImpl ticketTransferService;

	public DownloadTicketAttachmentListResponse downloadTicketAttachmentListByTicketId() throws Exception {
		System.out
				.println("downloadTicketAttachmentListByTicketId - start ");
		VsmRequestHeader requestHeader = new VsmRequestHeader();

		requestHeader.setOrigin("PORTAL");
		requestHeader.setUserId("rbharara");
		requestHeader.setUserOrgId("12812");
		requestHeader.setUserOrgName("Cuscal");

		DownloadTicketAttachmentListRequest ticketAttachmentListRequest = new DownloadTicketAttachmentListRequest();

		ticketAttachmentListRequest.setHeader(requestHeader);
		ticketAttachmentListRequest.setTicketId(1l);

		DownloadTicketAttachmentListResponse attachmentListResponse = ticketTransferService.findTicketAttachmentListByTicketId(ticketAttachmentListRequest);

		List<Attachment> attachments = attachmentListResponse.getAttachmentObj();

		for (Attachment attachment : attachments) {
			System.out
			.println("downloadTicketAttachmentListByTicketId  attachment are  " + attachment.getAttachmentName());
		}

		System.out
				.println("downloadTicketAttachmentListByTicketId - end ");

		return attachmentListResponse;
	}

	public DownloadTicketAttachmentResponse downloadTicketAttachmentByTicketAndAttachId() throws Exception {
		System.out
				.println("downloadTicketAttachmentByTicketAndAttachId - start ");

		VsmRequestHeader requestHeader = new VsmRequestHeader();

		requestHeader.setOrigin("PORTAL");
		requestHeader.setUserId("rbharara");
		requestHeader.setUserOrgId("12812");
		requestHeader.setUserOrgName("Cuscal");

		DownloadTicketAttachmentRequest downloadTicketAttachmentRequest = new DownloadTicketAttachmentRequest();

		downloadTicketAttachmentRequest.setHeader(requestHeader);
		downloadTicketAttachmentRequest.setAttachmentId(6l);
		downloadTicketAttachmentRequest.setTicketId(1l);
		DownloadTicketAttachmentResponse downloadTicketAttachmentResponse = ticketTransferService
				.getTicketAttachmentBlob(downloadTicketAttachmentRequest);

		FileOutputStream fileName = new FileOutputStream("C:/Temp/test.txt");

		DataHandler dataHandler = downloadTicketAttachmentResponse
				.getAttachmentObj().getAttachmentData();

		dataHandler.writeTo(fileName);
		fileName.flush();
		fileName.close();

		System.out
				.println("downloadTicketAttachmentByTicketAndAttachId - end ");

		return downloadTicketAttachmentResponse;
	}

	public UploadTicketAttachmentResponse uploadTicketAttachmentByTicketAndAttachId() throws Exception {
		System.out
				.println("uploadTicketAttachmentByTicketAndAttachId - start ");

		UploadTicketAttachmentRequest uploadTicketAttachmentRequest = new UploadTicketAttachmentRequest();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte buffer[] = new byte[1024];
		int length = 0;
		File file = new File("C:/Temp/login.txt");

		InputStream inputStream = new FileInputStream(file);
		while ((length = inputStream.read(buffer)) > 0) {
			byteArrayOutputStream.write(buffer, 0, length);
		}

		inputStream.close();
		byte[] data = byteArrayOutputStream.toByteArray();
		byteArrayOutputStream.close();
		DataHandler handler = new DataHandler(new ByteArrayDataSource(data,
				"application/octet-stream"));

		Attachment attachment = new Attachment();

		attachment.setAttachmentId(2l);
		attachment.setAttachmentName("test");
		attachment.setAttachmentSize(20l);
		attachment.setCompressed(false);
		attachment.setCreatedBy("rbharara");
		attachment.setEncrypted(false);
		attachment.setTicketId(6l);
		attachment.setAttachmentData(handler);
		uploadTicketAttachmentRequest.setAttachmentObj(attachment);

		UploadTicketAttachmentResponse response = ticketTransferService
				.addTicketAttachment(uploadTicketAttachmentRequest);

		System.out.println("uploadTicketAttachmentByTicketAndAttachId : " + response.isAttached());

		System.out.println("uploadTicketAttachmentByTicketAndAttachId - end ");

		return response;
	}*/

}