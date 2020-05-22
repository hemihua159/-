package demo;

import java.io.File;
import java.io.IOException;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;


//上传文件
public class Main {
	private final static String bucketName = "hemihua";
	private final static String accessKey = "4C1D5EF21C436099F7AA";
	private final static String secretKey = "WzlBQjA2Q0VGNTFBRTU0RDYzMEE5NDVBOUY1RDY0NEVCRENFNjczRUJd";
	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";

	public static void main(String[] args) throws IOException {
		final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		final ClientConfiguration ccfg = new ClientConfiguration().withUseExpectContinue(false);

		final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withClientConfiguration(ccfg)
				.withEndpointConfiguration(endpoint).withPathStyleAccessEnabled(true).build();


		try {
			//s3.createBucket(bucketName);
			s3.putObject(bucketName,"hello.txt",new File("C:\\Users\\nxhmh\\Desktop\\hello.txt") );
		} catch (AmazonClientException e) {
			System.err.println(e.toString());
			System.exit(1);
		}

		System.out.println("Done!");
	}
}

