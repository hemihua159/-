package demo;


import java.io.File;
import java.util.ArrayList;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class S3brower {
	
	private final static String accessKey = "4C1D5EF21C436099F7AA";
	private final static String secretKey = 
"WzlBQjA2Q0VGNTFBRTU0RDYzMEE5NDVBOUY1RDY0NEVCRENFNjczRUJd";
	private final static String serviceEndpoint = 
			"http://scuts3.depts.bingosoft.net:29999";
	private static long partSize = 5 << 20;
	private static long maxSize = 20 << 20;
	private final static String signingRegion = "";
	
	private static S3brower instance = null;
	private AmazonS3 s3 = null;
	private static String bucketName = "";

	

	private S3brower() {

		final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        final ClientConfiguration ccfg = new ClientConfiguration().

                withUseExpectContinue(false);



        final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);



        s3 = AmazonS3ClientBuilder.standard()

                .withCredentials(new AWSStaticCredentialsProvider(credentials))

                .withClientConfiguration(ccfg)

                .withEndpointConfiguration(endpoint)

                .withPathStyleAccessEnabled(true)

                .build();

	}

	

	public static S3brower getInstance() {

		if(instance == null) {

			instance = new S3brower();

			return instance;

		}

		else {

			return instance;

		}

	}

	
//新建目标桶
	public void setBucket(String name) {

		System.out.println("Set bucket name as: "+name);

		bucketName = name;

		try {

			s3.getBucketAcl(name);

		} catch (AmazonServiceException e) {

			if(e.getErrorCode().equalsIgnoreCase("NoSuchBucket")) {

				System.out.format("Creating bucket %s\n", name);

				s3.createBucket(name);

				System.out.println("Done");

			} else {

			System.err.println(e.toString());

			System.exit(1);

			}

		}

	}

	
//文件列表
	public ObjectListing listObjects() {

		ObjectListing ol = null;

		try {

			ol = s3.listObjects(bucketName);

		} catch (AmazonClientException e) {

			System.err.println(e.toString());

			System.exit(1);

		}

		return ol;

	}

	
//放入文件
	public void putObject(String key, File file) {

		try {

			System.out.format("Uploading file %s to bucket %s ...\n", key, bucketName);

			if(file.length() >= maxSize) {

				this.multPartUpload(key, file);

			} else {

				s3.putObject(bucketName, key, file);

			}

		} catch (AmazonClientException e) {

			System.err.println(e.toString());

			System.exit(1);

		}

		System.out.println("Done");

	}

	
//删除文件
	public void deleteObject(String key) {

		try {

			System.out.format("Deleting file %s in bucket %s ...\n", key, bucketName);

			s3.deleteObject(bucketName, key);

		} catch (AmazonClientException e) {

			System.err.println(e.toString());

			System.exit(1);

		}

		System.out.println("Done");

	}

	

	public void multPartUpload(String key, File file) {

		ArrayList<PartETag> partETags = new ArrayList<PartETag>();

		String uploadId = null;

		long contentLength = file.length();

		try {

			InitiateMultipartUploadRequest initRequest = 

					new InitiateMultipartUploadRequest(bucketName, key);

			uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();

			long filePosition = 0;

			for (int i = 1; filePosition < contentLength; i++) {

				partSize = Math.min(partSize, contentLength - filePosition);

				UploadPartRequest uploadRequest = new UploadPartRequest()

						.withBucketName(bucketName)

						.withKey(key)

						.withUploadId(uploadId)

						.withPartNumber(i)

						.withFileOffset(filePosition)

						.withFile(file)

						.withPartSize(partSize);

				System.out.format("Uploading part %d\n", i);

				partETags.add(s3.uploadPart(uploadRequest).getPartETag());

				filePosition += partSize;

			}

			System.out.println("Completing upload");

			CompleteMultipartUploadRequest compRequest = 

					new CompleteMultipartUploadRequest(bucketName, key, uploadId, partETags);

			s3.completeMultipartUpload(compRequest);

		} catch (Exception e) {

			System.err.println(e.toString());

			if (uploadId != null && !uploadId.isEmpty()) {

				System.out.println("Break of upload");

				s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, key, uploadId));

			}

			System.exit(1);

		}

	}

}
